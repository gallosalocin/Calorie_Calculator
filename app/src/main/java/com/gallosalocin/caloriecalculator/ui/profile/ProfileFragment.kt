package com.gallosalocin.caloriecalculator.ui.profile

import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.gallosalocin.caloriecalculator.R
import com.gallosalocin.caloriecalculator.databinding.FragmentProfileBinding
import com.gallosalocin.caloriecalculator.models.User
import com.gallosalocin.caloriecalculator.ui.mainActivity.MainActivity.Companion.isBottomChoice
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by viewModels()

    private lateinit var currentUser: User
    private var dailyCalories: Float = 0F
    private var bmrResult: Float = 0F
    private var genderValue: Int = 0
    private var activityValue: Int = 0
    private var isCustomCalories = false
    private var isFirstAppOpen = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        viewModel.readFromDataStore.observe(viewLifecycleOwner) {
            isFirstAppOpen = it

            if (isFirstAppOpen) {
                (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
            } else {
                if (isBottomChoice) {
                    (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

                    viewModel.getUser.observe(viewLifecycleOwner) { user ->
                        currentUser = user
                        loadUserData()
                        isCustomCalories = currentUser.isCustomCalories
                        displayOrHideViews()
                        binding.etDailyCalorieResult.isEnabled = isCustomCalories
                    }
                } else {
                    findNavController().navigate(R.id.action_profileFragment_to_dayFragment)
                }
            }
        }

        binding.ivProfileInfo.setOnClickListener { displayInfoMacrosRatioDialog() }
        switchCaloriesDayInputMode()
    }

    // Setup toolbar
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.toolbar, menu)
        menu.getItem(0).isVisible = true
        menu.getItem(5).isVisible = true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.tb_menu_save -> {
                if (isCustomCalories) {
                    if (confirmInputFieldsValidation()) {
                        isFirstAppOpen = false
                        viewModel.saveToDataStore(isFirstAppOpen)
                        dailyCalories = binding.etDailyCalorieResult.text.toString().toFloat()
                        calculateMacros()
                        saveUserData()
                        findNavController().navigate(R.id.action_profileFragment_to_dayFragment)
                    }
                } else {
                    if (confirmInputFieldsValidation()) {
                        isFirstAppOpen = false
                        viewModel.saveToDataStore(isFirstAppOpen)
                        calculateBmr()
                        calculateDailyCalories()
                        calculateMacros()
                        saveUserData()
                        findNavController().navigate(R.id.action_profileFragment_to_dayFragment)
                    }
                }
            }

            R.id.tb_menu_calculate -> {
                if (isCustomCalories) {
                    if (confirmInputFieldsValidation()) {
                        dailyCalories = binding.etDailyCalorieResult.text.toString().toFloat()
                        calculateMacros()
                    }
                } else {
                    if (confirmInputFieldsValidation()) {
                        calculateBmr()
                        calculateDailyCalories()
                        calculateMacros()
                    }
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun calculateBmr() {
        val genderId = binding.radioGroupGender.checkedRadioButtonId
        val genderRB = requireActivity().findViewById<RadioButton>(genderId)
        val age = binding.ageValue.text.toString().toInt()
        val height = binding.heightValue.text.toString().toInt()
        val weight = binding.weightValue.text.toString().toInt()

        bmrResult = (if (genderRB == binding.genderMale) 66 + (13.7 * weight) + (5 * height) - (6.8 * age)
        else 655 + (9.6 * weight) + (1.8 * height) - (4.7 * age)).toFloat()

        binding.tvBmrResult.text = bmrResult.toInt().toString()

        genderValue = if (genderId == R.id.gender_male) 1 else 2
    }

    private fun calculateDailyCalories() {
        val activityId = binding.radioGroupActivityLevel.checkedRadioButtonId

        when (requireActivity().findViewById<RadioButton>(activityId)) {
            binding.level1 -> {
                dailyCalories = bmrResult * 1.2F
                activityValue = 1
            }
            binding.level2 -> {
                dailyCalories = bmrResult * 1.375F
                activityValue = 2
            }
            binding.level3 -> {
                dailyCalories = bmrResult * 1.55F
                activityValue = 3
            }
            binding.level4 -> {
                dailyCalories = bmrResult * 1.725F
                activityValue = 4
            }
            binding.level5 -> {
                dailyCalories = bmrResult * 1.9F
                activityValue = 5
            }
        }
        binding.etDailyCalorieResult.setText(dailyCalories.toInt().toString())
    }

    private fun calculateMacros() {
        val fatPercent = binding.fatPercent.text.toString().toInt()
        val carbPercent = binding.carbPercent.text.toString().toInt()
        val protPercent = binding.protPercent.text.toString().toInt()

        val fatResult = (((dailyCalories * fatPercent) / 100) / 9).toInt()
        val carbResult = (((dailyCalories * carbPercent) / 100) / 4).toInt()
        val protResult = (((dailyCalories * protPercent) / 100) / 4).toInt()

        binding.fatResult.text = fatResult.toString()
        binding.carbResult.text = carbResult.toString()
        binding.protResult.text = protResult.toString()
    }


    private fun saveUserData() {
        if (isCustomCalories) {
            currentUser = User(
                gender = 0,
                age = 0,
                height = 0,
                weight = 0,
                bmrResult = 0F,
                activity = 0,
                fatPercent = binding.fatPercent.text.toString().toInt(),
                carbPercent = binding.carbPercent.text.toString().toInt(),
                protPercent = binding.protPercent.text.toString().toInt(),
                dailyCalories = binding.etDailyCalorieResult.text.toString().toFloat(),
                fatResult = binding.fatResult.text.toString().toInt(),
                carbResult = binding.carbResult.text.toString().toInt(),
                protResult = binding.protResult.text.toString().toInt(),
                isCustomCalories = isCustomCalories
            )
            viewModel.insertUser(currentUser)
        } else {
            currentUser = User(
                gender = genderValue,
                age = binding.ageValue.text.toString().toInt(),
                height = binding.heightValue.text.toString().toInt(),
                weight = binding.weightValue.text.toString().toInt(),
                bmrResult = binding.tvBmrResult.text.toString().toFloat(),
                activity = activityValue,
                fatPercent = binding.fatPercent.text.toString().toInt(),
                carbPercent = binding.carbPercent.text.toString().toInt(),
                protPercent = binding.protPercent.text.toString().toInt(),
                dailyCalories = binding.etDailyCalorieResult.text.toString().toFloat(),
                fatResult = binding.fatResult.text.toString().toInt(),
                carbResult = binding.carbResult.text.toString().toInt(),
                protResult = binding.protResult.text.toString().toInt(),
                isCustomCalories = isCustomCalories
            )
            viewModel.insertUser(currentUser)
        }
    }

    private fun loadUserData() {
        (requireActivity() as AppCompatActivity).supportActionBar?.title =
            if (isCustomCalories) getString(R.string.custom_calories) else getString(R.string.calories_calculator)

        when (currentUser.gender) {
            1 -> binding.radioGroupGender.check(R.id.gender_male)
            2 -> binding.radioGroupGender.check(R.id.gender_female)
        }

        when (currentUser.activity) {
            1 -> binding.radioGroupActivityLevel.check(R.id.level1)
            2 -> binding.radioGroupActivityLevel.check(R.id.level2)
            3 -> binding.radioGroupActivityLevel.check(R.id.level3)
            4 -> binding.radioGroupActivityLevel.check(R.id.level4)
            5 -> binding.radioGroupActivityLevel.check(R.id.level5)
        }

        binding.apply {
            if (currentUser.age.toString() == "0") ageValue.setText("") else ageValue.setText(currentUser.age.toString())
            if (currentUser.height.toString() == "0") heightValue.setText("") else heightValue.setText(currentUser.height.toString())
            if (currentUser.weight.toString() == "0") weightValue.setText("") else weightValue.setText(currentUser.weight.toString())
            if (currentUser.bmrResult.toString() == "0") tvBmrResult.text = "" else tvBmrResult.text = String.format("%.0f", currentUser.bmrResult)
            fatPercent.setText(currentUser.fatPercent.toString())
            carbPercent.setText(currentUser.carbPercent.toString())
            protPercent.setText(currentUser.protPercent.toString())
            etDailyCalorieResult.setText(String.format("%.0f", currentUser.dailyCalories))
            fatResult.text = currentUser.fatResult.toString()
            carbResult.text = currentUser.carbResult.toString()
            protResult.text = currentUser.protResult.toString()
        }
    }

    // Check if all input fields are fill correctly
    private fun confirmInputFieldsValidation(): Boolean {
        if (isCustomCalories) {
            return if (!validateInputFieldIfNullOrEmpty(binding.etDailyCalorieResult)
                or !validateInputFieldIfNullOrEmpty(binding.fatPercent)
                or !validateInputFieldIfNullOrEmpty(binding.carbPercent)
                or !validateInputFieldIfNullOrEmpty(binding.protPercent)
            ) {
                Snackbar.make(requireView(), getString(R.string.toast_fill_fields), Snackbar.LENGTH_SHORT).show()
                false
            } else {
                true
            }
        } else {
            return if (!validateInputFieldIfNullOrEmpty(binding.ageValue)
                or !validateInputFieldIfNullOrEmpty(binding.heightValue)
                or !validateInputFieldIfNullOrEmpty(binding.weightValue)
                or !validateInputFieldIfNullOrEmpty(binding.fatPercent)
                or !validateInputFieldIfNullOrEmpty(binding.carbPercent)
                or !validateInputFieldIfNullOrEmpty(binding.protPercent)
                || binding.radioGroupGender.checkedRadioButtonId == -1
                || binding.radioGroupActivityLevel.checkedRadioButtonId == -1
            ) {
                Snackbar.make(requireView(), getString(R.string.toast_fill_fields), Snackbar.LENGTH_SHORT).show()
                false
            } else {
                true
            }
        }
    }

    // Check if input is not null or empty
    private fun validateInputFieldIfNullOrEmpty(field: EditText): Boolean {
        return if (field.text.isNullOrEmpty()) {
            field.error = "Can't be empty"
            false
        } else {
            field.error = null
            true
        }
    }

    // Switch between calories day input mode
    private fun switchCaloriesDayInputMode() {
        binding.btnSwitchBetweenProfileMode.setOnClickListener {
            isCustomCalories = !isCustomCalories
            displayOrHideViews()
            binding.etDailyCalorieResult.isEnabled = isCustomCalories
            (requireActivity() as AppCompatActivity).supportActionBar?.title =
                if (isCustomCalories) getString(R.string.custom_calories) else getString(R.string.calories_calculator)
        }
    }

    // Display or hide views
    private fun displayOrHideViews() {
        if (isCustomCalories) {
            binding.apply {
                gender.visibility = View.GONE
                radioGroupGender.visibility = View.GONE
                age.visibility = View.GONE
                ageValue.visibility = View.GONE
                height.visibility = View.GONE
                heightValue.visibility = View.GONE
                tvCmSymbol.visibility = View.GONE
                weight.visibility = View.GONE
                weightValue.visibility = View.GONE
                tvKgSymbol.visibility = View.GONE
                tvBmrText.visibility = View.GONE
                tvBmrResult.visibility = View.GONE
                tvExercisePerWeek.visibility = View.GONE
                radioGroupActivityLevel.visibility = View.GONE
            }
        } else {
            binding.apply {
                gender.visibility = View.VISIBLE
                radioGroupGender.visibility = View.VISIBLE
                age.visibility = View.VISIBLE
                ageValue.visibility = View.VISIBLE
                height.visibility = View.VISIBLE
                heightValue.visibility = View.VISIBLE
                tvCmSymbol.visibility = View.VISIBLE
                weight.visibility = View.VISIBLE
                weightValue.visibility = View.VISIBLE
                tvKgSymbol.visibility = View.VISIBLE
                tvBmrText.visibility = View.VISIBLE
                tvBmrResult.visibility = View.VISIBLE
                tvExercisePerWeek.visibility = View.VISIBLE
                radioGroupActivityLevel.visibility = View.VISIBLE
            }
        }
    }

    /** Display Alert Dialog Info Macros Ratio */
    private fun displayInfoMacrosRatioDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_profile_info, null)

        val alertDialog = MaterialAlertDialogBuilder(requireContext())
            .setView(dialogView)
            .create()

        alertDialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}