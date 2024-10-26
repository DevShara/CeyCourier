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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddDriverDialog(onDismiss: () -> Unit, onAdd: (Driver) -> Unit) {
    var name by remember { mutableStateOf(TextFieldValue("")) }
    var email by remember { mutableStateOf(TextFieldValue("")) }
    var phone by remember { mutableStateOf(TextFieldValue("")) }
    var address by remember { mutableStateOf(TextFieldValue("")) }
    var availability by remember { mutableStateOf(true) } // Default to available
    var vehicleType by remember { mutableStateOf(VehicleType.CAR) } // Default vehicle type
    var expanded by remember { mutableStateOf(false) } // State for dropdown menu

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Driver") },
        text = {
            Column {
                TextField(value = name.text, onValueChange = { name = TextFieldValue(it) }, label = { Text("Name") })
                TextField(value = email.text, onValueChange = { email = TextFieldValue(it) }, label = { Text("Email") })
                TextField(value = phone.text, onValueChange = { phone = TextFieldValue(it) }, label = { Text("Phone") })
                TextField(value = address.text, onValueChange = { address = TextFieldValue(it) }, label = { Text("Address") })

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
                            }) {
                                Text(type.name)
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                onAdd(Driver(0, name.text, email.text, phone.text, address.text, availability, vehicleType)) // ID can be generated later
                onDismiss()
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