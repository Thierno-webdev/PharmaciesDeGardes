package com.autoformation.pharmaciesdegardes.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.autoformation.pharmaciesdegardes.data.model.Pharmacie
import com.autoformation.pharmaciesdegardes.ui.theme.Amber50
import com.autoformation.pharmaciesdegardes.ui.theme.Amber90
import com.autoformation.pharmaciesdegardes.ui.theme.Emerald10
import com.autoformation.pharmaciesdegardes.ui.theme.Emerald40
import com.autoformation.pharmaciesdegardes.ui.theme.Emerald50
import com.autoformation.pharmaciesdegardes.ui.theme.Emerald60
import com.autoformation.pharmaciesdegardes.viewmodel.FavoriUiState
import com.autoformation.pharmaciesdegardes.viewmodel.FavoriViewModel
import com.autoformation.pharmaciesdegardes.viewmodel.PharmacieUiState
import com.autoformation.pharmaciesdegardes.viewmodel.PharmacieViewModel

// ──────────────────────────────────────────────────────────────────────
// Écran principal : Dashboard — Design premium
// ──────────────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    pharmacieViewModel: PharmacieViewModel,
    favoriViewModel: FavoriViewModel,
    onNavigateToPharmacies: () -> Unit = {},
    onNavigateToMedicaments: () -> Unit = {},
    onNavigateToFavoris: () -> Unit = {}
) {
    val pharmaciesDeGardeState by pharmacieViewModel.pharmaciesDeGardeState.collectAsState()
    val favorisState by favoriViewModel.favorisState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "💊", style = MaterialTheme.typography.titleLarge)
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "Pharmacies de Garde",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "Conakry, Guinée",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.80f)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Emerald50,
                    titleContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            // ── Bannière de bienvenue avec dégradé ──────────────
            item {
                BannerBienvenue()
            }

            // ── Cartes de navigation rapide ──────────────────────
            item {
                CarteNavigationRapide(
                    onNavigateToPharmacies = onNavigateToPharmacies,
                    onNavigateToMedicaments = onNavigateToMedicaments,
                    onNavigateToFavoris = onNavigateToFavoris
                )
            }

            // ── Section : Pharmacies de garde ────────────────────
            item {
                SectionTitre(
                    emoji = "🏥",
                    titre = "De garde cette semaine",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                )
            }

            when (val state = pharmaciesDeGardeState) {
                is PharmacieUiState.Loading -> {
                    item { EtatChargement() }
                }
                is PharmacieUiState.Empty -> {
                    item {
                        EtatVide(
                            emoji = "🔍",
                            message = "Aucune pharmacie de garde\ncette semaine."
                        )
                    }
                }
                is PharmacieUiState.Error -> {
                    item { EtatErreur(message = state.message) }
                }
                is PharmacieUiState.Success -> {
                    items(
                        items = state.pharmacies,
                        key = { it.id }
                    ) { pharmacie ->
                        CartePharmacieDeGarde(
                            pharmacie = pharmacie,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            // ── Section : Favoris ────────────────────────────────
            item {
                SectionTitre(
                    emoji = "❤️",
                    titre = "Mes pharmacies favorites",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                )
            }

            item {
                ResumeFavoris(
                    state = favorisState,
                    onNavigateToFavoris = onNavigateToFavoris,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
    }
}

// ──────────────────────────────────────────────────────────────────────
// Composants internes
// ──────────────────────────────────────────────────────────────────────

@Composable
private fun BannerBienvenue() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(Emerald50, Emerald40)
                )
            )
            .padding(horizontal = 20.dp, vertical = 20.dp)
    ) {
        Column {
            Text(
                text = "Bonjour 👋",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Trouvez la pharmacie ouverte la plus proche.",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White.copy(alpha = 0.88f)
            )
        }
    }
}

@Composable
private fun CarteNavigationRapide(
    onNavigateToPharmacies: () -> Unit,
    onNavigateToMedicaments: () -> Unit,
    onNavigateToFavoris: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        BoutonAccesRapide(
            icone = Icons.Default.LocalHospital,
            label = "Pharmacies",
            couleurFond = Emerald10,
            couleurIcone = Emerald60,
            onClick = onNavigateToPharmacies,
            modifier = Modifier.weight(1f)
        )
        BoutonAccesRapide(
            icone = Icons.Default.MedicalServices,
            label = "Médicaments",
            couleurFond = Amber90,
            couleurIcone = Amber50,
            onClick = onNavigateToMedicaments,
            modifier = Modifier.weight(1f)
        )
        BoutonAccesRapide(
            icone = Icons.Default.Favorite,
            label = "Favoris",
            couleurFond = Color(0xFFFFEBEE),
            couleurIcone = Color(0xFFE53935),
            onClick = onNavigateToFavoris,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun BoutonAccesRapide(
    icone: ImageVector,
    label: String,
    couleurFond: Color,
    couleurIcone: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(couleurFond)
            .clickable(onClick = onClick)
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(couleurIcone.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icone,
                contentDescription = label,
                tint = couleurIcone,
                modifier = Modifier.size(24.dp)
            )
        }
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
private fun SectionTitre(
    emoji: String,
    titre: String,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Text(text = emoji, style = MaterialTheme.typography.titleMedium)
        Text(
            text = titre,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
private fun CartePharmacieDeGarde(
    pharmacie: Pharmacie,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icône circulaire
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Emerald10),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.LocalHospital,
                    contentDescription = null,
                    tint = Emerald60,
                    modifier = Modifier.size(26.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = pharmacie.nom,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "${pharmacie.commune} · ${pharmacie.quartier}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = pharmacie.telephone,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Badge "De garde"
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Emerald10
            ) {
                Text(
                    text = "✓ Garde",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = Emerald60,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                )
            }
        }
    }
}

@Composable
private fun ResumeFavoris(
    state: FavoriUiState,
    onNavigateToFavoris: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onNavigateToFavoris),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFFEBEE)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = null,
                    tint = Color(0xFFE53935),
                    modifier = Modifier.size(22.dp)
                )
            }

            when (state) {
                is FavoriUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                    Text(
                        text = "Chargement des favoris…",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
                is FavoriUiState.Empty -> {
                    Text(
                        text = "Aucune pharmacie enregistrée en favori.\nAppuyez pour en ajouter.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
                is FavoriUiState.Success -> {
                    val count = state.favoris.size
                    val msg = if (count == 1) "1 pharmacie enregistrée"
                    else "$count pharmacies enregistrées"
                    Column {
                        Text(
                            text = msg,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        Text(
                            text = "Voir mes favoris →",
                            style = MaterialTheme.typography.bodySmall,
                            color = Emerald60
                        )
                    }
                }
                is FavoriUiState.Error -> {
                    Text(
                        text = "⚠️ Impossible de charger les favoris.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@Composable
private fun EtatChargement() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = Emerald50)
    }
}

@Composable
private fun EtatVide(emoji: String, message: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(text = emoji, style = MaterialTheme.typography.headlineMedium)
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}

@Composable
private fun EtatErreur(message: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
    ) {
        Text(
            text = "⚠️ $message",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onErrorContainer,
            modifier = Modifier.padding(16.dp)
        )
    }
}
