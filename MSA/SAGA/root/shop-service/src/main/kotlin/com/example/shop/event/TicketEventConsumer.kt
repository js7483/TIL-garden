package com.example.shop.event

import com.example.common.channel.ShopServiceChannels
import com.example.common.events.TicketCreatedEvent
import io.eventuate.tram.events.subscriber.DomainEventEnvelope
import io.eventuate.tram.events.subscriber.DomainEventHandlers
import io.eventuate.tram.events.subscriber.DomainEventHandlersBuilder
import mu.KLogging
import org.springframework.stereotype.Component

@Component
class TicketEventConsumer {

    companion object: KLogging()

    fun domainEventHandlers(): DomainEventHandlers {
        return DomainEventHandlersBuilder
                .forAggregateType(ShopServiceChannels.SHOP_EVENT_CHANNEL)
                .onEvent(TicketCreatedEvent::class.java, this::createTicket)
                .build()
    }

    private fun createTicket(de: DomainEventEnvelope<TicketCreatedEvent>) {
        logger.debug("Receive TicketCreatedEvent")
    }
}