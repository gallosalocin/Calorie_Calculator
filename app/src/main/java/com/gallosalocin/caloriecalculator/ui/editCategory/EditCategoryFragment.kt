package com.gallosalocin.caloriecalculator.ui.editCategory

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.gallosalocin.caloriecalculator.R
import com.gallosalocin.caloriecalculator.databinding.FragmentEditCategoryBinding
import com.gallosalocin.caloriecalculator.models.Category
import com.github.dhaval2404.colorpicker.ColorPickerDialog
import com.github.dhaval2404.colorpicker.model.ColorShape
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditCategoryFragment : Fragment(R.layout.fragment_edit_category) {

    private var _binding: FragmentEditCategoryBinding? = null
    private val binding get() = _binding!!

    private val viewModel: EditCategoryViewModel by viewModels()

    private lateinit var currentCategory: Category
    private var categoryColor = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentEditCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        loadCategory()
        setupColorPickerDialog()
    }

    // Setup toolbar
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.toolbar, menu)
        menu.getItem(1).isVisible = true
        menu.getItem(7).isVisible = true

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.tb_menu_save -> confirmAllInputs()
            R.id.tb_menu_delete -> displayAlertDialogToDelete()
        }
        return super.onOptionsItemSelected(item)
    }

    // Display Alert Dialog to delete
    private fun displayAlertDialogToDelete() {
        AlertDialog.Builder(requireContext(), R.style.DialogTheme)
            .setCancelable(false)
            .setTitle(getString(R.string.title_alert_dialog_category))
            .setIcon(R.drawable.ic_delete_swipe_black)
            .setMessage(getString(R.string.delete_alert_dialog_question, currentCategory.name))
            .setPositiveButton(getString(R.string.yes_alert_dialog)) { _, _ ->
                viewModel.deleteCategory(currentCategory)
                findNavController().navigate(R.id.action_editCategoryFragment_to_allCategoriesFragment)
            }
            .setNegativeButton(getString(R.string.no_alert_dialog)) { dialogInterface, _ ->
                dialogInterface.dismiss()
            }.create().show()
    }

    // Load food
    private fun loadCategory() {
        viewModel.getViewStateLiveData().observe(viewLifecycleOwner) { category ->
            currentCategory = category
            categoryColor = category.color

            binding.apply {
                etName.setText(currentCategory.name)
                btnColorPicker.setBackgroundColor(currentCategory.color)
            }
        }
    }

    // Update Food
    private fun updateCategory() {
        val categoryUpdated = Category(
            id = currentCategory.id,
            name = binding.etName.text.toString(),
            color = categoryColor
        )
        viewModel.updateCategory(categoryUpdated)
        findNavController().navigate(R.id.action_editCategoryFragment_to_allCategoriesFragment)
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
        updateCategory()
    }

    // Setup Color Picker Dialog
    private fun setupColorPickerDialog() {
        binding.btnColorPicker.setOnClickListener {
            ColorPickerDialog.Builder(requireContext())
                .setColorShape(ColorShape.CIRCLE)
                .setDefaultColor(currentCategory.color)
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