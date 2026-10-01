package com.example.pharmaciedegarde.ui.pharmacies

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.pharmaciedegarde.data.model.Pharmacie

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailPharmacieScreen(
    pharmacie: Pharmacie,
    isFavorite: Boolean,
    onToggleFavorite: (Int) -> Unit,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(pharmacie.nom) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Retour")
                    }
                },
                actions = {
                    IconButton(onClick = { onToggleFavorite(pharmacie.id) }) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favori",
                            tint = if (isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = pharmacie.nom, style = MaterialTheme.typography.headlineSmall)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Commune : ${pharmacie.commune}", style = MaterialTheme.typography.bodyMedium)
                    Text(text = "Quartier : ${pharmacie.quartier}", style = MaterialTheme.typography.bodyMedium)
                    Text(text = "Téléphone : ${pharmacie.telephone}", style = MaterialTheme.typography.bodyMedium)

                    Spacer(modifier = Modifier.height(12.dp))

                    if (pharmacie.estDeGarde) {
                        Text(
                            text = "Statut : DE GARDE",
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.titleSmall
                        )
                        Text(
                            text = "Période : Du ${pharmacie.debutGarde} au ${pharmacie.finGarde}",
                            style = MaterialTheme.typography.bodySmall
                        )
                    } else {
                        Text(text = "Statut : Service Normal", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // Action 1: Appel téléphonique
                Button(
                    onClick = {
                        val intent = Intent(Intent.ACTION_DIAL).apply {
                            data = Uri.parse("tel:${pharmacie.telephone}")
                        }
                        context.startActivity(intent)
                    },
                    modifier = Modifier.weight(1f).padding(end = 8.dp)
                ) {
                    Icon(Icons.Default.Phone, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Appeler")
                }

                // Action 2: Ouverture GPS / Carte
                Button(
                    onClick = {
                        val gmmIntentUri = Uri.parse("geo:${pharmacie.latitude},${pharmacie.longitude}?q=${pharmacie.latitude},${pharmacie.longitude}(${Uri.encode(pharmacie.nom)})")
                        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri).apply {
                            setPackage("com.google.android.apps.maps")
                        }
                        context.startActivity(mapIntent)
                    },
                    modifier = Modifier.weight(1f).padding(start = 8.dp)
                ) {
                    Icon(Icons.Default.LocationOn, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Itinéraire")
                }
            }
        }
    }
}