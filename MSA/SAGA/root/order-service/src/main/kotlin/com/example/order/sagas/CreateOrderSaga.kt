package com.example.order.sagas

import com.example.common.channel.ConsumerServiceChannels
import com.example.common.channel.OrderServiceChannels
import com.example.common.channel.PaymentServiceChannels
import com.example.common.channel.ShopServiceChannels
import com.example.common.command.*
import com.example.common.dto.OrderDetailsDto
import com.example.order.sagaparticipatns.order.ApproveOrderCommand
import com.example.order.sagaparticipatns.order.RejectOrderCommand
import io.eventuate.tram.commands.consumer.CommandWithDestination
import io.eventuate.tram.commands.consumer.CommandWithDestinationBuilder
import io.eventuate.tram.sagas.orchestration.SagaDefinition
import io.eventuate.tram.sagas.simpledsl.SimpleSaga
import mu.KLogging
import org.springframework.stereotype.Component

@Component
class CreateOrderSaga: SimpleSaga<CreateOrderSagaState> {

    companion object: KLogging()

    private val sagaDefinition: SagaDefinition<CreateOrderSagaState> = step().withCompensation(this::rejectOrder)
            .step().invokeParticipant(this::createTicket).onReply(CreateTicketReply::class.java, this::handleCreateTicketReply).withCompensation(this::cancelCreateTicket)
            .step().invokeParticipant(this::confirmCreateTicket)
            .step().invokeParticipant(this::approveOrder)
            .build()

    override fun getSagaDefinition(): SagaDefinition<CreateOrderSagaState> {
        return sagaDefinition
    }

    private fun rejectOrder(data: CreateOrderSagaState): CommandWithDestination {
        logger.debug("Send RejectOrderCommand to orderService channel")
        return CommandWithDestinationBuilder.send(RejectOrderCommand(data.orderId))
                .to(OrderServiceChannels.ORDER_SERVICE_COMMAND_CHANNEL).build()
    }

    private fun validateOrderByConsumer(data: CreateOrderSagaState): CommandWithDestination {
        logger.debug("Send ValidateOrderByConsumerCommand to consumerService channel")
        return CommandWithDestinationBuilder.send(ValidateOrderByConsumerCommand(data.orderDetails.consumerId, data.orderId, data.orderDetails.orderTotal))
                .to(ConsumerServiceChannels.CONSUMER_SERVICE_COMMAND_CHANNEL).build()
    }

    private fun createTicket(data: CreateOrderSagaState): CommandWithDestination {
        logger.debug("Send CreateTicketCommand to kitchenService channel")
        return CommandWithDestinationBuilder.send(CreateTicketCommand(data.orderId, data.orderDetails))
                .to(ShopServiceChannels.SHOP_SERVICE_COMMAND_CHANNEL).build()
    }

    private fun cancelCreateTicket(data: CreateOrderSagaState): CommandWithDestination {
        logger.debug("Send CancelCreateTicketCommand to kitchenService channel")
        return CommandWithDestinationBuilder.send(CancelCreateTicketCommand(data.ticketId ?: throw Exception("")))
                .to(ShopServiceChannels.SHOP_SERVICE_COMMAND_CHANNEL).build()
    }

    private fun payment(data: CreateOrderSagaState): CommandWithDestination {
        logger.debug("Send PaymentCommand to paymentService channel")
        return CommandWithDestinationBuilder.send(PaymentCommand(data.orderDetails.consumerId, data.orderId, data.orderDetails.orderTotal))
                .to(PaymentServiceChannels.PAYMENT_SERVICE_COMMAND_CHANNEL).build()
    }

    private fun cancelPayment(data: CreateOrderSagaState): CommandWithDestination {
        logger.debug("Send CancelPaymentCommand to paymentService channel")
        return CommandWithDestinationBuilder.send(CancelPaymentCommand(data.orderDetails.consumerId, data.orderId, data.orderDetails.orderTotal))
                .to(PaymentServiceChannels.PAYMENT_SERVICE_COMMAND_CHANNEL).build()
    }

    private fun confirmCreateTicket(data: CreateOrderSagaState): CommandWithDestination {
        logger.debug("Send ConfirmCreateTicketCommand to kitchenService channel")
        return CommandWithDestinationBuilder.send(ConfirmCreateTicketCommand(data.ticketId ?: throw Exception("TicketId should not be null")))
                .to(ShopServiceChannels.SHOP_SERVICE_COMMAND_CHANNEL).build()
    }

    private fun approveOrder(data: CreateOrderSagaState): CommandWithDestination? {
        logger.debug("Send ApproveOrderCommand to orderService channel")
        return CommandWithDestinationBuilder.send(ApproveOrderCommand(data.orderId))
                .to(OrderServiceChannels.ORDER_SERVICE_COMMAND_CHANNEL).build()
    }

    private fun handleCreateTicketReply(data: CreateOrderSagaState, reply: CreateTicketReply) {
        logger.debug("Receive CreateTicketReply {}", reply.ticketId)
         data.ticketId = reply.ticketId
    }
}

data class CreateOrderSagaState(
        val orderId: Long,
        val orderDetails: OrderDetailsDto,
        var ticketId: Long? = null
)