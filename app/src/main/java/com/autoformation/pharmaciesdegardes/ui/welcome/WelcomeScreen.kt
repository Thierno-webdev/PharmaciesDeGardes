package com.autoformation.pharmaciesdegardes.ui.welcome

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.autoformation.pharmaciesdegardes.ui.theme.Emerald40
import com.autoformation.pharmaciesdegardes.ui.theme.Emerald50
import com.autoformation.pharmaciesdegardes.ui.theme.Emerald60

// ──────────────────────────────────────────────────────────────────────
// Écran de bienvenue — Design premium avec fond dégradé et animations
// ──────────────────────────────────────────────────────────────────────

@Composable
fun WelcomeScreen(
    onNavigateToDashboard: () -> Unit
) {
    // ── Animation de pulsation de l'icône ────────────────────────────
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue  = 1.08f,
        animationSpec = infiniteRepeatable(
            animation  = tween(1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    // ── Animation d'entrée du bouton ──────────────────────────────────
    var buttonVisible by remember { mutableFloatStateOf(0f) }
    val buttonAlpha by animateFloatAsState(
        targetValue = buttonVisible,
        animationSpec = tween(800, delayMillis = 600),
        label = "buttonAlpha"
    )
    LaunchedEffect(Unit) { buttonVisible = 1f }

    // ── Fond : dégradé vert émeraude du haut vers le bas ──────────────
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Emerald60,
                        Emerald50,
                        Emerald40,
                        Color(0xFF2AB055)
                    )
                )
            )
    ) {
        // Cercle décoratif en haut à droite
        Box(
            modifier = Modifier
                .size(260.dp)
                .align(Alignment.TopEnd)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.07f))
        )
        // Cercle décoratif en bas à gauche
        Box(
            modifier = Modifier
                .size(180.dp)
                .align(Alignment.BottomStart)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.07f))
        )

        // ── Contenu centré ────────────────────────────────────────────
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 36.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Icône entourée d'un halo blanc doux
            Box(
                modifier = Modifier
                    .size(140.dp)
                    .scale(pulseScale)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(110.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.20f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "🏥", fontSize = 52.sp)
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Titre principal
            Text(
                text = "Pharmacies\nde Garde",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                textAlign = TextAlign.Center,
                lineHeight = 40.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Sous-titre descriptif
            Text(
                text = "Trouvez instantanément une pharmacie\nouverte près de vous, même sans internet.",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White.copy(alpha = 0.85f),
                textAlign = TextAlign.Center,
                lineHeight = 24.sp
            )

            Spacer(modifier = Modifier.height(60.dp))

            // Bouton principal avec alpha animé
            Button(
                onClick = onNavigateToDashboard,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor   = Emerald60
                )
            ) {
                Text(
                    text = "Commencer →",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Emerald60
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Mention mode hors-ligne
            Text(
                text = "✓ Fonctionne hors connexion",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.70f),
                textAlign = TextAlign.Center
            )
        }
    }
}
