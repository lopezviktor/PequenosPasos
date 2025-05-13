package com.example.pequenospasos.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.pequenospasos.data.network.ApiService
import com.example.pequenospasos.data.network.RetrofitClient
import com.example.pequenospasos.ui.components.CustomTopBar
import com.example.pequenospasos.viewmodel.ActividadViewModel
import com.example.pequenospasos.viewmodel.ActividadViewModelFactory
import com.example.pequenospasos.data.repository.ActividadRepository

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActividadesScreen(
    navController: NavHostController,
    viewModel: ActividadViewModel = viewModel(
        factory = ActividadViewModelFactory(
            ActividadRepository(RetrofitClient.api)
        )
    )
) {
    val actividades by viewModel.actividades.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    Scaffold(
        topBar = {
            CustomTopBar(
                title = "Actividades",
                showProfileIcon = true,
                onProfileClick = { navController.navigate("perfil_screen") }
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(16.dp)) {

            if (errorMessage != null) {
                Snackbar {
                    Text(text = errorMessage ?: "Error desconocido")
                }
            }

            if (actividades.isEmpty() && errorMessage == null) {
                Text("No hay actividades disponibles", style = TextStyle(fontSize = 18.sp))
            } else {
                LazyColumn {
                    items(actividades) { actividadResponse ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFD7E8FF))
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "Actividad: ${actividadResponse.actividad.nombre}",
                                    style = TextStyle(fontSize = 20.sp)
                                )
                                Text(
                                    text = "Descripción: ${actividadResponse.actividad.descripcion}",
                                    style = TextStyle(fontSize = 16.sp)
                                )
                                val fechaFormateada = try {
                                    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                                    LocalDateTime.parse(actividadResponse.fechaRegistro).format(formatter)
                                } catch (e: Exception) {
                                    actividadResponse.fechaRegistro
                                }
                                Text(
                                    text = "Fecha: $fechaFormateada",
                                    style = TextStyle(fontSize = 14.sp)
                                )
                                val educador = actividadResponse.nino.clase.educador
                                Text(
                                    text = "Educador: ${educador.nombre} ${educador.apellidos}",
                                    style = TextStyle(fontSize = 12.sp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.loadActividades(1) // ID temporal
    }
}