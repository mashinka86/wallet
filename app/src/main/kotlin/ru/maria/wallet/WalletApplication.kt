package ru.maria.wallet

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
class WalletApplication

fun main(args: Array<String>) {
	runApplication<WalletApplication>(*args)
}
