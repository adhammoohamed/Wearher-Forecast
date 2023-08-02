package com.example.weatherforecast.ui.loading

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.weatherforecast.Constants
import com.example.weatherforecast.MainActivity
import com.example.weatherforecast.R
import com.example.weatherforecast.databinding.FragmentLoadingBinding
import com.example.weatherforecast.viewModel.SharedViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.PermissionRequest

@Suppress("DEPRECATION")
@AndroidEntryPoint
class LoadingFragment : Fragment(), EasyPermissions.PermissionCallbacks {

    private lateinit var binding: FragmentLoadingBinding
    private lateinit var viewModel: SharedViewModel
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val locationRequest = LocationRequest.create().apply {
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        interval = 5000
    }
    private var isLocationUpdateRequested = false
    private lateinit var navController: NavController
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_loading, container, false)

        viewModel = (activity as MainActivity).getSharedViewModel()

        navController = findNavController()

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())


        requestPermissions()

        return binding.root
    }

    private fun requestPermissions() {
        if (!EasyPermissions.hasPermissions(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) && !EasyPermissions.hasPermissions(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            EasyPermissions.requestPermissions(
                PermissionRequest.Builder(
                    this,
                    Constants.REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
                    .setRationale("This app requires location permission to function properly.")
                    .setPositiveButtonText("OK")
                    .setNegativeButtonText("Cancel")
                    .build()
            )
        } else {
            getUserLocation()
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    private fun getUserLocation() {
        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                locationResult.lastLocation.let { location: Location? ->
                    viewModel.updateUserLocation(location!!)

                    getDataFromRemote()
                    if (viewModel.currentWeather.value != null && location != null) {
                        if (!isLocationUpdateRequested) {
                            fusedLocationProviderClient.removeLocationUpdates(this)
                            isLocationUpdateRequested = true
                            navController.navigate(LoadingFragmentDirections.actionLoadingFragmentToHomeFragment())
                        }
                    }
                }
            }
        }
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        getUserLocation()
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        Toast.makeText(
            requireContext(),
            "This app requires location permission to function properly.",
            Toast.LENGTH_SHORT
        )
            .show()
        requestPermissions()
    }

    private fun getDataFromRemote() {
        viewModel.getCurrentWeather()
        viewModel.currentWeather.observe(viewLifecycleOwner) {
            viewModel.updateCurrentWeatherDate(it)
        }
        viewModel.getDaysForecast()
    }

}