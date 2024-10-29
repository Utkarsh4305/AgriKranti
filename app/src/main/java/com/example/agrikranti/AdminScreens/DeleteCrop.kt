package com.example.agrikranti.AdminScreens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

@Composable
fun DeleteCropScreen(navController: NavHostController) {
    val firestore = FirebaseFirestore.getInstance()
    var crops by remember { mutableStateOf<List<Crop>>(emptyList()) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Fetch crops when the screen is loaded
    LaunchedEffect(Unit) {
        fetchCrops(firestore, onSuccess = { cropList ->
            crops = cropList
        }, onError = { error ->
            errorMessage = error
        })
    }

    Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Column {
            Text(text = "Delete Crops", style = MaterialTheme.typography.titleLarge)

            if (errorMessage != null) {
                Text(text = errorMessage ?: "Error", color = MaterialTheme.colorScheme.error)
            }

            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(crops) { crop ->
                    CropItem(crop = crop, onDeleteClick = {
                        deleteCrop(firestore, crop.name) {
                            // Update the list after deletion
                            crops = crops.filter { it.name != crop.name }
                        }
                    })
                }
            }
        }
    }
}

@Composable
fun CropItem(crop: Crop, onDeleteClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "${crop.name}: ₹${crop.price}", style = MaterialTheme.typography.bodyLarge)
            Button(onClick = onDeleteClick) {
                Text("Delete")
            }
        }
    }
}

data class Crop(val name: String, val price: String)

fun fetchCrops(
    firestore: FirebaseFirestore,
    onSuccess: (List<Crop>) -> Unit,
    onError: (String) -> Unit
) {
    firestore.collection("MSP")
        .get()
        .addOnSuccessListener { result ->
            val cropList = result.documents.map { document ->
                Crop(name = document.id, price = document.getString("Price") ?: "Unknown")
            }
            onSuccess(cropList)
        }
        .addOnFailureListener { e ->
            onError(e.message ?: "An error occurred")
        }
}

fun deleteCrop(
    firestore: FirebaseFirestore,
    cropName: String,
    onSuccess: () -> Unit
) {
    firestore.collection("MSP")
        .document(cropName)
        .delete()
        .addOnSuccessListener {
            onSuccess()
        }
        .addOnFailureListener { e ->
            // Handle failure
        }
}
