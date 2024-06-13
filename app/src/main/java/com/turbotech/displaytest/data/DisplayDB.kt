package com.turbotech.displaytest.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.turbotech.displaytest.model.DisplayEntities

@Database(entities = [DisplayEntities::class], version = 3, exportSchema = false)
abstract class DisplayDB : RoomDatabase() {

    abstract fun displayTestDao() : DisplayTestDao

}