package com.vsened.weatherappn.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.tabs.TabLayoutMediator
import com.squareup.picasso.Picasso
import com.vsened.weatherappn.MainViewModel
import com.vsened.weatherappn.adapters.VpAdapter
import com.vsened.weatherappn.adapters.WeatherModel
import com.vsened.weatherappn.databinding.FragmentMainBinding
import org.json.JSONObject

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
        viewModel.requestWeatherData("Chelyabinsk")
        binding.ibSync.setOnClickListener {
            viewModel.requestWeatherData("Berlin")
        }
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

    companion object {
        fun newInstance() = MainFragment()
        private const val API_KEY = "f4f09c08bf134ae89c6170817220506"
    }
}