package com.uan.epilepsyalarm20.ui.navigation.graphs

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.uan.epilepsyalarm20.ui.navigation.routes.Routes
import com.uan.epilepsyalarm20.ui.screens.menu.ExplicationScreen
import com.uan.epilepsyalarm20.ui.screens.InitialNotificationScreen
import com.uan.epilepsyalarm20.ui.screens.menu.ActivationMethodScreen
import com.uan.epilepsyalarm20.ui.navigation.MainMenuNavigation
import com.uan.epilepsyalarm20.ui.screens.menu.RegisterScreen
import com.uan.epilepsyalarm20.ui.screens.menu.SoundSelectScreen

fun NavGraphBuilder.appGraph(
    startDestination: Routes,
    go: (Any) -> Unit
) {
    navigation<Graph.AppGraph>(startDestination = startDestination) {

        composable<Routes.MenuPrincipal> {
            MainMenuNavigation(rememberNavController())
        }
        // Notificación 1ra vez que se abre la App
        composable<Routes.NotificacionInicial> {
            InitialNotificationScreen(hiltViewModel(), hiltViewModel(), go)
        }

        composable<Routes.PerfilUsuario> {
            RegisterScreen(
                registerViewModel = hiltViewModel(),
                contactsViewModel = hiltViewModel(),
                go = go
            )
        }

        // La información de activación de la alarma
        composable<Routes.ConfigAlarma> {
            ExplicationScreen(null, go)
        }
        // Elegir método de activación de la alarma
        composable<Routes.ConfigActivacionAlarma> {
            ActivationMethodScreen(hiltViewModel(), null, go)
        }
        // Seleccionar el sonido de la alarma
        composable<Routes.ConfigSonidoAlarma> {
            SoundSelectScreen(hiltViewModel(), hiltViewModel(), null, go)
        }
    }
}