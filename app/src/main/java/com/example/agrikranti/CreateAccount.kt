package com.example.agrikranti



import android.content.Context
import android.content.res.Configuration
import android.util.Log
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
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.agrikranti.Navigation.Screen
import com.example.agrikranti.ui.theme.colorWhite
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateAccount(navController: NavController) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val passwordFocusRequester = remember { FocusRequester() }
    val fricBergenFontFamily = FontFamily(
        Font(R.font.panton_black_caps, FontWeight.Normal)
    )
    // Firebase Auth and Firestore instances
    val auth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE8F5E9))
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.BottomCenter)
                .padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Agri Kranti",
                color = Color(0xFF4CAF50), // Earthy green color matching agriculture theme
                fontFamily = fricBergenFontFamily,
                fontSize = 48.sp,
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(8.dp))
            // Add an image at the top of the screen
            Image(
                painter = painterResource(id = R.drawable.tractor), // Replace with your image resource
                contentDescription = "App Logo",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(550.dp) // Adjust height based on the space needed
                    .padding(bottom = 20.dp), // Padding to provide space below the image for buttons
                contentScale = ContentScale.Crop,

            )


            var username by remember { mutableStateOf("") }
            var userpassword by remember { mutableStateOf("") }

            // Username TextField
            TextField(
                value = username,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = "",
                        tint = Color.Gray
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                label = { Text(text = "Username") },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(24.dp),
                singleLine = true,
                onValueChange = {
                    username = it
                }
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Password TextField
            TextField(
                value = userpassword,
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
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                        checkAndCreateAccount(
                            username = username,
                            password = userpassword,
                            firestore = firestore,
                            navController = navController,
                            context = context
                        )
                    }
                ),
                visualTransformation = PasswordVisualTransformation(),
                label = { Text(text = "Password") },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(24.dp),
                singleLine = true,
                onValueChange = {
                    userpassword = it
                }
            )

            Spacer(modifier = Modifier.height(15.dp))

            // Create Account Button
            Button(
                onClick = {
                    checkAndCreateAccount(
                        username = username,
                        password = userpassword,
                        firestore = firestore,
                        navController = navController,
                        context = context
                    )
                    navController.navigate(Screen.LoginScreen.route)

                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4CAF50), // Agriculture-themed green button color
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(24.dp)
            ) {
                Text(
                    text = "Create New Account",
                    color = colorWhite,
                    modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))



        }
    }
}
private fun checkAndCreateAccount(
    username: String,
    password: String,
    firestore: FirebaseFirestore,
    navController: NavController,
    context: Context
) {
    if (username.isEmpty()) {
        Toast.makeText(context, "Username cannot be empty", Toast.LENGTH_SHORT).show()
        return
    }

    if (password.isEmpty()) {
        Toast.makeText(context, "Password cannot be empty", Toast.LENGTH_SHORT).show()
        return
    }

    // Check if the username exists in Firestore
    firestore.collection("Users").document(username)
        .get()
        .addOnSuccessListener { document ->
            if (document.exists()) {
                // If the username already exists in Firestore, show an error message
                Toast.makeText(context, "Username already exists", Toast.LENGTH_SHORT).show()
            } else {
                // Username doesn't exist, proceed to save user data in Firestore
                val userMap = hashMapOf(
                    "password" to password // Be cautious about storing passwords in plain text
                )
                firestore.collection("Users").document(username)
                    .set(userMap)
                    .addOnCompleteListener { saveTask ->
                        if (saveTask.isSuccessful) {
                            // Data saved successfully, navigate to the home screen
                            navController.popBackStack()
                            navController.navigate(Screen.HomeScreen.route)
                        } else {
                            // Failed to save user data, show an error message
                            val errorMessage = saveTask.exception?.message ?: "Unknown error"
                            Log.e("FirestoreError", errorMessage)
                            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
        .addOnFailureListener { exception ->
            // Failed to check the username in Firestore, show an error message
            val errorMessage = "Error checking username: ${exception.message}"
            Log.e("FirestoreError", errorMessage)
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
        }
}




