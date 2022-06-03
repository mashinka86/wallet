package ru.maria.wallet.config

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import ru.maria.wallet.events.config.EventsConfig

@Configuration
@Import(EventsConfig::class)
class AppConfig