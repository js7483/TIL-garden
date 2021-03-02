package com.example.order.domain

import com.example.common.utils.Address
import java.time.LocalDateTime
import javax.persistence.*

@Access(AccessType.FIELD)
@Embeddable
data class DeliveryInformation(
        val deliveryTime: LocalDateTime,

        @Embedded
        @AttributeOverrides(AttributeOverride(name = "state", column = Column(name = "delivery_state")))
        val deliveryAddress: Address
)