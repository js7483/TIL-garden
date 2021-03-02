package com.example.order.event

import com.example.common.utils.Money
import io.eventuate.tram.events.common.DomainEvent

interface ShopDomainEvent: DomainEvent

class ShopCreatedEvent(
        val shopId: Long,
        val name: String,
        val shopItems: List<ShopItemDto>
): ShopDomainEvent

data class ShopItemDto(
        val id: Long,
        val name: String,
        val price: Money
)