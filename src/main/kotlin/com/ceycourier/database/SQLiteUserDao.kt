package com.ceycourier.database

import com.ceycourier.model.User

class SQLiteUserDao : UserDao {
    override fun getUser(username: String): User? {
        DatabaseManager.getConnection().use { conn ->
            val stmt = conn.prepareStatement("SELECT * FROM users WHERE username = ?")
            stmt.setString(1, username)
            val rs = stmt.executeQuery()
            if (rs.next()) {
                return User(rs.getString("username"), rs.getString("password"))
            }
        }
        return null
    }

    override fun addUser(user: User): Boolean {
        DatabaseManager.getConnection().use { conn ->
            val stmt = conn.prepareStatement("INSERT INTO users (username, password) VALUES (?, ?)")
            stmt.setString(1, user.username)
            stmt.setString(2, user.password)
            return stmt.executeUpdate() > 0
        }
    }
}