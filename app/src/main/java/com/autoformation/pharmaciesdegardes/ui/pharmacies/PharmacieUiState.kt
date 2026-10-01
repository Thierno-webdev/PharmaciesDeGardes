package com.example.pharmaciedegarde.ui.pharmacies

import com.example.pharmaciedegarde.data.model.Pharmacie

sealed interface PharmacieUiState {
    object Loading : PharmacieUiState

    data class Success(
        val pharmacies: List<Pharmacie>,
        val selectedCommune: String? = null,
        val isOnlyDeGarde: Boolean = false
    ) : PharmacieUiState

    object Empty : PharmacieUiState

    data class Error(val message: String) : PharmacieUiState
}