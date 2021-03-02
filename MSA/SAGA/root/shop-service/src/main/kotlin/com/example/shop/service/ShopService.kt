package com.example.shop.service

import com.example.common.dto.TicketDetailsDto
import com.example.shop.domain.Ticket
import com.example.shop.event.TicketEventPublisher
import com.example.shop.repository.ShopRepository
import com.example.shop.repository.TicketRepository
import org.springframework.stereotype.Service

@Service
class ShopService(
        private val shopRepository: ShopRepository,
        private val ticketRepository: TicketRepository,
        private val ticketEventPublisher: TicketEventPublisher
) {

    fun createTicket(shopId: Long, ticketId: Long, ticketDetails: TicketDetailsDto): Ticket {
        val shop = shopRepository.findById(shopId).orElseThrow { throw Exception("") }
        val rwe = Ticket.create(shop, ticketId, ticketDetails)
        ticketRepository.save(rwe.result)
        ticketEventPublisher.publish(rwe.result, rwe.events)
        return rwe.result
    }

    fun cancelCreateTicket(ticketId: Long) {
        val ticket = ticketRepository.findById(ticketId).orElseThrow{ throw  Exception("") }
        val rwe = ticket.cancelCreate()
        ticketRepository.save(rwe.result)
        ticketEventPublisher.publish(rwe.result, rwe.events)
    }

    fun confirmCreateTicket(ticketId: Long) {
        val ticket = ticketRepository.findById(ticketId).orElseThrow{ throw  Exception("") }
        val rwe = ticket.confirmCreate()
        ticketRepository.save(rwe.result)
        ticketEventPublisher.publish(rwe.result, rwe.events)
    }
}