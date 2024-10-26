// src/main/kotlin/com/ceycourier/components/AddDriverDialog.kt
package com.ceycourier.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import com.ceycourier.model.Driver
import com.ceycourier.model.VehicleType
import com.ceycourier.database.SQLiteDriverDao
import java.util.regex.Pattern

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddDriverDialog(onDismiss: () -> Unit, onSuccess: () -> Unit) {
    var name by remember { mutableStateOf(TextFieldValue("")) }
    var email by remember { mutableStateOf(TextFieldValue("")) }
    var phone by remember { mutableStateOf(TextFieldValue("")) }
    var address by remember { mutableStateOf(TextFieldValue("")) }
    var availability by remember { mutableStateOf(true) } // Default to available
    var vehicleType by remember { mutableStateOf(VehicleType.CAR) } // Default vehicle type
    var expanded by remember { mutableStateOf(false) } // State for dropdown menu

    val driverDao = SQLiteDriverDao()

    // Validation messages
    var nameError by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }
    var phoneError by remember { mutableStateOf("") }
    var addressError by remember { mutableStateOf("") }
    var vehicleTypeError by remember { mutableStateOf("") } // New error for vehicle type

    val emailPattern = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Driver") },
        text = {
            Column {
                TextField(
                    value = name.text,
                    onValueChange = {
                        name = TextFieldValue(it)
                        nameError = "" // Clear error on change
                    },
                    label = { Text("Name") },
                    isError = nameError.isNotEmpty()
                )
                if (nameError.isNotEmpty()) {
                    Text(text = nameError, color = MaterialTheme.colors.error)
                }

                TextField(
                    value = email.text,
                    onValueChange = {
                        email = TextFieldValue(it)
                        emailError = "" // Clear error on change
                    },
                    label = { Text("Email") },
                    isError = emailError.isNotEmpty()
                )
                if (emailError.isNotEmpty()) {
                    Text(text = emailError, color = MaterialTheme.colors.error)
                }

                TextField(
                    value = phone.text,
                    onValueChange = {
                        phone = TextFieldValue(it)
                        phoneError = "" // Clear error on change
                    },
                    label = { Text("Phone") },
                    isError = phoneError.isNotEmpty()
                )
                if (phoneError.isNotEmpty()) {
                    Text(text = phoneError, color = MaterialTheme.colors.error)
                }

                TextField(
                    value = address.text,
                    onValueChange = {
                        address = TextFieldValue(it)
                        addressError = "" // Clear error on change
                    },
                    label = { Text("Address") },
                    isError = addressError.isNotEmpty()
                )
                if (addressError.isNotEmpty()) {
                    Text(text = addressError, color = MaterialTheme.colors.error)
                }

                // Availability Checkbox
                Row {
                    Text("Available")
                    Checkbox(checked = availability, onCheckedChange = { availability = it })
                }

                // Vehicle Type Dropdown
                ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
                    TextField(
                        value = vehicleType.name,
                        onValueChange = {},
                        label = { Text("Vehicle Type") },
                        readOnly = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { expanded = true }
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        VehicleType.values().forEach { type ->
                            DropdownMenuItem(onClick = {
                                vehicleType = type
                                expanded = false // Close the dropdown after selection
                                vehicleTypeError = "" // Clear error on selection
                            }) {
                                Text(type.name)
                            }
                        }
                    }
                }
                if (vehicleTypeError.isNotEmpty()) {
                    Text(text = vehicleTypeError, color = MaterialTheme.colors.error)
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                // Validate fields
                var isValid = true
                if (name.text.isEmpty()) {
                    nameError = "Name cannot be empty"
                    isValid = false
                }
                if (email.text.isEmpty() || !emailPattern.matcher(email.text).matches()) {
                    emailError = "Enter a valid email"
                    isValid = false
                }
                if (phone.text.isEmpty() || !phone.text.all { it.isDigit() }) {
                    phoneError = "Enter a valid phone number"
                    isValid = false
                }
                if (address.text.isEmpty()) {
                    addressError = "Address cannot be empty"
                    isValid = false
                }
                if (vehicleType == VehicleType.CAR) { // Assuming CAR is the default and needs to be changed
                    vehicleTypeError = "Please select a vehicle type"
                    isValid = false
                }

                if (isValid) {
                    val newDriver = Driver(0, name.text, email.text, phone.text, address.text, availability, vehicleType)
                    if (driverDao.addDriver(newDriver)) { // Check if adding was successful
                        onSuccess() // Notify success
                        // Clear fields after successful addition
                        name = TextFieldValue("")
                        email = TextFieldValue("")
                        phone = TextFieldValue("")
                        address = TextFieldValue("")
                        availability = true
                        vehicleType = VehicleType.CAR // Reset to default
                    }
                    onDismiss()
                }
            }) {
                Text("Add")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}