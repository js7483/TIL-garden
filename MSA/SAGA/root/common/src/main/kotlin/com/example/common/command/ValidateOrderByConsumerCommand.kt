package com.example.common.command

import com.example.common.utils.Money
import io.eventuate.tram.commands.common.Command

class ValidateOrderByConsumerCommand(
        val consumerId: Long,
        val orderId: Long,
        val orderTotal: Money
): Command