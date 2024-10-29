package com.example.agrikranti

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.WbCloudy
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.accompanist.permissions.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun WeatherScreen(navController: NavHostController) {
    val context = LocalContext.current
    var weather by remember { mutableStateOf<WeatherResponse?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Handling location permissions using the updated Accompanist API
    val locationPermissionsState = rememberMultiplePermissionsState(
        listOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )

    // Automatically trigger the permission request on entering the screen
    LaunchedEffect(Unit) {
        if (!locationPermissionsState.allPermissionsGranted) {
            locationPermissionsState.launchMultiplePermissionRequest()
        }
    }

    // Checking if permission is granted
    if (locationPermissionsState.allPermissionsGranted) {
        LaunchedEffect(Unit) {
            getCurrentLocation(context) { location ->
                fetchWeatherData(location.latitude, location.longitude, onSuccess = {
                    weather = it
                }, onError = {
                    errorMessage = it
                })
            }
        }

        // Display weather data
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            when {
                weather != null -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Location: ${weather!!.name}",
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            text = "${weather!!.main.temp}°C",
                            style = MaterialTheme.typography.displayLarge
                        )
                        Text(
                            text = weather!!.weather[0].description.capitalize(),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.secondary
                        )

                        WeatherIcon(description = weather!!.weather[0].description)

                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
                errorMessage != null -> {
                    Text(
                        text = errorMessage ?: "Unknown error",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                else -> {
                    CircularProgressIndicator()
                }
            }
        }
    } else {
        // If permission isn't granted, show message to enable permission
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Location permission is required to fetch weather data.")
        }
    }
}

@Composable
fun WeatherIcon(description: String) {
    val icon = when {
        description.contains("clear", ignoreCase = true) -> Icons.Filled.WbSunny
        description.contains("cloud", ignoreCase = true) -> Icons.Filled.Cloud
        description.contains("rain", ignoreCase = true) -> Icons.Filled.WbCloudy
        else -> Icons.Filled.Cloud
    }

    Icon(
        imageVector = icon,
        contentDescription = description,
        tint = MaterialTheme.colorScheme.primary,
        modifier = Modifier.size(80.dp)
    )
}

fun fetchWeatherData(
    lat: Double,
    lon: Double,
    onSuccess: (WeatherResponse) -> Unit,
    onError: (String) -> Unit
) {
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://api.openweathermap.org/data/2.5/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiKey = "9d6b957e66bdaa6a847db6268717538e"
    val weatherApi = retrofit.create(WeatherApi::class.java)

    CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = weatherApi.getWeather(lat, lon, apiKey)
            withContext(Dispatchers.Main) {
                onSuccess(response)
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                onError(e.message ?: "An error occurred")
            }
        }
    }
}

interface WeatherApi {
    @GET("weather")
    suspend fun getWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric"
    ): WeatherResponse
}

data class WeatherResponse(
    val main: Main,
    val weather: List<Weather>,
    val name: String
)

data class Main(
    val temp: Float
)

data class Weather(
    val description: String
)

@SuppressLint("MissingPermission")
fun getCurrentLocation(context: Context, onLocationReceived: (Location) -> Unit) {
    val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

    val cancellationTokenSource = CancellationTokenSource()
    val currentLocationTask: Task<Location> = fusedLocationClient.getCurrentLocation(
        com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY,
        cancellationTokenSource.token
    )

    currentLocationTask.addOnSuccessListener { location: Location? ->
        location?.let {
            onLocationReceived(it)
        }
    }.addOnFailureListener {
        // Handle failure case here
    }
}
