package com.autoformation.pharmaciesdegardes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.autoformation.pharmaciesdegardes.data.model.Stock
import com.autoformation.pharmaciesdegardes.data.repository.IStockRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

// État de l'UI pour les stocks
sealed class StockUiState {
    object Loading : StockUiState()
    data class Success(val stocks: List<Stock>) : StockUiState()
    object Empty : StockUiState()
    data class Error(val message: String) : StockUiState()
}

class StockViewModel(
    private val repository: IStockRepository
) : ViewModel() {

    private val _stocksState = MutableStateFlow<StockUiState>(StockUiState.Loading)
    val stocksState: StateFlow<StockUiState> = _stocksState.asStateFlow()

    // Identifiant de la pharmacie dont on affiche les stocks
    private val _currentPharmacieId = MutableStateFlow<Long?>(null)
    val currentPharmacieId: StateFlow<Long?> = _currentPharmacieId.asStateFlow()

    /**
     * Charge les stocks pour la pharmacie donnée.
     * Annule automatiquement le collect précédent via le scope du ViewModel.
     */
    fun loadStocksForPharmacie(pharmacieId: Long) {
        _currentPharmacieId.value = pharmacieId
        viewModelScope.launch {
            _stocksState.value = StockUiState.Loading
            repository.getStocksForPharmacie(pharmacieId)
                .catch { e ->
                    _stocksState.value = StockUiState.Error(
                        e.message ?: "Erreur lors du chargement des stocks"
                    )
                }
                .collect { stocks ->
                    _stocksState.value = if (stocks.isEmpty()) {
                        StockUiState.Empty
                    } else {
                        StockUiState.Success(stocks)
                    }
                }
        }
    }

    /**
     * Charge les stocks pour le médicament donné.
     * Annule automatiquement le collect précédent via le scope du ViewModel.
     */
    fun loadStocksForMedicament(medicamentId: Long) {
        viewModelScope.launch {
            _stocksState.value = StockUiState.Loading
            repository.getStocksForMedicament(medicamentId)
                .catch { e ->
                    _stocksState.value = StockUiState.Error(
                        e.message ?: "Erreur lors du chargement des stocks"
                    )
                }
                .collect { stocks ->
                    _stocksState.value = if (stocks.isEmpty()) {
                        StockUiState.Empty
                    } else {
                        StockUiState.Success(stocks)
                    }
                }
        }
    }
}
