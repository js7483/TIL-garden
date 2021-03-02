package com.example.common.utils

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

object JacksonMapper {

    private val mapper: ObjectMapper = jacksonObjectMapper()
            .registerModule(getMoneyObjectModule())

    fun objectMapper() = mapper
}
