package com.autoformation.pharmaciesdegardes.ui.pharmacies

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.platform.LocalContext
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

// ──────────────────────────────────────────────────────────────────────
// Écran : Détail d'une pharmacie — Design premium
// ──────────────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PharmacieDetailScreen(
    pharmacie: Pharmacie,
    favoriViewModel: FavoriViewModel,
    onBack: () -> Unit = {}
) {
    val context = LocalContext.current
    val favorisState by favoriViewModel.favorisState.collectAsState()

    val estFavori = when (val s = favorisState) {
        is FavoriUiState.Success -> s.favoris.any { it.pharmacieId == pharmacie.id }
        else -> false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Détail de la pharmacie",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Retour",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    // Bouton favori dans la TopBar
                    IconButton(
                        onClick = {
                            if (estFavori) {
                                val favoriASupprimer = when (val s = favorisState) {
                                    is FavoriUiState.Success -> s.favoris.firstOrNull { it.pharmacieId == pharmacie.id }
                                    else -> null
                                }
                                favoriASupprimer?.let { favoriViewModel.supprimerFavori(it) }
                            } else {
                                favoriViewModel.ajouterFavori(pharmacieId = pharmacie.id)
                            }
                        }
                    ) {
                        Icon(
                            imageVector = if (estFavori) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = if (estFavori) "Retirer des favoris" else "Ajouter aux favoris",
                            tint = if (estFavori) Color(0xFFFF6B6B) else Color.White
                        )
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
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            // ── Bannière d'en-tête ─────────────────────────
            item {
                BannerEnTete(pharmacie = pharmacie)
            }

            // ── Carte : Informations ─────────────────────
            item {
                SectionInformations(
                    pharmacie = pharmacie,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                )
            }

            // ── Boutons d'action ─────────────────────────
            item {
                BoutonsAction(
                    pharmacie = pharmacie,
                    context = context,
                    estFavori = estFavori,
                    onToggleFavori = {
                        if (estFavori) {
                            val favoriASupprimer = when (val s = favorisState) {
                                is FavoriUiState.Success -> s.favoris.firstOrNull { it.pharmacieId == pharmacie.id }
                                else -> null
                            }
                            favoriASupprimer?.let { favoriViewModel.supprimerFavori(it) }
                        } else {
                            favoriViewModel.ajouterFavori(pharmacieId = pharmacie.id)
                        }
                    },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                )
            }

            item { Spacer(modifier = Modifier.height(32.dp)) }
        }
    }
}

// ──────────────────────────────────────────────────────────────────────
// Composants internes
// ──────────────────────────────────────────────────────────────────────

@Composable
private fun BannerEnTete(pharmacie: Pharmacie) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Emerald50, Emerald40)
                )
            )
            .padding(20.dp)
    ) {
        Column {
            // Icône + Nom
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.20f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.LocalHospital,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(36.dp)
                    )
                }
                Column {
                    Text(
                        text = pharmacie.nom,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${pharmacie.commune} · ${pharmacie.quartier}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.85f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Badge statut de garde
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = if (pharmacie.estDeGarde) Color.White.copy(alpha = 0.25f)
                else Color.White.copy(alpha = 0.15f)
            ) {
                Text(
                    text = if (pharmacie.estDeGarde) "✓ De garde cette semaine" else "Pas de garde actuellement",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                )
            }
        }
    }
}

@Composable
private fun SectionInformations(pharmacie: Pharmacie, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "Informations",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = Emerald60
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Adresse
            LigneInfo(
                icone = Icons.Default.LocationOn,
                couleurIcone = Emerald50,
                label = "Adresse",
                valeur = "${pharmacie.commune}, ${pharmacie.quartier}"
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp),
                color = MaterialTheme.colorScheme.surfaceVariant)

            // Téléphone
            LigneInfo(
                icone = Icons.Default.Phone,
                couleurIcone = Emerald50,
                label = "Téléphone",
                valeur = pharmacie.telephone
            )

            // Période de garde
            if (pharmacie.estDeGarde && pharmacie.debutGarde != null && pharmacie.finGarde != null) {
                HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant)
                LigneInfo(
                    icone = Icons.Default.Schedule,
                    couleurIcone = Amber50,
                    label = "Période de garde",
                    valeur = "Du ${pharmacie.debutGarde} au ${pharmacie.finGarde}"
                )
            }
        }
    }
}

@Composable
private fun LigneInfo(
    icone: ImageVector,
    couleurIcone: Color,
    label: String,
    valeur: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(couleurIcone.copy(alpha = 0.10f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icone,
                contentDescription = null,
                tint = couleurIcone,
                modifier = Modifier.size(18.dp)
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = valeur,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun BoutonsAction(
    pharmacie: Pharmacie,
    context: android.content.Context,
    estFavori: Boolean,
    onToggleFavori: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Bouton Appeler
        Button(
            onClick = {
                if (pharmacie.telephone.isNotBlank()) {
                    val intent = Intent(Intent.ACTION_DIAL).apply {
                        data = Uri.parse("tel:${pharmacie.telephone.trim()}")
                    }
                    context.startActivity(intent)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Emerald50),
            enabled = pharmacie.telephone.isNotBlank()
        ) {
            Icon(Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Appeler", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
        }

        // Bouton Itinéraire
        OutlinedButton(
            onClick = {
                val query = if (pharmacie.latitude != 0.0 && pharmacie.longitude != 0.0) {
                    "${pharmacie.latitude},${pharmacie.longitude}"
                } else "${pharmacie.commune}, ${pharmacie.quartier}"
                val queryEncodee = Uri.encode(query.trim())
                val geoUri   = Uri.parse("geo:0,0?q=$queryEncodee")
                val intent   = Intent(Intent.ACTION_VIEW, geoUri)
                if (intent.resolveActivity(context.packageManager) != null) {
                    context.startActivity(intent)
                } else {
                    val mapsUri = Uri.parse("https://www.google.com/maps/search/?api=1&query=$queryEncodee")
                    context.startActivity(Intent(Intent.ACTION_VIEW, mapsUri))
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(14.dp)
        ) {
            Icon(Icons.Default.LocationOn, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Itinéraire", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
        }

        // Bouton Favoris
        Button(
            onClick = onToggleFavori,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (estFavori) Color(0xFFFFEBEE) else Amber90,
                contentColor   = if (estFavori) Color(0xFFE53935) else Amber50
            )
        ) {
            Icon(
                imageVector = if (estFavori) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (estFavori) "Retirer des favoris" else "Ajouter aux favoris",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
