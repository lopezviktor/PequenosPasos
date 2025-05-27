package com.example.pequenospasos.ui.screens

import android.util.Log
import com.example.pequenospasos.ui.components.CustomTopBar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.pequenospasos.data.model.Padre

@Composable
fun PerfilScreen(padre: Padre) {
    Log.d("PERFIL_SCREEN", "Padre recibido: $padre")
    Scaffold(
        topBar = {
            CustomTopBar(
                title = "Perfil del Padre",
                showProfileIcon = false
            )
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            tonalElevation = 4.dp,
            shape = MaterialTheme.shapes.large
        ) {
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.elevatedCardColors(),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Datos del padre",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        textAlign = TextAlign.Center
                    )
                    Text("Nombre: ${padre.nombre} ${padre.apellidos}")
                    Text("Email: ${padre.email}")
                    Text("Teléfono: ${padre.telefono}")
                    Text("Tipo de usuario: ${padre.tipoUsuario}")

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Hijos",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        textAlign = TextAlign.Center
                    )

                    padre.hijos.forEach { hijo ->
                        ElevatedCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            colors = CardDefaults.elevatedCardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            ),
                            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "${hijo.nombre} ${hijo.apellidos}",
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                                Text("Alergias: ${hijo.alergias?.ifBlank { "Ninguna" }}")
                                Text("Condiciones médicas: ${hijo.condicionesMedicas?.ifBlank { "Ninguna" }}")
                                hijo.clase?.let { clase ->
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text("Clase: ${clase.nombre}")
                                    Text("Educador: ${clase.educador.nombre} ${clase.educador.apellidos}")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}