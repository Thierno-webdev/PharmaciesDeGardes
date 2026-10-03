package com.autoformation.pharmaciesdegardes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.autoformation.pharmaciesdegardes.data.model.Pharmacie
import com.autoformation.pharmaciesdegardes.data.repository.IPharmacieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

// État de l'UI pour les pharmacies
sealed class PharmacieUiState {
    object Loading : PharmacieUiState()
    data class Success(val pharmacies: List<Pharmacie>) : PharmacieUiState()
    object Empty : PharmacieUiState()
    data class Error(val message: String) : PharmacieUiState()
}

class PharmacieViewModel(
    private val repository: IPharmacieRepository
) : ViewModel() {

    // Toutes les pharmacies (données brutes du repository)
    private val _allPharmaciesState = MutableStateFlow<PharmacieUiState>(PharmacieUiState.Loading)
    val allPharmaciesState: StateFlow<PharmacieUiState> = _allPharmaciesState.asStateFlow()

    // Pharmacies de garde uniquement
    private val _pharmaciesDeGardeState = MutableStateFlow<PharmacieUiState>(PharmacieUiState.Loading)
    val pharmaciesDeGardeState: StateFlow<PharmacieUiState> = _pharmaciesDeGardeState.asStateFlow()

    // Pharmacie sélectionnée (pour l'écran détail)
    private val _selectedPharmacie = MutableStateFlow<Pharmacie?>(null)
    val selectedPharmacie: StateFlow<Pharmacie?> = _selectedPharmacie.asStateFlow()

    // ── Filtres ──────────────────────────────────────────────────────────────

    /** Commune sélectionnée pour le filtre. Valeur nulle = « Toutes » */
    private val _filtreCommune = MutableStateFlow<String?>(null)
    val filtreCommune: StateFlow<String?> = _filtreCommune.asStateFlow()

    /** true = pharmacies de garde uniquement, false = toutes */
    private val _filtreDeGarde = MutableStateFlow(false)
    val filtreDeGarde: StateFlow<Boolean> = _filtreDeGarde.asStateFlow()

    /**
     * Liste des communes disponibles déduites des pharmacies chargées.
     */
    private val _communesDisponibles = MutableStateFlow<List<String>>(emptyList())
    val communesDisponibles: StateFlow<List<String>> = _communesDisponibles.asStateFlow()

    /** État de l'UI après application des filtres */
    private val _pharmaciesFiltreesState = MutableStateFlow<PharmacieUiState>(PharmacieUiState.Loading)
    val pharmaciesFiltreesState: StateFlow<PharmacieUiState> = _pharmaciesFiltreesState.asStateFlow()

    init {
        loadAllPharmacies()
        loadPharmaciesDeGarde()
        observerFiltres()
    }

    fun loadAllPharmacies() {
        viewModelScope.launch {
            _allPharmaciesState.value = PharmacieUiState.Loading
            repository.getAllPharmacies()
                .catch { e ->
                    _allPharmaciesState.value = PharmacieUiState.Error(
                        e.message ?: "Erreur lors du chargement des pharmacies"
                    )
                }
                .collect { pharmacies ->
                    _allPharmaciesState.value = if (pharmacies.isEmpty()) {
                        PharmacieUiState.Empty
                    } else {
                        // Mettre à jour les communes disponibles dès que les données arrivent
                        _communesDisponibles.value = extraireCommunesUniques(pharmacies)
                        PharmacieUiState.Success(pharmacies)
                    }
                }
        }
    }

    fun loadPharmaciesDeGarde() {
        viewModelScope.launch {
            _pharmaciesDeGardeState.value = PharmacieUiState.Loading
            repository.getPharmaciesDeGarde()
                .catch { e ->
                    _pharmaciesDeGardeState.value = PharmacieUiState.Error(
                        e.message ?: "Erreur lors du chargement des pharmacies de garde"
                    )
                }
                .collect { pharmacies ->
                    _pharmaciesDeGardeState.value = if (pharmacies.isEmpty()) {
                        PharmacieUiState.Empty
                    } else {
                        PharmacieUiState.Success(pharmacies)
                    }
                }
        }
    }

    /**
     * Observe les changements de filtres et de données pour recalculer
     * la liste filtrée. La logique est entièrement dans le ViewModel.
     */
    private fun observerFiltres() {
        viewModelScope.launch {
            combine(
                _allPharmaciesState,
                _filtreCommune,
                _filtreDeGarde
            ) { etat, commune, deGarde ->
                appliquerFiltres(etat, commune, deGarde)
            }.collect { etatFiltre ->
                _pharmaciesFiltreesState.value = etatFiltre
            }
        }
    }

    /**
     * Applique les filtres sélectionnés sur la liste brute.
     * Retourne un [PharmacieUiState] adapté au résultat.
     */
    private fun appliquerFiltres(
        etat: PharmacieUiState,
        commune: String?,
        deGarde: Boolean
    ): PharmacieUiState {
        return when (etat) {
            is PharmacieUiState.Loading -> PharmacieUiState.Loading
            is PharmacieUiState.Error -> etat
            is PharmacieUiState.Empty -> PharmacieUiState.Empty
            is PharmacieUiState.Success -> {
                var resultats = etat.pharmacies

                // Filtre par commune
                if (commune != null) {
                    resultats = resultats.filter { pharmacie ->
                        pharmacie.commune == commune
                    }
                }

                // Filtre de garde
                if (deGarde) {
                    resultats = resultats.filter { it.estDeGarde }
                }

                if (resultats.isEmpty()) {
                    // Cas : aucune pharmacie ne correspond aux filtres
                    // On retourne un état Success vide pour distinguer
                    // « aucun résultat de filtre » de « liste vide »
                    PharmacieUiState.Success(emptyList())
                } else {
                    PharmacieUiState.Success(resultats)
                }
            }
        }
    }

    // ── Actions sur les filtres ───────────────────────────────────────────

    /** Sélectionne une commune. Passer null pour « Toutes ». */
    fun setFiltreCommune(commune: String?) {
        _filtreCommune.value = commune
    }

    /** Active ou désactive le filtre « de garde ». */
    fun setFiltreDeGarde(deGarde: Boolean) {
        _filtreDeGarde.value = deGarde
    }

    // ── Pharmacie sélectionnée ────────────────────────────────────────────

    fun selectPharmacie(pharmacie: Pharmacie) {
        _selectedPharmacie.value = pharmacie
    }

    fun clearSelectedPharmacie() {
        _selectedPharmacie.value = null
    }

    // ── Fonctions utilitaires (privées) ───────────────────────────────────

    /**
     * Déduit la liste triée et dédupliquée des communes depuis une liste
     * de pharmacies.
     */
    private fun extraireCommunesUniques(pharmacies: List<Pharmacie>): List<String> {
        return pharmacies
            .map { it.commune }
            .distinct()
            .sorted()
    }
}
