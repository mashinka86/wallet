package ru.maria.wallet.events.model

import java.math.BigDecimal
import java.time.OffsetDateTime
import javax.validation.constraints.NotNull

data class Transaction(
    @get:NotNull(message = "amount is empty") val amount:BigDecimal?=null,
    @get:NotNull(message = "datetime is empty") val datetime:OffsetDateTime?=null
)