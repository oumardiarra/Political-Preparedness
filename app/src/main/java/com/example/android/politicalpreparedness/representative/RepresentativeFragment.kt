package com.example.android.politicalpreparedness.representative

import android.Manifest
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.BuildConfig
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.databinding.FragmentRepresentativeBinding
import com.example.android.politicalpreparedness.election.ElectionsViewModel
import com.example.android.politicalpreparedness.election.ElectionsViewModelFactory
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.adapter.RepresentativeListAdapter
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber
import java.security.Permission
import java.util.Locale

class DetailFragment : Fragment() {
    private val viewModel: RepresentativeViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onViewCreated()"
        }
        ViewModelProvider(
                this,
                RepresentativeViewModel.RepresentativeViewModelFactory(activity.application)
        ).get(RepresentativeViewModel::class.java)
    }

    companion object {
        const val REQUEST_LOCATION_PERMISSION_REQUEST_CODE = 1
        const val REQUEST_TURN_DEVICE_LOCATION_ON = 29
    }

    lateinit var binding: FragmentRepresentativeBinding

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {


        binding = FragmentRepresentativeBinding.inflate(inflater)
        val states = resources.getStringArray(R.array.states)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, states)
        binding.state.adapter = adapter
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.buttonSearch.setOnClickListener {
            Timber.e("buttonSearch ${binding.state.selectedItem.toString()}")
            viewModel.getRepresentative(binding.state.selectedItem.toString())
        }
        binding.buttonLocation.setOnClickListener {
            checkLocationPermissions()
        }
        val representativeAdapter = RepresentativeListAdapter()
        binding.recycleMyRepresentative.adapter = representativeAdapter
        viewModel.representatives.observe(viewLifecycleOwner, Observer {
            if (it != null)
                representativeAdapter.submitList(it)
        })
        return binding.root
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isEmpty() ||
                        grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    /*
                    * We should explain here why we need to access to the user location
                    * */
                    Snackbar.make(
                            binding.motionRepresentation,
                            R.string.permission_denied_explanation,
                            Snackbar.LENGTH_INDEFINITE
                    ).setAction(R.string.settings) {
                        startActivity(Intent().apply {
                            action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                            data = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        })
                    }.show()
                    Timber.e("Permission is denied")
                } else {
                    checkDeviceLocationSettings()

                }
            }
            REQUEST_TURN_DEVICE_LOCATION_ON -> {
                checkDeviceLocationSettings(false)
            }
        }

    }

    private fun checkLocationPermissions(): Boolean {
        return if (isPermissionGranted()) {
            checkDeviceLocationSettings()
            true
        } else {
            requestPermissions(arrayOf(ACCESS_FINE_LOCATION),
                    REQUEST_LOCATION_PERMISSION_REQUEST_CODE)
            false
        }
    }

    private fun isPermissionGranted(): Boolean {
        val locationPermissionApproved = PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
        return locationPermissionApproved

    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        Timber.e("getLocation called")

        val fusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(requireContext())
        fusedLocationProviderClient.lastLocation.addOnSuccessListener {
            if (it != null) {
                val address = geoCodeLocation(it)
                viewModel.setAddressGromGeoCaode(address)
                val states = resources.getStringArray(R.array.states)
                val selectedStateIndex = states.indexOf(address.state)
                binding.state.setSelection(selectedStateIndex)
                viewModel.getRepresentative(address.state)
            } else {
                Timber.e("fusedLocationProviderClient is null")
            }
        }
                .addOnFailureListener {
                    Timber.e("fusedLocationProviderClient failure: ${it.message}")
                }

    }

    private fun geoCodeLocation(location: Location): Address {
        val geocoder = Geocoder(context, Locale.getDefault())
        Timber.e("location latlng: ${location.latitude}/${location.longitude
        }")
        return geocoder.getFromLocation(location.latitude, location.longitude, 1)
                .map { address ->
                    Timber.e("location address: ${address}")
                    var thoroughfare = ""
                    var subThoroughfare = ""
                    var adminArea = ""
                    var postalCode = ""
                    if (address.thoroughfare != null)
                        thoroughfare = address.thoroughfare
                    if (address.subThoroughfare != null)
                        subThoroughfare = address.subThoroughfare
                    if (address.adminArea != null)
                        adminArea = address.adminArea
                    if (address.postalCode != null)
                        postalCode = address.postalCode
                    Address(thoroughfare, subThoroughfare, address.locality, adminArea, postalCode)

                }
                .first()
    }

    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view!!.windowToken, 0)
    }

    private fun checkDeviceLocationSettings(resolve: Boolean = true) {
        val locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_LOW_POWER
        }
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val settingsClient = LocationServices.getSettingsClient(requireActivity())
        val locationSettingsResponseTask =
                settingsClient.checkLocationSettings(builder.build())
        locationSettingsResponseTask.addOnSuccessListener {
            getLocation()
        }
        locationSettingsResponseTask.addOnFailureListener { exception ->
            if (exception is ResolvableApiException && resolve) {
                try {
                    Timber.e(" exception.startResolutionForResult")
                    /* exception.startResolutionForResult(
                         requireActivity(),
                         REQUEST_TURN_DEVICE_LOCATION_ON
                     )*/
                    startIntentSenderForResult(
                            exception.resolution.intentSender,
                            REQUEST_TURN_DEVICE_LOCATION_ON,
                            null,
                            0,
                            0,
                            0,
                            null
                    )
                } catch (sendEx: IntentSender.SendIntentException) {
                    Timber.e("Error getting location settings resolution: " + sendEx.message)
                }
            } else {
                Timber.e("Error getting location settings resolution: showing snackbar")
                Snackbar.make(
                        binding.motionRepresentation,
                        R.string.location_required_error, Snackbar.LENGTH_INDEFINITE
                ).setAction(android.R.string.ok) {
                    Timber.e("setAction oK")
                    checkDeviceLocationSettings()
                }.show()
            }
        }
    }
}