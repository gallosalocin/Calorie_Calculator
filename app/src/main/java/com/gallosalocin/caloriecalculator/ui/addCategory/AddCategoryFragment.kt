package com.gallosalocin.caloriecalculator.ui.addCategory

import android.os.Bundle
import android.view.*
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.gallosalocin.caloriecalculator.R
import com.gallosalocin.caloriecalculator.databinding.FragmentAddCategoryBinding
import com.gallosalocin.caloriecalculator.models.Category
import com.github.dhaval2404.colorpicker.ColorPickerDialog
import com.github.dhaval2404.colorpicker.model.ColorShape
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddCategoryFragment : androidx.fragment.app.Fragment(R.layout.fragment_add_category) {

    private var _binding: FragmentAddCategoryBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AddCategoryViewModel by viewModels()

    private var categoryColor = 0


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAddCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        setupColorPickerDialog()
    }

    // Setup toolbar
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.toolbar, menu)
        menu.getItem(1).isVisible = true

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.tb_menu_save -> confirmAllInputs()
        }
        return super.onOptionsItemSelected(item)
    }


    // Save category
    private fun saveCategory() {
        val category = Category(
            name = binding.etName.text.toString(),
            color = categoryColor
        )
        viewModel.insertCategory(category)
        findNavController().navigate(R.id.action_addCategoryFragment_to_allCategoriesFragment)
    }

    // Validate All Inputs Methods
    private fun confirmAllInputs() {
        if (binding.etName.text.isNullOrEmpty()) {
            binding.etName.error = "Fill the field"
            return
        }
        if (categoryColor == 0) {
            Snackbar.make(requireView(), "Pick a color", Snackbar.LENGTH_SHORT).show()
            return
        }
        saveCategory()
    }

    // Setup Color Picker Dialog
    private fun setupColorPickerDialog() {
        binding.btnColorPicker.setOnClickListener {
            ColorPickerDialog.Builder(requireContext())
                .setColorShape(ColorShape.CIRCLE)
                .setColorListener { color, colorHex ->
                    binding.btnColorPicker.apply {
                        setBackgroundColor(color)
                        categoryColor = color
                    }
                }
                .show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}