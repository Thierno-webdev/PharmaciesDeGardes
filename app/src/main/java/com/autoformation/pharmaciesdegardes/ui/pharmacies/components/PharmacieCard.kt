package com.autoformation.pharmaciesdegardes.ui.pharmacies.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.autoformation.pharmaciesdegardes.ui.pharmacies.PharmacieItemUi

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PharmacieCard(
    pharmacie: PharmacieItemUi,
    queryMedicament: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = pharmacie.nom,
                    style = MaterialTheme.typography.titleMedium
                )
                if (pharmacie.estDeGarde) {
                    Text(
                        text = "🟢 DE GARDE",
                        color = Color(0xFF2E7D32),
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "📍 Commune : ${pharmacie.commune}", style = MaterialTheme.typography.bodySmall)
            Text(text = "📞 ${pharmacie.telephone}", style = MaterialTheme.typography.bodySmall)

            // Indication de la disponibilité en stock si une recherche de médicament est active
            if (queryMedicament.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(8.dp))

                val medsDispo = pharmacie.medicamentsEnStock.filter {
                    it.nom.contains(queryMedicament, ignoreCase = true) ||
                            it.nomCommercial.contains(queryMedicament, ignoreCase = true)
                }

                if (medsDispo.isNotEmpty()) {
                    Text(
                        text = "💊 Disponible en stock : ${medsDispo.joinToString { it.nom }}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}