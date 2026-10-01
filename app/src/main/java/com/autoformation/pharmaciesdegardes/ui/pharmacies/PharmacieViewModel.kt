package com.autoformation.pharmaciesdegardes.ui.pharmacies

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PharmacieViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(PharmacieUiState())
    val uiState: StateFlow<PharmacieUiState> = _uiState.asStateFlow()

    // Données de test structurées selon le modèle de stock (Pharmacie <-> Stock <-> Medicament)
    private val toutesLesPharmacies = listOf(
        PharmacieItemUi(
            id = "1",
            nom = "Pharmacie Kipé",
            commune = "Ratoma",
            adresse = "Kipé Centre, en face de la mosquée",
            telephone = "+224 620 00 00 00",
            estDeGarde = true,
            medicamentsEnStock = listOf(
                MedicamentUi("m1", "Paracétamol", "Antalgique 500mg", "Doliprane"),
                MedicamentUi("m2", "Amoxicilline", "Antibiotique 1g", "Clamoxyl"),
                MedicamentUi("m3", "Ibuprofène", "Anti-inflammatoire 400mg", "Advique")
            )
        ),
        PharmacieItemUi(
            id = "2",
            nom = "Pharmacie Boulbinet",
            commune = "Kaloum",
            adresse = "Avenue de la République",
            telephone = "+224 621 11 22 33",
            estDeGarde = true,
            medicamentsEnStock = listOf(
                MedicamentUi("m1", "Paracétamol", "Antalgique 500mg", "Doliprane"),
                MedicamentUi("m4", "Oméprazole", "Anti-acide 20mg", "Mopral")
            )
        ),
        PharmacieItemUi(
            id = "3",
            nom = "Pharmacie Lambanyi",
            commune = "Ratoma",
            adresse = "Carrefour Lambanyi",
            telephone = "+224 622 33 44 55",
            estDeGarde = false,
            medicamentsEnStock = listOf(
                MedicamentUi("m2", "Amoxicilline", "Antibiotique 1g", "Clamoxyl")
            )
        )
    )

    init {
        applyFilters()
    }

    fun onRechercheMedicamentChanged(query: String) {
        _uiState.update { it.copy(rechercheMedicament = query) }
        applyFilters()
    }

    fun onCommuneChanged(commune: String) {
        _uiState.update { it.copy(communeSelectionnee = commune) }
        applyFilters()
    }

    fun onDeGardeToggled(checked: Boolean) {
        _uiState.update { it.copy(deGardeUniquement = checked) }
        applyFilters()
    }

    private fun applyFilters() {
        val state = _uiState.value
        val queryMed = state.rechercheMedicament.trim().lowercase()

        val resultat = toutesLesPharmacies.filter { pharmacie ->
            // 1. Filtre Commune
            val matchCommune = state.communeSelectionnee == "Toutes" ||
                    pharmacie.commune.equals(state.communeSelectionnee, ignoreCase = true)

            // 2. Filtre Garde
            val matchGarde = !state.deGardeUniquement || pharmacie.estDeGarde

            // 3. Filtre Médicament (Stock)
            val matchMedicament = queryMed.isEmpty() || pharmacie.medicamentsEnStock.any { med ->
                med.nom.lowercase().contains(queryMed) ||
                        med.nomCommercial.lowercase().contains(queryMed) ||
                        med.description.lowercase().contains(queryMed)
            }

            matchCommune && matchGarde && matchMedicament
        }

        _uiState.update { it.copy(pharmaciesAffichees = resultat) }
    }
}