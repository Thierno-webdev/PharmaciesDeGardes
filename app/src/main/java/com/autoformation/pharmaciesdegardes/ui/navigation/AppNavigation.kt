package com.autoformation.pharmaciesdegardes.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.autoformation.pharmaciesdegardes.ui.dashboard.DashboardScreen
import com.autoformation.pharmaciesdegardes.ui.medicaments.AjoutMedicamentScreen
import com.autoformation.pharmaciesdegardes.ui.medicaments.MedicamentsScreen
import com.autoformation.pharmaciesdegardes.ui.pharmacies.PharmacieDetailScreen
import com.autoformation.pharmaciesdegardes.ui.pharmacies.PharmaciesScreen
import com.autoformation.pharmaciesdegardes.ui.welcome.WelcomeScreen
import com.autoformation.pharmaciesdegardes.viewmodel.FavoriViewModel
import com.autoformation.pharmaciesdegardes.viewmodel.MedicamentViewModel
import com.autoformation.pharmaciesdegardes.viewmodel.PharmacieUiState
import com.autoformation.pharmaciesdegardes.viewmodel.PharmacieViewModel
import com.autoformation.pharmaciesdegardes.viewmodel.StockViewModel

// ─────────────────────────────────────────────
// Définition des routes
// ─────────────────────────────────────────────

object AppRoutes {
    const val WELCOME    = "welcome"
    const val DASHBOARD  = "dashboard"
    const val PHARMACIES = "pharmacies"
    const val PHARMACIE_DETAIL = "pharmacie/{pharmacieId}"
    const val MEDICAMENTS       = "medicaments"
    const val AJOUT_MEDICAMENT  = "ajout_medicament"
    const val FAVORIS            = "favoris"

    fun pharmacieDetail(pharmacieId: Long) = "pharmacie/$pharmacieId"
}

// ─────────────────────────────────────────────
// Graphe de navigation principal
// ─────────────────────────────────────────────

@Composable
fun AppNavigation(
    pharmacieViewModel: PharmacieViewModel,
    favoriViewModel: FavoriViewModel,
    medicamentViewModel: MedicamentViewModel,
    stockViewModel: StockViewModel
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppRoutes.WELCOME
    ) {

        // ── Accueil (Welcome Screen) ──────────────────
        composable(AppRoutes.WELCOME) {
            WelcomeScreen(
                onNavigateToDashboard = {
                    navController.navigate(AppRoutes.DASHBOARD) {
                        popUpTo(AppRoutes.WELCOME) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        // ── Dashboard ─────────────────────────────────
        composable(AppRoutes.DASHBOARD) {
            DashboardScreen(
                pharmacieViewModel = pharmacieViewModel,
                favoriViewModel = favoriViewModel,
                onNavigateToPharmacies = {
                    navController.navigate(AppRoutes.PHARMACIES)
                },
                onNavigateToMedicaments = {
                    navController.navigate(AppRoutes.MEDICAMENTS)
                },
                onNavigateToFavoris = {
                    navController.navigate(AppRoutes.FAVORIS)
                }
            )
        }

        // ── Liste des pharmacies ──────────────────────
        composable(AppRoutes.PHARMACIES) {
            PharmaciesScreen(
                pharmacieViewModel = pharmacieViewModel,
                onNavigateBack = { navController.popBackStack() },
                onPharmacieClick = { pharmacie ->
                    navController.navigate(AppRoutes.pharmacieDetail(pharmacie.id))
                }
            )
        }

        // ── Détail d'une pharmacie ────────────────────
        composable(
            route = AppRoutes.PHARMACIE_DETAIL,
            arguments = listOf(
                navArgument("pharmacieId") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val pharmacieId = backStackEntry.arguments?.getLong("pharmacieId") ?: return@composable

            // Résoudre la pharmacie depuis le ViewModel (liste déjà chargée)
            val allState by pharmacieViewModel.allPharmaciesState.collectAsState()
            val pharmacie = when (val state = allState) {
                is PharmacieUiState.Success -> state.pharmacies.find { it.id == pharmacieId }
                else -> null
            }

            if (pharmacie != null) {
                PharmacieDetailScreen(
                    pharmacie = pharmacie,
                    favoriViewModel = favoriViewModel,
                    onBack = { navController.popBackStack() }
                )
            }
        }

        // ── Médicaments ───────────────────────────────
        composable(AppRoutes.MEDICAMENTS) {
            MedicamentsScreen(
                medicamentViewModel = medicamentViewModel,
                stockViewModel = stockViewModel,
                pharmacieViewModel = pharmacieViewModel,
                onNavigateBack = { navController.popBackStack() },
                onAjouterClick = {
                    navController.navigate(AppRoutes.AJOUT_MEDICAMENT)
                }
            )
        }

        // ── Ajout médicament (formulaire) ─────────────
        composable(AppRoutes.AJOUT_MEDICAMENT) {
            AjoutMedicamentScreen(
                medicamentViewModel = medicamentViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // ── Favoris ───────────────────────────────────
        composable(AppRoutes.FAVORIS) {
            FavorisScreen(
                favoriViewModel = favoriViewModel,
                pharmacieViewModel = pharmacieViewModel,
                onNavigateBack = { navController.popBackStack() },
                onPharmacieClick = { pharmacieId ->
                    navController.navigate(AppRoutes.pharmacieDetail(pharmacieId))
                }
            )
        }
    }
}
