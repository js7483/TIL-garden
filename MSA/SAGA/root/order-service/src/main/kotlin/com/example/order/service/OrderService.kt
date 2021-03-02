package com.example.order.service

import com.example.common.dto.OrderDetailsDto
import com.example.common.dto.OrderLineItemDto
import com.example.order.domain.DeliveryInformation
import com.example.order.domain.Order
import com.example.order.domain.OrderLineItem
import com.example.order.domain.Shop
import com.example.order.dto.req.LineItem
import com.example.order.event.OrderEventPublisher
import com.example.order.repository.OrderRepository
import com.example.order.repository.ShopRepository
import com.example.order.sagas.CreateOrderSaga
import com.example.order.sagas.CreateOrderSagaState
import io.eventuate.tram.sagas.orchestration.SagaInstanceFactory
import mu.KLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class OrderService(
        private val orderRepository: OrderRepository,
        private val shopRepository: ShopRepository,
        private val sagaInstanceFactory: SagaInstanceFactory,
        private val createOrderSaga: CreateOrderSaga,
        private val orderAggregateEventPublisher: OrderEventPublisher
) {
    companion object: KLogging()

    fun createOrder(consumerId: Long, shopId: Long, deliveryInformation: DeliveryInformation, lineItems: List<LineItem>): Order {
        val shop = shopRepository.findById(shopId).orElseThrow { Exception("") }

        val orderLineItems = makeOrderLineItems(lineItems, shop)

        val orderAndEvents = Order.create(consumerId, shop, deliveryInformation, orderLineItems)

        val order = orderAndEvents.result
        orderRepository.save(order)

        orderAggregateEventPublisher.publish(order, orderAndEvents.events) // publish the domain event
        logger.debug("Send OrderCreatedEvent to Order event channel")

        val orderDetails = OrderDetailsDto(orderLineItems.map { OrderLineItemDto(it.quantity, it.itemId, it.name, it.price) }, order.getOrderTotal(), shopId, consumerId)

        sagaInstanceFactory.create(createOrderSaga, CreateOrderSagaState(orderId = order.id, orderDetails = orderDetails))

        return order
    }

    private fun makeOrderLineItems(lineItems: List<LineItem>, shop: Shop): List<OrderLineItem> {
        return lineItems.map { li ->
            val menuItem = shop.findMenuItem(li.itemId) ?: throw Exception("")
            OrderLineItem(itemId = menuItem.id, name = menuItem.name, price = menuItem.price, quantity = li.quantity)
        }
    }

    fun approveOrder(orderId: Long) {
        val order = orderRepository.findById(orderId).orElseThrow { throw Exception("") }
        val rwe = order.approveCreate()
        orderRepository.save(rwe.result)
        orderAggregateEventPublisher.publish(rwe.result, rwe.events)
    }
}