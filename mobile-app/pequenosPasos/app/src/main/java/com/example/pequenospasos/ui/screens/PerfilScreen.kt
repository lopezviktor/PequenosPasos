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
import com.example.pequenospasos.data.model.Padre

@Composable
fun PerfilScreen(
    padre: Padre
) {
    Scaffold(
        topBar = {
            CustomTopBar(
                title = "Perfil del Padre",
                showProfileIcon = false
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "${padre.nombre} ${padre.apellidos}",
                style = TextStyle(fontSize = 24.sp),
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Text("Email: ${padre.email}", style = TextStyle(fontSize = 18.sp))
            Text("Teléfono: ${padre.telefono}", style = TextStyle(fontSize = 18.sp))
            Spacer(modifier = Modifier.height(24.dp))
            Text("Hijos:", style = TextStyle(fontSize = 20.sp))
            padre.hijos.forEach { hijo ->
                Text("- ${hijo.nombre}", style = TextStyle(fontSize = 16.sp))
            }
        }
    }
}