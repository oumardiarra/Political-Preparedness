package com.example.android.politicalpreparedness.representative

import android.app.Application
import androidx.lifecycle.*
import com.example.android.politicalpreparedness.database.ElectionDatabase.Companion.getInstance
import com.example.android.politicalpreparedness.election.VoterInfoViewModel
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.repository.ElectionRepository
import com.example.android.politicalpreparedness.representative.model.Representative
import kotlinx.coroutines.launch
import timber.log.Timber

class RepresentativeViewModel(application: Application) : AndroidViewModel(application) {
    val database = getInstance(application)
    val repository = ElectionRepository(database)

    //TODO: Establish live data for representatives and address
   private var _representatives = MutableLiveData<List<Representative>>()
    val representatives: LiveData<List<Representative>>
        get() = _representatives
   private var _address = MutableLiveData<Address>()
    val address: LiveData<Address>
        get() = _address

    init {
        _representatives.value = null
    }

    /**
     *  The following code will prove helpful in constructing a representative from the API. This code combines the two nodes of the RepresentativeResponse into a single official :

    val (offices, officials) = getRepresentativesDeferred.await()
    _representatives.value = offices.flatMap { office -> office.getRepresentatives(officials) }

    Note: getRepresentatives in the above code represents the method used to fetch data from the API
    Note: _representatives in the above code represents the established mutable live data housing representatives

     */
    fun getRepresentative(address: String) {
        viewModelScope.launch {
            val representativeInfo = repository.getRepresentative(address)
            if (representativeInfo != null) {
                val (offices, officials) = representativeInfo
                Timber.e("representativeInfo size is ${offices.size}")
                _representatives.value = offices.flatMap { office -> office.getRepresentatives(officials) }
            }
        }
    }

    fun setAddressGromGeoCaode(geoCodeAddress: Address) {
        _address.value = geoCodeAddress
    }
    //TODO: Create function get address from geo location

    //TODO: Create function to get address from individual fields
    class RepresentativeViewModelFactory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(RepresentativeViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return RepresentativeViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }

    }
}
