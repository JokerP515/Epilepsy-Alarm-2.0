package com.uan.epilepsyalarm20.ui.navigation.routes

import kotlinx.serialization.Serializable

@Serializable
sealed class Routes(val id: String) {

    @Serializable
    data object NotificacionInicial: Routes("notificacion_inicial")
    @Serializable
    data object Inicio : Routes("Inicio")
    @Serializable
    data object PerfilUsuario : Routes("Perfil")
    @Serializable
    data object ConfigAlarma : Routes("Config")
    @Serializable
    data object ConfigActivacionAlarma: Routes("configuracion_activacion_alarma")
    @Serializable
    data object ConfigSonidoAlarma: Routes("configuracion_sonido_alarma")
    @Serializable
    data object Informacion : Routes("Info")
    @Serializable
    data object Contactos: Routes("Contactos")
    @Serializable
    data object MenuPrincipal: Routes("menu_principal")
    @Serializable
    data object Emergencia: Routes("emergencia")
}
