package com.autoformation.pharmaciesdegardes.ui.pharmacies

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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.autoformation.pharmaciesdegardes.data.model.Pharmacie
import com.autoformation.pharmaciesdegardes.ui.theme.Emerald10
import com.autoformation.pharmaciesdegardes.ui.theme.Emerald50
import com.autoformation.pharmaciesdegardes.ui.theme.Emerald60
import com.autoformation.pharmaciesdegardes.viewmodel.PharmacieUiState
import com.autoformation.pharmaciesdegardes.viewmodel.PharmacieViewModel

// ──────────────────────────────────────────────────────────────────────
// Écran : Liste des pharmacies — Design premium
// ──────────────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PharmaciesScreen(
    pharmacieViewModel: PharmacieViewModel,
    onNavigateBack: () -> Unit = {},
    onPharmacieClick: (Pharmacie) -> Unit = {}
) {
    val pharmaciesFiltreesState by pharmacieViewModel.pharmaciesFiltreesState.collectAsState()
    val communesDisponibles     by pharmacieViewModel.communesDisponibles.collectAsState()
    val filtreCommune           by pharmacieViewModel.filtreCommune.collectAsState()
    val filtreDeGarde           by pharmacieViewModel.filtreDeGarde.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Pharmacies",
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
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Retour",
                            tint = Color.White
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
        ) {
            // ── Zone de filtres (chips) ──────────────────────
            if (pharmaciesFiltreesState is PharmacieUiState.Success ||
                pharmaciesFiltreesState is PharmacieUiState.Empty) {
                ZoneFiltresChips(
                    communesDisponibles = communesDisponibles,
                    communeSelectionnee = filtreCommune,
                    filtreDeGarde       = filtreDeGarde,
                    onCommuneChange     = { pharmacieViewModel.setFiltreCommune(it) },
                    onDeGardeChange     = { pharmacieViewModel.setFiltreDeGarde(it) }
                )
                HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.surfaceVariant)
            }

            // ── Contenu principal ────────────────────────────
            when (val state = pharmaciesFiltreesState) {
                is PharmacieUiState.Loading -> EtatChargement(modifier = Modifier.weight(1f))
                is PharmacieUiState.Empty   -> EtatVide(
                    message  = "Aucune pharmacie disponible pour le moment.",
                    modifier = Modifier.weight(1f)
                )
                is PharmacieUiState.Error   -> EtatErreur(
                    message  = state.message,
                    modifier = Modifier.weight(1f)
                )
                is PharmacieUiState.Success -> {
                    if (state.pharmacies.isEmpty()) {
                        EtatVide(
                            message  = "Aucune pharmacie ne correspond\naux filtres sélectionnés.",
                            modifier = Modifier.weight(1f)
                        )
                    } else {
                        ListePharmacies(
                            pharmacies      = state.pharmacies,
                            modifier        = Modifier.weight(1f),
                            onPharmacieClick = onPharmacieClick
                        )
                    }
                }
            }
        }
    }
}

// ──────────────────────────────────────────────────────────────────────
// Zone de filtres avec FilterChip (design Material 3)
// ──────────────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ZoneFiltresChips(
    communesDisponibles: List<String>,
    communeSelectionnee: String?,
    filtreDeGarde: Boolean,
    onCommuneChange: (String?) -> Unit,
    onDeGardeChange: (Boolean) -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // ── Chip "De garde" ───────────────────────────────
            FilterChip(
                selected = filtreDeGarde,
                onClick  = { onDeGardeChange(!filtreDeGarde) },
                label    = { Text("✓ De garde", style = MaterialTheme.typography.labelMedium) },
                colors   = FilterChipDefaults.filterChipColors(
                    selectedContainerColor      = Emerald10,
                    selectedLabelColor          = Emerald60,
                    selectedLeadingIconColor    = Emerald60
                )
            )

            // ── Menu commune ──────────────────────────────────
            MenuDeroulantCommune(
                communesDisponibles = communesDisponibles,
                communeSelectionnee = communeSelectionnee,
                onCommuneChange     = onCommuneChange
            )
        }
    }
}

@Composable
private fun MenuDeroulantCommune(
    communesDisponibles: List<String>,
    communeSelectionnee: String?,
    onCommuneChange: (String?) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        OutlinedButton(
            onClick = { expanded = true },
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = communeSelectionnee ?: "Toutes communes ▼",
                style = MaterialTheme.typography.labelMedium,
                maxLines = 1
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Toutes les communes", fontWeight = if (communeSelectionnee == null) FontWeight.Bold else FontWeight.Normal) },
                onClick = { onCommuneChange(null); expanded = false }
            )
            HorizontalDivider()
            communesDisponibles.forEach { commune ->
                DropdownMenuItem(
                    text = { Text(commune, fontWeight = if (commune == communeSelectionnee) FontWeight.Bold else FontWeight.Normal) },
                    onClick = { onCommuneChange(commune); expanded = false }
                )
            }
        }
    }
}

// ──────────────────────────────────────────────────────────────────────
// Liste des pharmacies
// ──────────────────────────────────────────────────────────────────────

@Composable
private fun ListePharmacies(
    pharmacies: List<Pharmacie>,
    modifier: Modifier = Modifier,
    onPharmacieClick: (Pharmacie) -> Unit
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Text(
                text = if (pharmacies.size == 1) "1 pharmacie trouvée"
                else "${pharmacies.size} pharmacies trouvées",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }

        items(items = pharmacies, key = { it.id }) { pharmacie ->
            CartePharmacieItem(
                pharmacie = pharmacie,
                onClick   = { onPharmacieClick(pharmacie) }
            )
        }

        item { Spacer(modifier = Modifier.height(8.dp)) }
    }
}

@Composable
private fun CartePharmacieItem(
    pharmacie: Pharmacie,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // ── Icône circulaire ────────────────────────────
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(if (pharmacie.estDeGarde) Emerald10 else MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.LocalHospital,
                    contentDescription = null,
                    tint = if (pharmacie.estDeGarde) Emerald60 else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(26.dp)
                )
            }

            // ── Informations ────────────────────────────────
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = pharmacie.nom,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = "${pharmacie.commune} · ${pharmacie.quartier}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(3.dp))
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

            // ── Badge garde ──────────────────────────────────
            if (pharmacie.estDeGarde) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Emerald10
                ) {
                    Text(
                        text = "Garde",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = Emerald60,
                        modifier = Modifier.padding(horizontal = 9.dp, vertical = 5.dp)
                    )
                }
            }

            // ── Chevron ──────────────────────────────────────
            Text(
                text = "›",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// ──────────────────────────────────────────────────────────────────────
// États
// ──────────────────────────────────────────────────────────────────────

@Composable
private fun EtatChargement(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = Emerald50)
    }
}

@Composable
private fun EtatVide(message: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "🏥", style = MaterialTheme.typography.displaySmall)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun EtatErreur(message: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxWidth().padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = "⚠️", style = MaterialTheme.typography.headlineMedium)
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }
    }
}
