package com.uan.epilepsyalarm20.ui.navigation.graphs

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.uan.epilepsyalarm20.ui.navigation.routes.Routes
import com.uan.epilepsyalarm20.ui.screens.mainMenu.ExplicationScreen
import com.uan.epilepsyalarm20.ui.screens.mainMenu.InformationScreen
import com.uan.epilepsyalarm20.ui.screens.InitialNotificationScreen
import com.uan.epilepsyalarm20.ui.screens.mainMenu.ActivationMethodScreen
import com.uan.epilepsyalarm20.ui.screens.mainMenu.MainMenuScreen
import com.uan.epilepsyalarm20.ui.screens.mainMenu.RegisterScreen
import com.uan.epilepsyalarm20.ui.screens.mainMenu.SoundSelectScreen
import com.uan.epilepsyalarm20.ui.screens.mainMenu.StartScreen

fun NavGraphBuilder.appGraph(startDestination: Routes, go: (Any) -> Unit) {
    navigation<Graph.AppGraph>(startDestination = startDestination) {

        composable<Routes.MenuPrincipal> {
            MainMenuScreen(rememberNavController())
        }
        // Notificación 1ra vez que se abre la App
        composable<Routes.NotificacionInicial> {
            InitialNotificationScreen(hiltViewModel(), hiltViewModel(), go)
        }

        composable<Routes.PerfilUsuario> {
            RegisterScreen(hiltViewModel(), go)
        }
        composable<Routes.Inicio> {
            StartScreen(hiltViewModel())
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