package com.example.order.config

import com.example.order.event.OrderEventConsumer
import io.eventuate.tram.events.subscriber.DomainEventDispatcher
import io.eventuate.tram.events.subscriber.DomainEventDispatcherFactory
import io.eventuate.tram.sagas.spring.orchestration.SagaOrchestratorConfiguration
import io.eventuate.tram.spring.events.publisher.TramEventsPublisherConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(DomainEventDispatcherFactory::class, TramEventsPublisherConfiguration::class, SagaOrchestratorConfiguration::class)
class OrderServiceEventConfiguration {

    @Bean
    fun domainEventDispatcher(orderEventConsumer: OrderEventConsumer, domainEventDispatcherFactory: DomainEventDispatcherFactory): DomainEventDispatcher? {
        return domainEventDispatcherFactory.make("orderServiceEvents", orderEventConsumer.domainEventHandlers())
    }
}