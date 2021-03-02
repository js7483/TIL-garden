package com.example.shop.config

import com.example.common.channel.ShopServiceChannels
import com.example.shop.command.ShopServiceCommandHandler
import io.eventuate.tram.sagas.participant.SagaCommandDispatcher
import io.eventuate.tram.sagas.participant.SagaCommandDispatcherFactory
import io.eventuate.tram.sagas.spring.participant.SagaParticipantConfiguration
import io.eventuate.tram.spring.events.publisher.TramEventsPublisherConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(SagaParticipantConfiguration::class, TramEventsPublisherConfiguration::class)
class ShopServiceCommandConfig {
    @Bean
    fun shopServiceCommandHandlerDispatcher(shopServiceCommandHandlers: ShopServiceCommandHandler, sagaCommandDispatcherFactory: SagaCommandDispatcherFactory): SagaCommandDispatcher {
        return sagaCommandDispatcherFactory.make(ShopServiceChannels.SHOP_SERVICE_COMMAND_CHANNEL, shopServiceCommandHandlers.commandHandlers())
    }
}