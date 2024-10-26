package com.ceycourier.services

import com.ceycourier.database.SQLiteUserDao
import com.ceycourier.database.UserDao
import com.ceycourier.model.User

object AuthService {
    private val userDao: UserDao = SQLiteUserDao()

    fun authenticate(username: String, password: String): Boolean {
        val user = userDao.getUser(username)
        return user != null && checkPassword(password, user.password)
    }

    private fun checkPassword(inputPassword: String, storedPassword: String): Boolean {
        // Implement password verification logic (e.g., bcrypt)
        return inputPassword == storedPassword // Replace with proper password checking
    }
}