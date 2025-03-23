package com.uan.epilepsyalarm20.ui.cards

import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

@Composable
fun SuccessSnackbar(
    showMessage: Boolean,
    onDismiss: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    if (showMessage) {
        LaunchedEffect(true) {
            scope.launch {
                snackbarHostState.showSnackbar("¡Información editada con éxito!")
            }
            onDismiss() // Llamar la función de dismiss después de mostrar el mensaje
        }
    }

    SnackbarHost(hostState = snackbarHostState)
}
