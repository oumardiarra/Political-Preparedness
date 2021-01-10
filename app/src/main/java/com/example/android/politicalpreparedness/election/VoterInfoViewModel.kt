package com.example.android.politicalpreparedness.election

import android.app.Application
import androidx.lifecycle.*
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.domain.Election
import com.example.android.politicalpreparedness.repository.ElectionRepository
import kotlinx.coroutines.launch
import java.lang.Exception

class VoterInfoViewModel(application: Application) : AndroidViewModel(application) {

    private val database = ElectionDatabase.getInstance(application)
    private val repository = ElectionRepository(database)
    private var _browseUrl = MutableLiveData<String>()
    val browseUrl: LiveData<String>
        get() = _browseUrl

    //TODO: Add live data to hold voter info
    val voterInfo = repository.voterInfo
    private var _electionsDetails = MutableLiveData<Election>()

    val electionsDetails: LiveData<Election>
        get() = _electionsDetails
    val electionInfo = Transformations.map(electionsDetails) {
        it
    }
    //TODO: Add var and methods to populate voter info

    //TODO: Add var and methods to support loading URLs

    //TODO: Add var and methods to save and remove elections to local database
    //TODO: cont'd -- Populate initial state of save button to reflect proper action based on election saved status
    init {
        _electionsDetails.value = null
        _browseUrl.value = null
    }

    fun setElectionInfo(election: Election) {
        _electionsDetails.value = election
    }


    fun getVoterInfo(electionId: Int, address: String) {
        viewModelScope.launch {
            repository.getVoterInfo(electionId, address)
        }
    }

    private fun setBrowseUrl(url: String) {
        _browseUrl.value = url
    }

    fun browseToUrl(url: String) {
        setBrowseUrl(url)
    }

    fun saveElection(election: Election) {
        viewModelScope.launch {
            try {
                if (!election.isSaved) {
                    repository.saveElection(election)
                    election.isSaved = true
                    setElectionInfo(election)

                } else {

                    repository.unFollowElection(election)
                    election.isSaved = false
                    setElectionInfo(election)
                }

            } catch (ex: Exception) {

            }

        }
    }

    /**
     * Hint: The saved state can be accomplished in multiple ways. It is directly related to how elections are saved/removed from the database.
     */

}