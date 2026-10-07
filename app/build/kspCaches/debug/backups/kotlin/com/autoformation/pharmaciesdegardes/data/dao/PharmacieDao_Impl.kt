package com.autoformation.pharmaciesdegardes.`data`.dao

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.autoformation.pharmaciesdegardes.`data`.model.Pharmacie
import javax.`annotation`.processing.Generated
import kotlin.Boolean
import kotlin.Double
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass
import kotlinx.coroutines.flow.Flow

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class PharmacieDao_Impl(
  __db: RoomDatabase,
) : PharmacieDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfPharmacie: EntityInsertAdapter<Pharmacie>
  init {
    this.__db = __db
    this.__insertAdapterOfPharmacie = object : EntityInsertAdapter<Pharmacie>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `pharmacies` (`id`,`nom`,`commune`,`quartier`,`telephone`,`latitude`,`longitude`,`estDeGarde`,`debutGarde`,`finGarde`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: Pharmacie) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.nom)
        statement.bindText(3, entity.commune)
        statement.bindText(4, entity.quartier)
        statement.bindText(5, entity.telephone)
        statement.bindDouble(6, entity.latitude)
        statement.bindDouble(7, entity.longitude)
        val _tmp: Int = if (entity.estDeGarde) 1 else 0
        statement.bindLong(8, _tmp.toLong())
        val _tmpDebutGarde: String? = entity.debutGarde
        if (_tmpDebutGarde == null) {
          statement.bindNull(9)
        } else {
          statement.bindText(9, _tmpDebutGarde)
        }
        val _tmpFinGarde: String? = entity.finGarde
        if (_tmpFinGarde == null) {
          statement.bindNull(10)
        } else {
          statement.bindText(10, _tmpFinGarde)
        }
      }
    }
  }

  public override suspend fun insert(pharmacie: Pharmacie): Long = performSuspending(__db, false,
      true) { _connection ->
    val _result: Long = __insertAdapterOfPharmacie.insertAndReturnId(_connection, pharmacie)
    _result
  }

  public override fun getAllPharmacies(): Flow<List<Pharmacie>> {
    val _sql: String = "SELECT * FROM pharmacies"
    return createFlow(__db, false, arrayOf("pharmacies")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfNom: Int = getColumnIndexOrThrow(_stmt, "nom")
        val _columnIndexOfCommune: Int = getColumnIndexOrThrow(_stmt, "commune")
        val _columnIndexOfQuartier: Int = getColumnIndexOrThrow(_stmt, "quartier")
        val _columnIndexOfTelephone: Int = getColumnIndexOrThrow(_stmt, "telephone")
        val _columnIndexOfLatitude: Int = getColumnIndexOrThrow(_stmt, "latitude")
        val _columnIndexOfLongitude: Int = getColumnIndexOrThrow(_stmt, "longitude")
        val _columnIndexOfEstDeGarde: Int = getColumnIndexOrThrow(_stmt, "estDeGarde")
        val _columnIndexOfDebutGarde: Int = getColumnIndexOrThrow(_stmt, "debutGarde")
        val _columnIndexOfFinGarde: Int = getColumnIndexOrThrow(_stmt, "finGarde")
        val _result: MutableList<Pharmacie> = mutableListOf()
        while (_stmt.step()) {
          val _item: Pharmacie
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpNom: String
          _tmpNom = _stmt.getText(_columnIndexOfNom)
          val _tmpCommune: String
          _tmpCommune = _stmt.getText(_columnIndexOfCommune)
          val _tmpQuartier: String
          _tmpQuartier = _stmt.getText(_columnIndexOfQuartier)
          val _tmpTelephone: String
          _tmpTelephone = _stmt.getText(_columnIndexOfTelephone)
          val _tmpLatitude: Double
          _tmpLatitude = _stmt.getDouble(_columnIndexOfLatitude)
          val _tmpLongitude: Double
          _tmpLongitude = _stmt.getDouble(_columnIndexOfLongitude)
          val _tmpEstDeGarde: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfEstDeGarde).toInt()
          _tmpEstDeGarde = _tmp != 0
          val _tmpDebutGarde: String?
          if (_stmt.isNull(_columnIndexOfDebutGarde)) {
            _tmpDebutGarde = null
          } else {
            _tmpDebutGarde = _stmt.getText(_columnIndexOfDebutGarde)
          }
          val _tmpFinGarde: String?
          if (_stmt.isNull(_columnIndexOfFinGarde)) {
            _tmpFinGarde = null
          } else {
            _tmpFinGarde = _stmt.getText(_columnIndexOfFinGarde)
          }
          _item =
              Pharmacie(_tmpId,_tmpNom,_tmpCommune,_tmpQuartier,_tmpTelephone,_tmpLatitude,_tmpLongitude,_tmpEstDeGarde,_tmpDebutGarde,_tmpFinGarde)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getPharmaciesDeGarde(): Flow<List<Pharmacie>> {
    val _sql: String = "SELECT * FROM pharmacies WHERE estDeGarde = 1"
    return createFlow(__db, false, arrayOf("pharmacies")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfNom: Int = getColumnIndexOrThrow(_stmt, "nom")
        val _columnIndexOfCommune: Int = getColumnIndexOrThrow(_stmt, "commune")
        val _columnIndexOfQuartier: Int = getColumnIndexOrThrow(_stmt, "quartier")
        val _columnIndexOfTelephone: Int = getColumnIndexOrThrow(_stmt, "telephone")
        val _columnIndexOfLatitude: Int = getColumnIndexOrThrow(_stmt, "latitude")
        val _columnIndexOfLongitude: Int = getColumnIndexOrThrow(_stmt, "longitude")
        val _columnIndexOfEstDeGarde: Int = getColumnIndexOrThrow(_stmt, "estDeGarde")
        val _columnIndexOfDebutGarde: Int = getColumnIndexOrThrow(_stmt, "debutGarde")
        val _columnIndexOfFinGarde: Int = getColumnIndexOrThrow(_stmt, "finGarde")
        val _result: MutableList<Pharmacie> = mutableListOf()
        while (_stmt.step()) {
          val _item: Pharmacie
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpNom: String
          _tmpNom = _stmt.getText(_columnIndexOfNom)
          val _tmpCommune: String
          _tmpCommune = _stmt.getText(_columnIndexOfCommune)
          val _tmpQuartier: String
          _tmpQuartier = _stmt.getText(_columnIndexOfQuartier)
          val _tmpTelephone: String
          _tmpTelephone = _stmt.getText(_columnIndexOfTelephone)
          val _tmpLatitude: Double
          _tmpLatitude = _stmt.getDouble(_columnIndexOfLatitude)
          val _tmpLongitude: Double
          _tmpLongitude = _stmt.getDouble(_columnIndexOfLongitude)
          val _tmpEstDeGarde: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfEstDeGarde).toInt()
          _tmpEstDeGarde = _tmp != 0
          val _tmpDebutGarde: String?
          if (_stmt.isNull(_columnIndexOfDebutGarde)) {
            _tmpDebutGarde = null
          } else {
            _tmpDebutGarde = _stmt.getText(_columnIndexOfDebutGarde)
          }
          val _tmpFinGarde: String?
          if (_stmt.isNull(_columnIndexOfFinGarde)) {
            _tmpFinGarde = null
          } else {
            _tmpFinGarde = _stmt.getText(_columnIndexOfFinGarde)
          }
          _item =
              Pharmacie(_tmpId,_tmpNom,_tmpCommune,_tmpQuartier,_tmpTelephone,_tmpLatitude,_tmpLongitude,_tmpEstDeGarde,_tmpDebutGarde,_tmpFinGarde)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
