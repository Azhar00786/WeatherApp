package com.example.weatherapp.Fragmants


import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.weatherapp.Database.AppDatabase

import com.example.weatherapp.R
import com.example.weatherapp.ViewModel.MainFragmantViewModel
import com.example.weatherapp.ViewModel.MainFragmantViewModelFactory
import kotlinx.android.synthetic.main.fragment_main_fragmant.*

/**
    Coder : AZhar
 */
class MainFragmant : Fragment() {

    private lateinit var viewModel : MainFragmantViewModel
    private lateinit var viewModelFactory :MainFragmantViewModelFactory


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val connMgr = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connMgr.activeNetworkInfo
        if(activeNetwork != null){
            if(activeNetwork.isConnected){

                //viewmodel and viewmodelfactory is initialized
                viewModelFactory = MainFragmantViewModelFactory(AppDatabase.getInstance(requireContext()).userDao())
                viewModel = ViewModelProviders.of(this,viewModelFactory).get(MainFragmantViewModel::class.java)

                // Inflate the layout for this fragment
                return inflater.inflate(R.layout.fragment_main_fragmant, container, false)

            }
        }
        alertBoxCaller()
        return null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.handlerForDbData()

        viewModel.serverDataGetter()


        viewModel.weatherDescription.observe(viewLifecycleOwner, Observer {
            weatherDescription.setText(it)
        })

        viewModel.locationQuery.observe(viewLifecycleOwner, Observer {
            cityName.setText(it)
        })

        viewModel.temperature.observe(viewLifecycleOwner, Observer {
            apiTemperature.setText(it)
        })

        viewModel.windDirection.observe(viewLifecycleOwner, Observer {
            apiWindDirection.setText(it)
        })

        viewModel.windSpeed.observe(viewLifecycleOwner, Observer {

            apiWindSpeed.setText(it)
        })

        viewModel.humidity.observe(viewLifecycleOwner, Observer {
            apiHumidity.setText(it)
        })

        viewModel.cloudCover.observe(viewLifecycleOwner, Observer {
            apiCloudCover.setText(it)
        })

        viewModel.uvIndex.observe(viewLifecycleOwner, Observer {
            apiUvIndex.setText(it)
        })

        viewModel.visibility.observe(viewLifecycleOwner, Observer {
            apiVisibility.setText(it)
        })

        viewModel.latitude.observe(viewLifecycleOwner, Observer {
            apiLatitude.setText(it)
        })
        viewModel.longitude.observe(viewLifecycleOwner, Observer {
            apiLongitude.setText(it)
        })

        viewModel.localTime.observe(viewLifecycleOwner, Observer {
            apiLocalTime.setText(it)
        })

        viewModel.imageUrl.observe(viewLifecycleOwner, Observer {
            Glide.with(temperatureImage.context)
                    .load(it)
                    .apply(RequestOptions()
                    .error(R.drawable.ic_broken_image_black_24dp))
                    .into(temperatureImage)
        })

        settingsButton.setOnClickListener{
            settingsFragmantCaller()
        }
    }

    private fun settingsFragmantCaller(){
        findNavController().navigate(R.id.action_global_settingsFragmant)
    }

    private fun alertBoxCaller(){
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("No Internet Connection")
        builder.setMessage("Check Internet connectivity")
        builder.setIcon(R.drawable.ic_error_outline_black_24dp)

        builder.setPositiveButton("RETRY"){dialogInterface, which: Int ->
            findNavController().navigate(R.id.action_navigation2_self,null,
                NavOptions.Builder().setPopUpTo(R.id.navigation2,true).build())
        }
        builder.setNeutralButton("EXIT"){dialog: DialogInterface?, which: Int ->
            System.exit(-1)
        }

        val alertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

}