package com.example.common.config

import com.example.common.utils.getMoneyObjectModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.eventuate.common.json.mapper.JSonMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.annotation.PostConstruct


@Configuration
class CommonConfig {
    @Bean
    fun commonJsonMapperInitializer(): CommonJsonMapperInitializer {
        return CommonJsonMapperInitializer()
    }
}

class CommonJsonMapperInitializer {
    @PostConstruct
    fun initialize() {
        registerModule()
    }
    companion object {
        fun registerModule() {
            JSonMapper.objectMapper
                    .registerModule(KotlinModule())
                    .registerModule(getMoneyObjectModule())
        }
    }
}