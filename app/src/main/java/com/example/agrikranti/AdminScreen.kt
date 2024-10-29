package com.example.agrikranti


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.agrikranti.Navigation.Screen
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AdminScreen(navController: NavHostController) {
    var showDialog by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title
        Text(
            text = "Admin Panel",
            color = Color.White,
            fontSize = 32.sp,
            modifier = Modifier
                .padding(bottom = 32.dp)
                .align(Alignment.CenterHorizontally)
        )

        // Button to navigate to view reports
        Button(
            onClick = {
                navController.navigate(Screen.DeleteCropScreen.route)
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(text = "Delete Crops", color = Color.White)
        }

        // Button to navigate to settings
        Button(
            onClick = {

                navController.navigate(Screen.UploadMspScreen.route)
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(text = "Upload Crops", color = Color.White)
        }
        Button(
            onClick = { showDialog = true }, // Show dialog on click
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1F1F1F)), // Red color for logout
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
        ) {
            Text(text = "Log Out", color = Color.White, fontSize = 20.sp)
        }
        // Confirmation Dialog
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            // Log out and navigate to LoginScreen
                            FirebaseAuth.getInstance().signOut()
                            navController.navigate(Screen.LoginScreen.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    inclusive = true
                                }
                            }
                            showDialog = false
                        }
                    ) {
                        Text("Confirm")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text("Cancel")
                    }
                },
                title = { Text("Log Out") },
                text = { Text("Are you sure you want to log out?") }
            )
        }
    }

}


@Preview(showBackground = true)
@Composable
fun AdminScreenPreview() {
    // This preview will not show actual navigation since NavHostController is not provided
}
