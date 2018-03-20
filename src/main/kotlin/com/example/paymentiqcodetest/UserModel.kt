package com.example.paymentiqcodetest

import java.math.BigDecimal
import java.util.*

data class User(val id: String,
                val transfers: MutableList<Transfer>,
                val firstName: String,
                val lastName: String,
                val street: String,
                val city: String,
                val zip: String,
                val country: Country,
                val dob: String,
                val mobile: String,
                var balance: BigDecimal,
                val balanceCy: Currency)

data class Transfer(val amount: BigDecimal,
                    var status: TransferStatus,
                    val id: UUID = UUID.randomUUID(),
                    val authCode: UUID = UUID.randomUUID())

enum class Country { SWE }

enum class Currency { SEK }

enum class TransferStatus { AUTHORIZED, DONE, CANCELED }
