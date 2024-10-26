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
import com.ceycourier.database.SQLiteDriverDao
import com.ceycourier.model.Driver
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun DriverPage() {
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    var selectedDriver by remember { mutableStateOf<Driver?>(null) }
    var showEditDialog by remember { mutableStateOf(false) }
    var showAddDialog by remember { mutableStateOf(false) }
    var snackbarVisible by remember { mutableStateOf(false) }
    var drivers by remember { mutableStateOf(listOf<Driver>()) } // State to hold drivers
    val driverDao = SQLiteDriverDao()
    val coroutineScope = rememberCoroutineScope()

    // Load drivers from the database
    LaunchedEffect(Unit) {
        drivers = driverDao.getAllDrivers() // Fetch drivers from the database
        println("Drivers loaded: ${drivers.size}") // Log the number of drivers loaded
    }

    // Create a mutable state list to manage driver availability
    val driverList = remember { drivers.toMutableStateList() }
    LaunchedEffect(drivers) {
        driverList.clear()
        driverList.addAll(drivers)
    }

    // Filter drivers based on search query
    val filteredDrivers = driverList.filter { driver ->
        driver.name.contains(searchQuery.text, ignoreCase = true) ||
                driver.email.contains(searchQuery.text, ignoreCase = true) ||
                driver.phone.contains(searchQuery.text, ignoreCase = true) ||
                driver.address.contains(searchQuery.text, ignoreCase = true)
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

        if (snackbarVisible) {
            Snackbar(
                action = {
                    Button(onClick = { snackbarVisible = false }) {
                        Text("Dismiss")
                    }
                }
            ) {
                Text("Driver added successfully!")
            }

            LaunchedEffect(snackbarVisible) {
                if (snackbarVisible) {
                    delay(3000)
                    snackbarVisible = false
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Driver Table
        LazyColumn {
            items(filteredDrivers) { driver ->
                println("Displaying driver: ${driver.name}") // Debugging output
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

    // Show the add dialog if needed
    if (showAddDialog) {
        AddDriverDialog(
            onDismiss = { showAddDialog = false },
            onSuccess = {
                snackbarVisible = true
                // Reload drivers from the database after adding a new driver
                coroutineScope.launch {
                    drivers = driverDao.getAllDrivers() // Fetch updated driver list
                    println("Drivers reloaded: ${drivers.size}") // Log the number of drivers reloaded
                }
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

        val driverDao = SQLiteDriverDao()


        // Availability Checkbox
        var isAvailable by remember { mutableStateOf(driver.availability) }
        Checkbox(
            checked = isAvailable,
            onCheckedChange = {
                isAvailable = it
                driverDao.updateDriver(driver.copy(availability = it)) // Update the driver in the database
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