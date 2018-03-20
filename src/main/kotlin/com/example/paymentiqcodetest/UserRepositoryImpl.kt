package com.example.paymentiqcodetest

import org.springframework.stereotype.Repository
import java.math.BigDecimal
import java.util.*

@Repository
class UserRepositoryImpl : UserRepository {

    private val users = HashMap<String, User>()

    override fun insert(user: User) {
        users[user.id] = user
    }

    override fun byId(id: String): User? {
        return users[id]
    }

    override fun addBalance(userId: String, amount: BigDecimal) {
        val user = users[userId]
        if (user != null) {
            user.balance = user.balance.plus(amount)
        }
    }

    override fun insertTransfer(userId: String, transfer: Transfer) {
        users[userId]?.transfers?.add(transfer)
    }

    override fun setTransferStatus(userId: String, transferId: UUID, status: TransferStatus) {
        transfers(userId)?.find { it.id == transferId }?.status = status
    }

    override fun transfers(userId: String): List<Transfer>? {
        return users[userId]?.transfers
    }
}