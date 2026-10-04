package com.autoformation.pharmaciesdegardes.ui.medicaments

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.autoformation.pharmaciesdegardes.viewmodel.MedicamentViewModel
import kotlinx.coroutines.launch

// Catégories disponibles selon le cahier des charges
private val CATEGORIES = listOf(
    "Antalgique",
    "Anti-inflammatoire",
    "Antibiotique",
    "Antispasmodique",
    "Antihistaminique",
    "Gastro-entérologie",
    "Supplément",
    "Respiratoire",
    "Général"
)

// ─────────────────────────────────────────────
// Écran : Formulaire d'ajout d'un médicament
// ─────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AjoutMedicamentScreen(
    medicamentViewModel: MedicamentViewModel,
    onNavigateBack: () -> Unit
) {
    var nom by remember { mutableStateOf("") }
    var categorie by remember { mutableStateOf(CATEGORIES.first()) }
    var description by remember { mutableStateOf("") }
    var prixTexte by remember { mutableStateOf("") }

    // États de validation
    var nomError by remember { mutableStateOf<String?>(null) }
    var prixError by remember { mutableStateOf<String?>(null) }

    var isLoading by remember { mutableStateOf(false) }
    var categorieExpanded by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "➕ Ajouter un médicament",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Renseigner les informations",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Text(
                            text = "←",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->

        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 20.dp, vertical = 16.dp)
                    .verticalScroll(rememberScrollState())
                    .imePadding(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                // ── Nom ─────────────────────────────────────
                OutlinedTextField(
                    value = nom,
                    onValueChange = {
                        nom = it
                        nomError = null
                    },
                    label = { Text("Nom du médicament *") },
                    placeholder = { Text("Ex : Paracétamol 500mg") },
                    isError = nomError != null,
                    supportingText = {
                        if (nomError != null) {
                            Text(
                                text = nomError!!,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                // ── Catégorie ────────────────────────────────
                ExposedDropdownMenuBox(
                    expanded = categorieExpanded,
                    onExpandedChange = { categorieExpanded = !categorieExpanded }
                ) {
                    OutlinedTextField(
                        value = categorie,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Catégorie *") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categorieExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = categorieExpanded,
                        onDismissRequest = { categorieExpanded = false }
                    ) {
                        CATEGORIES.forEach { cat ->
                            DropdownMenuItem(
                                text = { Text(cat) },
                                onClick = {
                                    categorie = cat
                                    categorieExpanded = false
                                }
                            )
                        }
                    }
                }

                // ── Description ──────────────────────────────
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    placeholder = { Text("Ex : Antalgique et antipyrétique") },
                    minLines = 2,
                    maxLines = 4,
                    modifier = Modifier.fillMaxWidth()
                )

                // ── Prix ─────────────────────────────────────
                OutlinedTextField(
                    value = prixTexte,
                    onValueChange = {
                        prixTexte = it
                        prixError = null
                    },
                    label = { Text("Prix (GNF) *") },
                    placeholder = { Text("Ex : 15000") },
                    isError = prixError != null,
                    supportingText = {
                        if (prixError != null) {
                            Text(
                                text = prixError!!,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                // ── Bouton Enregistrer ────────────────────────
                Button(
                    onClick = {
                        // Validation
                        var isValid = true
                        if (nom.isBlank()) {
                            nomError = "Le nom du médicament est obligatoire."
                            isValid = false
                        }
                        val prix = prixTexte.toDoubleOrNull()
                        if (prixTexte.isBlank() || prix == null || prix < 0) {
                            prixError = "Veuillez saisir un prix valide en GNF."
                            isValid = false
                        }
                        if (isValid && prix != null) {
                            isLoading = true
                            scope.launch {
                                val succes = medicamentViewModel.ajouterMedicament(
                                    nom = nom,
                                    categorie = categorie,
                                    description = description,
                                    prix = prix
                                )
                                isLoading = false
                                if (succes) {
                                    snackbarHostState.showSnackbar("✅ Médicament ajouté avec succès !")
                                    onNavigateBack()
                                } else {
                                    snackbarHostState.showSnackbar("⚠️ Une erreur est survenue. Veuillez réessayer.")
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    Text(
                        text = "Enregistrer",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }
        }
    }
}
