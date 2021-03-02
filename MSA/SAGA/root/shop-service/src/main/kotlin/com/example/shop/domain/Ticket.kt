package com.example.shop.domain

import com.example.common.dto.TicketDetailsDto
import com.example.common.events.TicketCancelledEvent
import com.example.common.events.TicketCreatedEvent
import com.example.common.events.TicketDomainEvent
import io.eventuate.tram.events.aggregates.ResultWithDomainEvents
import javax.persistence.*

@Entity
@Table(name = "tickets")
@Access(AccessType.FIELD)
data class Ticket(
        @Id
        @Column(name = "ticket_id")
        val id: Long = 0,

        val shopId: Long,

        @ElementCollection
        @CollectionTable(name = "ticket_line_items")
        val lineItems: List<TicketLineItem>,

        @Enumerated(EnumType.STRING)
        val state: TicketState? = null,

        @Enumerated(EnumType.STRING)
        val previousState: TicketState? = null,
) {
    companion object {
        fun create(shop: Shop, ticketId: Long, ticketDetailsDto: TicketDetailsDto): ResultWithDomainEvents<Ticket, TicketDomainEvent> {
                val ticket = Ticket(
                        id = ticketId,
                        shopId = shop.id,
                        lineItems = ticketDetailsDto.lineItems.map { TicketLineItem(it.quantity, it.itemId, it.name) },
                        state = TicketState.CREATE_PENDING
                )
                return ResultWithDomainEvents(ticket, listOf())
        }
    }

    fun cancelCreate(): ResultWithDomainEvents<Ticket, TicketDomainEvent> {
        val ticket = Ticket(
                id = id,
                shopId = shopId,
                lineItems = lineItems,
                state = TicketState.CANCELLED,
                previousState = state
        )
        val events = listOf(TicketCancelledEvent(ticket.id))
        return ResultWithDomainEvents(ticket, events)
    }

    fun confirmCreate(): ResultWithDomainEvents<Ticket, TicketDomainEvent> {
        val ticket = Ticket(
                id = id,
                shopId = shopId,
                lineItems = lineItems,
                state = TicketState.AWAITING_ACCEPTANCE,
                previousState = state
        )
        val events = listOf(TicketCreatedEvent(ticket.id))
        return ResultWithDomainEvents(ticket, events)
    }

}

enum class TicketState(val description: String) {
        CREATE_PENDING(""),
        AWAITING_ACCEPTANCE(""),
        ACCEPTED(""),
        PREPARING(""),
        READY_FOR_PICKUP(""),
        PICKED_UP(""),
        CANCEL_PENDING(""),
        CANCELLED(""),
        REVISION_PENDING("")
}

@Embeddable
@Access(AccessType.FIELD)
data class TicketLineItem(
        val quantity: Int,
        val itemId: Long,
        val name: String
)