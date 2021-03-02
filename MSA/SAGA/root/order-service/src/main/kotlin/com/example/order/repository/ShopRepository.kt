package com.example.order.repository

import com.example.order.domain.Shop
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ShopRepository: JpaRepository<Shop, Long>