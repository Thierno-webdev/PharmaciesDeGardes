package com.example.pharmaciedegarde.ui.pharmacies

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.pharmaciedegarde.ui.pharmacies.components.FilterChipRow
import com.example.pharmaciedegarde.ui.pharmacies.components.PharmacieCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListePharmaciesScreen(
    viewModel: PharmacieViewModel,
    onPharmacieClick: (Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val communesList = listOf("Kaloum", "Dixinn", "Matam", "Ratoma", "Matoto")

    Scaffold(
        topBar = { TopAppBar(title = { Text("Pharmacies") }) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val state = uiState) {
                is PharmacieUiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                is PharmacieUiState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = state.message,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }

                is PharmacieUiState.Empty -> {
                    Column(modifier = Modifier.fillMaxSize()) {
                        FilterChipRow(
                            communes = communesList,
                            selectedCommune = null,
                            isOnlyDeGarde = false,
                            onCommuneSelected = { viewModel.filterByCommune(it) },
                            onDeGardeToggled = { viewModel.toggleDeGardeFilter(it) }
                        )
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("Aucune pharmacie ne correspond aux critères.")
                        }
                    }
                }

                is PharmacieUiState.Success -> {
                    FilterChipRow(
                        communes = communesList,
                        selectedCommune = state.selectedCommune,
                        isOnlyDeGarde = state.isOnlyDeGarde,
                        onCommuneSelected = { viewModel.filterByCommune(it) },
                        onDeGardeToggled = { viewModel.toggleDeGardeFilter(it) }
                    )

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 16.dp)
                    ) {
                        items(state.pharmacies) { pharmacie ->
                            PharmacieCard(
                                pharmacie = pharmacie,
                                onClick = { onPharmacieClick(pharmacie.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}