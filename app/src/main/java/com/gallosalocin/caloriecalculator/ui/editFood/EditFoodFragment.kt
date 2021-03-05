package com.gallosalocin.caloriecalculator.ui.editFood

import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.gallosalocin.caloriecalculator.R
import com.gallosalocin.caloriecalculator.databinding.FragmentEditFoodBinding
import com.gallosalocin.caloriecalculator.models.Category
import com.gallosalocin.caloriecalculator.models.Food
import com.gallosalocin.caloriecalculator.utils.Utils.Companion.validateInputEditTextWithLayout
import com.gallosalocin.caloriecalculator.utils.Utils.Companion.validateSpinnerCategory
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditFoodFragment : Fragment(R.layout.fragment_edit_food) {

    private var _binding: FragmentEditFoodBinding? = null
    private val binding get() = _binding!!

    private val viewModel: EditFoodViewModel by viewModels()

    private var categoryList: List<Category> = ArrayList()
    private lateinit var currentFood: Food
    private var spinnerCategorySelected: Category? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentEditFoodBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        configSpinner()
        loadFood()

    }

    /** Setup toolbar */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.toolbar, menu)
        menu.getItem(6).isVisible = true

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.tb_menu_save -> confirmAllInputs()
        }
        return super.onOptionsItemSelected(item)
    }

    /** Load food */
    private fun loadFood() {
        var position = 0

        viewModel.getViewStateLiveData().observe(viewLifecycleOwner) { currentFoodWithCategory ->
            currentFood = currentFoodWithCategory.food

            for (item in categoryList) {
                if (item.name == currentFoodWithCategory.category.name) {
                    position = categoryList.indexOf(item)
                }
            }

            binding.apply {
                etEditName.setText(currentFood.name)
                spinnerCategory.setSelection(position)
                etEditCalorie.setText(String.format("%.0f", ((100 * currentFood.calories) / currentFood.weight)))
                etEditFat.setText(String.format("%.1f", ((100 * currentFood.fats) / currentFood.weight)))
                etEditCarb.setText(String.format("%.1f", ((100 * currentFood.carbs) / currentFood.weight)))
                etEditProt.setText(String.format("%.1f", ((100 * currentFood.prots) / currentFood.weight)))
                etEditNote.setText(currentFood.note)
            }
        }
    }

    /** Update Food */
    private fun updateFood() {
        val foodUpdated = Food(
            id = currentFood.id,
            name = binding.etEditName.text.toString(),
            categoryId = spinnerCategorySelected!!.id,
            calories = binding.etEditCalorie.text.toString().toFloat(),
            fats = binding.etEditFat.text.toString().replace(',', '.').toFloat(),
            carbs = binding.etEditCarb.text.toString().replace(',', '.').toFloat(),
            prots = binding.etEditProt.text.toString().replace(',', '.').toFloat(),
            note = binding.etEditNote.text.toString()
        )
        viewModel.updateFood(foodUpdated)
        findNavController().navigate(R.id.action_editFoodFragment_to_allFoodsFragment)
    }

    /** Config Category Spinner */
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

    /** Validate All Inputs Methods */
    private fun confirmAllInputs() {
        if (!binding.spinnerCategory.validateSpinnerCategory(getString(R.string.choose_a_category))
            or !binding.etEditName.validateInputEditTextWithLayout(binding.editName, getString(R.string.error_fill_field))
            or !binding.etEditCalorie.validateInputEditTextWithLayout(binding.editCalorie, getString(R.string.error_fill_field))
            or !binding.etEditFat.validateInputEditTextWithLayout(binding.editFat, getString(R.string.error_fill_field))
            or !binding.etEditCarb.validateInputEditTextWithLayout(binding.editCarb, getString(R.string.error_fill_field))
            or !binding.etEditProt.validateInputEditTextWithLayout(binding.editProt, getString(R.string.error_fill_field))
        ) {
            return
        }
        updateFood()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}