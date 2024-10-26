package com.ceycourier.model

data class User(
    val username: String,
    val password: String // Note: This should ideally be a hashed password
)