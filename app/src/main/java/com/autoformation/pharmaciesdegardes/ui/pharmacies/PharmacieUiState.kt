package com.autoformation.pharmaciesdegardes.ui.pharmacies

data class MedicamentUi(
    val id: String,
    val nom: String,
    val description: String = "",
    val nomCommercial: String = ""
)

data class PharmacieItemUi(
    val id: String,
    val nom: String,
    val commune: String,
    val adresse: String,
    val telephone: String,
    val estDeGarde: Boolean,
    val medicamentsEnStock: List<MedicamentUi> = emptyList()
)

data class PharmacieUiState(
    val rechercheMedicament: String = "",
    val communeSelectionnee: String = "Toutes",
    val deGardeUniquement: Boolean = false,
    val pharmaciesAffichees: List<PharmacieItemUi> = emptyList(),
    val isLoading: Boolean = false
)