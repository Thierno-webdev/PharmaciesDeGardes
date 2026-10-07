package com.autoformation.pharmaciesdegardes.`data`.dao

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.autoformation.pharmaciesdegardes.`data`.model.Medicament
import javax.`annotation`.processing.Generated
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
public class MedicamentDao_Impl(
  __db: RoomDatabase,
) : MedicamentDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfMedicament: EntityInsertAdapter<Medicament>
  init {
    this.__db = __db
    this.__insertAdapterOfMedicament = object : EntityInsertAdapter<Medicament>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `medicaments` (`id`,`nom`,`categorie`,`description`,`prix`) VALUES (nullif(?, 0),?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: Medicament) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.nom)
        statement.bindText(3, entity.categorie)
        statement.bindText(4, entity.description)
        statement.bindDouble(5, entity.prix)
      }
    }
  }

  public override suspend fun insert(medicament: Medicament): Long = performSuspending(__db, false,
      true) { _connection ->
    val _result: Long = __insertAdapterOfMedicament.insertAndReturnId(_connection, medicament)
    _result
  }

  public override fun getAllMedicaments(): Flow<List<Medicament>> {
    val _sql: String = "SELECT * FROM medicaments"
    return createFlow(__db, false, arrayOf("medicaments")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfNom: Int = getColumnIndexOrThrow(_stmt, "nom")
        val _columnIndexOfCategorie: Int = getColumnIndexOrThrow(_stmt, "categorie")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfPrix: Int = getColumnIndexOrThrow(_stmt, "prix")
        val _result: MutableList<Medicament> = mutableListOf()
        while (_stmt.step()) {
          val _item: Medicament
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpNom: String
          _tmpNom = _stmt.getText(_columnIndexOfNom)
          val _tmpCategorie: String
          _tmpCategorie = _stmt.getText(_columnIndexOfCategorie)
          val _tmpDescription: String
          _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          val _tmpPrix: Double
          _tmpPrix = _stmt.getDouble(_columnIndexOfPrix)
          _item = Medicament(_tmpId,_tmpNom,_tmpCategorie,_tmpDescription,_tmpPrix)
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
