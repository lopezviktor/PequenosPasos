package com.example.pequenospasos.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pequenospasos.R
import com.example.pequenospasos.ui.components.CustomTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuPrincipal(
    onComidaClick: () -> Unit,
    onHigieneClick: () -> Unit,
    onSiestaClick: () -> Unit,
    onHabitosClick: () -> Unit,
    onActividadesClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    Scaffold(
        topBar = {
            CustomTopBar(
                title = "Menú Principal"
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
            Text("Selecciona una opción", style = TextStyle(fontSize = 24.sp))
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                MenuCard("Comida", R.drawable.ic_comida, onComidaClick)
                MenuCard("Higiene", R.drawable.ic_higiene, onHigieneClick)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                MenuCard("Siesta", R.drawable.ic_siesta, onSiestaClick)
                MenuCard("Hábitos", R.drawable.ic_habitos, onHabitosClick)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                MenuCard("Actividades", R.drawable.ic_actividad, onActividadesClick)
            }
        }
    }
}

@Composable
fun MenuCard(titulo: String, icono: Int, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .padding(8.dp)
            .size(150.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE0E0E0))
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(painter = painterResource(id = icono), contentDescription = titulo)
            Spacer(modifier = Modifier.height(4.dp))
            Text(titulo, style = TextStyle(fontSize = 18.sp))
        }
    }
}
