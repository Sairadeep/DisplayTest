package com.turbotech.displaytest.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.turbotech.displaytest.model.DisplayEntities
import kotlinx.coroutines.flow.Flow

@Dao
interface DisplayTestDao {

    @Query("SELECT * FROM display_entities_tbl")
    fun getResultsData(): Flow<List<DisplayEntities>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(displayEntities: DisplayEntities)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(displayEntities: DisplayEntities)

}