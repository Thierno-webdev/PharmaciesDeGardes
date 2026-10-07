package com.autoformation.pharmaciesdegardes.`data`.dao

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.autoformation.pharmaciesdegardes.`data`.model.Stock
import javax.`annotation`.processing.Generated
import kotlin.Boolean
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
public class StockDao_Impl(
  __db: RoomDatabase,
) : StockDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfStock: EntityInsertAdapter<Stock>
  init {
    this.__db = __db
    this.__insertAdapterOfStock = object : EntityInsertAdapter<Stock>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `stocks` (`id`,`pharmacieId`,`medicamentId`,`disponible`) VALUES (nullif(?, 0),?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: Stock) {
        statement.bindLong(1, entity.id)
        statement.bindLong(2, entity.pharmacieId)
        statement.bindLong(3, entity.medicamentId)
        val _tmp: Int = if (entity.disponible) 1 else 0
        statement.bindLong(4, _tmp.toLong())
      }
    }
  }

  public override suspend fun insert(stock: Stock): Long = performSuspending(__db, false, true) {
      _connection ->
    val _result: Long = __insertAdapterOfStock.insertAndReturnId(_connection, stock)
    _result
  }

  public override fun getStocksForPharmacie(pharmacieId: Long): Flow<List<Stock>> {
    val _sql: String = "SELECT * FROM stocks WHERE pharmacieId = ?"
    return createFlow(__db, false, arrayOf("stocks")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, pharmacieId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfPharmacieId: Int = getColumnIndexOrThrow(_stmt, "pharmacieId")
        val _columnIndexOfMedicamentId: Int = getColumnIndexOrThrow(_stmt, "medicamentId")
        val _columnIndexOfDisponible: Int = getColumnIndexOrThrow(_stmt, "disponible")
        val _result: MutableList<Stock> = mutableListOf()
        while (_stmt.step()) {
          val _item: Stock
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpPharmacieId: Long
          _tmpPharmacieId = _stmt.getLong(_columnIndexOfPharmacieId)
          val _tmpMedicamentId: Long
          _tmpMedicamentId = _stmt.getLong(_columnIndexOfMedicamentId)
          val _tmpDisponible: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfDisponible).toInt()
          _tmpDisponible = _tmp != 0
          _item = Stock(_tmpId,_tmpPharmacieId,_tmpMedicamentId,_tmpDisponible)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getStocksForMedicament(medicamentId: Long): Flow<List<Stock>> {
    val _sql: String = "SELECT * FROM stocks WHERE medicamentId = ? AND disponible = 1"
    return createFlow(__db, false, arrayOf("stocks")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, medicamentId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfPharmacieId: Int = getColumnIndexOrThrow(_stmt, "pharmacieId")
        val _columnIndexOfMedicamentId: Int = getColumnIndexOrThrow(_stmt, "medicamentId")
        val _columnIndexOfDisponible: Int = getColumnIndexOrThrow(_stmt, "disponible")
        val _result: MutableList<Stock> = mutableListOf()
        while (_stmt.step()) {
          val _item: Stock
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpPharmacieId: Long
          _tmpPharmacieId = _stmt.getLong(_columnIndexOfPharmacieId)
          val _tmpMedicamentId: Long
          _tmpMedicamentId = _stmt.getLong(_columnIndexOfMedicamentId)
          val _tmpDisponible: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfDisponible).toInt()
          _tmpDisponible = _tmp != 0
          _item = Stock(_tmpId,_tmpPharmacieId,_tmpMedicamentId,_tmpDisponible)
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
