package com.example.agrikranti

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.example.agrikranti.Navigation.SetupNavGraph
import com.example.agrikranti.ui.theme.AgriKrantiTheme
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        setContent {
            AgriKrantiTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val firestore = FirebaseFirestore.getInstance()

                    // Set the status bar color


                    // Setup the navigation graph
                    SetupNavGraph(navController = navController, firestore = firestore)
                }
            }
        }
    }
}


