package com.gallosalocin.caloriecalculator.ui.mealChoice

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.gallosalocin.caloriecalculator.R
import com.gallosalocin.caloriecalculator.databinding.FragmentMealChoiceBinding
import com.gallosalocin.caloriecalculator.ui.mainActivity.MainActivity.Companion.dayTag
import com.gallosalocin.caloriecalculator.ui.mainActivity.MainActivity.Companion.mealTag
import com.gallosalocin.caloriecalculator.utils.Constants.DAY_TAG_FRIDAY
import com.gallosalocin.caloriecalculator.utils.Constants.DAY_TAG_MONDAY
import com.gallosalocin.caloriecalculator.utils.Constants.DAY_TAG_SATURDAY
import com.gallosalocin.caloriecalculator.utils.Constants.DAY_TAG_SUNDAY
import com.gallosalocin.caloriecalculator.utils.Constants.DAY_TAG_THURSDAY
import com.gallosalocin.caloriecalculator.utils.Constants.DAY_TAG_TUESDAY
import com.gallosalocin.caloriecalculator.utils.Constants.DAY_TAG_WEDNESDAY
import com.gallosalocin.caloriecalculator.utils.Constants.MEAL_TAG_BREAKFAST
import com.gallosalocin.caloriecalculator.utils.Constants.MEAL_TAG_DINNER
import com.gallosalocin.caloriecalculator.utils.Constants.MEAL_TAG_LUNCH
import com.gallosalocin.caloriecalculator.utils.Constants.MEAL_TAG_SNACK
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MealChoiceFragment : Fragment(R.layout.fragment_meal_choice) {

    private var _binding: FragmentMealChoiceBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MealChoiceViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMealChoiceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        mealChoice()
        loadUserMacrosExpected()
        showBreakfastDailyMacros()
        showLunchDailyMacros()
        showDinnerDailyMacros()
        showSnackDailyMacros()

    }

    override fun onStart() {
        super.onStart()
        toolbarChangeTitleNameDay(dayTag)
    }

    // Display User Daily Macros Expected
    private fun loadUserMacrosExpected() {
        viewModel.getUser.observe(viewLifecycleOwner) {
            binding.apply {
                expectedDayCalTotal.text = it.dailyCalories.toInt().toString()
                expectedDayFatTotal.text = it.fatResult.toString()
                expectedDayCarbTotal.text = it.carbResult.toString()
                expectedDayProtTotal.text = it.protResult.toString()
            }
            showDayDetailMacros()
        }
    }

    // Display User Daily Macros
    private fun showDayDetailMacros() {
        viewModel.getDayDetail.observe(viewLifecycleOwner) { foodWithCategoryList ->
            binding.apply {
                dayCalTotal.text = String.format("%.0f", foodWithCategoryList.sumByDouble { it.food.calories.toDouble() })
                dayFatTotal.text = String.format("%.1f", foodWithCategoryList.sumByDouble { it.food.fats.toDouble() })
                dayCarbTotal.text = String.format("%.1f", foodWithCategoryList.sumByDouble { it.food.carbs.toDouble() })
                dayProtTotal.text = String.format("%.1f", foodWithCategoryList.sumByDouble { it.food.prots.toDouble() })
            }
            checkIfDayMacrosBiggerExpectedDayMacros()
        }
    }

    // Check if dayMacrosTotal are bigger than expectedDayMacrosTotal
    private fun checkIfDayMacrosBiggerExpectedDayMacros() {
        if (binding.dayCalTotal.text.toString().toFloat()
            > binding.expectedDayCalTotal.text.toString().toFloat()
        ) {
            binding.dayCalTotal.setTextColor(ContextCompat.getColor(requireContext(), R.color.red_700))
        } else {
            binding.dayCalTotal.setTextColor(ContextCompat.getColor(requireContext(), R.color.design_default_color_secondary_variant))
        }
        if (binding.dayFatTotal.text.toString().replace(',', '.').toFloat()
            > binding.expectedDayFatTotal.text.toString().toFloat()
        ) {
            binding.dayFatTotal.setTextColor(ContextCompat.getColor(requireContext(), R.color.red_700))
        } else {
            binding.dayFatTotal.setTextColor(ContextCompat.getColor(requireContext(), R.color.design_default_color_secondary_variant))
        }
        if (binding.dayCarbTotal.text.toString().replace(',', '.').toFloat()
            > binding.expectedDayCarbTotal.text.toString().toFloat()
        ) {
            binding.dayCarbTotal.setTextColor(ContextCompat.getColor(requireContext(), R.color.red_700))
        } else {
            binding.dayCarbTotal.setTextColor(ContextCompat.getColor(requireContext(), R.color.design_default_color_secondary_variant))
        }
        if (binding.dayProtTotal.text.toString().replace(',', '.').toFloat()
            > binding.expectedDayProtTotal.text.toString().toFloat()
        ) {
            binding.dayProtTotal.setTextColor(ContextCompat.getColor(requireContext(), R.color.red_700))
        } else {
            binding.dayProtTotal.setTextColor(ContextCompat.getColor(requireContext(), R.color.design_default_color_secondary_variant))
        }
    }

    // Display Breakfast Daily Macros
    private fun showBreakfastDailyMacros() {
        viewModel.getBreakfastMacros.observe(viewLifecycleOwner) { foodWithCategoryList ->
            binding.apply {
                breakfastCal.text = String.format("%.0f", foodWithCategoryList.sumByDouble { it.food.calories.toDouble() })
                breakfastFat.text = String.format("%.1f", foodWithCategoryList.sumByDouble { it.food.fats.toDouble() })
                breakfastCarb.text = String.format("%.1f", foodWithCategoryList.sumByDouble { it.food.carbs.toDouble() })
                breakfastProt.text = String.format("%.1f", foodWithCategoryList.sumByDouble { it.food.prots.toDouble() })
            }
        }
    }

    // Display Lunch Daily Macros
    private fun showLunchDailyMacros() {
        viewModel.getLunchMacros.observe(viewLifecycleOwner) { foodWithCategoryList ->
            binding.apply {
                lunchCal.text = String.format("%.0f", foodWithCategoryList.sumByDouble { it.food.calories.toDouble() })
                lunchFat.text = String.format("%.1f", foodWithCategoryList.sumByDouble { it.food.fats.toDouble() })
                lunchCarb.text = String.format("%.1f", foodWithCategoryList.sumByDouble { it.food.carbs.toDouble() })
                lunchProt.text = String.format("%.1f", foodWithCategoryList.sumByDouble { it.food.prots.toDouble() })
            }
        }
    }

    // Display Dinner Daily Macros
    private fun showDinnerDailyMacros() {
        viewModel.getDinnerMacros.observe(viewLifecycleOwner) { foodWithCategoryList ->
            binding.apply {
                dinnerCal.text = String.format("%.0f", foodWithCategoryList.sumByDouble { it.food.calories.toDouble() })
                dinnerFat.text = String.format("%.1f", foodWithCategoryList.sumByDouble { it.food.fats.toDouble() })
                dinnerCarb.text = String.format("%.1f", foodWithCategoryList.sumByDouble { it.food.carbs.toDouble() })
                dinnerProt.text = String.format("%.1f", foodWithCategoryList.sumByDouble { it.food.prots.toDouble() })
            }
        }
    }

    // Display Snack Daily Macros
    private fun showSnackDailyMacros() {
        viewModel.getSnackMacros.observe(viewLifecycleOwner) { foodWithCategoryList ->
            binding.apply {
                snackCal.text = String.format("%.0f", foodWithCategoryList.sumByDouble { it.food.calories.toDouble() })
                snackFat.text = String.format("%.1f", foodWithCategoryList.sumByDouble { it.food.fats.toDouble() })
                snackCarb.text = String.format("%.1f", foodWithCategoryList.sumByDouble { it.food.carbs.toDouble() })
                snackProt.text = String.format("%.1f", foodWithCategoryList.sumByDouble { it.food.prots.toDouble() })
            }
        }
    }

    // Setup toolbar
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.toolbar, menu)
    }

    private fun mealChoice() {
        binding.cvBreakfast.setOnClickListener {
            mealTag = MEAL_TAG_BREAKFAST
            findNavController().navigate(R.id.action_mealFragment_to_mealListFragment)
            requireView().performHapticFeedback(HapticFeedbackConstants.CONTEXT_CLICK)
        }
        binding.cvLunch.setOnClickListener {
            mealTag = MEAL_TAG_LUNCH
            findNavController().navigate(R.id.action_mealFragment_to_mealListFragment)
            requireView().performHapticFeedback(HapticFeedbackConstants.CONTEXT_CLICK)
        }
        binding.cvDinner.setOnClickListener {
            mealTag = MEAL_TAG_DINNER
            findNavController().navigate(R.id.action_mealFragment_to_mealListFragment)
            requireView().performHapticFeedback(HapticFeedbackConstants.CONTEXT_CLICK)
        }
        binding.cvSnack.setOnClickListener {
            mealTag = MEAL_TAG_SNACK
            findNavController().navigate(R.id.action_mealFragment_to_mealListFragment)
            requireView().performHapticFeedback(HapticFeedbackConstants.CONTEXT_CLICK)
        }
    }

    private fun toolbarChangeTitleNameDay(dayTag: String) {
        var day = ""
        when (dayTag) {
            DAY_TAG_MONDAY -> day = getString(R.string.monday_cap)
            DAY_TAG_TUESDAY -> day = getString(R.string.tuesday_cap)
            DAY_TAG_WEDNESDAY -> day = getString(R.string.wednesday_cap)
            DAY_TAG_THURSDAY -> day = getString(R.string.thursday_cap)
            DAY_TAG_FRIDAY -> day = getString(R.string.friday_cap)
            DAY_TAG_SATURDAY -> day = getString(R.string.saturday_cap)
            DAY_TAG_SUNDAY -> day = getString(R.string.sunday_cap)
        }
        (requireActivity() as AppCompatActivity).supportActionBar?.title = day
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}