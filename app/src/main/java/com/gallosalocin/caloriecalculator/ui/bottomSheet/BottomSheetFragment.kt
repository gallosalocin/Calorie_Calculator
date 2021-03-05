package com.gallosalocin.caloriecalculator.ui.bottomSheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.gallosalocin.caloriecalculator.R
import com.gallosalocin.caloriecalculator.databinding.FragmentBottomSheetBinding
import com.gallosalocin.caloriecalculator.ui.mainActivity.MainActivity.Companion.globalChoices
import com.gallosalocin.caloriecalculator.utils.Constants.GLOBAL_CHOICE_BOTTOM
import com.gallosalocin.caloriecalculator.utils.Constants.GLOBAL_CHOICE_BOTTOM_DISHES
import com.gallosalocin.caloriecalculator.utils.Constants.GLOBAL_CHOICE_BOTTOM_FOODS
import com.gallosalocin.caloriecalculator.utils.Constants.GLOBAL_CHOICE_BOTTOM_PROFILE
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

        binding.ivBottomFoods.setOnClickListener {
            globalChoices = GLOBAL_CHOICE_BOTTOM_FOODS
            findNavController().navigate(R.id.action_bottomSheetFragment_to_allFoodsFragment)
        }

        binding.ivBottomCategories.setOnClickListener {
            globalChoices = GLOBAL_CHOICE_BOTTOM
            findNavController().navigate(R.id.action_bottomSheetFragment_to_allCategoriesFragment)
        }

        binding.ivBottomDishes.setOnClickListener {
            globalChoices = GLOBAL_CHOICE_BOTTOM_DISHES
            findNavController().navigate(R.id.action_bottomSheetFragment_to_allDishesFragment)
        }

        binding.ivBottomProfile.setOnClickListener {
            globalChoices = GLOBAL_CHOICE_BOTTOM_PROFILE
            findNavController().navigate(R.id.action_bottomSheetFragment_to_profileFragment)
        }

        binding.ivBottomBackupRestore.setOnClickListener {
            findNavController().navigate(R.id.action_bottomSheetFragment_to_backupRestoreFragment)
        }

        binding.ivBottomNutritionix.setOnClickListener {
            findNavController().navigate(R.id.action_bottomSheetFragment_to_searchNutritionixFragment)
        }


//        binding.ivBottomNutritionix.visibility = View.GONE
//        binding.tvBottomNutritionix.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}