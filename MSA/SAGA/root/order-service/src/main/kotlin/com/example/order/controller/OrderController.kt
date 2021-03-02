package com.example.order.controller

import com.example.order.domain.DeliveryInformation
import com.example.order.dto.req.OrderCreateReq
import com.example.order.dto.res.OrderCreateRes
import com.example.order.service.OrderService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("orders")
class OrderController(
        private val orderService: OrderService
) {

    @PostMapping
    fun createOrder(@RequestBody orderCreateReq: OrderCreateReq): ResponseEntity<OrderCreateRes> {

        val order = orderService.createOrder(
                orderCreateReq.consumerId,
                orderCreateReq.shopId,
                DeliveryInformation(orderCreateReq.deliveryTime, orderCreateReq.deliveryAddress),
                orderCreateReq.lineItems
        )
        return ResponseEntity(OrderCreateRes(order.id, order.orderState), HttpStatus.OK)
    }
}