package com.ceycourier.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.TextFieldValue
import com.ceycourier.model.Driver
import com.ceycourier.model.VehicleType

@Composable
fun DriverPage(drivers: List<Driver>, onAddDriver: () -> Unit) {
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    var selectedDriver by remember { mutableStateOf<Driver?>(null) }
    var showEditDialog by remember { mutableStateOf(false) }
    var showAddDialog by remember { mutableStateOf(false) }

    // Create a mutable state list to manage driver availability
    val driverList = remember { drivers.toMutableStateList() }

    val filteredDrivers = driverList.filter { driver ->
        driver.name.contains(searchQuery.text, ignoreCase = true) ||
                driver.email.contains(searchQuery.text, ignoreCase = true) ||
                driver.phone.contains(searchQuery.text, ignoreCase = true)
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Search Bar and Add Driver Button
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Search Drivers") },
                modifier = Modifier.weight(1f).padding(end = 8.dp)
            )
            Button(onClick = { showAddDialog = true }) {
                Text("Add Driver")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Driver Table
        LazyColumn {
            items(filteredDrivers) { driver ->
                DriverRow(driver,
                    onEdit = {
                        selectedDriver = driver
                        showEditDialog = true
                    },
                    onAvailabilityChange = { newAvailability ->
                        // Update the driver's availability
                        val index = driverList.indexOf(driver)
                        if (index != -1) {
                            driverList[index] = driver.copy(availability = newAvailability)
                        }
                    }
                )
            }
        }
    }

    // Show the edit dialog if needed
    if (showEditDialog && selectedDriver != null) {
        EditDriverDialog(
            driver = selectedDriver!!,
            onDismiss = { showEditDialog = false; selectedDriver = null },
            onSave = { updatedDriver ->
                // Handle the updated driver (e.g., update the list or database)
                val index = driverList.indexOf(selectedDriver)
                if (index != -1) {
                    driverList[index] = updatedDriver
                }
                showEditDialog = false
                selectedDriver = null
            }
        )
    }

    if (showAddDialog) {
        AddDriverDialog(
            onDismiss = { showAddDialog = false },
            onAdd = { newDriver ->
                // Add the new driver to the list
                driverList.add(newDriver.copy(id = driverList.size + 1)) // Simple ID generation
                showAddDialog = false
            }
        )
    }
}
@Composable
fun DriverRow(driver: Driver, onEdit: () -> Unit, onAvailabilityChange: (Boolean) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth().padding(8.dp).border(1.dp, MaterialTheme.colors.onSurface)) {
        Text(driver.id.toString(), modifier = Modifier.weight(1f).padding(8.dp))
        Text(driver.name, modifier = Modifier.weight(2f).padding(8.dp))
        Text(driver.email, modifier = Modifier.weight(2f).padding(8.dp))
        Text(driver.phone, modifier = Modifier.weight(2f).padding(8.dp))
        Text(driver.address, modifier = Modifier.weight(3f).padding(8.dp))

        // Availability Checkbox
        var isAvailable by remember { mutableStateOf(driver.availability) }
        Checkbox(
            checked = isAvailable,
            onCheckedChange = {
                isAvailable = it
                onAvailabilityChange(it) // Notify the change
            },
            modifier = Modifier.padding(8.dp)
        )

        Text(if (isAvailable) "Available" else "Unavailable", modifier = Modifier.weight(1f).padding(8.dp))
        Text(driver.vehicleType.name, modifier = Modifier.weight(1f).padding(8.dp))

        // Edit Button
        Button(onClick = onEdit, modifier = Modifier.padding(8.dp)) {
            Text("Edit")
        }
    }
}