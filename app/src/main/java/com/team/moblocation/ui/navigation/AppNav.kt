package com.team.moblocation.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.team.moblocation.ui.screens.login.LoginScreen
import com.team.moblocation.ui.screens.register.RegisterScreen
import com.team.moblocation.ui.screens.home.HomeScreen

@Composable
fun AppNav() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.Login
    ) {
        composable<Screen.Home> {
            HomeScreen(navController)
        }

        composable<Screen.Login> {
            LoginScreen(navController)
        }

        composable<Screen.Register> {
            RegisterScreen(navController)
        }
    }
}