package com.vsened.weatherappn.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.vsened.weatherappn.MainViewModel
import com.vsened.weatherappn.adapters.WeatherAdapter
import com.vsened.weatherappn.adapters.WeatherModel
import com.vsened.weatherappn.databinding.FragmentHoursBinding
import org.json.JSONArray


class HoursFragment : Fragment() {

    private lateinit var binding: FragmentHoursBinding
    private lateinit var adapter: WeatherAdapter

    private val viewModel: MainViewModel by activityViewModels()
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
        viewModel.liveDataCurrent.observe(viewLifecycleOwner) {
            adapter.submitList(viewModel.getHourlyTempList(it.hours))
        }
    }

    private fun initRecyclerView() = with(binding) {
        rvWeather.layoutManager = LinearLayoutManager(activity)
        adapter = WeatherAdapter()
        rvWeather.adapter = adapter
   }


    companion object {
        @JvmStatic
        fun newInstance() =
            HoursFragment()
    }
}