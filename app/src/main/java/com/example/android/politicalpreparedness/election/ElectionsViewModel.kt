package com.example.android.politicalpreparedness.election

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.database.ElectionDatabase.Companion.getInstance
import com.example.android.politicalpreparedness.domain.Election
import com.example.android.politicalpreparedness.repository.ElectionRepository
import kotlinx.coroutines.launch
import java.util.*

//TODO: Construct ViewModel and provide election datasource
class ElectionsViewModel(application: Application) : AndroidViewModel(application) {
    private val database = getInstance(application)
    private val repository = ElectionRepository(database)
    private var _navigateToSelectedElection = MutableLiveData<Election>()
    val navigateToSelectedElection: LiveData<Election>
        get() = _navigateToSelectedElection


    init {
        val calendar = Calendar.getInstance()

        //_list.value = listOf(Election(1, "name", calendar.time, Division("", "", "")))
        refreshElection()
    }

    var elections = repository.electionList


    //TODO: Create live data val for saved elections

    //TODO: Create val and functions to populate live data for upcoming elections from the API and saved elections from local database
    private fun refreshElection() {
        viewModelScope.launch {
            repository.refreshElections()
        }
    }

    //TODO: Create functions to navigate to saved or upcoming election voter info
    fun displayElectiondDetails(election: Election) {
        _navigateToSelectedElection.value = election
    }
    fun displayElectionDetailsComplete() {
        _navigateToSelectedElection.value = null
    }

}