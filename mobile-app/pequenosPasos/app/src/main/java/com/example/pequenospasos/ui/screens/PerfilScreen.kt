package com.example.pequenospasos.ui.screens

import com.example.pequenospasos.ui.components.CustomTopBar
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PerfilScreen(
    nombre: String = "Carlitos",
    apellidos: String = "Martinez Lopez",
    edad: String = "3 años",
    alergias: String = "Polen",
    condicionesMedicas: String = "Asma"
) {
    Scaffold(
        topBar = {
            CustomTopBar(
                title = "Perfil del Niño",
                showProfileIcon = false
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "$nombre $apellidos",
                style = TextStyle(fontSize = 24.sp),
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Text("Edad: $edad", style = TextStyle(fontSize = 18.sp))
            Text("Alergias: $alergias", style = TextStyle(fontSize = 18.sp))
            Text("Condiciones Médicas: $condicionesMedicas", style = TextStyle(fontSize = 18.sp))
        }
    }
}