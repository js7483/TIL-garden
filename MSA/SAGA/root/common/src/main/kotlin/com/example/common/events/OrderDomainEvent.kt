package com.example.order.event

import com.example.common.dto.OrderDetailsDto
import com.example.common.utils.Address
import io.eventuate.tram.events.common.DomainEvent

interface OrderDomainEvent: DomainEvent

class OrderCreatedEvent(
        val orderDetails: OrderDetailsDto,
        val deliveryAddress: Address
): OrderDomainEvent

class OrderAuthorizedEvent: OrderDomainEvent

class OrderRejectedEvent: OrderDomainEvent