package ru.maria.wallet.events.config

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.kafka.annotation.EnableKafka
import ru.maria.wallet.events.config.properties.WalletKafkaProperties
import ru.maria.wallet.events.service.WalletService
import ru.maria.wallet.query.config.MongoConvertersConfig

@EnableKafka
@Configuration
@EnableConfigurationProperties(WalletKafkaProperties::class)
@Import(MongoConvertersConfig::class, WalletService::class, KafkaConsumerConfig::class, KafkaProducerConfig::class, KafkaTopicConfig::class, ObjectConfig::class)
class EventsConfig