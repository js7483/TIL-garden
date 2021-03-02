package com.example.common.dto

import com.example.common.utils.Money

data class OrderDetailsDto(
        val lineItems: List<OrderLineItemDto>,
        val orderTotal: Money,
        val shopId: Long,
        val consumerId: Long
)

data class OrderLineItemDto(
        val quantity: Int,
        val itemId: Long,
        val name: String,
        val price: Money
)