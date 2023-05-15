package com.vsened.weatherappn.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.material.tabs.TabLayoutMediator
import com.squareup.picasso.Picasso
import com.vsened.weatherappn.DialogManager
import com.vsened.weatherappn.MainViewModel
import com.vsened.weatherappn.adapters.VpAdapter
import com.vsened.weatherappn.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private lateinit var pLauncher: ActivityResultLauncher<String>
    private val fList = listOf(
        HoursFragment.newInstance(),
        DaysFragment.newInstance()
    )
    private val tList = listOf(
        "HOURS",
        "DAYS"
    )
    private val viewModel by activityViewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermission()
        init()
        updateCurrentCard()
        binding.ibSync.setOnClickListener {
            binding.tabLayout.selectTab(binding.tabLayout.getTabAt(0))
            checkLocation()
        }
        binding.ibSearch.setOnClickListener {
            DialogManager.searchByName(requireContext(), object : DialogManager.DialogListener {
                override fun onClick(name: String?) {
                    name?.let { location -> viewModel.getUserChoiceLocation(location) }
                }
            })
        }
    }

    override fun onResume() {
        super.onResume()
        checkLocation()
    }

    private fun init() = with(binding) {
        vp.adapter = VpAdapter(
            activity as FragmentActivity,
            fList
        )
        TabLayoutMediator(tabLayout, vp) {
            tab, pos -> tab.text = tList[pos]
        }.attach()

    }

    @SuppressLint("SetTextI18n")
    private fun updateCurrentCard() = with(binding) {
        viewModel.liveDataCurrent.observe(viewLifecycleOwner) {
            tvData.text = it.time
            tvCity.text = it.city
            tvCurrentTemp.text = it.currentTemp
            tvCondition.text = it.condition
            tvMinMaxTemp.text = "${it.minTemp}°C / ${it.maxTemp}°C"
            Picasso.get().load(it.imageUrl).into(ivWeather)
        }
    }
    private fun permissionListener() {
        pLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) {
            Toast.makeText(activity, "Permission is $it", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkPermission() {
        if (!isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
            permissionListener()
            pLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun checkLocation() {
        if (isLocationEnabled()) {
            viewModel.getCurrentLocation()
        } else {
            DialogManager.locationSettingsDialog(requireContext(), object: DialogManager.DialogListener {
                override fun onClick(name: String?) {
                    startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                }
            })
        }
    }

    private fun inputLocation() {

    }

    private fun isLocationEnabled(): Boolean {
        val locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    companion object {
        fun newInstance() = MainFragment()
        private const val API_KEY = "f4f09c08bf134ae89c6170817220506"
    }
}