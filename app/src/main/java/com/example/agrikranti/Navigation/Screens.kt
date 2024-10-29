package com.example.agrikranti.Navigation



sealed class Screen(val route: String) {
    object LoginScreen : Screen("Login_Screen")
    object HomeScreen : Screen("Home_Screen")
    object AdminScreen : Screen("Admin_Screen")
    object CreateAccountScreen : Screen("Create_Account_Screen")
    object WeatherScreen : Screen("Weather_Screen")
    object MspScreen : Screen("Msp_Screen")
    object UploadMspScreen : Screen("Upload_Screen")
    object DeleteCropScreen : Screen("Delete_Screen")
    object BuyerScreen : Screen("Buyer_Screen")
}
