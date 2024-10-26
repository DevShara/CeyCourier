package com.ceycourier.database

import java.sql.Connection
import java.sql.DriverManager

object DatabaseManager {
    private const val url = "jdbc:sqlite:ceycourier.db"

    init {
        initializeDatabase()
    }

    private fun initializeDatabase() {
        getConnection().use { conn ->
            conn.createStatement().use { stmt ->
                stmt.execute(
                    """
                    CREATE TABLE IF NOT EXISTS users (
                        username TEXT PRIMARY KEY,
                        password TEXT NOT NULL
                    );
                    """
                )
            }
        }
    }

    fun getConnection(): Connection {
        return DriverManager.getConnection(url)
    }
}