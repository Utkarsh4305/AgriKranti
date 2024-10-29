package com.example.agrikranti.AdminScreens



import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.navigation.NavHostController
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.launch

@Composable
fun UploadMspScreen(navController: NavHostController) {
    val cropNameState = remember { mutableStateOf(TextFieldValue("")) }
    val mspPriceState = remember { mutableStateOf(TextFieldValue("")) }
    val coroutineScope = rememberCoroutineScope()
    var uploadStatus by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Upload Crop MSP",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Crop Name Input Field
        OutlinedTextField(
            value = cropNameState.value,
            onValueChange = { cropNameState.value = it },
            label = { Text("Crop Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // MSP Price Input Field
        OutlinedTextField(
            value = mspPriceState.value,
            onValueChange = { mspPriceState.value = it },
            label = { Text("MSP Price (₹)") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            )
        )


        Spacer(modifier = Modifier.height(24.dp))

        // Submit Button
        Button(
            onClick = {
                coroutineScope.launch {
                    val success = uploadCropToFirestore(
                        cropNameState.value.text,
                        mspPriceState.value.text
                    )
                    if (success) {
                        uploadStatus = "Successfully uploaded!"
                        cropNameState.value = TextFieldValue("") // Clear fields after successful upload
                        mspPriceState.value = TextFieldValue("")
                    } else {
                        uploadStatus = "Failed to upload. Try again."
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Submit")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Status Text (Success or Error)
        uploadStatus?.let {
            Text(
                text = it,
                color = if (it.contains("Successfully")) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

// Function to upload crop to Firestore
suspend fun uploadCropToFirestore(cropName: String, mspPrice: String): Boolean {
    return try {
        val firestore = FirebaseFirestore.getInstance()
        val mspCollection = firestore.collection("MSP")

        // Uploading document with crop name as document ID
        val data = hashMapOf(
            "Price" to mspPrice
        )
        mspCollection.document(cropName).set(data).await()
        true // Return true on success
    } catch (e: Exception) {
        false // Return false if there's an error
    }
}
