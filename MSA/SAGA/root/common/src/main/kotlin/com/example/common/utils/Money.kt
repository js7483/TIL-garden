package com.example.common.utils

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.module.SimpleModule
import java.math.BigDecimal
import javax.persistence.AttributeConverter
import javax.persistence.Converter

// TODO: change BigDecimal to JSR 354: Money and Currency API
data class Money(
        val amount: BigDecimal
) {

    constructor(l: Long) : this(BigDecimal(l))

    constructor(i: Int): this(BigDecimal(i))

    companion object {
        val ZERO = Money(0L)
    }

    fun add(delta: Money): Money {
        return Money(amount.add(delta.amount))
    }

    fun isGreaterThanOrEqual(other: Money): Boolean {
        return amount >= other.amount
    }

    fun multiply(x: Int): Money {
        return Money(amount.multiply(BigDecimal(x)))
    }

}

@Converter
class MoneyConverter: AttributeConverter<Money?, BigDecimal?> {
    override fun convertToDatabaseColumn(attribute: Money?): BigDecimal? {
        return attribute?.amount
    }

    override fun convertToEntityAttribute(dbData: BigDecimal?): Money? {
        return dbData?.let { Money(it) }
    }
}

class MoneySerializer: JsonSerializer<Money>() {
    override fun serialize(value: Money?, gen: JsonGenerator?, serializers: SerializerProvider?) {
        gen?.writeObject(value?.amount?.toLong())
    }
}

class MoneyDeserializer: JsonDeserializer<Money>() {
    override fun deserialize(p: JsonParser?, ctxt: DeserializationContext?): Money {
        return Money(BigDecimal(p?.valueAsLong ?: 0L))
    }
}

fun getMoneyObjectModule(): SimpleModule {
    return SimpleModule().apply {
        addSerializer(Money::class.java, MoneySerializer())
        addDeserializer(Money::class.java, MoneyDeserializer())
    }
}