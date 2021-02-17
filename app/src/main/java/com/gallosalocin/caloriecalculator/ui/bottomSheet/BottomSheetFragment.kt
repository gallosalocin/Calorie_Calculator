package com.gallosalocin.caloriecalculator.ui.bottomSheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.gallosalocin.caloriecalculator.R
import com.gallosalocin.caloriecalculator.databinding.FragmentBottomSheetBinding
import com.gallosalocin.caloriecalculator.ui.mainActivity.MainActivity.Companion.isBottomChoice
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentBottomSheetBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ivBottomProfile.setOnClickListener {
            isBottomChoice = true
            findNavController().navigate(R.id.action_bottomSheetFragment_to_profileFragment)
        }

        binding.ivBottomCategories.setOnClickListener {
            isBottomChoice = true
            findNavController().navigate(R.id.action_bottomSheetFragment_to_allCategoriesFragment)
        }

        binding.ivBottomFoods.setOnClickListener {
            isBottomChoice = true
            findNavController().navigate(R.id.action_bottomSheetFragment_to_allFoodsFragment)
        }

        binding.ivBottomBackupRestore.setOnClickListener {
            isBottomChoice = true
            findNavController().navigate(R.id.action_bottomSheetFragment_to_backupRestoreFragment)
        }

//        binding.ivBottomDishes.setOnClickListener {
//            isBottomChoice = true
//            findNavController().navigate(R.id.action_bottomSheetFragment_to_allDishesFragment)
//        }

        binding.ivBottomDishes.visibility = View.GONE
        binding.tvBottomDishes.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}