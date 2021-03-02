package com.example.order.dto.req

import com.example.common.utils.Address
import com.example.common.utils.Money
import java.time.LocalDateTime

data class OrderCreateReq(
        val consumerId: Long,
        val shopId: Long,
        val lineItems: List<LineItem>,
        val deliveryAddress: Address,
        val deliveryTime: LocalDateTime = LocalDateTime.now()
)

data class LineItem(
        val itemId: Long,
        val quantity: Int
)

data class OrderTestReq(
        val consumerId: Long,
        val price: Money
)