package com.example.common.utils

import javax.persistence.Embeddable

@Embeddable
data class Address(
    private val street1: String,
    private val street2: String,
    private val city: String,
    private val state: String,
    private val zip: String,
)