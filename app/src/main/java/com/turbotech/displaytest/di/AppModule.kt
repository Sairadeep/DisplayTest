package com.turbotech.displaytest.di

import android.content.Context
import androidx.room.Room
import com.turbotech.displaytest.data.DeviceBluetoothController
import com.turbotech.displaytest.data.DisplayDB
import com.turbotech.displaytest.data.DisplayTestDao
import com.turbotech.displaytest.data.domain.BluetoothController
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Singleton
    @Provides
    fun provideDTDao(displayDB: DisplayDB): DisplayTestDao = displayDB.displayTestDao()

    @Singleton
    @Provides
    fun provideAppDB(@ApplicationContext context: Context): DisplayDB = Room.databaseBuilder(
        context,
        DisplayDB::class.java,
        name = "health_report"
    ).fallbackToDestructiveMigration().build()

   @Singleton
    @Provides
    fun provideBluetoothController(@ApplicationContext context: Context): BluetoothController {
        return DeviceBluetoothController(context)
    }

}