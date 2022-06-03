package ru.maria.wallet.query.repository

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import ru.maria.wallet.query.config.MongoConvertersConfig
import ru.maria.wallet.query.model.db.Operation
import java.math.BigDecimal
import java.time.ZoneOffset
import java.time.ZonedDateTime

@SpringBootTest(classes = [MongoConvertersConfig::class])
class OperationRepositoryTest {

    @Autowired
    lateinit var operationRepository: OperationRepository

    @Test
    fun test(){
       val operation = Operation(date = ZonedDateTime.of(2019,2,1,10,10,10,0, ZoneOffset.UTC), amount = BigDecimal.ONE, sum = BigDecimal.TEN)
        operationRepository.save(operation)
        val operationFind = operationRepository.findFirstByOrderByDateDesc()
        assertEquals(operation, operationFind)
        val operationBetween=operationRepository.findByDateBetween(ZonedDateTime.of(2019,1,1,10,10,10,0, ZoneOffset.UTC),ZonedDateTime.of(2019,3,1,10,10,10,0, ZoneOffset.UTC))
        assertEquals(operation, operationBetween.get(0))
    }
}