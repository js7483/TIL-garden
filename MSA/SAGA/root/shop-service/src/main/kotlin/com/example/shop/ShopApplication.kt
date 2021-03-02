package com.example.shop

import com.fasterxml.jackson.databind.ObjectMapper
import io.eventuate.common.json.mapper.JSonMapper
import io.eventuate.tram.spring.jdbckafka.TramJdbcKafkaConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Primary

@SpringBootApplication
@Import(TramJdbcKafkaConfiguration::class)
class ShopApplication {
    @Bean
    @Primary
    fun objectMapper(): ObjectMapper {
        return JSonMapper.objectMapper
    }
}

fun main(args: Array<String>) {
    runApplication<ShopApplication>(*args)
}