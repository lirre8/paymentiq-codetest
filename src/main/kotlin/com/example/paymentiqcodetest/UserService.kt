package com.example.paymentiqcodetest

import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.util.*

@Service
class UserService(private val userRepository: UserRepository) {

    fun byId(userId: String): User? {
        return userRepository.byId(userId)
    }

    fun authorizeTransfer(userId: String, amount: BigDecimal, currency: String): Transfer? {
        val user = byId(userId)!!
        return if (validateBalance(user, amount, currency)) {
            if (amount < BigDecimal.ZERO) {
                userRepository.addBalance(user.id, amount)
            }
            val transfer = Transfer(amount, TransferStatus.AUTHORIZED)
            userRepository.insertTransfer(user.id, transfer)
            transfer
        } else {
            null
        }
    }

    private fun validateBalance(user: User, amount: BigDecimal, currency: String): Boolean {
       return currency == user.balanceCy.name && (amount > BigDecimal.ZERO || user.balance >= amount.negate())
    }

    fun makeTransfer(userId: String, transferAuthCode: UUID?, amount: BigDecimal): Transfer? {
        val transfer = userRepository.transfers(userId)?.find { it.authCode == transferAuthCode }
        when {
            transfer == null -> {
                userRepository.addBalance(userId, amount)
            }

            transfer.status == TransferStatus.AUTHORIZED -> {
                userRepository.setTransferStatus(userId, transfer.id, TransferStatus.DONE)
                if (transfer.amount < BigDecimal.ZERO) {
                    val amountToCredit = amount.minus(transfer.amount)
                    userRepository.addBalance(userId, amountToCredit)
                } else {
                    userRepository.addBalance(userId, amount)
                }
            }
        }
        return transfer
    }

    fun cancelTransfer(userId: String, transferAuthCode: UUID): Boolean {
        val transfer = userRepository.transfers(userId)?.find { it.authCode == transferAuthCode }
        if (transfer != null && transfer.status == TransferStatus.AUTHORIZED) {
            userRepository.setTransferStatus(userId, transfer.id, TransferStatus.CANCELED)
            if (transfer.amount < BigDecimal.ZERO) {
                userRepository.addBalance(userId, transfer.amount.negate())
            }
        }
        return transfer != null
    }
}