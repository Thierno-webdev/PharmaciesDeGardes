package com.autoformation.pharmaciesdegardes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.autoformation.pharmaciesdegardes.data.model.Medicament
import com.autoformation.pharmaciesdegardes.data.repository.IMedicamentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

// État de l'UI pour les médicaments
sealed class MedicamentUiState {
    object Loading : MedicamentUiState()
    data class Success(val medicaments: List<Medicament>) : MedicamentUiState()
    object Empty : MedicamentUiState()
    data class Error(val message: String) : MedicamentUiState()
}

class MedicamentViewModel(
    private val repository: IMedicamentRepository
) : ViewModel() {

    // Liste complète (source de vérité)
    private var _allMedicaments: List<Medicament> = emptyList()

    // État exposé à l'UI (liste complète ou résultat de recherche)
    private val _medicamentsState = MutableStateFlow<MedicamentUiState>(MedicamentUiState.Loading)
    val medicamentsState: StateFlow<MedicamentUiState> = _medicamentsState.asStateFlow()

    // Terme de recherche courant
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    init {
        loadMedicaments()
    }

    fun loadMedicaments() {
        viewModelScope.launch {
            _medicamentsState.value = MedicamentUiState.Loading
            repository.getAllMedicaments()
                .catch { e ->
                    _medicamentsState.value = MedicamentUiState.Error(
                        e.message ?: "Erreur lors du chargement des médicaments"
                    )
                }
                .collect { medicaments ->
                    _allMedicaments = medicaments
                    applySearch(_searchQuery.value)
                }
        }
    }

    // Filtrage simple par nom côté mémoire
    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
        applySearch(query)
    }

    private fun applySearch(query: String) {
        val filtered = if (query.isBlank()) {
            _allMedicaments
        } else {
            _allMedicaments.filter { medicament ->
                medicament.nom.contains(query.trim(), ignoreCase = true)
            }
        }
        _medicamentsState.value = if (filtered.isEmpty()) {
            MedicamentUiState.Empty
        } else {
            MedicamentUiState.Success(filtered)
        }
    }

    /**
     * Enregistre un nouveau médicament dans la base de données Room.
     * Retourne true si l'insertion a réussi, false sinon.
     */
    suspend fun ajouterMedicament(
        nom: String,
        categorie: String,
        description: String,
        prix: Double
    ): Boolean {
        return try {
            val medicament = Medicament(
                nom = nom.trim(),
                categorie = categorie.trim(),
                description = description.trim(),
                prix = prix
            )
            repository.insert(medicament)
            true
        } catch (e: Exception) {
            false
        }
    }
}
