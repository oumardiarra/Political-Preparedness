package com.example.android.politicalpreparedness.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.database.model.asDomainModel
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.asDataBaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.lang.Exception

class ElectionRepository(private val database: ElectionDatabase) {
    //val electionList = database.electionDao.getAllElections()
    //val electionList = database.electionDao.getAllElections()
    val electionList = Transformations.map(database.electionDao.getAllElections()) {
        it.asDomainModel()
    }


    suspend fun refreshElections() {
        withContext(Dispatchers.IO) {
            try {
                Timber.i("Fetching election from network...")
                val electionResponse = CivicsApi.retrofitService.getElections()
                Timber.e("End getting election from network, size is ${electionResponse.elections.size}")
                Timber.i("Start refreshing election data into the database")
                database.electionDao.insertAll(electionResponse.elections.asDataBaseModel())
                Timber.i("End refreshing election data into the database")
            } catch (e: Exception) {
                Timber.e("Error when fecthing ELections from internet: ${e.message}")
            }


        }
    }
}