package com.autoformation.pharmaciesdegardes.`data`.dao

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.autoformation.pharmaciesdegardes.`data`.model.Favori
import javax.`annotation`.processing.Generated
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass
import kotlinx.coroutines.flow.Flow

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class FavoriDao_Impl(
  __db: RoomDatabase,
) : FavoriDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfFavori: EntityInsertAdapter<Favori>

  private val __deleteAdapterOfFavori: EntityDeleteOrUpdateAdapter<Favori>
  init {
    this.__db = __db
    this.__insertAdapterOfFavori = object : EntityInsertAdapter<Favori>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `favoris` (`id`,`pharmacieId`) VALUES (nullif(?, 0),?)"

      protected override fun bind(statement: SQLiteStatement, entity: Favori) {
        statement.bindLong(1, entity.id)
        statement.bindLong(2, entity.pharmacieId)
      }
    }
    this.__deleteAdapterOfFavori = object : EntityDeleteOrUpdateAdapter<Favori>() {
      protected override fun createQuery(): String = "DELETE FROM `favoris` WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: Favori) {
        statement.bindLong(1, entity.id)
      }
    }
  }

  public override suspend fun insert(favori: Favori): Long = performSuspending(__db, false, true) {
      _connection ->
    val _result: Long = __insertAdapterOfFavori.insertAndReturnId(_connection, favori)
    _result
  }

  public override suspend fun delete(favori: Favori): Unit = performSuspending(__db, false, true) {
      _connection ->
    __deleteAdapterOfFavori.handle(_connection, favori)
  }

  public override fun getAllFavoris(): Flow<List<Favori>> {
    val _sql: String = "SELECT * FROM favoris"
    return createFlow(__db, false, arrayOf("favoris")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfPharmacieId: Int = getColumnIndexOrThrow(_stmt, "pharmacieId")
        val _result: MutableList<Favori> = mutableListOf()
        while (_stmt.step()) {
          val _item: Favori
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpPharmacieId: Long
          _tmpPharmacieId = _stmt.getLong(_columnIndexOfPharmacieId)
          _item = Favori(_tmpId,_tmpPharmacieId)
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
