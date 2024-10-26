package com.ceycourier.database

import com.ceycourier.model.Driver


interface DriverDao {
    fun addDriver(driver: Driver): Boolean
    fun getAllDrivers(): List<Driver>
    fun updateDriver(driver: Driver): Boolean
}