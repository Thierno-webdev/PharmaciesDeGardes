package com.autoformation.pharmaciesdegardes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.autoformation.pharmaciesdegardes.data.model.Favori
import com.autoformation.pharmaciesdegardes.data.repository.IFavoriRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

// État de l'UI pour les favoris
sealed class FavoriUiState {
    object Loading : FavoriUiState()
    data class Success(val favoris: List<Favori>) : FavoriUiState()
    object Empty : FavoriUiState()
    data class Error(val message: String) : FavoriUiState()
}

class FavoriViewModel(
    private val repository: IFavoriRepository
) : ViewModel() {

    private val _favorisState = MutableStateFlow<FavoriUiState>(FavoriUiState.Loading)
    val favorisState: StateFlow<FavoriUiState> = _favorisState.asStateFlow()

    init {
        loadFavoris()
    }

    fun loadFavoris() {
        viewModelScope.launch {
            _favorisState.value = FavoriUiState.Loading
            repository.getAllFavoris()
                .catch { e ->
                    _favorisState.value = FavoriUiState.Error(
                        e.message ?: "Erreur lors du chargement des favoris"
                    )
                }
                .collect { favoris ->
                    _favorisState.value = if (favoris.isEmpty()) {
                        FavoriUiState.Empty
                    } else {
                        FavoriUiState.Success(favoris)
                    }
                }
        }
    }

    fun ajouterFavori(pharmacieId: Long) {
        viewModelScope.launch {
            val favori = Favori(pharmacieId = pharmacieId)
            repository.insert(favori)
        }
    }

    fun supprimerFavori(favori: Favori) {
        viewModelScope.launch {
            repository.delete(favori)
        }
    }
}
