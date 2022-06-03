package ru.maria.wallet.query.model.db

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.ZonedDateTime

@Document("operations")
data class Operation(
    @Id
    val id:ObjectId = ObjectId.get(),
    var date:ZonedDateTime,
    var amount:BigDecimal,
    var sum:BigDecimal
)