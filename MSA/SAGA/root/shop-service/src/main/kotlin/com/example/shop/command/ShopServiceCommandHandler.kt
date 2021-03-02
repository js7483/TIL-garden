package com.example.shop.command

import com.example.common.channel.ShopServiceChannels
import com.example.common.command.CancelCreateTicketCommand
import com.example.common.command.ConfirmCreateTicketCommand
import com.example.common.command.CreateTicketCommand
import com.example.common.command.CreateTicketReply
import com.example.common.dto.TicketDetailsDto
import com.example.common.dto.TicketLineItemDto
import com.example.shop.domain.Ticket
import com.example.shop.service.ShopService
import io.eventuate.tram.commands.consumer.CommandHandlerReplyBuilder
import io.eventuate.tram.commands.consumer.CommandHandlers
import io.eventuate.tram.commands.consumer.CommandMessage
import io.eventuate.tram.messaging.common.Message
import io.eventuate.tram.sagas.participant.SagaCommandHandlersBuilder
import io.eventuate.tram.sagas.participant.SagaReplyMessageBuilder
import mu.KLogging
import org.springframework.stereotype.Component

@Component
class ShopServiceCommandHandler(
        private val shopService: ShopService
) {

    companion object: KLogging()

    fun commandHandlers(): CommandHandlers {
        return SagaCommandHandlersBuilder.fromChannel(ShopServiceChannels.SHOP_SERVICE_COMMAND_CHANNEL)
                .onMessage(CreateTicketCommand::class.java, this::createTicket)
                .onMessage(CancelCreateTicketCommand::class.java, this::cancelCreateTicket)
                .onMessage(ConfirmCreateTicketCommand::class.java, this::confirmCreateTicket)
                .build()
    }

    private fun createTicket(cm: CommandMessage<CreateTicketCommand>): Message {
        logger.debug("Receive CreateTicketCommand")

        val shopId = cm.command.orderDetails.shopId
        val ticketId = cm.command.orderId
        val orderDetails = cm.command.orderDetails
        val ticketDetails = TicketDetailsDto(orderDetails.lineItems.map { TicketLineItemDto(it.quantity, it.itemId, it.name) })

        return try {
            val ticket = shopService.createTicket(shopId, ticketId, ticketDetails)
            val reply = CreateTicketReply(ticket.id)
            SagaReplyMessageBuilder.withLock(Ticket::class.java, ticket.id).withSuccess(reply)
        } catch (e: Exception) {
            CommandHandlerReplyBuilder.withFailure()
        }
    }

    private fun cancelCreateTicket(cm: CommandMessage<CancelCreateTicketCommand>): Message {
        logger.debug("Receive CancelCreateTicketCommand")

        shopService.cancelCreateTicket(cm.command.ticketId)
        return CommandHandlerReplyBuilder.withSuccess()
    }

    private fun confirmCreateTicket(cm: CommandMessage<ConfirmCreateTicketCommand>): Message {
        logger.debug("Receive ConfirmCancelTicketCommand")

        shopService.confirmCreateTicket(cm.command.ticketId)
        return CommandHandlerReplyBuilder.withSuccess()
    }
}