package com.example.shop.config

import com.example.shop.event.ShopEventConsumer
import com.example.shop.event.TicketEventConsumer
import io.eventuate.tram.events.subscriber.DomainEventDispatcher
import io.eventuate.tram.events.subscriber.DomainEventDispatcherFactory
import io.eventuate.tram.sagas.spring.orchestration.SagaOrchestratorConfiguration
import io.eventuate.tram.spring.events.publisher.TramEventsPublisherConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(DomainEventDispatcherFactory::class, TramEventsPublisherConfiguration::class, SagaOrchestratorConfiguration::class)
class ShopServiceEventConfig {

    @Bean
    fun shopDomainEventDispatcher(shopEventConsumer: ShopEventConsumer, domainEventDispatcherFactory: DomainEventDispatcherFactory): DomainEventDispatcher {
        return domainEventDispatcherFactory
                .make("shopServiceEvents", shopEventConsumer.domainEventHandlers())
    }

    @Bean
    fun ticketDomainEventDispatcher(ticketEventConsumer: TicketEventConsumer, domainEventDispatcherFactory: DomainEventDispatcherFactory): DomainEventDispatcher {
        return domainEventDispatcherFactory
                .make("shopServiceEvents", ticketEventConsumer.domainEventHandlers())
    }
}