package com.example.paymentiqcodetest

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal
import java.util.*

@RestController
@RequestMapping("/api")
class RestController(private val userService: UserService) {

    @PostMapping("/verifyuser")
    fun verifyUser(@RequestBody request: VerifyUserRequest): Response {
        val user = userService.byId(request.userId)
        return if (user != null) {
            VerifyUserResponse(request.userId,true, user.firstName, user.lastName, user.street,
                    user.city, user.zip, user.country, user.dob, user.mobile, user.balance, user.balanceCy)
        } else {
            ErrorResponse(request.userId, false, "123", "Unknown id")
        }
    }

    @PostMapping("/authorize")
    fun authorize(@RequestBody request: AuthorizeRequest): Response {
        val userId = request.userId
        val transfer = userService.authorizeTransfer(userId, request.txAmount, request.txAmountCy)
        return if (transfer != null) {
            AuthorizeResponse(userId, true, transfer.id, transfer.authCode)
        } else {
            ErrorResponse(userId, false, "456", "Insufficient balance or wrong currency")
        }
    }

    @PostMapping("/transfer")
    fun transfer(@RequestBody request: TransferRequest): TransferResponse {
        val transfer = userService.makeTransfer(request.userId, request.authCode, request.txAmount)
        return TransferResponse(request.userId, true, request.txId, transfer?.id)
    }

    @PostMapping("/cancel")
    fun cancel(@RequestBody request: CancelRequest): Response {
        val isCanceled = userService.cancelTransfer(request.userId, request.authCode)
        return if (isCanceled) {
            CancelResponse(request.userId, true)
        } else {
            ErrorResponse(request.userId, false, "890", "No such auth code")
        }
    }
}

data class VerifyUserRequest(val userId: String)

data class AuthorizeRequest(val userId: String, val txAmount: BigDecimal, val txAmountCy: String)

data class TransferRequest(val userId: String, val authCode: UUID?, val txAmount: BigDecimal, val txId: String)

data class CancelRequest(val userId: String, val authCode: UUID)

sealed class Response

data class VerifyUserResponse(val userId: String,
                              val success: Boolean,
                              val firstName: String,
                              val lastName: String,
                              val street: String,
                              val city: String,
                              val zip: String,
                              val country: Country,
                              val dob: String,
                              val mobile: String,
                              val balance: BigDecimal,
                              val balanceCy: Currency) : Response()

data class AuthorizeResponse(val userId: String,
                             val success: Boolean,
                             val merchantTxId: UUID,
                             val authCode: UUID) : Response()

data class TransferResponse(val userId: String,
                            val success: Boolean,
                            val txId: String,
                            val merchantTxId: UUID?)

data class CancelResponse(val userId: String, val success: Boolean) : Response()

data class ErrorResponse(val userID: String,
                         val success: Boolean,
                         val errCode: String,
                         val errMsg: String) : Response()
