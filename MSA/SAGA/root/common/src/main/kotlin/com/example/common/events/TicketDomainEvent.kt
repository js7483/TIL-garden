package com.example.common.events

import io.eventuate.tram.events.common.DomainEvent

interface TicketDomainEvent: DomainEvent {
    val ticketId: Long
}

class TicketCreatedEvent(override val ticketId: Long): TicketDomainEvent

class TicketCancelledEvent(override val ticketId: Long): TicketDomainEvent