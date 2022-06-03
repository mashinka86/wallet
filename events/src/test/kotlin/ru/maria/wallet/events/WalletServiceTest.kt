package ru.maria.wallet.events

import com.nhaarman.mockitokotlin2.whenever
import org.awaitility.kotlin.await
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.kafka.config.KafkaListenerEndpointRegistry
import org.springframework.kafka.listener.MessageListenerContainer
import org.springframework.kafka.test.context.EmbeddedKafka
import org.springframework.kafka.test.utils.ContainerTestUtils
import org.springframework.test.context.junit.jupiter.SpringExtension
import ru.maria.wallet.events.config.*
import ru.maria.wallet.events.model.Transaction
import ru.maria.wallet.events.service.WalletService
import ru.maria.wallet.query.model.db.Operation
import ru.maria.wallet.query.repository.OperationRepository
import java.math.BigDecimal
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.util.concurrent.TimeUnit

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE,
    properties = ["spring.main.allow-bean-definition-overriding=true"],
    classes = [EventsConfig::class])
@ExtendWith(SpringExtension::class)
@EmbeddedKafka(partitions = 1, zkConnectionTimeout = 20000, zkSessionTimeout = 20000, count = 1, controlledShutdown = true)
class WalletServiceTest {

    @MockBean
    lateinit var operationRepository: OperationRepository

    @Autowired
    lateinit var registry:KafkaListenerEndpointRegistry

    @Autowired
    lateinit var walletService:WalletService

    @Captor
    lateinit var operationCaptor:ArgumentCaptor<Operation>

    @BeforeEach
    fun setUp(){
        for(con:MessageListenerContainer in registry.listenerContainers){
            val prt=1
            ContainerTestUtils.waitForAssignment(con, prt)
        }
    }


    @Test
    fun testSuccess(){
        val operation = Operation(date = ZonedDateTime.of(2019,2,1,10,10,10,0,
            ZoneOffset.UTC), amount = BigDecimal.ONE, sum = BigDecimal.TEN)
        whenever(operationRepository.findFirstByOrderByDateDesc()).thenReturn(operation)

        val transaction= Transaction(BigDecimal.TEN, OffsetDateTime.now())
        walletService.sendMessage(transaction)
        await.atMost(20, TimeUnit.SECONDS).untilAsserted{
            verify(operationRepository, times(1)).save(operationCaptor.capture())
        }
        val date =transaction.datetime!!.atZoneSameInstant(ZoneOffset.UTC).withFixedOffsetZone().withMinute(0).withSecond(0).plusHours(1)
        assertEquals(operationCaptor.value.date,date)
        assertEquals(operationCaptor.value.amount,transaction.amount)
        assertEquals(operationCaptor.value.sum, operation.sum.plus(transaction.amount!!))
    }


}