package com.turbotech.displaytest.repository

import com.turbotech.displaytest.data.DisplayTestDao
import com.turbotech.displaytest.model.DisplayEntities
import javax.inject.Inject

class ResultsRepo @Inject constructor(private val displayTestDao: DisplayTestDao) {

    suspend fun insertResults(displayEntities: DisplayEntities) =
        displayTestDao.insert(displayEntities)

    fun getResultsData() = displayTestDao.getResultsData()

    suspend fun updateResults(displayEntities: DisplayEntities) =
        displayTestDao.update(displayEntities)



//    suspend fun deleteResults(displayEntities: DisplayEntities) = displayTestDao.delete(displayEntities)

}