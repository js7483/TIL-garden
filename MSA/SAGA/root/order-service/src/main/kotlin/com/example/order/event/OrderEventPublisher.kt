package com.example.order.event

import com.example.order.domain.Order
import io.eventuate.tram.events.aggregates.AbstractAggregateDomainEventPublisher
import io.eventuate.tram.events.publisher.DomainEventPublisher
import org.springframework.stereotype.Component

@Component
class OrderEventPublisher(
        eventPublisher: DomainEventPublisher
): AbstractAggregateDomainEventPublisher<Order, OrderDomainEvent>(eventPublisher, Order::class.java, Order::id)