package com.example.pequenospasos.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Pantalla Principal", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = {
            // Aquí puedes agregar funcionalidades futuras
        }) {
            Text("Bienvenido")
        }
    }
}