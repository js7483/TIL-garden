package com.example.shop.event

import com.example.common.events.TicketDomainEvent
import com.example.shop.domain.Ticket
import io.eventuate.tram.events.aggregates.AbstractAggregateDomainEventPublisher
import io.eventuate.tram.events.publisher.DomainEventPublisher
import org.springframework.stereotype.Component

@Component
class TicketEventPublisher(
        eventPublisher: DomainEventPublisher
): AbstractAggregateDomainEventPublisher<Ticket, TicketDomainEvent>(eventPublisher, Ticket::class.java, Ticket::id)