package com.uan.epilepsyalarm20.ui.navigation

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.uan.epilepsyalarm20.ui.navigation.graphs.Graph
import com.uan.epilepsyalarm20.ui.navigation.graphs.appGraph
import com.uan.epilepsyalarm20.ui.navigation.routes.Routes

@Composable
fun AppNavigation(startDestination: Routes) {
    val navController = rememberNavController()
    Surface (
        color = MaterialTheme.colorScheme.background
    ) {
        NavHost(
            navController = navController,
            startDestination = Graph.AppGraph
        ) {
            appGraph(
                startDestination = startDestination,
            ) { route ->
                navController.navigate(route)
            }
        }
    }
}