package com.example.order.event

import com.example.common.channel.ShopServiceChannels
import com.example.order.service.OrderService
import io.eventuate.tram.events.subscriber.DomainEventEnvelope
import io.eventuate.tram.events.subscriber.DomainEventHandlers
import io.eventuate.tram.events.subscriber.DomainEventHandlersBuilder
import mu.KLogging
import org.springframework.stereotype.Component

@Component
class OrderEventConsumer(
        private val orderService: OrderService
) {

    companion object: KLogging()

    fun domainEventHandlers(): DomainEventHandlers {
        return DomainEventHandlersBuilder
//                .forAggregateType("com.ftgo.restaurantservice.model.Restaurant")
                .forAggregateType(ShopServiceChannels.SHOP_EVENT_CHANNEL)
                .onEvent(ShopCreatedEvent::class.java, this::createShop).build()
    }

    private fun createShop(de: DomainEventEnvelope<ShopCreatedEvent>) {
        logger.debug("Receive ShopCreatedEvent")
//        val shopId = de.aggregateId
//        orderService.createShop(de.event.shopId, de.event.name, de.event.shopItems)
    }
}