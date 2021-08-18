package com.example.weatherapp.fragments


import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.weatherapp.database.AppDatabase

import com.example.weatherapp.R
import com.example.weatherapp.viewmodels.MainFragmantViewModel
import com.example.weatherapp.viewmodels.MainFragmantViewModelFactory
import kotlinx.android.synthetic.main.fragment_main_fragmant.*
import kotlinx.coroutines.launch
import kotlin.system.exitProcess


class MainFragmant : Fragment() {

    private lateinit var viewModel: MainFragmantViewModel
    private lateinit var viewModelFactory: MainFragmantViewModelFactory


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        if (checkConnection()?.isConnected == true) {

            //viewmodel and viewmodelfactory is initialized
            viewModelFactory = MainFragmantViewModelFactory(
                AppDatabase.getInstance(requireContext()).userDao()
            )
            viewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(MainFragmantViewModel::class.java)

            // Inflate the layout for this fragment
            return inflater.inflate(R.layout.fragment_main_fragmant, container, false)

        }
        alertBoxCaller()
        return null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.accessDatabase()
            viewModel.serverDataGetter()
        }

        viewModel.weatherDescription.observe(viewLifecycleOwner, Observer {
            weatherDescription.text = it
        })

        viewModel.locationQuery.observe(viewLifecycleOwner, Observer {
            cityName.text = it
        })

        viewModel.temperature.observe(viewLifecycleOwner, Observer {
            apiTemperature.text = it
        })

        viewModel.windDirection.observe(viewLifecycleOwner, Observer {
            apiWindDirection.text = it
        })

        viewModel.windSpeed.observe(viewLifecycleOwner, Observer {

            apiWindSpeed.text = it
        })

        viewModel.humidity.observe(viewLifecycleOwner, Observer {
            apiHumidity.text = it
        })

        viewModel.cloudCover.observe(viewLifecycleOwner, Observer {
            apiCloudCover.text = it
        })

        viewModel.uvIndex.observe(viewLifecycleOwner, Observer {
            apiUvIndex.text = it
        })

        viewModel.visibility.observe(viewLifecycleOwner, Observer {
            apiVisibility.text = it
        })

        viewModel.latitude.observe(viewLifecycleOwner, Observer {
            apiLatitude.text = it
        })
        viewModel.longitude.observe(viewLifecycleOwner, Observer {
            apiLongitude.text = it
        })

        viewModel.localTime.observe(viewLifecycleOwner, Observer {
            apiLocalTime.text = it
        })

        viewModel.imageUrl.observe(viewLifecycleOwner, Observer {
            Glide.with(temperatureImage.context)
                .load(it)
                .apply(
                    RequestOptions()
                        .error(R.drawable.ic_broken_image_black_24dp)
                )
                .into(temperatureImage)
        })

        settingsButton.setOnClickListener {
            settingsFragmantCaller()
        }
    }

    private fun settingsFragmantCaller() {
        findNavController().navigate(R.id.action_global_settingsFragmant)
    }

    private fun alertBoxCaller() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("No Internet Connection")
        builder.setMessage("Check Internet connectivity")
        builder.setIcon(R.drawable.ic_error_outline_black_24dp)

        builder.setPositiveButton("RETRY") { dialogInterface, which: Int ->
            findNavController().navigate(
                R.id.action_navigation2_self, null,
                NavOptions.Builder().setPopUpTo(R.id.navigation2, true).build()
            )
        }
        builder.setNeutralButton("EXIT") { dialog: DialogInterface?, which: Int ->
            exitProcess(-1)
        }

        val alertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    private fun checkConnection(): NetworkInfo? {
        val connMgr = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connMgr.activeNetworkInfo
    }

}