package com.example.pharmaciedegarde.ui.pharmacies.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterChipRow(
    communes: List<String>,
    selectedCommune: String?,
    isOnlyDeGarde: Boolean,
    onCommuneSelected: (String?) -> Unit,
    onDeGardeToggled: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Uniquement de garde", style = MaterialTheme.typography.bodyMedium)
            Switch(
                checked = isOnlyDeGarde,
                onCheckedChange = { onDeGardeToggled(it) }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                FilterChip(
                    selected = selectedCommune == null,
                    onClick = { onCommuneSelected(null) },
                    label = { Text("Toutes") }
                )
            }
            items(communes) { commune ->
                FilterChip(
                    selected = selectedCommune == commune,
                    onClick = {
                        if (selectedCommune == commune) onCommuneSelected(null)
                        else onCommuneSelected(commune)
                    },
                    label = { Text(commune) }
                )
            }
        }
    }
}