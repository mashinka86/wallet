package ru.maria.wallet.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.*
import ru.maria.wallet.events.model.Filter
import ru.maria.wallet.events.model.Transaction
import ru.maria.wallet.events.service.WalletService
import ru.maria.wallet.model.Success
import javax.validation.Valid


@RestController
@Validated
@RequestMapping("/wallet")
class WalletController(private val walletService: WalletService) {


    @PostMapping("/save")
    fun save(@Valid @RequestBody transaction: Transaction): ResponseEntity<Success>{
        walletService.sendMessage(transaction)
        return ResponseEntity(Success(0,"success"), HttpStatus.OK)
    }

    @PostMapping("/find")
    fun find(@Valid @RequestBody filter: Filter):List<Transaction>{
        return walletService.findOperations(filter)

    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(
        ex: MethodArgumentNotValidException
    ): Map<String, String>? {
        val errors: MutableMap<String, String> = HashMap()
        ex.bindingResult.allErrors.forEach { error ->
            val fieldName = (error as FieldError).field
            val errorMessage: String = error.defaultMessage
            errors.put(fieldName, errorMessage)
        }
        return errors
    }
}