package com.example.agrikranti


import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

@Composable
fun MspScreen(navController: NavHostController) {
    var mspList by remember { mutableStateOf<List<CropMsp>>(emptyList()) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(Unit) {
        try {
            val crops = fetchMspData()
            mspList = crops
        } catch (e: Exception) {
            errorMessage = "Failed to fetch MSP data: ${e.message}"
        }
    }
    Image(
        painter = painterResource(id = R.drawable.farm), // Replace with your image resource
        contentDescription = "Msp Screen",
        modifier = Modifier
            .fillMaxWidth()
            .height(350.dp) // Adjust height based on the space needed
            .padding(bottom = 20.dp), // Padding to provide space below the image for buttons
        contentScale = ContentScale.Crop,

        )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        if (errorMessage != null) {
            Text(text = errorMessage!!, color = MaterialTheme.colorScheme.error)
        } else if (mspList.isEmpty()) {
            CircularProgressIndicator()
        } else {
            MspList(crops = mspList)
        }
    }
}

@Composable
fun MspList(crops: List<CropMsp>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(crops) { crop ->
            MspItem(crop = crop)
        }
    }
}

@Composable
fun MspItem(crop: CropMsp) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF4169E1))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "Crop: ${crop.cropName}",
                style = MaterialTheme.typography.titleLarge,
                color = Color(0xFFFF9800) // Orange color in HEX
            )

            Text(
                text = "MSP Price: ₹${crop.mspPrice}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFFFF9800) // Orange color in HEX
            )

        }
    }
}

// Data class to hold the Crop MSP details
data class CropMsp(
    val cropName: String,
    val mspPrice: String
)

// Function to fetch MSP data from Firestore
suspend fun fetchMspData(): List<CropMsp> {
    val firestore = FirebaseFirestore.getInstance()
    val mspCollection = firestore.collection("MSP")
    val mspDocuments = mspCollection.get().await()

    return mspDocuments.documents.mapNotNull { document ->
        try {
            val cropName = document.id  // Use document ID as crop name
            val mspPrice = document.getString("Price") ?: "Unknown"
            CropMsp(cropName, mspPrice)
        } catch (e: Exception) {
            Log.e("Firestore", "Error fetching document: ${document.id}", e)
            null
        }
    }
}
