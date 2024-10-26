package com.ceycourier.model

data class Driver(
    val id: Int,                // Unique identifier for the driver
    val name: String,           // Driver's name
    val email: String,          // Driver's email address
    val phone: String,          // Driver's phone number
    val address: String,        // Driver's address
    val availability: Boolean,   // Driver's availability status
    val vehicleType: VehicleType // Type of vehicle the driver uses
)

enum class VehicleType {
    CAR,
    VAN_SUV,
    CARGOVAN,
    TRUCK
}