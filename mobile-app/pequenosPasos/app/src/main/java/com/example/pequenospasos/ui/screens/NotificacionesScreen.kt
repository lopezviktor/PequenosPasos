package com.example.pequenospasos.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.pequenospasos.data.network.RetrofitClient
import com.example.pequenospasos.data.repository.NotificacionesRepository
import com.example.pequenospasos.ui.components.CustomTopBar
import com.example.pequenospasos.viewmodel.NotificacionesViewModel
import com.example.pequenospasos.viewmodel.NotificacionesViewModelFactory
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NotificacionesScreen(
    navController: NavHostController,
    padreId: Long,
    viewModel: NotificacionesViewModel
) {
    val context = LocalContext.current
    val viewModel: NotificacionesViewModel = viewModel(
        factory = NotificacionesViewModelFactory(
            NotificacionesRepository(RetrofitClient.api)
        )
    )

    val notificaciones by viewModel.notificaciones.collectAsState()

    LaunchedEffect(padreId) {
        viewModel.cargarNotificaciones(padreId)
    }

    Scaffold(
        topBar = {
            CustomTopBar(
                title = "Notificaciones",
                showProfileIcon = true,
                onProfileClick = { navController.navigate("perfil_screen") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Button(
                onClick = { viewModel.marcarTodasComoLeidas(padreId) },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Marcar todas como leídas")
            }

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(notificaciones.sortedByDescending { it.fechaHora }) { notificacion ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .shadow(
                                elevation = 6.dp,
                                shape = RoundedCornerShape(16.dp)
                            )
                            .clickable {
                                if (notificacion.estado != "LEIDO") {
                                    viewModel.marcarComoLeida(notificacion.id)
                                }
                            },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (notificacion.estado == "LEIDO") Color.LightGray else MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = notificacion.mensaje)

                            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
                            val fechaFormateada = try {
                                LocalDateTime.parse(notificacion.fechaHora).format(formatter)
                            } catch (e: Exception) {
                                notificacion.fechaHora
                            }

                            Text(
                                text = "Fecha: $fechaFormateada",
                                style = MaterialTheme.typography.bodySmall
                            )
                            Text(
                                text = "Estado: ${notificacion.estado}",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}