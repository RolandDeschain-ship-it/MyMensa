package com.example.mymensa.ui.home

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mymensa.R
import com.example.mymensa.databinding.FragmentHomeBinding
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class HomeFragment : Fragment() {

    private val homeViewModel by viewModel<HomeViewModel>()
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(com.example.mymensa.R.layout.fragment_home, container, false)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.rvFoodItems.apply{
            layoutManager = LinearLayoutManager(context)
            adapter = FoodItemAdapter().also { adapter ->
                homeViewModel.todayFoodItems.observe(viewLifecycleOwner) {
                    adapter.submitList(it)
                }
            }
        }


        val heuteButton =  binding.heuteButton
        val morgenButton =  binding.morgenButton
        context?.let { it1 -> heuteButton.setBackgroundColor(it1.getColor(R.color.purple_200)) }
        context?.let { it1 -> morgenButton.setBackgroundColor(it1.getColor(R.color.purple_500)) }
        heuteButton.setOnClickListener{
            context?.let { it1 -> heuteButton.setBackgroundColor(it1.getColor(R.color.purple_200)) }
            context?.let { it1 -> morgenButton.setBackgroundColor(it1.getColor(R.color.purple_500)) }
            binding.rvFoodItems.apply{
                layoutManager = LinearLayoutManager(context)
                adapter = FoodItemAdapter().also { adapter ->
                    homeViewModel.todayFoodItems.observe(viewLifecycleOwner) {
                        adapter.submitList(it)
                    }

                }
            }
            binding.rvFoodItems.adapter?.notifyDataSetChanged()
        }
        morgenButton.setOnClickListener{
            context?.let { it1 -> heuteButton.setBackgroundColor(it1.getColor(R.color.purple_500)) }
            context?.let { it1 -> morgenButton.setBackgroundColor(it1.getColor(R.color.purple_200)) }
            binding.rvFoodItems.apply{
                layoutManager = LinearLayoutManager(context)
                adapter = FoodItemAdapter().also { adapter ->
                    homeViewModel.tomorrowFoodItems.observe(viewLifecycleOwner) {
                        adapter.submitList(it)
                    }
                }
            }
            binding.rvFoodItems.adapter?.notifyDataSetChanged()
        }

        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}