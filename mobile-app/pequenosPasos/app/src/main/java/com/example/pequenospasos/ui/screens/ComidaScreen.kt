package com.example.pequenospasos.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.pequenospasos.ui.components.CustomTopBar
import com.example.pequenospasos.viewmodel.ComidaViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComidaScreen(
    navController: NavHostController,
    ninoId: Long,
    viewModel: ComidaViewModel = viewModel()
) {
    val comidas by viewModel.comidas.collectAsState()

    Scaffold(
        topBar = {
            CustomTopBar(
                title = "Comidas",
                showProfileIcon = true,
                onProfileClick = { navController.navigate("perfil_screen") }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(comidas) { comida ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF1E4FF))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "Descripción: ${comida.descripcionComida}", fontSize = 18.sp)
                        Text(text = "Hora: ${formatearFecha(comida.horaComida)}", fontSize = 14.sp)
                        comida.observaciones?.let {
                            Text(text = "Observaciones: $it", fontSize = 14.sp)
                        }
                        Text(text = "Educador: ${comida.educador}", fontSize = 14.sp)
                    }
                }
            }
        }
    }

    // Cargar datos al entrar
    LaunchedEffect(ninoId) {
        viewModel.loadComidas(ninoId)
    }
}



@RequiresApi(Build.VERSION_CODES.O)
fun formatearFecha(fechaOriginal: String): String {
    return try {
        val fecha = LocalDateTime.parse(fechaOriginal)
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
        fecha.format(formatter)
    } catch (e: DateTimeParseException) {
        fechaOriginal
    }
}