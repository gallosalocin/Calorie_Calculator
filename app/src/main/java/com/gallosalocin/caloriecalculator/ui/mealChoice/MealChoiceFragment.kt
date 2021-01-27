package com.gallosalocin.caloriecalculator.ui.mealChoice

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.gallosalocin.caloriecalculator.R
import com.gallosalocin.caloriecalculator.databinding.FragmentMealChoiceBinding
import com.gallosalocin.caloriecalculator.ui.mainActivity.MainActivity.Companion.dayTag
import com.gallosalocin.caloriecalculator.ui.mainActivity.MainActivity.Companion.mealTag
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
        showDayDetailMacros()
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
            mealTag = 1
            findNavController().navigate(R.id.action_mealFragment_to_mealListFragment)
            requireView().performHapticFeedback(HapticFeedbackConstants.CONTEXT_CLICK)
        }
        binding.cvLunch.setOnClickListener {
            mealTag = 2
            findNavController().navigate(R.id.action_mealFragment_to_mealListFragment)
            requireView().performHapticFeedback(HapticFeedbackConstants.CONTEXT_CLICK)
        }
        binding.cvDinner.setOnClickListener {
            mealTag = 3
            findNavController().navigate(R.id.action_mealFragment_to_mealListFragment)
            requireView().performHapticFeedback(HapticFeedbackConstants.CONTEXT_CLICK)
        }
        binding.cvSnack.setOnClickListener {
            mealTag = 4
            findNavController().navigate(R.id.action_mealFragment_to_mealListFragment)
            requireView().performHapticFeedback(HapticFeedbackConstants.CONTEXT_CLICK)
        }
    }

    private fun toolbarChangeTitleNameDay(dayTag: Int) {
        val dayTagArray = arrayOf(
                getString(R.string.monday_cap), getString(R.string.tuesday_cap), getString(R.string.wednesday_cap), getString(R.string.thursday_cap),
                getString(R.string.friday_cap), getString(R.string.saturday_cap), getString(R.string.sunday_cap)
        )
        (requireActivity() as AppCompatActivity).supportActionBar?.title = dayTagArray[dayTag - 1]
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}