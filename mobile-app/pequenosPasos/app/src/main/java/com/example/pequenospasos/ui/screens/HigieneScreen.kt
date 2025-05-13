package com.example.pequenospasos.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.pequenospasos.ui.components.CustomTopBar
import com.example.pequenospasos.viewmodel.HigieneViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HigieneScreen(
    navController: NavHostController,
    viewModel: HigieneViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val higienes by viewModel.higienes.collectAsState()

    Scaffold(
        topBar = {
            CustomTopBar(
                title = "Higienes",
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
            items(higienes) { higiene ->
                androidx.compose.material3.Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    colors = androidx.compose.material3.CardDefaults.cardColors(containerColor = Color(0xFFF1F8E9))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Estado: ${higiene.estado}",
                            style = androidx.compose.ui.text.TextStyle(fontSize = 20.sp)
                        )
                        higiene.observaciones?.let {
                            Text(
                                text = "Observaciones: $it",
                                style = androidx.compose.ui.text.TextStyle(fontSize = 16.sp)
                            )
                        }
                        val fechaFormateada = try {
                            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                            LocalDateTime.parse(higiene.fechaHora).format(formatter)
                        } catch (e: Exception) {
                            higiene.fechaHora
                        }
                        Text(
                            text = "Fecha: ${formatearFecha(higiene.fechaHora)}",
                            style = androidx.compose.ui.text.TextStyle(fontSize = 14.sp)
                        )
                        Text(
                            text = "Educador: ${higiene.educador}",
                            style = androidx.compose.ui.text.TextStyle(fontSize = 12.sp)
                        )
                    }
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.loadHigienes(1) // Reemplazar con ID del niño real
    }
}
