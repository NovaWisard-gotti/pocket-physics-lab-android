package com.kidslab.pocketphysics.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.kidslab.pocketphysics.R
import com.kidslab.pocketphysics.ui.navigation.Rutas

private data class AtajoLaboratorio(val titulo: String, val descripcion: String, val ruta: String)

@Composable
fun HomeScreen(navController: NavHostController) {
    val atajos = listOf(
        AtajoLaboratorio("Laboratorio de movimiento", "Explora el acelerómetro", Rutas.LAB_MOVIMIENTO),
        AtajoLaboratorio("Laboratorio de rotación", "Explora el giroscopio", Rutas.LAB_ROTACION),
        AtajoLaboratorio("Laboratorio de sonido", "Explora el micrófono", Rutas.LAB_SONIDO),
        AtajoLaboratorio("Laboratorio de luz", "Explora el sensor de luz", Rutas.LAB_LUZ)
    )

    Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
        Text(
            text = "¡Bienvenido de vuelta, científico!",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = stringResource(R.string.aviso_seguridad_general),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
        )

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(atajos) { atajo ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    onClick = { navController.navigate(atajo.ruta) }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(atajo.titulo, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
                        Text(atajo.descripcion, style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
            item {
                Text(
                    text = stringResource(R.string.aviso_precision),
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(top = 12.dp)
                )
            }
        }
    }
}
