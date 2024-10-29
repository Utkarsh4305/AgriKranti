package com.example.agrikranti.Navigation


import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.agrikranti.AdminScreens.UploadMspScreen
import com.example.agrikranti.CreateAccount
import com.example.agrikranti.MspScreen
import com.example.agrikranti.WeatherScreen
import com.example.agrikranti.AdminScreen
import com.example.agrikranti.AdminScreens.DeleteCropScreen
import com.example.agrikranti.Buyer.BuyerScreen
import com.example.agrikranti.screens.HomeScreen
import com.example.agrikranti.screens.LoginScreen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


@SuppressLint("SuspiciousIndentation")
@Composable
fun SetupNavGraph(
    navController: NavHostController,
    firestore: FirebaseFirestore,
) {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val activityContext = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
    val isAdmin = sharedPreferences.getBoolean("isAdmin", false)


    val startDestination = when {
        isLoggedIn && isAdmin -> Screen.AdminScreen.route
        isLoggedIn && !isAdmin -> Screen.HomeScreen.route
        else -> Screen.LoginScreen.route
    }


    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = Modifier.fillMaxSize()
    ) {
        composable(route = Screen.LoginScreen.route) {
            LoginScreen(navController = navController, onLoginSuccess = {
                navController.navigate(Screen.HomeScreen.route) {
                    popUpTo(Screen.LoginScreen.route) { inclusive = true }
                }
            })
        }
        composable(route = Screen.HomeScreen.route) {
            HomeScreen(navController)
        }
        composable(route = Screen.AdminScreen.route) {
            AdminScreen(navController = navController)
        }
        composable(route = Screen.CreateAccountScreen.route) {
            CreateAccount(navController = navController)
        }
        composable(route = Screen.WeatherScreen.route) {
            WeatherScreen(navController = navController)
        }
        composable(route = Screen.MspScreen.route) {
            MspScreen(navController = navController)
        }
        composable(route = Screen.UploadMspScreen.route) {
            UploadMspScreen(navController = navController)
        }
        composable(route = Screen.DeleteCropScreen.route) {
            DeleteCropScreen(navController = navController)
        }
        composable(route = Screen.BuyerScreen.route) {
            BuyerScreen(navController = navController)
        }
    }
}
