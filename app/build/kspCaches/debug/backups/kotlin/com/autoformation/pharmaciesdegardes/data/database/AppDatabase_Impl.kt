package com.autoformation.pharmaciesdegardes.`data`.database

import androidx.room.InvalidationTracker
import androidx.room.RoomOpenDelegate
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.room.util.TableInfo
import androidx.room.util.TableInfo.Companion.read
import androidx.room.util.dropFtsSyncTriggers
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import com.autoformation.pharmaciesdegardes.`data`.dao.FavoriDao
import com.autoformation.pharmaciesdegardes.`data`.dao.FavoriDao_Impl
import com.autoformation.pharmaciesdegardes.`data`.dao.MedicamentDao
import com.autoformation.pharmaciesdegardes.`data`.dao.MedicamentDao_Impl
import com.autoformation.pharmaciesdegardes.`data`.dao.PharmacieDao
import com.autoformation.pharmaciesdegardes.`data`.dao.PharmacieDao_Impl
import com.autoformation.pharmaciesdegardes.`data`.dao.StockDao
import com.autoformation.pharmaciesdegardes.`data`.dao.StockDao_Impl
import javax.`annotation`.processing.Generated
import kotlin.Lazy
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.MutableList
import kotlin.collections.MutableMap
import kotlin.collections.MutableSet
import kotlin.collections.Set
import kotlin.collections.mutableListOf
import kotlin.collections.mutableMapOf
import kotlin.collections.mutableSetOf
import kotlin.reflect.KClass

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class AppDatabase_Impl : AppDatabase() {
  private val _pharmacieDao: Lazy<PharmacieDao> = lazy {
    PharmacieDao_Impl(this)
  }

  private val _medicamentDao: Lazy<MedicamentDao> = lazy {
    MedicamentDao_Impl(this)
  }

  private val _stockDao: Lazy<StockDao> = lazy {
    StockDao_Impl(this)
  }

  private val _favoriDao: Lazy<FavoriDao> = lazy {
    FavoriDao_Impl(this)
  }

  protected override fun createOpenDelegate(): RoomOpenDelegate {
    val _openDelegate: RoomOpenDelegate = object : RoomOpenDelegate(3,
        "4c12ebda3dee74f921d33912e591753e", "c3f560d4976dca69980b64aa2bcdf5c6") {
      public override fun createAllTables(connection: SQLiteConnection) {
        connection.execSQL("CREATE TABLE IF NOT EXISTS `pharmacies` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `nom` TEXT NOT NULL, `commune` TEXT NOT NULL, `quartier` TEXT NOT NULL, `telephone` TEXT NOT NULL, `latitude` REAL NOT NULL, `longitude` REAL NOT NULL, `estDeGarde` INTEGER NOT NULL, `debutGarde` TEXT, `finGarde` TEXT)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `medicaments` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `nom` TEXT NOT NULL, `categorie` TEXT NOT NULL, `description` TEXT NOT NULL, `prix` REAL NOT NULL)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `stocks` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `pharmacieId` INTEGER NOT NULL, `medicamentId` INTEGER NOT NULL, `disponible` INTEGER NOT NULL, FOREIGN KEY(`pharmacieId`) REFERENCES `pharmacies`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`medicamentId`) REFERENCES `medicaments`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_stocks_pharmacieId` ON `stocks` (`pharmacieId`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_stocks_medicamentId` ON `stocks` (`medicamentId`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `favoris` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `pharmacieId` INTEGER NOT NULL, FOREIGN KEY(`pharmacieId`) REFERENCES `pharmacies`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_favoris_pharmacieId` ON `favoris` (`pharmacieId`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)")
        connection.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '4c12ebda3dee74f921d33912e591753e')")
      }

      public override fun dropAllTables(connection: SQLiteConnection) {
        connection.execSQL("DROP TABLE IF EXISTS `pharmacies`")
        connection.execSQL("DROP TABLE IF EXISTS `medicaments`")
        connection.execSQL("DROP TABLE IF EXISTS `stocks`")
        connection.execSQL("DROP TABLE IF EXISTS `favoris`")
      }

      public override fun onCreate(connection: SQLiteConnection) {
      }

      public override fun onOpen(connection: SQLiteConnection) {
        connection.execSQL("PRAGMA foreign_keys = ON")
        internalInitInvalidationTracker(connection)
      }

      public override fun onPreMigrate(connection: SQLiteConnection) {
        dropFtsSyncTriggers(connection)
      }

      public override fun onPostMigrate(connection: SQLiteConnection) {
      }

      public override fun onValidateSchema(connection: SQLiteConnection):
          RoomOpenDelegate.ValidationResult {
        val _columnsPharmacies: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsPharmacies.put("id", TableInfo.Column("id", "INTEGER", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsPharmacies.put("nom", TableInfo.Column("nom", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsPharmacies.put("commune", TableInfo.Column("commune", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsPharmacies.put("quartier", TableInfo.Column("quartier", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsPharmacies.put("telephone", TableInfo.Column("telephone", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsPharmacies.put("latitude", TableInfo.Column("latitude", "REAL", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsPharmacies.put("longitude", TableInfo.Column("longitude", "REAL", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsPharmacies.put("estDeGarde", TableInfo.Column("estDeGarde", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsPharmacies.put("debutGarde", TableInfo.Column("debutGarde", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsPharmacies.put("finGarde", TableInfo.Column("finGarde", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysPharmacies: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesPharmacies: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoPharmacies: TableInfo = TableInfo("pharmacies", _columnsPharmacies,
            _foreignKeysPharmacies, _indicesPharmacies)
        val _existingPharmacies: TableInfo = read(connection, "pharmacies")
        if (!_infoPharmacies.equals(_existingPharmacies)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |pharmacies(com.autoformation.pharmaciesdegardes.data.model.Pharmacie).
              | Expected:
              |""".trimMargin() + _infoPharmacies + """
              |
              | Found:
              |""".trimMargin() + _existingPharmacies)
        }
        val _columnsMedicaments: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsMedicaments.put("id", TableInfo.Column("id", "INTEGER", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsMedicaments.put("nom", TableInfo.Column("nom", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsMedicaments.put("categorie", TableInfo.Column("categorie", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsMedicaments.put("description", TableInfo.Column("description", "TEXT", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsMedicaments.put("prix", TableInfo.Column("prix", "REAL", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysMedicaments: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesMedicaments: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoMedicaments: TableInfo = TableInfo("medicaments", _columnsMedicaments,
            _foreignKeysMedicaments, _indicesMedicaments)
        val _existingMedicaments: TableInfo = read(connection, "medicaments")
        if (!_infoMedicaments.equals(_existingMedicaments)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |medicaments(com.autoformation.pharmaciesdegardes.data.model.Medicament).
              | Expected:
              |""".trimMargin() + _infoMedicaments + """
              |
              | Found:
              |""".trimMargin() + _existingMedicaments)
        }
        val _columnsStocks: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsStocks.put("id", TableInfo.Column("id", "INTEGER", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsStocks.put("pharmacieId", TableInfo.Column("pharmacieId", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsStocks.put("medicamentId", TableInfo.Column("medicamentId", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsStocks.put("disponible", TableInfo.Column("disponible", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysStocks: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        _foreignKeysStocks.add(TableInfo.ForeignKey("pharmacies", "CASCADE", "NO ACTION",
            listOf("pharmacieId"), listOf("id")))
        _foreignKeysStocks.add(TableInfo.ForeignKey("medicaments", "CASCADE", "NO ACTION",
            listOf("medicamentId"), listOf("id")))
        val _indicesStocks: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesStocks.add(TableInfo.Index("index_stocks_pharmacieId", false, listOf("pharmacieId"),
            listOf("ASC")))
        _indicesStocks.add(TableInfo.Index("index_stocks_medicamentId", false,
            listOf("medicamentId"), listOf("ASC")))
        val _infoStocks: TableInfo = TableInfo("stocks", _columnsStocks, _foreignKeysStocks,
            _indicesStocks)
        val _existingStocks: TableInfo = read(connection, "stocks")
        if (!_infoStocks.equals(_existingStocks)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |stocks(com.autoformation.pharmaciesdegardes.data.model.Stock).
              | Expected:
              |""".trimMargin() + _infoStocks + """
              |
              | Found:
              |""".trimMargin() + _existingStocks)
        }
        val _columnsFavoris: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsFavoris.put("id", TableInfo.Column("id", "INTEGER", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsFavoris.put("pharmacieId", TableInfo.Column("pharmacieId", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysFavoris: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        _foreignKeysFavoris.add(TableInfo.ForeignKey("pharmacies", "CASCADE", "NO ACTION",
            listOf("pharmacieId"), listOf("id")))
        val _indicesFavoris: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesFavoris.add(TableInfo.Index("index_favoris_pharmacieId", false,
            listOf("pharmacieId"), listOf("ASC")))
        val _infoFavoris: TableInfo = TableInfo("favoris", _columnsFavoris, _foreignKeysFavoris,
            _indicesFavoris)
        val _existingFavoris: TableInfo = read(connection, "favoris")
        if (!_infoFavoris.equals(_existingFavoris)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |favoris(com.autoformation.pharmaciesdegardes.data.model.Favori).
              | Expected:
              |""".trimMargin() + _infoFavoris + """
              |
              | Found:
              |""".trimMargin() + _existingFavoris)
        }
        return RoomOpenDelegate.ValidationResult(true, null)
      }
    }
    return _openDelegate
  }

  protected override fun createInvalidationTracker(): InvalidationTracker {
    val _shadowTablesMap: MutableMap<String, String> = mutableMapOf()
    val _viewTables: MutableMap<String, Set<String>> = mutableMapOf()
    return InvalidationTracker(this, _shadowTablesMap, _viewTables, "pharmacies", "medicaments",
        "stocks", "favoris")
  }

  public override fun clearAllTables() {
    super.performClear(true, "pharmacies", "medicaments", "stocks", "favoris")
  }

  protected override fun getRequiredTypeConverterClasses(): Map<KClass<*>, List<KClass<*>>> {
    val _typeConvertersMap: MutableMap<KClass<*>, List<KClass<*>>> = mutableMapOf()
    _typeConvertersMap.put(PharmacieDao::class, PharmacieDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(MedicamentDao::class, MedicamentDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(StockDao::class, StockDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(FavoriDao::class, FavoriDao_Impl.getRequiredConverters())
    return _typeConvertersMap
  }

  public override fun getRequiredAutoMigrationSpecClasses(): Set<KClass<out AutoMigrationSpec>> {
    val _autoMigrationSpecsSet: MutableSet<KClass<out AutoMigrationSpec>> = mutableSetOf()
    return _autoMigrationSpecsSet
  }

  public override
      fun createAutoMigrations(autoMigrationSpecs: Map<KClass<out AutoMigrationSpec>, AutoMigrationSpec>):
      List<Migration> {
    val _autoMigrations: MutableList<Migration> = mutableListOf()
    return _autoMigrations
  }

  public override fun pharmacieDao(): PharmacieDao = _pharmacieDao.value

  public override fun medicamentDao(): MedicamentDao = _medicamentDao.value

  public override fun stockDao(): StockDao = _stockDao.value

  public override fun favoriDao(): FavoriDao = _favoriDao.value
}
