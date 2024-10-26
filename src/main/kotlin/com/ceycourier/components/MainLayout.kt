package com.ceycourier.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.ceycourier.AppScreens
import com.ceycourier.components.Dashboard
import com.ceycourier.components.Orders
import com.ceycourier.model.Driver
import com.ceycourier.model.VehicleType

@Composable
fun MainLayout() {
    var currentScreen by remember { mutableStateOf(AppScreens.Dashboard) }
    val items = listOf(
        NavigationItem("Dashboard", Icons.Filled.Dashboard, AppScreens.Dashboard),
        NavigationItem("Orders", Icons.Filled.ShoppingCart, AppScreens.Orders),
        NavigationItem("Drivers", Icons.Filled.DirectionsCar, AppScreens.Drivers),
        NavigationItem("Customers", Icons.Filled.People, AppScreens.Customers)
    )

    MaterialTheme {
        Row(modifier = Modifier.fillMaxSize()) {
            Drawer(items, currentScreen) { screen ->
                currentScreen = screen
            }
            Box(modifier = Modifier.fillMaxSize().padding(10.dp)) {
                val drivers = listOf(
                    Driver(1, "John Doe", "john@example.com", "123-456-7890", "123 Main St, Anytown, USA", true, VehicleType.CAR),
                    Driver(2, "Jane Smith", "jane@example.com", "987-654-3210", "456 Elm St, Othertown, USA", false, VehicleType.VAN_SUV),
                    Driver(3, "Alice Johnson", "alice@example.com", "555-123-4567", "789 Oak St, Sometown, USA", true, VehicleType.CARGOVAN),
                    Driver(4, "Bob Brown", "bob@example.com", "444-987-6543", "321 Pine St, Anycity, USA", true, VehicleType.TRUCK)
                )
                when (currentScreen) {
                    AppScreens.Dashboard -> Dashboard()
                    AppScreens.Orders -> Orders()
                    AppScreens.Drivers -> DriverPage(drivers, onAddDriver = {})
                    AppScreens.Customers -> Orders()
                }
            }
        }
    }
}

@Composable
fun Drawer(items: List<NavigationItem>, currentScreen: AppScreens, onItemSelected: (AppScreens) -> Unit) {

    Column(

        modifier = Modifier.width(200.dp).background(color = MaterialTheme.colors.background) .fillMaxHeight().verticalScroll(rememberScrollState())
    ) {
        items.forEach { item ->
            DrawerButton(item.title, item.icon, item.screen, currentScreen == item.screen) {
                onItemSelected(item.screen)
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DrawerButton(text: String, icon: ImageVector, screen: AppScreens, isSelected: Boolean, onClick: () -> Unit) {
    val backgroundColor = if (isSelected) MaterialTheme.colors.primary.copy(alpha = 0.12f) else MaterialTheme.colors.surface
    ListItem(
        modifier = Modifier.fillMaxWidth().background(backgroundColor).clickable(onClick = onClick),
        icon = { Icon(icon, contentDescription = null) },
        text = { Text(text) }
    )
}

data class NavigationItem(val title: String, val icon: ImageVector, val screen: AppScreens)