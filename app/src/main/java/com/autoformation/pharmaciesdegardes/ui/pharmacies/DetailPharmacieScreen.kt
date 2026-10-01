package com.autoformation.pharmaciesdegardes.ui.pharmacies

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailPharmacieScreen(
    pharmacie: PharmacieItemUi,
    onBackClick: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(pharmacie.nom) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Retour")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(text = pharmacie.nom, style = MaterialTheme.typography.headlineMedium)
            Text(text = "Adresse : ${pharmacie.adresse}", style = MaterialTheme.typography.bodyLarge)
            Text(text = "Commune : ${pharmacie.commune}", style = MaterialTheme.typography.bodyLarge)
            Text(text = "Téléphone : ${pharmacie.telephone}", style = MaterialTheme.typography.bodyLarge)

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "💊 Médicaments disponibles en stock",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            pharmacie.medicamentsEnStock.forEach { med ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(text = "${med.nom} (${med.nomCommercial})", style = MaterialTheme.typography.titleSmall)
                        if (med.description.isNotEmpty()) {
                            Text(text = med.description, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { /* Déclencher l'appel via Intent */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Phone, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Appeler la pharmacie")
            }
        }
    }
}