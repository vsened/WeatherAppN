package com.vsened.weatherappn.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.vsened.weatherappn.adapters.WeatherAdapter
import com.vsened.weatherappn.adapters.WeatherModel
import com.vsened.weatherappn.databinding.FragmentHoursBinding


class HoursFragment : Fragment() {

    private lateinit var binding: FragmentHoursBinding
    private lateinit var adapter: WeatherAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHoursBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
    }

    private fun initRecyclerView() = with(binding) {
        rvWeather.layoutManager = LinearLayoutManager(activity)
        adapter = WeatherAdapter()
        rvWeather.adapter = adapter
        val list = listOf(
            WeatherModel(
                "Berlin",
                "12:00",
                "Sunny",
                "22°C",
                "20°C",
                "25°C",
                "",
                ""
            ),WeatherModel(
                "London",
                "12:00",
                "Sunny",
                "17°C",
                "10°C",
                "14°C",
                "",
                ""
            ),WeatherModel(
                "Paris",
                "12:00",
                "Sunny",
                "22°C",
                "22°C",
                "22°C",
                "",
                ""
            )
        )
        adapter.submitList(list)
    }


    companion object {
        @JvmStatic
        fun newInstance() =
            HoursFragment()
    }
}