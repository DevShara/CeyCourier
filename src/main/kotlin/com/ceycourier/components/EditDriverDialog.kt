package com.ceycourier.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import com.ceycourier.model.Driver
import com.ceycourier.model.VehicleType

@Composable
fun EditDriverDialog(driver: Driver, onDismiss: () -> Unit, onSave: (Driver) -> Unit) {
    var name by remember { mutableStateOf(driver.name) }
    var email by remember { mutableStateOf(driver.email) }
    var phone by remember { mutableStateOf(driver.phone) }
    var address by remember { mutableStateOf(driver.address) }
    var availability by remember { mutableStateOf(driver.availability) }
    var vehicleType by remember { mutableStateOf(driver.vehicleType) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Driver") },
        text = {
            Column {
                TextField(value = name, onValueChange = { name = it }, label = { Text("Name") })
                TextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
                TextField(value = phone, onValueChange = { phone = it }, label = { Text("Phone") })
                TextField(value = address, onValueChange = { address = it }, label = { Text("Address") })
                
                // Availability Checkbox
                Row {
                    Text("Available")
                    Checkbox(checked = availability, onCheckedChange = { availability = it })
                }

                // Vehicle Type Dropdown (or you can use a TextField for simplicity)
                TextField(value = vehicleType.name, onValueChange = { 
                    vehicleType = VehicleType.valueOf(it.uppercase()) // Ensure the input matches the enum
                }, label = { Text("Vehicle Type (CAR, VAN_SUV, CARGOVAN, TRUCK)") })
            }
        },
        confirmButton = {
            Button(onClick = {
                onSave(driver.copy(name = name, email = email, phone = phone, address = address, availability = availability, vehicleType = vehicleType))
                onDismiss()
            }) {
                Text("Save")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}