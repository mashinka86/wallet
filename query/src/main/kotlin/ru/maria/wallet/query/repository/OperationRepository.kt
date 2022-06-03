package ru.maria.wallet.query.repository

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import ru.maria.wallet.query.model.db.Operation
import java.time.ZonedDateTime

@Repository
interface OperationRepository: MongoRepository<Operation, String> {

    fun findFirstByOrderByDateDesc(): Operation?

    fun findByDateBetween(startDate:ZonedDateTime, endDate:ZonedDateTime):List<Operation>
}