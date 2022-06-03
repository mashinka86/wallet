package ru.maria.wallet.events.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory

import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.support.serializer.JsonDeserializer
import ru.maria.wallet.events.config.properties.WalletKafkaProperties
import ru.maria.wallet.events.model.Transaction


@Configuration
class KafkaConsumerConfig(val walletKafkaProperties: WalletKafkaProperties, val objectMapper: ObjectMapper) {
    @Bean
    fun consumerFactory(): ConsumerFactory<String, Transaction> {
        val props= mapOf<String, Any>(
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to walletKafkaProperties.bootstrapAddress,
            ConsumerConfig.GROUP_ID_CONFIG to walletKafkaProperties.group)
        return DefaultKafkaConsumerFactory(props,StringDeserializer(), JsonDeserializer(Transaction::class.java, objectMapper))
    }

    @Bean
    fun kafkaListenerContainerFactory(consumerFactory:ConsumerFactory<String, Transaction>): ConcurrentKafkaListenerContainerFactory<String, Transaction> {
        val factory = ConcurrentKafkaListenerContainerFactory<String, Transaction>()
        factory.consumerFactory=consumerFactory
        return factory
    }
}
