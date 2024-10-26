// src/main/kotlin/com/ceycourier/database/SQLiteDriverDao.kt
package com.ceycourier.database

import com.ceycourier.model.Driver
import com.ceycourier.model.VehicleType
import java.sql.Connection

class SQLiteDriverDao : DriverDao {
    override fun addDriver(driver: Driver): Boolean {
        val sql = "INSERT INTO drivers (name, email, phone, address, availability, vehicle_type) VALUES (?, ?, ?, ?, ?, ?)"
        DatabaseManager.getConnection().use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1, driver.name)
                stmt.setString(2, driver.email)
                stmt.setString(3, driver.phone)
                stmt.setString(4, driver.address)
                stmt.setBoolean(5, driver.availability)
                stmt.setString(6, driver.vehicleType.name)
                return stmt.executeUpdate() > 0
            }
        }
    }

    override fun getAllDrivers(): List<Driver> {
        val drivers = mutableListOf<Driver>()
        val sql = "SELECT * FROM drivers"
        DatabaseManager.getConnection().use { conn ->
            conn.createStatement().use { stmt ->
                val rs = stmt.executeQuery(sql)
                while (rs.next()) {
                    val driver = Driver(
                        id = rs.getInt("id"),
                        name = rs.getString("name"),
                        email = rs.getString("email"),
                        phone = rs.getString("phone"),
                        address = rs.getString("address"),
                        availability = rs.getBoolean("availability"),
                        vehicleType = VehicleType.valueOf(rs.getString("vehicle_type"))
                    )
                    drivers.add(driver)
                }
            }
        }
        return drivers
    }

    override fun updateDriver(driver: Driver): Boolean {
        val sql = "UPDATE drivers SET name = ?, email = ?, phone = ?, address = ?, availability = ?, vehicle_type = ? WHERE id = ?"
        DatabaseManager.getConnection().use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1, driver.name)
                stmt.setString(2, driver.email)
                stmt.setString(3, driver.phone)
                stmt.setString(4, driver.address)
                stmt.setBoolean(5, driver.availability)
                stmt.setString(6, driver.vehicleType.name)
                stmt.setInt(7, driver.id)
                return stmt.executeUpdate() > 0
            }
        }
    }
}