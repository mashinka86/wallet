package wallet

import com.fasterxml.jackson.databind.ObjectMapper
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.whenever
import org.hamcrest.core.Is
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import ru.maria.wallet.config.AppConfig
import ru.maria.wallet.controller.WalletController
import ru.maria.wallet.events.model.Filter
import ru.maria.wallet.events.model.Transaction
import ru.maria.wallet.events.service.WalletService
import java.math.BigDecimal
import java.time.OffsetDateTime
import java.time.ZoneOffset


@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.MOCK,classes = [AppConfig::class, WalletController::class])
@AutoConfigureMockMvc
class WalletControllerTest {


    @Autowired
    lateinit var walletController: WalletController

    @MockBean
    lateinit var walletService: WalletService

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Autowired
    lateinit var mockMvc: MockMvc


    @Test
    fun testSaveSuccess(){
        val transaction = Transaction(amount = BigDecimal.TEN, datetime = OffsetDateTime.now())
        mockMvc.perform(
            MockMvcRequestBuilders.post("/wallet/save").content(objectMapper.writeValueAsString(transaction))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.result", Is.`is`(0)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.message", Is.`is`("success")))
    }

    @Test
    fun testSaveError(){
        val transaction = Transaction(amount = BigDecimal.TEN, datetime = null)
        mockMvc.perform(
            MockMvcRequestBuilders.post("/wallet/save").content(objectMapper.writeValueAsString(transaction))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.datetime", Is.`is`("datetime is empty")))
    }

    @Test
    fun testFindSuccess(){
        val filter= Filter(
            OffsetDateTime.of(2019,1,1,10,10,10,0, ZoneOffset.UTC),
            OffsetDateTime.of(2019,3,1,10,10,10,0, ZoneOffset.UTC))
        val operation= Transaction(datetime = OffsetDateTime.of(2019,1,1,10,10,10,0, ZoneOffset.UTC),
        amount = BigDecimal.TEN
        )
        whenever(walletService.findOperations(eq(filter))).thenReturn(listOf(operation))
        mockMvc.perform(
            MockMvcRequestBuilders.post("/wallet/find").content(objectMapper.writeValueAsString(filter))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].amount", Is.`is`(10)))
    }

    @Test
    fun testFindError(){
        val filter= Filter(
            null,
            OffsetDateTime.of(2019,3,1,10,10,10,0, ZoneOffset.UTC))

        mockMvc.perform(
            MockMvcRequestBuilders.post("/wallet/find").content(objectMapper.writeValueAsString(filter))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.startDatetime", Is.`is`("startDatetime is empty")))
    }

}