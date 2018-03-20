package com.example.paymentiqcodetest

import java.math.BigDecimal
import java.util.*

interface UserRepository {

    fun insert(user: User)

    fun byId(id: String): User?

    fun addBalance(userId: String, amount: BigDecimal)

    fun insertTransfer(userId: String, transfer: Transfer)

    fun setTransferStatus(userId: String, transferId: UUID, status: TransferStatus)

    fun transfers(userId: String): List<Transfer>?
}

