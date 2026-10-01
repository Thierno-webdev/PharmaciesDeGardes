package com.autoformation.pharmaciesdegardes.ui.pharmacies

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.autoformation.pharmaciesdegardes.ui.pharmacies.components.FilterChipRow
import com.autoformation.pharmaciesdegardes.ui.pharmacies.components.PharmacieCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListePharmaciesScreen(
    viewModel: PharmacieViewModel = viewModel(),
    onPharmacieClick: (String) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pharmacies & Médicaments") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Utilisation du composant réutilisable FilterChipRow
            FilterChipRow(
                rechercheMedicament = uiState.rechercheMedicament,
                onRechercheChanged = { viewModel.onRechercheMedicamentChanged(it) },
                deGardeUniquement = uiState.deGardeUniquement,
                onDeGardeToggled = { viewModel.onDeGardeToggled(it) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Affichage des résultats
            if (uiState.pharmaciesAffichees.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Aucune pharmacie ne dispose de ce médicament ou ne correspond à vos critères.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(uiState.pharmaciesAffichees) { pharmacie ->
                        // Utilisation du composant réutilisable PharmacieCard
                        PharmacieCard(
                            pharmacie = pharmacie,
                            queryMedicament = uiState.rechercheMedicament,
                            onClick = { onPharmacieClick(pharmacie.id) }
                        )
                    }
                }
            }
        }
    }
}