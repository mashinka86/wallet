package ru.maria.wallet.events.service

import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Service
import ru.maria.wallet.events.config.properties.WalletKafkaProperties
import ru.maria.wallet.events.model.Filter
import ru.maria.wallet.events.model.Transaction
import ru.maria.wallet.query.model.db.Operation
import ru.maria.wallet.query.repository.OperationRepository
import java.math.BigDecimal
import java.time.ZoneOffset
import java.time.ZonedDateTime
import javax.annotation.PostConstruct


@Service
class WalletService(private val kafkaTemplate: KafkaTemplate<String, Transaction>,
                    private val operationRepository: OperationRepository,
                    private val kafkaProperties: WalletKafkaProperties
) {

    fun sendMessage(msg: Transaction) {
        kafkaTemplate.send(kafkaProperties.topic, msg)
    }

    @KafkaListener(topics = ["\${kafka.topic}"], groupId = "\${kafka.group}")
    fun readMessage(@Payload transaction: Transaction){
        val operation = operationRepository.findFirstByOrderByDateDesc()
        val date = transaction.datetime!!.atZoneSameInstant(ZoneOffset.UTC).withFixedOffsetZone().withMinute(0).withSecond(0).plusHours(1)
        val amount = transaction.amount!!
        val newOperation=operation?.let {
            if (operation.date == date){
                operation.amount = operation.amount.plus(amount)
                operation.sum =operation.sum.plus(amount)
                operation
            }else{
                Operation(date=date, amount = amount, sum=operation.sum.plus(amount))
            }
        }?: Operation(date=date, amount = amount, sum = amount)
        operationRepository.save(newOperation)
    }

    fun findOperations(filter: Filter) =
        operationRepository.findByDateBetween(filter.startDatetime!!.toZonedDateTime(),filter.endDatetime!!.toZonedDateTime()).map {
                operation ->  Transaction(operation.sum, operation.date.toOffsetDateTime())
    }


}