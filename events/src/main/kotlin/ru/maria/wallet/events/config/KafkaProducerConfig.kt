package ru.maria.wallet.events.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import org.springframework.kafka.support.serializer.JsonSerializer
import ru.maria.wallet.events.config.properties.WalletKafkaProperties
import ru.maria.wallet.events.model.Transaction


@Configuration
class KafkaProducerConfig(val kafkaProperties: WalletKafkaProperties, val objectMapper: ObjectMapper) {
    @Bean
    fun producerFactory(): ProducerFactory<String, Transaction> {
        val configProps:Map<String, Any> = mapOf(
        ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to kafkaProperties.bootstrapAddress)
        return DefaultKafkaProducerFactory(configProps, StringSerializer(), JsonSerializer(objectMapper))
    }

    @Bean
    fun kafkaTemplate(producerFactory: ProducerFactory<String, Transaction>): KafkaTemplate<String, Transaction> {
        return KafkaTemplate(producerFactory)
    }
}
