package com.uan.epilepsyalarm20.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.uan.designsystem.uikit.components.UanAppBar
import com.uan.epilepsyalarm20.R
import com.uan.epilepsyalarm20.ui.navigation.routes.Routes
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
    val navItems = listOf(
        Routes.Config to Icons.Outlined.Settings,
        Routes.Inicio to Icons.Outlined.Home,
        Routes.PerfilUsuario to Icons.Outlined.Person
    )
    val currentBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry.value?.destination?.route
    val selectedIndex = navItems.indexOfFirst { it.first.id == currentRoute }

    Scaffold(
        topBar = {
            UanAppBar(
                title = stringResource(R.string.epilepsy_alarm),
                modifier = Modifier.padding(
                    top = WindowInsets.systemBars.asPaddingValues().calculateTopPadding()
                )
            )
        },
        bottomBar = {
            CustomUanToolbar(
                selectedIndex = selectedIndex,
                onItemSelected = { index ->
                    val targetRoute = navItems[index].first.id

                    if (currentRoute != targetRoute) {
                        navController.navigate(targetRoute) {
                            popUpTo(Routes.Inicio.id) { inclusive = false }
                            launchSingleTop = true
                        }
                    }
                },
                items = navItems.map { (route, icon) ->
                    CustomToolbarItemData(
                        contentDescription = route.id,
                        icon = {
                            Icon(
                                imageVector = icon,
                                contentDescription = null
                            )
                        }
                    )
                }
            )
        }
    ) { innerPadding ->
        Box (modifier = Modifier.padding(innerPadding)) {
            NavHost(
                navController = navController,
                startDestination = Routes.Inicio.id
            ) {
                composable(Routes.Inicio.id) { StartScreen() }
                composable(Routes.PerfilUsuario.id) { RegisterScreen(
                    registerViewModel = hiltViewModel(),
                    navController = navController,
                    boolean = true,
                    contactsViewModel = hiltViewModel(),
                    emergencyViewModel = hiltViewModel()
                ) }
                composable(Routes.Contactos.id) { ContactsScreen(hiltViewModel(),emergencyViewModel = hiltViewModel(), navController) }
                composable(Routes.NuevoContacto.id) { NewContactScreen(hiltViewModel(), navController) }
                composable(Routes.Informacion.id) { InformationScreen(navController) }
                composable(Routes.Config.id) { ConfigurationScreen(navController) }
                composable(Routes.ConfigAlarma.id) { ExplicationScreen(navController=navController, boolean = true) }
//                composable(Routes.ConfigActivacionAlarma.id) {
//                    ActivationMethodScreen(
//                        hiltViewModel(),
//                        navController,
//                        {},
//                        true
//                    )
//                }
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
