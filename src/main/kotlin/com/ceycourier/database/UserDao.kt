package com.ceycourier.database

import com.ceycourier.model.User

interface UserDao {
    fun getUser(username: String): User?
    fun addUser(user: User): Boolean
}