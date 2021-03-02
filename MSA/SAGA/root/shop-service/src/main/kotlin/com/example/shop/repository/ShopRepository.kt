package com.example.shop.repository

import com.example.shop.domain.Shop
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ShopRepository: JpaRepository<Shop, Long>