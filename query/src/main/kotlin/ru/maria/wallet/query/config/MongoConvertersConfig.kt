package ru.maria.wallet.query.config

import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter
import org.springframework.data.mongodb.core.convert.MongoCustomConversions
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.util.*


@Configuration
@EnableMongoRepositories(basePackages = ["ru.maria.wallet.query.repository"])
@EnableAutoConfiguration
class MongoConvertersConfig {

    @Bean
    fun mongoCustomConversions(): MongoCustomConversions {
        return MongoCustomConversions(listOf(
            ZonedDateTimeReadConverter(),
            ZonedDateTimeWriteConverter()
        ))
    }
}

class ZonedDateTimeReadConverter :
    Converter<Date, ZonedDateTime> {
    override fun convert(date: Date): ZonedDateTime {
        return date.toInstant().atZone(ZoneOffset.UTC).withFixedOffsetZone()
    }
}


class ZonedDateTimeWriteConverter :
    Converter<ZonedDateTime, Date> {
    override fun convert(zonedDateTime: ZonedDateTime): Date {
        return Date.from(zonedDateTime.toInstant())
    }
}
