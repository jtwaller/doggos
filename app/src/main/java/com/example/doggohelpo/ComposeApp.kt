package com.example.doggohelpo

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.doggohelpo.features.alldogs.AllDogsScreen
import com.example.doggohelpo.features.alldogs.MainViewModel
import com.example.doggohelpo.features.dogdetails.DogDetailsScreen

val LocalNavController = compositionLocalOf<NavHostController> { error("No NavController") }
val LocalMainViewModel = compositionLocalOf<MainViewModel> { error("No AllDogsViewModel") }

@Composable
fun App() {
    val navController = rememberNavController()
    val vm: MainViewModel = viewModel()
    CompositionLocalProvider(
        LocalNavController provides navController,
        LocalMainViewModel provides vm
    ) {
        NavHost(navController, startDestination = "alldogs") {
            composable("alldogs") { AllDogsScreen() }
            composable("dogdetails/{dogId}") { navBackStackEntry ->
                DogDetailsScreen(navBackStackEntry.arguments?.getString("dogId") ?: "-1")
            }
        }
    }

}