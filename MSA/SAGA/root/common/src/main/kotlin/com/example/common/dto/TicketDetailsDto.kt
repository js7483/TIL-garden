package com.example.common.dto

data class TicketDetailsDto(
        val lineItems: List<TicketLineItemDto>
)

data class TicketLineItemDto(
        val quantity: Int,
        val itemId: Long,
        val name: String
)