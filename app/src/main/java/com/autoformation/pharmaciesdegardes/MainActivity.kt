package com.autoformation.pharmaciesdegardes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.ViewModelProvider
import com.autoformation.pharmaciesdegardes.data.database.AppDatabase
import com.autoformation.pharmaciesdegardes.data.repository.FavoriRepositoryImpl
import com.autoformation.pharmaciesdegardes.data.repository.MedicamentRepositoryImpl
import com.autoformation.pharmaciesdegardes.data.repository.PharmacieRepositoryImpl
import com.autoformation.pharmaciesdegardes.data.repository.StockRepositoryImpl
import com.autoformation.pharmaciesdegardes.data.repository.IFavoriRepository
import com.autoformation.pharmaciesdegardes.data.repository.IMedicamentRepository
import com.autoformation.pharmaciesdegardes.data.repository.IPharmacieRepository
import com.autoformation.pharmaciesdegardes.data.repository.IStockRepository
import com.autoformation.pharmaciesdegardes.ui.navigation.AppNavigation
import com.autoformation.pharmaciesdegardes.ui.theme.PharmaciesDeGardesTheme
import com.autoformation.pharmaciesdegardes.viewmodel.FavoriViewModel
import com.autoformation.pharmaciesdegardes.viewmodel.MedicamentViewModel
import com.autoformation.pharmaciesdegardes.viewmodel.PharmacieViewModel
import com.autoformation.pharmaciesdegardes.viewmodel.StockViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // ── Initialisation de la base de données ─────────
        val database = AppDatabase.getDatabase(applicationContext)

        // ── Repositories ─────────────────────────────────
        val pharmacieRepository: IPharmacieRepository = PharmacieRepositoryImpl(database.pharmacieDao())
        val medicamentRepository: IMedicamentRepository = MedicamentRepositoryImpl(database.medicamentDao())
        val stockRepository: IStockRepository = StockRepositoryImpl(database.stockDao())
        val favoriRepository: IFavoriRepository = FavoriRepositoryImpl(database.favoriDao())

        // ── ViewModels avec factory manuelle ─────────────
        val pharmacieViewModel = ViewModelProvider(
            this,
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                    return PharmacieViewModel(pharmacieRepository) as T
                }
            }
        )[PharmacieViewModel::class.java]

        val medicamentViewModel = ViewModelProvider(
            this,
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                    return MedicamentViewModel(medicamentRepository) as T
                }
            }
        )[MedicamentViewModel::class.java]

        val stockViewModel = ViewModelProvider(
            this,
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                    return StockViewModel(stockRepository) as T
                }
            }
        )[StockViewModel::class.java]

        val favoriViewModel = ViewModelProvider(
            this,
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                    return FavoriViewModel(favoriRepository) as T
                }
            }
        )[FavoriViewModel::class.java]

        setContent {
            PharmaciesDeGardesTheme {
                AppNavigation(
                    pharmacieViewModel = pharmacieViewModel,
                    favoriViewModel = favoriViewModel,
                    medicamentViewModel = medicamentViewModel,
                    stockViewModel = stockViewModel
                )
            }
        }
    }
}