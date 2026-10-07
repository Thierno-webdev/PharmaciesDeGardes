package com.autoformation.pharmaciesdegardes.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.autoformation.pharmaciesdegardes.data.model.Pharmacie
import com.autoformation.pharmaciesdegardes.data.model.Medicament
import com.autoformation.pharmaciesdegardes.data.model.Stock
import com.autoformation.pharmaciesdegardes.data.model.Favori
import com.autoformation.pharmaciesdegardes.data.dao.PharmacieDao
import com.autoformation.pharmaciesdegardes.data.dao.MedicamentDao
import com.autoformation.pharmaciesdegardes.data.dao.StockDao
import com.autoformation.pharmaciesdegardes.data.dao.FavoriDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

@Database(
    entities = [Pharmacie::class, Medicament::class, Stock::class, Favori::class],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun pharmacieDao(): PharmacieDao
    abstract fun medicamentDao(): MedicamentDao
    abstract fun stockDao(): StockDao
    abstract fun favoriDao(): FavoriDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        private val databaseScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

        val MIGRATION_1_2 = object : androidx.room.migration.Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Création de la nouvelle table avec le nouveau schéma
                database.execSQL("""
                    CREATE TABLE IF NOT EXISTS `pharmacies_new` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 
                        `nom` TEXT NOT NULL, 
                        `commune` TEXT NOT NULL, 
                        `quartier` TEXT NOT NULL, 
                        `telephone` TEXT NOT NULL, 
                        `latitude` REAL NOT NULL, 
                        `longitude` REAL NOT NULL, 
                        `estDeGarde` INTEGER NOT NULL, 
                        `debutGarde` TEXT, 
                        `finGarde` TEXT
                    )
                """.trimIndent())
                
                // Copie des données depuis l'ancienne table avec transformation
                // Utilisation de SUBSTR et INSTR pour séparer 'adresse' en 'commune' et 'quartier'
                database.execSQL("""
                    INSERT INTO pharmacies_new (id, nom, commune, quartier, telephone, latitude, longitude, estDeGarde, debutGarde, finGarde)
                    SELECT 
                        id, 
                        nom, 
                        CASE WHEN INSTR(adresse, ',') > 0 THEN TRIM(SUBSTR(adresse, 1, INSTR(adresse, ',') - 1)) ELSE adresse END, 
                        CASE WHEN INSTR(adresse, ',') > 0 THEN TRIM(SUBSTR(adresse, INSTR(adresse, ',') + 1)) ELSE '' END, 
                        telephone, 
                        latitude, 
                        longitude, 
                        deGarde, 
                        NULL, 
                        NULL 
                    FROM pharmacies
                """.trimIndent())
                
                // Suppression de l'ancienne table et renommage
                database.execSQL("DROP TABLE pharmacies")
                database.execSQL("ALTER TABLE pharmacies_new RENAME TO pharmacies")
            }
        }

        val MIGRATION_2_3 = object : androidx.room.migration.Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // 1. Ajout de la colonne 'categorie' dans medicaments
                database.execSQL("ALTER TABLE medicaments ADD COLUMN `categorie` TEXT NOT NULL DEFAULT 'G\u00e9n\u00e9ral'")

                // 2. Recr\u00e9er la table stocks avec 'disponible' (Boolean) au lieu de 'quantite' (Int)
                database.execSQL("""
                    CREATE TABLE IF NOT EXISTS `stocks_new` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `pharmacieId` INTEGER NOT NULL,
                        `medicamentId` INTEGER NOT NULL,
                        `disponible` INTEGER NOT NULL,
                        FOREIGN KEY(`pharmacieId`) REFERENCES `pharmacies`(`id`) ON DELETE CASCADE,
                        FOREIGN KEY(`medicamentId`) REFERENCES `medicaments`(`id`) ON DELETE CASCADE
                    )
                """.trimIndent())
                // Copie avec conversion quantite > 0 => disponible = 1
                database.execSQL("""
                    INSERT INTO stocks_new (id, pharmacieId, medicamentId, disponible)
                    SELECT id, pharmacieId, medicamentId, CASE WHEN quantite > 0 THEN 1 ELSE 0 END
                    FROM stocks
                """.trimIndent())
                database.execSQL("DROP TABLE stocks")
                database.execSQL("ALTER TABLE stocks_new RENAME TO stocks")
                database.execSQL("CREATE INDEX IF NOT EXISTS `index_stocks_pharmacieId` ON `stocks` (`pharmacieId`)")
                database.execSQL("CREATE INDEX IF NOT EXISTS `index_stocks_medicamentId` ON `stocks` (`medicamentId`)")
            }
        }

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "pharmacies_database"
                )
                .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        databaseScope.launch {
                            INSTANCE?.let { database ->
                                DatabaseSeeder.seedDatabase(database)
                            }
                        }
                    }
                })
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
