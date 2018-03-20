package com.example.paymentiqcodetest

import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.support.beans
import java.math.BigDecimal

@SpringBootApplication
class PaymentiqCodetestApplication

fun main(args: Array<String>) {
    SpringApplicationBuilder()
            .sources(PaymentiqCodetestApplication::class.java)
            .initializers(beans {
                bean {
                    /** Initialize database with some Users **/
                    ApplicationRunner {
                        val userService = ref<UserRepository>()
                        arrayOf(
                                User("1", mutableListOf(), "Johan", "Ek", "1th street", "New York", "99999", Country.SWE, "2346328", "0987654321", BigDecimal.valueOf(1000), Currency.SEK),
                                User("2", mutableListOf(), "Eva", "Holm", "2nd street", "Madrid", "99999", Country.SWE, "2346328", "0987654321", BigDecimal.valueOf(500), Currency.SEK),
                                User("3", mutableListOf(), "Karl", "Alm", "3rd street", "London", "99999", Country.SWE, "2346328", "0987654321", BigDecimal.valueOf(50), Currency.SEK),
                                User("4", mutableListOf(), "Jenny", "Sds", "11th street", "Stockholm", "99999", Country.SWE, "2346328", "0987654321", BigDecimal.valueOf(0), Currency.SEK),
                                User("5", mutableListOf(), "Pontus", "Opfp", "12th street", "Uppsala", "99999", Country.SWE, "2346328", "0987654321", BigDecimal.valueOf(1000), Currency.SEK)
                        ).forEach { userService.insert(it) }
                    }
                }
            })
            .run(*args)
}
