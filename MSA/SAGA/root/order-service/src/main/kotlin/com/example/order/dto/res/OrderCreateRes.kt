package com.example.order.dto.res

import com.example.order.domain.OrderState

data class OrderCreateRes(
        val orderId: Long,
        var orderState: OrderState
)
