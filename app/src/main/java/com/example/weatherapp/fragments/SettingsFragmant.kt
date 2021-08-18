package com.example.weatherapp.fragments

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.preference.*
import com.example.weatherapp.database.AppDatabase
import com.example.weatherapp.R
import com.example.weatherapp.viewmodels.SettingsFragmantViewModel
import com.example.weatherapp.viewmodels.SettingsFragmantViewModelFactory
import com.google.android.gms.location.*
import kotlinx.coroutines.*
import java.util.*

class SettingsFragmant : PreferenceFragmentCompat() {

    private lateinit var viewModelFact: SettingsFragmantViewModelFactory
    private lateinit var viewMod: SettingsFragmantViewModel
    private lateinit var pref: Preference
    private lateinit var prefOne: Preference
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)
        Log.d("preference", "preferenec called")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("preference", "onViewCreatedCalled")

        //for SwitchPreference
        prefOne = findPreference("USE_DEVICE_LOCATION")


        //viewModel and viewModelFactory Initilization
        viewModelFact =
            SettingsFragmantViewModelFactory(AppDatabase.getInstance(requireContext()).userDao())
        viewMod =
            ViewModelProviders.of(this, viewModelFact).get(SettingsFragmantViewModel::class.java)


        bindSwitchPreference(prefOne)
        booleanValue.observe(viewLifecycleOwner, Observer { it ->
            if (it == "true") {
                getLocationUpdates()
            } else {
                pref = findPreference("CUSTOM_LOCATION")
                bindPreferenceSummaryToValue(pref)
                stringValue.observe(viewLifecycleOwner, Observer {
                    viewLifecycleOwner.lifecycleScope.launch {
                        viewMod.setDataInDb(it)
                    }
                })
            }
        })
    }


    private fun getLocationUpdates() {
        if (checkPermission()) {
            if (isLocationEnabled()) {
                fusedLocationClient =
                    LocationServices.getFusedLocationProviderClient(requireContext())
                fusedLocationClient.lastLocation.addOnCompleteListener(requireActivity()) { task ->
                    val location: Location? = task.result
                    if (location == null) {
                        requestNewLocationData()
                    } else {
                        Log.d("locationUpdate", location.latitude.toString())
                        Log.d("locationUpdate", location.longitude.toString())

                        coordinatesConverter(location.latitude, location.longitude)
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Turn on location", Toast.LENGTH_LONG).show()
                val myIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(myIntent)
            }

        } else {
            requestPermissions()
        }
    }

    private fun coordinatesConverter(latitude: Double, longitude: Double) {
        val addresses: List<Address>
        val geoCoder = Geocoder(requireContext(), Locale.getDefault())
        addresses = geoCoder.getFromLocation(
            latitude,
            longitude,
            1
        )
        if (addresses != null && addresses.isNotEmpty()) {
            viewLifecycleOwner.lifecycleScope.launch {
                val address: String = addresses[0].getAddressLine(0)
                val city: String = addresses[0].locality
                val state: String = addresses[0].adminArea
                val country: String = addresses[0].countryName
                val postalCode: String = addresses[0].postalCode
                val knownName: String = addresses[0].featureName
                Log.d("G-location###", "$address $city $state $postalCode $country $knownName")
                Toast.makeText(requireContext(), "Location set to : $city", Toast.LENGTH_SHORT)
                    .show()
                viewMod.setDataInDb(city)
            }
        }
    }

    private fun checkPermission(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun requestNewLocationData() {
        locationRequest = LocationRequest()
        locationRequest.interval = 5000
        locationRequest.fastestInterval = 5000
        locationRequest.smallestDisplacement = 170f // 170m = 0.1mile
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.numUpdates = 1

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("ERROR", "Permission not granted by user")
            return
        }
        fusedLocationClient.requestLocationUpdates(
            locationRequest, locationCallback, Looper.myLooper()
        )
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            val lastLocation: Location = locationResult!!.lastLocation
            Log.d("locationUpdateTwo", lastLocation.longitude.toString())
            Log.d("locationUpdateTwo", lastLocation.latitude.toString())

            coordinatesConverter(lastLocation.latitude, lastLocation.longitude)
        }
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ),
            Companion.PERMISSION_ID
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == Companion.PERMISSION_ID) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLocationUpdates()
            }
        }
    }

    companion object {
        var stringValue = MutableLiveData<String>()
        var booleanValue = MutableLiveData<String>()

        private fun bindSwitchPreference(preference: Preference) {
            preference.onPreferenceChangeListener = sBindToSwitchPreference
            sBindToSwitchPreference
                .onPreferenceChange(
                    preference, PreferenceManager.getDefaultSharedPreferences(preference.context)
                        .getBoolean("USE_DEVICE_LOCATION", false)
                )
        }

        private val sBindToSwitchPreference =
            Preference.OnPreferenceChangeListener { preference, newValue ->
                booleanValue.value = newValue.toString()
                Log.d("preferenceBooleanValue@", newValue.toString())


                true
            }


        private fun bindPreferenceSummaryToValue(preference: Preference) {
            preference.onPreferenceChangeListener = sBindPreferenceSummaryToValueListener

            sBindPreferenceSummaryToValueListener
                .onPreferenceChange(
                    preference, PreferenceManager.getDefaultSharedPreferences(preference.context)
                        .getString(preference.key, "")
                )

        }

        private val sBindPreferenceSummaryToValueListener =
            Preference.OnPreferenceChangeListener { preference, newValue ->
                val stringValueTemp = newValue.toString()
                stringValue.value = stringValueTemp
                Log.d("value", stringValueTemp)

                if (preference is EditTextPreference) {
                    if (preference.getKey() == "CUSTOM_LOCATION") {
                        preference.summary = stringValueTemp
                    } else {
                        preference.summary = stringValueTemp
                    }
                }
                true
            }

        private const val PERMISSION_ID = 42
    }
}