package com.uan.epilepsyalarm20.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.uan.epilepsyalarm20.R
import com.uan.epilepsyalarm20.ui.navigation.routes.Routes
import com.uan.epilepsyalarm20.ui.screens.menu.ActivationMethodScreen
import com.uan.epilepsyalarm20.ui.screens.menu.ConfigurationScreen
import com.uan.epilepsyalarm20.ui.screens.menu.ContactsScreen
import com.uan.epilepsyalarm20.ui.screens.menu.ExplicationScreen
import com.uan.epilepsyalarm20.ui.screens.menu.InformationScreen
import com.uan.epilepsyalarm20.ui.screens.menu.NewContactScreen
import com.uan.epilepsyalarm20.ui.screens.menu.RegisterScreen
import com.uan.epilepsyalarm20.ui.screens.menu.SoundSelectScreen
import com.uan.epilepsyalarm20.ui.screens.menu.StartScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainMenuNavigation(navController: NavHostController) {
    val items = listOf(
        Routes.Inicio to Icons.Outlined.Home,
        Routes.Config to Icons.Outlined.Settings,
        Routes.PerfilUsuario to Icons.Outlined.Person,
        Routes.Informacion to Icons.Outlined.Info
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.epilepsy_alarm)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
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
                            if(currentRoute != route.id) {
                                navController.navigate(route.id) {
                                    popUpTo(Routes.Inicio.id) { inclusive = false }
                                    launchSingleTop = true
                                }
                            }
                        }
                    )
                }
            }
        }

    ) { innerPadding ->
        Box (modifier = Modifier.padding(innerPadding)) {
            NavHost(
                navController = navController,
                startDestination = Routes.Informacion.id
            ) {
                composable(Routes.Inicio.id) { StartScreen(hiltViewModel(), navController) }
                composable(Routes.PerfilUsuario.id) { RegisterScreen(
                    registerViewModel = hiltViewModel(),
                    navController = navController,
                    boolean = true
                ) }
                composable(Routes.Contactos.id) { ContactsScreen(hiltViewModel(), navController) }
                composable(Routes.NuevoContacto.id) { NewContactScreen(hiltViewModel(), navController) }
                composable(Routes.Informacion.id) { InformationScreen() }
                composable(Routes.Config.id) { ConfigurationScreen(navController) }
                composable(Routes.ConfigAlarma.id) { ExplicationScreen(navController, {}, true) }
                composable(Routes.ConfigActivacionAlarma.id) {
                    ActivationMethodScreen(
                        hiltViewModel(),
                        navController,
                        {},
                        true
                    )
                }
                composable(Routes.ConfigSonidoAlarma.id) {
                    SoundSelectScreen(
                        hiltViewModel(),
                        hiltViewModel(),
                        navController,
                        {},
                        true
                    )
                }
            }
        }
    }
}
