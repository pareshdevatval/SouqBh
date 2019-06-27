package com.souqbh.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.souqbh.data.db.entity.CountryCode

@Dao
public interface AppDao {

    @Query("SELECT * FROM CountryCode")
    fun getAllCountryCode(): List<CountryCode>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCountryCode(countryCode: CountryCode)

    @Query("SELECT * FROM CountryCode WHERE dial_code LIKE '%' || :searchString || '%' OR name LIKE '%' || :searchString || '%'")
    fun getFilteredCountryCode(searchString: String): List<CountryCode>
}