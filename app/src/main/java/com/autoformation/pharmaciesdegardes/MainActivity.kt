package com.example.pharmaciedegarde

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pharmaciedegarde.data.database.AppDatabase
import com.example.pharmaciedegarde.ui.pharmacies.DetailPharmacieScreen
import com.example.pharmaciedegarde.ui.pharmacies.ListePharmaciesScreen
import com.example.pharmaciedegarde.ui.pharmacies.PharmacieUiState
import com.example.pharmaciedegarde.ui.pharmacies.PharmacieViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Récupération du DAO via la base de données créée par Antony
        val database = AppDatabase.getDatabase(applicationContext)
        val pharmacieDao = database.pharmacieDao()

        // 2. Création de la Factory pour instancier ton ViewModel avec le DAO
        val viewModelFactory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return PharmacieViewModel(pharmacieDao) as T
            }
        }

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel: PharmacieViewModel = viewModel(factory = viewModelFactory)

                    // Variable d'état locale pour simuler la navigation vers le détail
                    var selectedPharmacieId by remember { mutableStateOf<Int?>(null) }

                    if (selectedPharmacieId == null) {
                        // Affichage de ton Écran 1 (Liste & Filtres)
                        ListePharmaciesScreen(
                            viewModel = viewModel,
                            onPharmacieClick = { id -> selectedPharmacieId = id }
                        )
                    } else {
                        // Affichage de ton Écran 2 (Détail de la pharmacie sélectionnée)
                        val uiState by viewModel.uiState.collectAsState()
                        val pharmacie = if (uiState is PharmacieUiState.Success) {
                            (uiState as PharmacieUiState.Success).pharmacies.find { it.id == selectedPharmacieId }
                        } else null

                        if (pharmacie != null) {
                            DetailPharmacieScreen(
                                pharmacie = pharmacie,
                                isFavorite = false,
                                onToggleFavorite = { /* Géré avec le module favoris */ },
                                onBackClick = { selectedPharmacieId = null }
                            )
                        }
                    }
                }
            }
        }
    }
}