package com.uan.epilepsyalarm20.ui.screens.mainMenu

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

import com.uan.epilepsyalarm20.ui.navigation.routes.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainMenuScreen(navController: NavHostController) {
    val items = listOf(
        Routes.Inicio to Icons.Default.Home,
        Routes.PerfilUsuario to Icons.Default.Person,
        Routes.Contactos to Icons.Default.Phone,
        Routes.ConfigAlarma to Icons.Default.Settings,
        Routes.Informacion to Icons.Default.Info
    )

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Menú Principal") })
        },
        bottomBar = {
            NavigationBar {
                val currentBackStackEntry = navController.currentBackStackEntryAsState()
                val currentRoute = currentBackStackEntry.value?.destination?.route

                items.forEach { (route, icon) ->
                    NavigationBarItem(
                        icon = { Icon(imageVector = icon, contentDescription = null) },
                        label = { Text(route.id) },
                        selected = currentRoute == route.id,
                        onClick = {
                            navController.navigate(route.id) {
                                popUpTo(Routes.Inicio.id) { inclusive = false }
                                launchSingleTop = true
                            }
                        }
                    )
                }
            }
        }

    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            NavHost(
                navController = navController,
                startDestination = Routes.Informacion.id
            ) {
                composable(Routes.Inicio.id) { StartScreen(hiltViewModel()) }
                composable(Routes.PerfilUsuario.id) { RegisterScreen(hiltViewModel(), {}, true) }
                composable(Routes.Contactos.id) { ContactsScreen(hiltViewModel()) }
                composable(Routes.Informacion.id) { InformationScreen() }
                composable(Routes.ConfigAlarma.id) { ExplicationScreen(navController) }
                composable(Routes.ConfigActivacionAlarma.id) { ActivationMethodScreen(hiltViewModel(), navController) }
                composable(Routes.ConfigSonidoAlarma.id) { SoundSelectScreen(hiltViewModel(), hiltViewModel(), navController) }
            }

        }
    }
}
