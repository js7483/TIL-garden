package com.example.order.config

import com.example.common.channel.OrderServiceChannels
import com.example.order.command.OrderServiceCommandHandlers
import io.eventuate.tram.sagas.participant.SagaCommandDispatcher
import io.eventuate.tram.sagas.participant.SagaCommandDispatcherFactory
import io.eventuate.tram.sagas.spring.participant.SagaParticipantConfiguration
import io.eventuate.tram.spring.events.publisher.TramEventsPublisherConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(SagaParticipantConfiguration::class, TramEventsPublisherConfiguration::class)
class OrderServiceCommandConfig {
    @Bean
    fun orderCommandHandlersDispatcher(orderCommandHandlers: OrderServiceCommandHandlers, sagaCommandDispatcherFactory: SagaCommandDispatcherFactory): SagaCommandDispatcher {
        return sagaCommandDispatcherFactory.make(OrderServiceChannels.ORDER_SERVICE_COMMAND_CHANNEL, orderCommandHandlers.commandHandlers())
    }
}