package ru.maria.wallet.events.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "kafka")
class WalletKafkaProperties(
     val bootstrapAddress: String,
     val topic: String,
     val group:String,
)