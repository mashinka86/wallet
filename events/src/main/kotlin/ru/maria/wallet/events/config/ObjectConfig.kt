package ru.maria.wallet.events.config

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.datatype.jsr310.deser.InstantDeserializer
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.util.function.BiFunction


@Configuration
class ObjectConfig{

    companion object{
        val DATE_FORMATTER:DateTimeFormatter = DateTimeFormatterBuilder()
        .parseCaseInsensitive()
        .append(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        .parseLenient().parseStrict()
            .appendOffset("+HH:MM","+00:00").toFormatter()
    }

    @Primary
    @Bean
    fun objectMapper(): ObjectMapper {
        return jacksonObjectMapper().configure(SerializationFeature.INDENT_OUTPUT, false)
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .configure(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE, false)
            .registerKotlinModule().registerModule(JavaTimeModule().apply {
                addSerializer(OffsetDateTime::class.java, object : JsonSerializer<OffsetDateTime?>() {
                    override fun serialize(
                        offsetDateTime: OffsetDateTime?,
                        jsonGenerator: JsonGenerator,
                        serializerProvider: SerializerProvider?
                    ) {
                        jsonGenerator.writeString(DATE_FORMATTER.format(offsetDateTime))
                    }
                })
            addDeserializer(OffsetDateTime::class.java, OffsetDateTimeDeserializer(DATE_FORMATTER))
        })
    }
}

class OffsetDateTimeDeserializer(formatter: DateTimeFormatter):InstantDeserializer<OffsetDateTime>(
    OffsetDateTime::class.java,
    formatter,
    OffsetDateTime::from,
    {a->OffsetDateTime.ofInstant(Instant.ofEpochMilli(a.value), a.zoneId)},
    {a->OffsetDateTime.ofInstant(Instant.ofEpochSecond(a.integer, a.fraction.toLong()), a.zoneId)},
    BiFunction { d: OffsetDateTime, z: ZoneId ->
        d.withOffsetSameInstant(
            z.rules.getOffset(d.toLocalDateTime())
        )
    },
    false
)

