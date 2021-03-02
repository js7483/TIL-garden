package com.example.order.domain

enum class OrderState(val description: String) {
    APPROVAL_PENDING(""),
    APPROVED(""),
    REJECTED(""),
    CANCEL_PENDING(""),
    CANCELLED(""),
    REVISION_PENDING("")
}