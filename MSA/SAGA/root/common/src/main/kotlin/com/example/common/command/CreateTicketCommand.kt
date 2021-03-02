package com.example.common.command

import com.example.common.dto.OrderDetailsDto
import io.eventuate.tram.commands.common.Command

data class CreateTicketCommand(
        val orderId: Long,
        val orderDetails: OrderDetailsDto
): Command


data class CancelCreateTicketCommand(
       val ticketId: Long
): Command

data class ConfirmCreateTicketCommand(
        val ticketId: Long
): Command

data class CreateTicketReply (
        val ticketId: Long
)