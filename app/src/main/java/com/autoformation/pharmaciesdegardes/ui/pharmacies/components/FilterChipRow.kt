package com.autoformation.pharmaciesdegardes.ui.pharmacies.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FilterChipRow(
    rechercheMedicament: String,
    onRechercheChanged: (String) -> Unit,
    deGardeUniquement: Boolean,
    onDeGardeToggled: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        // Barre de recherche de médicament
        OutlinedTextField(
            value = rechercheMedicament,
            onValueChange = onRechercheChanged,
            label = { Text("Rechercher un médicament (ex: Paracétamol)") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            trailingIcon = {
                if (rechercheMedicament.isNotEmpty()) {
                    IconButton(onClick = { onRechercheChanged("") }) {
                        Icon(Icons.Default.Clear, contentDescription = "Effacer")
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Puce de filtrage "De garde"
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            FilterChip(
                selected = deGardeUniquement,
                onClick = { onDeGardeToggled(!deGardeUniquement) },
                label = { Text("🟢 De garde uniquement") }
            )
        }
    }
}