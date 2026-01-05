package com.team.moblocation.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {
    @Serializable object Home: Screen()
    @Serializable object Login: Screen()
    @Serializable object Register: Screen()
}