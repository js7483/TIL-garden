package com.example.order.command

import com.example.common.channel.OrderServiceChannels
import com.example.order.sagaparticipatns.order.ApproveOrderCommand
import com.example.order.sagaparticipatns.order.RejectOrderCommand
import com.example.order.service.OrderService
import io.eventuate.tram.commands.consumer.CommandHandlerReplyBuilder.withSuccess
import io.eventuate.tram.commands.consumer.CommandHandlers
import io.eventuate.tram.commands.consumer.CommandMessage
import io.eventuate.tram.messaging.common.Message
import io.eventuate.tram.sagas.participant.SagaCommandHandlersBuilder
import mu.KLogging
import org.springframework.stereotype.Component

@Component
class OrderServiceCommandHandlers(
        private val orderService: OrderService
) {

    companion object: KLogging()

    fun commandHandlers(): CommandHandlers {
        return SagaCommandHandlersBuilder
                .fromChannel(OrderServiceChannels.ORDER_SERVICE_COMMAND_CHANNEL)
                .onMessage(ApproveOrderCommand::class.java, this::approveOrder)
                .onMessage(RejectOrderCommand::class.java, this::rejectOrder)
                .build()
    }

    private fun approveOrder(cm: CommandMessage<ApproveOrderCommand>): Message {
        logger.debug("Receive ApproveOrderCommand")
        orderService.approveOrder(cm.command.orderId)
        return withSuccess()
    }

    private fun rejectOrder(cm: CommandMessage<RejectOrderCommand>): Message {
        logger.debug("Receive RejectOrderCommand")
        orderService.rejectOrder(cm.command.orderId)
        return withSuccess()
    }
}