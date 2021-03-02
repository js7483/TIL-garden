package com.example.shop.event

import com.example.common.channel.ShopServiceChannels
import io.eventuate.tram.events.subscriber.DomainEventHandlers
import io.eventuate.tram.events.subscriber.DomainEventHandlersBuilder
import mu.KLogging
import org.springframework.stereotype.Component

@Component
class ShopEventConsumer {

    companion object: KLogging()

    fun domainEventHandlers(): DomainEventHandlers {
        return DomainEventHandlersBuilder
                .forAggregateType(ShopServiceChannels.SHOP_EVENT_CHANNEL)
                .build()
    }
}