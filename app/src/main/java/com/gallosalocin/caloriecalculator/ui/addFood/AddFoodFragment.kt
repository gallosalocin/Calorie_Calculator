package com.gallosalocin.caloriecalculator.ui.addFood

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.gallosalocin.caloriecalculator.R
import com.gallosalocin.caloriecalculator.databinding.FragmentAddFoodBinding
import com.gallosalocin.caloriecalculator.models.Category
import com.gallosalocin.caloriecalculator.models.Food
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AddFoodFragment : Fragment(R.layout.fragment_add_food) {

    private var _binding: FragmentAddFoodBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AddFoodViewModel by viewModels()

    private var categoryList: List<Category> = ArrayList()
    private var spinnerCategorySelected: Category? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAddFoodBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        binding.etAddName.apply {
            requestFocus()
            showSoftKeyboard()
        }

        configSpinner()

    }

    // Setup toolbar
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.toolbar, menu)
        menu.getItem(5).isVisible = true

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.tb_menu_save -> confirmAllInputs()
        }
        return super.onOptionsItemSelected(item)
    }

    // Config Category Spinner
    private fun configSpinner() {
        viewModel.getAllCategories.observe(viewLifecycleOwner) { allCategories ->
            categoryList = allCategories

            val adapter = ArrayAdapter(requireContext(), R.layout.spinner_custom_layout, categoryList)
            adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
            binding.spinnerCategory.adapter = adapter

            binding.spinnerCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(p0: AdapterView<*>?) {
                }

                override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    spinnerCategorySelected = adapter.getItem(position)
                }
            }
        }
    }

    // Save food
    private fun saveFood() {
        val food = Food(
            name = binding.etAddName.text.toString(),
            categoryId = spinnerCategorySelected!!.id,
            calories = binding.etAddCalorie.text.toString().toFloat(),
            fats = binding.etAddFat.text.toString().toFloat(),
            carbs = binding.etAddCarb.text.toString().toFloat(),
            prots = binding.etAddProt.text.toString().toFloat(),
            note = binding.etAddNote.text.toString()
        )
        viewModel.insertFood(food)
        findNavController().navigate(R.id.action_addFoodFragment_to_allFoodsFragment)
    }

    // Validate All Inputs Methods
    private fun confirmAllInputs() {
        if (!validateInput(binding.etAddName, binding.addName)
            or !validateSpinnerCategory()
            or !validateInput(binding.etAddCalorie, binding.addCalorie)
            or !validateInput(binding.etAddFat, binding.addFat)
            or !validateInput(binding.etAddCarb, binding.addCarb)
            or !validateInput(binding.etAddProt, binding.addProt)
        ) {
            return
        }
        saveFood()
    }

    // Check if input is not null or empty
    private fun validateInput(field: TextInputEditText, layout: TextInputLayout): Boolean {
        return if (field.text?.isEmpty() == true) {
            layout.error = getString(R.string.error_fill_field)
            false
        } else {
            layout.error = null
            true
        }
    }

    // Check if spinner is not null or empty
    private fun validateSpinnerCategory(): Boolean {
        val category = binding.spinnerCategory.selectedItem.toString().trim()
        val errorText: TextView = binding.spinnerCategory.selectedView as TextView

        return if (category == "") {
            errorText.error = getString(R.string.choose_a_category)
            false
        } else {
            errorText.error = null
            true
        }
    }

    // Show keyboard
    private fun EditText.showSoftKeyboard() {
        (this.context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager).showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}