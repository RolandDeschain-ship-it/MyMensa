package com.example.mymensa

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.lifecycle.lifecycleScope
import com.example.mymensa.databinding.FragmentDietSelectBinding
import com.example.mymensa.databinding.FragmentMensaSelectBinding
import com.example.mymensa.ui.data.DataStorageRepository
import com.google.android.material.chip.Chip
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DietSelect.newInstance] factory method to
 * create an instance of this fragment.
 */
class DietSelect : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }


    private val dataStore: DataStorageRepository by inject()

    private var _binding: FragmentDietSelectBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        _binding = FragmentDietSelectBinding.inflate(inflater, container, false)

        _binding!!.dietSelectChipGroup.children.forEach {
            it.setOnClickListener {
                val chip = it as Chip
                val chipText = chip.text.toString()
                if (chip.isChecked) {
                    lifecycleScope.launch {
                        dataStore.addToArray("diets", chipText)
                    }
                } else {
                    lifecycleScope.launch {
                        dataStore.removeFromArray("diets", chipText)
                    }
                }
            }
        }

        _binding!!.finishButton.setOnClickListener {
            val intent = Intent(context, MainActivity::class.java)
            startActivity(intent)
        }
        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DietSelect.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DietSelect().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}