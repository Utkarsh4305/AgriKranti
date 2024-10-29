package com.example.agrikranti.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.agrikranti.Navigation.Screen
import com.example.agrikranti.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun LoginScreen(onLoginSuccess: () -> Unit, navController: NavHostController) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val passwordFocusRequester = remember { FocusRequester() }

    val auth = FirebaseAuth.getInstance()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val fricBergenFontFamily = FontFamily(
        Font(R.font.panton_black_caps, FontWeight.Normal)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE8F5E9)) // Light green background similar to the image
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Spacer to push content down
        Spacer(modifier = Modifier.height(16.dp))

        // Display the app title at the top
        Text(
            text = "Agri Kranti",
            color = Color(0xFF4CAF50), // Earthy green color matching agriculture theme
            fontFamily = fricBergenFontFamily,
            fontSize = 48.sp,
            modifier = Modifier
                .padding(bottom = 16.dp)
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Adding the image and making it larger
        Image(
            painter = painterResource(id = R.drawable.farmer), // Make sure the image has transparent background
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp) // Adjust height for proper sizing
                .padding(horizontal = 8.dp)
                .background(Color(0xFFE8F5E9).copy(alpha = 0.85f)), // Semi-transparent background to blend with the image
            contentScale = ContentScale.FillWidth  // Fill width to avoid cropping
        )


        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFE8F5E9))  // Light green background continues
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.Center
            ) {
                // Row for User ID with image
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        value = email,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "",
                                tint = Color.Gray
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(onNext = {
                            focusManager.moveFocus(FocusDirection.Down)
                        }),
                        label = { Text(text = "User ID") },
                        shape = RoundedCornerShape(24.dp),
                        singleLine = true,
                        onValueChange = { email = it }
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Row for Password with image
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        value = password,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "",
                                tint = Color.Gray
                            )
                        },

                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(passwordFocusRequester),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(onDone = {
                            keyboardController?.hide()
                            validateAndSignIn(email, password, context, navController)
                        }),
                        visualTransformation = PasswordVisualTransformation(),
                        label = { Text(text = "Password") },
                        shape = RoundedCornerShape(24.dp),
                        singleLine = true,
                        onValueChange = { password = it }
                    )
                }

                Spacer(modifier = Modifier.height(30.dp))

                Button(
                    onClick = { validateAndSignIn(email, password, context, navController) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4CAF50), // Agriculture-themed green button color
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .align(Alignment.CenterHorizontally),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Text(
                        text = "Log In",
                        color = White,
                        modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = { navController.navigate(Screen.CreateAccountScreen.route) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4CAF50), // Agriculture-themed green button color
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .align(Alignment.CenterHorizontally),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Text(
                        text = "Create Account",
                        color = White,
                        modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
                    )
                }
            }
        }
    }
}




private fun validateAndSignIn(
    email: String,
    password: String,
    context: Context,
    navController: NavHostController
) {
    val db = FirebaseFirestore.getInstance()
    val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()

    // Query the "Admin" collection to find a document with the given email as its name
    db.collection("Admin").whereEqualTo(FieldPath.documentId(), email).get()
        .addOnSuccessListener { documents ->
            if (!documents.isEmpty) {
                val document = documents.documents[0]
                val adminPassword = document.getString("Password") ?: ""
                val adminName = document.getString("Name") ?: "Admin"  // Get the name field
                if (adminPassword == password) {
                    // Save login state, user type, and name in SharedPreferences
                    editor.putBoolean("isLoggedIn", true)
                    editor.putBoolean("isAdmin", true)
                    editor.putString("userName", adminName)  // Save the admin's name
                    editor.apply()

                    // Navigate to the admin screen if the password matches
                    navController.navigate(Screen.AdminScreen.route)
                } else {
                    Toast.makeText(context, "Invalid credentials", Toast.LENGTH_SHORT).show()
                }
            } else {
                db.collection("Users").whereEqualTo(FieldPath.documentId(), email).get()
                    .addOnSuccessListener { userDocuments ->
                        if (!userDocuments.isEmpty) {
                            val userDocument = userDocuments.documents[0]
                            val userPassword = userDocument.getString("Password") ?: ""
                            val userName = userDocument.getString("Name") ?: "User"  // Get the name field
                            if (userPassword == password) {
                                // Save login state, user type, and name in SharedPreferences
                                editor.putBoolean("isLoggedIn", true)
                                editor.putBoolean("isAdmin", false)
                                editor.putString("userName", userName)  // Save the user's name
                                editor.apply()

                                // Navigate to the home screen if the user is a regular user
                                navController.navigate(Screen.HomeScreen.route)
                            } else {
                                Toast.makeText(
                                    context,
                                    "Invalid credentials",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            Toast.makeText(
                                context,
                                "User data not found",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    .addOnFailureListener {
                        Toast.makeText(
                            context,
                            "Failed to retrieve user data",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }
        }
        .addOnFailureListener {
            Toast.makeText(context, "Failed to retrieve admin data", Toast.LENGTH_SHORT).show()
        }
}

fun logoutUser(context: Context) {
    val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.clear() // Clears all saved data
    editor.apply()

    // Navigate to the LoginScreen or perform additional logout actions
}



@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    //Preview with a mock NavController
}
