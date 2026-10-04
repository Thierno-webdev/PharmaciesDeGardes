package com.autoformation.pharmaciesdegardes.ui.medicaments

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.autoformation.pharmaciesdegardes.data.model.Medicament
import com.autoformation.pharmaciesdegardes.ui.theme.Amber50
import com.autoformation.pharmaciesdegardes.ui.theme.Amber90
import com.autoformation.pharmaciesdegardes.ui.theme.Emerald10
import com.autoformation.pharmaciesdegardes.ui.theme.Emerald50
import com.autoformation.pharmaciesdegardes.ui.theme.Emerald60
import com.autoformation.pharmaciesdegardes.viewmodel.MedicamentUiState
import com.autoformation.pharmaciesdegardes.viewmodel.MedicamentViewModel
import com.autoformation.pharmaciesdegardes.viewmodel.PharmacieViewModel
import com.autoformation.pharmaciesdegardes.viewmodel.StockViewModel

// ──────────────────────────────────────────────────────────────────────
// Écran : Recherche de médicaments — Design premium
// ──────────────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicamentsScreen(
    medicamentViewModel: MedicamentViewModel,
    stockViewModel: StockViewModel,
    pharmacieViewModel: PharmacieViewModel,
    onNavigateBack: () -> Unit = {},
    onAjouterClick: () -> Unit = {}
) {
    val medicamentsState by medicamentViewModel.medicamentsState.collectAsState()
    val searchQuery      by medicamentViewModel.searchQuery.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current

    var selectedMedicamentId by remember { mutableStateOf<Long?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Médicaments",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Trouvez où se procurer vos médicaments",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.80f)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Retour", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Emerald50,
                    titleContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAjouterClick,
                containerColor = Amber50,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Ajouter un médicament", modifier = Modifier.size(24.dp))
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
        ) {
            // ── Barre de recherche stylisée ──────────────
            Surface(
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { medicamentViewModel.onSearchQueryChanged(it) },
                    placeholder = {
                        Text(
                            text = "Tapez le nom d'un médicament…",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    leadingIcon = {
                        Text(text = "🔍", style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(start = 4.dp))
                    },
                    trailingIcon = {
                        if (searchQuery.isNotBlank()) {
                            TextButton(onClick = {
                                medicamentViewModel.onSearchQueryChanged("")
                                keyboardController?.hide()
                            }) {
                                Text("✕", style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = { keyboardController?.hide() }),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor   = Emerald50,
                        unfocusedBorderColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                )
            }

            // ── Contenu ──────────────────────────────────
            when (val state = medicamentsState) {
                is MedicamentUiState.Loading -> EtatChargement()
                is MedicamentUiState.Empty   -> EtatVide(
                    message = if (searchQuery.isBlank()) "Aucun médicament disponible.\nUtilisez + pour en ajouter."
                    else "Aucun résultat pour \"$searchQuery\"."
                )
                is MedicamentUiState.Error   -> EtatErreur(message = state.message)
                is MedicamentUiState.Success -> ListeMedicaments(
                    medicaments          = state.medicaments,
                    selectedMedicamentId = selectedMedicamentId,
                    onMedicamentSelected = { med ->
                        selectedMedicamentId = med.id
                        stockViewModel.loadStocksForMedicament(med.id)
                    },
                    stockViewModel       = stockViewModel,
                    pharmacieViewModel   = pharmacieViewModel
                )
            }
        }
    }
}

// ──────────────────────────────────────────────────────────────────────
// Liste de médicaments
// ──────────────────────────────────────────────────────────────────────

@Composable
private fun ListeMedicaments(
    medicaments: List<Medicament>,
    selectedMedicamentId: Long?,
    onMedicamentSelected: (Medicament) -> Unit,
    stockViewModel: StockViewModel,
    pharmacieViewModel: PharmacieViewModel
) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Text(
                text = "${medicaments.size} médicament(s) trouvé(s)",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }

        items(items = medicaments, key = { it.id }) { medicament ->
            Column {
                CarteMedicament(
                    medicament = medicament,
                    isSelected = selectedMedicamentId == medicament.id,
                    onClick    = { onMedicamentSelected(medicament) }
                )
                if (selectedMedicamentId == medicament.id) {
                    PharmaciesPourMedicament(
                        stockViewModel     = stockViewModel,
                        pharmacieViewModel = pharmacieViewModel
                    )
                }
            }
        }

        item { Spacer(modifier = Modifier.height(80.dp)) }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CarteMedicament(
    medicament: Medicament,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 5.dp else 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Emerald10 else MaterialTheme.colorScheme.surface
        )
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
                    .background(if (isSelected) Emerald50.copy(alpha = 0.15f) else Amber90),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.MedicalServices,
                    contentDescription = null,
                    tint = if (isSelected) Emerald60 else Amber50,
                    modifier = Modifier.size(24.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = medicament.nom,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) Emerald60 else MaterialTheme.colorScheme.onSurface
                )
                if (medicament.categorie.isNotBlank()) {
                    Spacer(modifier = Modifier.height(3.dp))
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Amber90
                    ) {
                        Text(
                            text = medicament.categorie,
                            style = MaterialTheme.typography.labelMedium,
                            color = Amber50,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }
            }

            // Prix GNF
            if (medicament.prix > 0) {
                Text(
                    text = "${java.text.NumberFormat.getNumberInstance(java.util.Locale.FRENCH).format(medicament.prix)} GNF",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = Emerald60
                )
            }

            // Chevron
            Text(
                text = if (isSelected) "▲" else "▼",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// ──────────────────────────────────────────────────────────────────────
// États
// ──────────────────────────────────────────────────────────────────────

@Composable
private fun EtatChargement() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = Emerald50)
    }
}

@Composable
private fun EtatVide(message: String) {
    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "💊", style = MaterialTheme.typography.displaySmall)
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
private fun EtatErreur(message: String) {
    Box(modifier = Modifier.fillMaxSize().padding(16.dp), contentAlignment = Alignment.Center) {
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = "⚠️", style = MaterialTheme.typography.headlineMedium)
                Text(text = message, style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer)
            }
        }
    }
}
