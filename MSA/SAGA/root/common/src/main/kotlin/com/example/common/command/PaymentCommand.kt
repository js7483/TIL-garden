package com.example.common.command

import com.example.common.utils.Money
import io.eventuate.tram.commands.common.Command

class PaymentCommand(
        val consumerId: Long,
        val orderId: Long,
        val orderTotal: Money
): Command

class CancelPaymentCommand(
        val consumerId: Long,
        val orderId: Long,
        val orderTotal: Money
): Command