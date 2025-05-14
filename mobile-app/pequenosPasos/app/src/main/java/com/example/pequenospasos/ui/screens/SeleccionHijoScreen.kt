package com.example.pequenospasos.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.pequenospasos.R
import com.example.pequenospasos.viewmodel.LoginViewModel
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun SeleccionHijoScreen(
    navController: NavController,
    loginViewModel: LoginViewModel
) {
    val hijos by loginViewModel.hijos.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "Selecciona un hijo",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (!hijos.isNullOrEmpty()) {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(items = hijos) { hijo ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                loginViewModel.seleccionarNino(hijo)
                                navController.navigate("menu_principal")
                            }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(16.dp)
                        ) {
                            val url = hijo.fotoUrl ?: ""
                            val painter = rememberAsyncImagePainter(
                                model = if (url.isNotBlank()) "http://10.0.2.2:8080$url" else R.drawable.ninos_default
                            )
                            Image(
                                painter = painter,
                                contentDescription = "Foto del niño",
                                modifier = Modifier.size(64.dp),
                                contentScale = ContentScale.Crop
                            )

                            Spacer(modifier = Modifier.width(16.dp))

                            Column {
                                Text(
                                    text = "${hijo.nombre} ${hijo.apellidos}",
                                    style = MaterialTheme.typography.titleLarge
                                )
                            }
                        }
                    }
                }
            }
        } else {
            Text(
                text = "No hay hijos disponibles.",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 24.dp)
            )
        }
    }
}