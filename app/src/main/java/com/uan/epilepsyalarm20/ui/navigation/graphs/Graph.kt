package com.uan.epilepsyalarm20.ui.navigation.graphs

import kotlinx.serialization.Serializable

sealed class Graph {
    @Serializable
    data object AppGraph : Graph()
}