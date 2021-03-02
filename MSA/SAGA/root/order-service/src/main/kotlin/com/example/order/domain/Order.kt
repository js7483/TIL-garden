package com.example.order.domain

import com.example.common.dto.OrderDetailsDto
import com.example.common.dto.OrderLineItemDto
import com.example.common.utils.Money
import com.example.common.utils.MoneyConverter
import com.example.order.event.OrderAuthorizedEvent
import com.example.order.event.OrderCreatedEvent
import com.example.order.event.OrderDomainEvent
import io.eventuate.tram.events.aggregates.ResultWithDomainEvents
import javax.persistence.*

@Entity
@Table(name = "orders")
data class Order(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "order_id")
        var id: Long = 0,

        @Column(nullable = false)
        @Enumerated(value = EnumType.STRING)
        var orderState: OrderState = OrderState.APPROVAL_PENDING,

        @Column(nullable = false)
        val consumerId: Long,

        @Column(nullable = false)
        val shopId: Long,

        @Embedded
        val orderLineItems: OrderLineItems,

        @Embedded
        val deliveryInformation: DeliveryInformation
) {
        companion object {
                fun create(consumerId: Long, shop: Shop, deliveryInformation: DeliveryInformation, orderLineItems: List<OrderLineItem>): ResultWithDomainEvents<Order, OrderDomainEvent> {
                        val order = Order(
                                consumerId = consumerId,
                                shopId = shop.id,
                                deliveryInformation = deliveryInformation,
                                orderLineItems = OrderLineItems(orderLineItems)
                        )

                        val events: List<OrderDomainEvent> = listOf(
                                OrderCreatedEvent(
                                        OrderDetailsDto(orderLineItems.map { OrderLineItemDto(it.quantity, it.itemId, it.name, it.price) }, order.getOrderTotal(), shop.id, consumerId),
                                        deliveryInformation.deliveryAddress
                                )
                        )

                        return ResultWithDomainEvents(order, events)
                }
        }

        fun getOrderTotal(): Money {
                return orderLineItems.orderTotal()
        }

        fun approveCreate(): ResultWithDomainEvents<Order, OrderDomainEvent> {
                return when(orderState) {
                        OrderState.APPROVAL_PENDING -> {
                                orderState = OrderState.APPROVED
                                ResultWithDomainEvents(this, listOf(OrderAuthorizedEvent()))
                        }
                        else -> { throw Exception("")}
                }
        }
}


@Embeddable
data class OrderLineItems(
        @ElementCollection
        @CollectionTable(name = "order_line_items")
        private val lineItems: List<OrderLineItem>
) {
        fun orderTotal(): Money {
                return lineItems.map { it.getTotalPrice() }.reduce { total, price -> total.add(price) }
        }
}

@Embeddable
data class OrderLineItem(
        val itemId: Long,
        val name: String,

        @Convert(converter = MoneyConverter::class)
        val price: Money,

        val quantity: Int,

        ) {
        fun getTotalPrice(): Money {
                return price.multiply(quantity)
        }
}