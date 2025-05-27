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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.pequenospasos.ui.components.CustomTopBar
import com.example.pequenospasos.viewmodel.ComidaViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import java.time.LocalDate
import java.util.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComidaScreen(
    navController: NavHostController,
    ninoId: Long,
    viewModel: ComidaViewModel = viewModel()
) {
    val comidas by viewModel.comidas.collectAsState()
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }

    val datePicker = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, day: Int ->
            selectedDate = LocalDate.of(year, month + 1, day)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    val comidasFiltradas = if (selectedDate != null) {
        comidas.filter {
            LocalDate.parse(it.horaComida.substring(0, 10)) == selectedDate
        }
    } else {
        comidas
    }

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
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = selectedDate?.format(dateFormatter) ?: "Filtrar por fecha",
                        modifier = Modifier.clickable { datePicker.show() },
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )

                    if (selectedDate != null) {
                        Text(
                            text = "Limpiar",
                            modifier = Modifier.clickable { selectedDate = null },
                            color = Color.Red,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            if (comidasFiltradas.isEmpty()) {
                item {
                    Text(
                        text = "No hay comidas registradas en esta fecha.",
                        modifier = Modifier.padding(16.dp),
                        color = Color.Gray
                    )
                }
            } else {
                items(comidasFiltradas.sortedByDescending { it.horaComida }) { comida ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF7ECFF)),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "🍽 ${comida.descripcionComida}",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "🕒 ${formatearFecha(comida.horaComida)}",
                                fontSize = 14.sp,
                                color = Color.DarkGray
                            )

                            comida.observaciones?.let {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "💬 $it",
                                    fontSize = 14.sp,
                                    color = Color.Black
                                )
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "👩‍🏫 ${comida.educador}",
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                        }
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