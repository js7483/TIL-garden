package com.example.shop.domain

import com.example.common.utils.Money
import com.example.common.utils.MoneyConverter
import javax.persistence.*

@Entity
@Table(name = "shops")
@Access(AccessType.FIELD)
data class Shop(

        @Id
        @GeneratedValue
        val id: Long = 0,

        val name: String,

        @Embedded
        @ElementCollection
        @CollectionTable(name = "shop_items")
        val items: List<ShopItem>
)

@Embeddable
@Access(AccessType.FIELD)
data class ShopItem(
        val id: Long,
        val name: String,

        @Convert(converter = MoneyConverter::class)
        val price: Money
)