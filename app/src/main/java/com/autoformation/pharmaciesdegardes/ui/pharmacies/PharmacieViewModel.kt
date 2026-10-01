package com.example.pharmaciedegarde.ui.pharmacies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pharmaciedegarde.data.dao.PharmacieDao
import com.example.pharmaciedegarde.data.model.Pharmacie
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class PharmacieViewModel(
    private val pharmacieDao: PharmacieDao
) : ViewModel() {

    private val _uiState = MutableStateFlow<PharmacieUiState>(PharmacieUiState.Loading)
    val uiState: StateFlow<PharmacieUiState> = _uiState.asStateFlow()

    private var allPharmacies: List<Pharmacie> = emptyList()
    private var currentCommune: String? = null
    private var isOnlyDeGarde: Boolean = false

    init {
        loadPharmacies()
    }

    fun loadPharmacies() {
        viewModelScope.launch {
            _uiState.value = PharmacieUiState.Loading
            pharmacieDao.getAllPharmacies()
                .catch { e ->
                    _uiState.value = PharmacieUiState.Error(e.message ?: "Erreur lors du chargement")
                }
                .collect { list ->
                    allPharmacies = list
                    applyFilters()
                }
        }
    }

    fun filterByCommune(commune: String?) {
        currentCommune = commune
        applyFilters()
    }

    fun toggleDeGardeFilter(deGardeOnly: Boolean) {
        isOnlyDeGarde = deGardeOnly
        applyFilters()
    }

    private fun applyFilters() {
        var filteredList = allPharmacies

        if (isOnlyDeGarde) {
            filteredList = filteredList.filter { it.estDeGarde }
        }

        if (!currentCommune.isNullOrEmpty()) {
            filteredList = filteredList.filter { it.commune.equals(currentCommune, ignoreCase = true) }
        }

        _uiState.value = if (filteredList.isEmpty()) {
            PharmacieUiState.Empty
        } else {
            PharmacieUiState.Success(
                pharmacies = filteredList,
                selectedCommune = currentCommune,
                isOnlyDeGarde = isOnlyDeGarde
            )
        }
    }
}