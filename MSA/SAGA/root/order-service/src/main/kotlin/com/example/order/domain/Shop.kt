package com.example.order.domain

import com.example.common.utils.Money
import com.example.common.utils.MoneyConverter
import javax.persistence.*

@Entity
@Table(name = "order_service_shops")
data class Shop(
        @Id
        val id: Long,

        @Embedded
        @ElementCollection
        @CollectionTable(name = "order_service_shop_items")
        val shopItems: List<ShopItem>?,

        val name: String
) {
        fun findMenuItem(itemId: Long): ShopItem? {
                return shopItems?.find { it.id == itemId }
        }
}

@Embeddable
@Access(AccessType.FIELD)
data class ShopItem(
        val id: Long,
        val name: String,

        @Convert(converter = MoneyConverter::class)
        val price: Money
)