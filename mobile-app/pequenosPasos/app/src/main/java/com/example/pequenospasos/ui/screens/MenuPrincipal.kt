package com.example.pequenospasos.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.pequenospasos.R
import com.example.pequenospasos.ui.components.CustomTopBar
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import com.example.pequenospasos.data.model.Nino
import com.example.pequenospasos.data.model.Padre
import com.example.pequenospasos.viewmodel.NotificacionesViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuPrincipal(
    onComidaClick: () -> Unit,
    onHigieneClick: () -> Unit,
    onSiestaClick: () -> Unit,
    onHabitosClick: () -> Unit,
    onActividadesClick: () -> Unit,
    onNotificacionesClick: () -> Unit,
    onProfileClick: () -> Unit,
    navController: NavHostController,
    nino: Nino,
    nombre: String,
    apellidos: String,
    padre: Padre,
    notificacionesViewModel: NotificacionesViewModel,

    ) {
    val notificacionesNoLeidas by notificacionesViewModel.notificacionesNoLeidas.collectAsState()
    LaunchedEffect(padre.id) {
        Log.d("MENU_PRINCIPAL", "ID del padre logueado: ${padre.id}")
        notificacionesViewModel.cargarContadorNoLeidas(padre.id)
        Log.d("MENU_PRINCIPAL", "Contador de no leídas (estado actual): ${notificacionesNoLeidas}")
    }
    Scaffold(
        topBar = {
            CustomTopBar(
                title = "Menu principal",
                showProfileIcon = true,
                onProfileClick = { navController.navigate("perfil_screen") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFCF1EC))
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val imageUrl = "http://10.0.2.2:8080${nino.fotoUrl}"

            AsyncImage(
                model = imageUrl,
                contentDescription = "Foto del niño",
                modifier = Modifier
                    .size(120.dp)
                    .padding(top = 8.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${nombre}",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                MenuCard("Comida", R.drawable.ic_comida, onComidaClick, Color(0xFFF1E4FF), Color(0xFF6200EA))
                MenuCard("Higiene", R.drawable.ic_higiene, onHigieneClick, Color(0xFFFFF9C4), Color(0xFFF57F17))
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                MenuCard("Siesta", R.drawable.ic_siesta, onSiestaClick, Color(0xFFE0F7FA), Color(0xFF00796B))
                MenuCard("Hábitos", R.drawable.ic_habitos, onHabitosClick, Color(0xFFD7CCC8), Color.Black)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                MenuCard(
                    "Actividades",
                    R.drawable.ic_actividad,
                    onActividadesClick,
                    backgroundColor = Color(0xFFBBDEFB),
                    textColor = Color(0xFF0D47A1)
                )
                MenuCard(
                    "Notificaciones",
                    R.drawable.ic_notificacion,
                    onNotificacionesClick,
                    backgroundColor = Color(0xFFB2EBF2),
                    textColor = Color(0xFF006064),
                    badgeCount = notificacionesNoLeidas
                )
            }
        }
    }
}

@Composable
fun MenuCard(
    titulo: String,
    icono: Int,
    onClick: () -> Unit,
    backgroundColor: Color,
    textColor: Color,
    badgeCount: Int? = null
) {
    ElevatedButton(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier
            .padding(8.dp)
            .size(150.dp),
        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor),
        elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 8.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box {
                Image(painter = painterResource(id = icono), contentDescription = titulo)
                if (badgeCount != null && badgeCount > 0) {
                    Box(
                        modifier = Modifier
                            .offset(x = (-20).dp, y = (-4).dp)
                            .size(28.dp)
                            .background(Color.Red, shape = RoundedCornerShape(10.dp))
                            .zIndex(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = badgeCount.toString(),
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(titulo, style = TextStyle(fontSize = 18.sp, color = textColor))
        }
    }
}
