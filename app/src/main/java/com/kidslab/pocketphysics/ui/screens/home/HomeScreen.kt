package com.kidslab.pocketphysics.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.kidslab.pocketphysics.R
import com.kidslab.pocketphysics.ui.components.BouncyCard
import com.kidslab.pocketphysics.ui.components.VerticalScrollHint
import com.kidslab.pocketphysics.ui.navigation.Rutas
import com.kidslab.pocketphysics.ui.theme.AppBackgroundGradient
import com.kidslab.pocketphysics.ui.theme.LabGradient

private data class AtajoLaboratorio(val titulo: String, val descripcion: String, val ruta: String, val gradient: LabGradient)

@Composable
fun HomeScreen(navController: NavHostController) {
    val atajos = listOf(
        AtajoLaboratorio("Laboratorio de movimiento", "Explora el acelerómetro", Rutas.LAB_MOVIMIENTO, LabGradient.MOVIMIENTO),
        AtajoLaboratorio("Laboratorio de rotación", "Explora el giroscopio", Rutas.LAB_ROTACION, LabGradient.ROTACION),
        AtajoLaboratorio("Laboratorio de sonido", "Explora el micrófono", Rutas.LAB_SONIDO, LabGradient.SONIDO),
        AtajoLaboratorio("Laboratorio de luz", "Explora el sensor de luz", Rutas.LAB_LUZ, LabGradient.LUZ)
    )

    val listState = rememberLazyListState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(AppBackgroundGradient))
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "¡Bienvenido de vuelta, científico! 👋",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(Modifier.height(10.dp))
            CambiarPerfilButton(onClick = {
                navController.navigate(Rutas.SELECTOR_PERFIL) {
                    popUpTo(Rutas.INICIO) { inclusive = true }
                }
            })
            Text(
                text = stringResource(R.string.aviso_seguridad_general),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 12.dp, bottom = 16.dp)
            )

            LazyColumn(
                state = listState,
                verticalArrangement = Arrangement.spacedBy(14.dp),
                contentPadding = PaddingValues(bottom = 12.dp)
            ) {
                items(atajos) { atajo ->
                    LaboratorioCard(atajo) { navController.navigate(atajo.ruta) }
                }
                item {
                    Text(
                        text = stringResource(R.string.aviso_precision),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
            VerticalScrollHint(listState = listState, modifier = Modifier.fillMaxWidth())
        }
    }
}

@Composable
private fun CambiarPerfilButton(onClick: () -> Unit) {
    BouncyCard(
        onClick = onClick,
        shape = RoundedCornerShape(50),
        backgroundColor = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("🔄", style = MaterialTheme.typography.bodyLarge)
            Spacer(Modifier.width(8.dp))
            Text(
                "Cambiar de perfil",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun LaboratorioCard(atajo: AtajoLaboratorio, onClick: () -> Unit) {
    BouncyCard(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        backgroundBrush = Brush.horizontalGradient(atajo.gradient.colors)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(52.dp).background(Color.White.copy(alpha = 0.25f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(atajo.gradient.emoji, style = MaterialTheme.typography.headlineSmall)
            }
            Spacer(Modifier.width(16.dp))
            Column {
                Text(atajo.titulo, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Color.White)
                Text(atajo.descripcion, style = MaterialTheme.typography.bodyMedium, color = Color.White.copy(alpha = 0.9f))
            }
        }
    }
}
