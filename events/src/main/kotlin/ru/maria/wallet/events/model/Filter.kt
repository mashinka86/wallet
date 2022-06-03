package ru.maria.wallet.events.model

import java.time.OffsetDateTime
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class Filter(@get:NotNull(message = "startDatetime is empty") val startDatetime:OffsetDateTime?=null,
                  @get:NotNull(message = "endDatetime is empty") val endDatetime:OffsetDateTime?=null)