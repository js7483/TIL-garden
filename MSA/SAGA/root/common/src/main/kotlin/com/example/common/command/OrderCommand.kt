package com.example.order.sagaparticipatns.order

import io.eventuate.tram.commands.common.Command

class OrderCommand(
        val orderId: Long
): Command

class RejectOrderCommand(
        val orderId: Long
): Command

class ApproveOrderCommand(
        val orderId: Long
): Command