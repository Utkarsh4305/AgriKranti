package com.example.agrikranti.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.agrikranti.Navigation.Screen
import com.example.agrikranti.R
import com.google.firebase.auth.FirebaseAuth

@Composable
fun HomeScreen(navController: NavHostController) {
    var showDialog by remember { mutableStateOf(false) } // State to control dialog visibility
    val fricBergenFontFamily = FontFamily(
        Font(R.font.panton_black_caps, FontWeight.Normal)
    )

    Scaffold(
        bottomBar = {
            BottomAppBar(
                containerColor = Color(0xFF1F1F1F), // Dark color for the bottom app bar
                contentColor = Color.White,
                contentPadding = PaddingValues(horizontal = 32.dp), // Increase padding to spread icons
                content = {
                    IconButton(onClick = {  }) {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Home",
                            tint = Color.White,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                    Spacer(Modifier.weight(1f)) // Spacers to spread icons

                    // Using drawable for the second icon (MSP)
                    IconButton(onClick = { navController.navigate(Screen.MspScreen.route) }) {
                        Icon(
                            painter = painterResource(id = R.drawable.msp),
                            contentDescription = "MSP",
                            tint = Color.White,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                    Spacer(Modifier.weight(1f))

                    // Using drawable for the third icon (Weather)
                    IconButton(onClick = { navController.navigate(Screen.WeatherScreen.route) }) {
                        Icon(
                            painter = painterResource(id = R.drawable.weather),
                            contentDescription = "Weather",
                            tint = Color.White,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                    Spacer(Modifier.weight(1f))

                    IconButton(onClick = { /* Handle Account Icon Click */ }) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Account",
                            tint = Color.White,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(paddingValues) // Account for bottom app bar height
        ) {
            // Text at the top
            Text(
                text = "Agri Kranti",
                color = Color.White,
                fontSize = 42.sp,
                fontFamily = fricBergenFontFamily,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 64.dp)
            )

            // Column to hold the buttons at the bottom
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 80.dp) // Padding to make space for the BottomAppBar
            ) {
                // Row for Finished Goods and Raw Material buttons
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    // Finished Goods Button
                    Button(
                        onClick = { /* Navigate to Finished Goods */ },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF707070)), // Lighter blue for contrast
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 8.dp)
                            .height(60.dp) // Increased height for more impact
                    ) {
                        Text(text = "FG", color = Color.White, fontSize = 32.sp)
                    }

                    // Raw Material Button
                    Button(
                        onClick = { /* Navigate to Raw Material */ },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF707070)), // Yellow for attention
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 8.dp)
                            .height(60.dp) // Increased height for more impact
                    ) {
                        Text(text = "RM", color = Color.White, fontSize = 32.sp)
                    }
                }

                // Logout Button
                Button(
                    onClick = { showDialog = true }, // Show dialog on click
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1F1F1F)), // Dark color for logout
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                ) {
                    Text(text = "Log Out", color = Color.White, fontSize = 20.sp)
                }
            }
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
fun HomeScreenPreview() {
    HomeScreen(navController = rememberNavController())
}
