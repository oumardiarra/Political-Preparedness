package com.example.android.politicalpreparedness.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.database.model.asDomainModel
import com.example.android.politicalpreparedness.domain.Election
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.RepresentativeResponse
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import com.example.android.politicalpreparedness.network.models.asDataBaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.lang.Exception

class ElectionRepository(private val database: ElectionDatabase) {

    val electionList = Transformations.map(database.electionDao.getAllElections()) {
        it.asDomainModel()
    }
    val allSavedElections = Transformations.map(database.electionDao.getAllSavedElection()) {
        it.asDomainModel()
    }
    private var _voterInfo = MutableLiveData<VoterInfoResponse>()
    val voterInfo: LiveData<VoterInfoResponse>
        get() = _voterInfo

    suspend fun refreshElections() {
        withContext(Dispatchers.IO) {
            try {
                val savedELections = database.electionDao.getSavedElection()
                Timber.i("Fetching election from network...")
                val electionResponse = CivicsApi.retrofitService.getElections()
                Timber.e("End getting election from network, size is ${electionResponse.elections.size}")
                Timber.i("Start refreshing election data into the database")
                database.electionDao.insertAll(electionResponse.elections.asDataBaseModel())
                Timber.i("End refreshing election data into the database")
                val ids: List<Int> = savedELections.map {
                    it.id
                }
                if (ids.isNotEmpty()) {
                    Timber.i("Number of elections saved in the database ${ids.size}")
                    database.electionDao.updateElections(ids)
                } else {
                    Timber.i("No ELections saved in the database")
                }
            } catch (e: Exception) {
                Timber.e("Error when fecthing ELections from internet: ${e.message}")
            }


        }
    }

    suspend fun saveElection(election: Election) {
        withContext(Dispatchers.IO) {
            try {
                Timber.i("Start saving election ${election.id}...")
                database.electionDao.saveElection(election.id)
                Timber.i("End saving election ${election.id}...")
            } catch (ex: Exception) {
                Timber.e("Exception occurs when saving election: ${ex.message}")
            }
        }
    }

    suspend fun unFollowElection(election: Election) {
        withContext(Dispatchers.IO) {
            try {
                Timber.i("Start unfollowing election ${election.id}...")
                database.electionDao.unFollowElection(election.id)
                Timber.i("End unfollowing election ${election.id}...")
            } catch (ex: Exception) {
                Timber.e("Exception occurs when unfollowing election: ${ex.message}")
            }
        }
    }

    suspend fun getVoterInfo(electionId: Int, address: String) {
        withContext(Dispatchers.IO) {
            try {
                Timber.i("Start getting voterInfo ${electionId}...")
                val voterInfo = CivicsApi.retrofitService.getVoterInfo(electionId, address)
                _voterInfo.postValue(voterInfo)
                Timber.i("End getting voterInfo ${electionId}...")
            } catch (ex: Exception) {
                Timber.e("Exception occurs when getting voterInfo: ${ex.message}")
            }
        }
    }

    suspend fun getRepresentative(address: String): RepresentativeResponse? {
        var representativeInfo: RepresentativeResponse? = null
        withContext(Dispatchers.IO) {
            try {
                Timber.i("Start getting representative info for address ${address}...")
                representativeInfo = CivicsApi.retrofitService.getRepresentatives(address)
                Timber.i("End getting representative info for address ${address}...")

            } catch (ex: Exception) {
                Timber.i("Exception getting representative info for address ${address}... :${ex.message}")


            }
        }
        return representativeInfo
    }
}