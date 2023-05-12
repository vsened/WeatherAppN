package com.vsened.weatherappn.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.vsened.weatherappn.MainViewModel
import com.vsened.weatherappn.R
import com.vsened.weatherappn.adapters.WeatherAdapter
import com.vsened.weatherappn.adapters.WeatherModel
import com.vsened.weatherappn.databinding.FragmentDaysBinding
import com.vsened.weatherappn.databinding.FragmentHoursBinding

class DaysFragment : Fragment() {
    private lateinit var binding: FragmentDaysBinding
    private lateinit var adapter: WeatherAdapter

    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDaysBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        viewModel.liveDataList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
        adapter.onItemClickListener = object : WeatherAdapter.OnItemClickListener {
            override fun onItemClick(item: WeatherModel) {
                viewModel.liveDataCurrent.value = item
            }

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
            DaysFragment()
    }
}