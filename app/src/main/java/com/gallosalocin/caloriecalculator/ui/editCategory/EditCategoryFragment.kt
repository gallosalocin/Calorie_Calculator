package com.gallosalocin.caloriecalculator.ui.editCategory

import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.gallosalocin.caloriecalculator.R
import com.gallosalocin.caloriecalculator.databinding.FragmentEditCategoryBinding
import com.gallosalocin.caloriecalculator.models.Category
import com.github.dhaval2404.colorpicker.ColorPickerDialog
import com.github.dhaval2404.colorpicker.model.ColorShape
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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
        displayColorPickerDialog()
    }

    /** Setup toolbar */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.toolbar, menu)
        menu.getItem(5).isVisible = true
        menu.getItem(6).isVisible = true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.tb_menu_save -> confirmAllInputs()
            R.id.tb_menu_delete -> {
                if (currentCategory.id <= 7 ) {
                    Snackbar.make(requireView(), getString(R.string.cannot_delete_this_category), Snackbar.LENGTH_SHORT).show()
                } else {
                    displayAlertDialogToDelete()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /** Display Alert Dialog to delete */
    private fun displayAlertDialogToDelete() {
        val title = SpannableString(getString(R.string.title_alert_dialog_category))
        title.setSpan(StyleSpan(Typeface.BOLD), 0, title.length, 0)
        title.setSpan(RelativeSizeSpan(1.2F), 0, title.length, 0)

        val message = SpannableString(getString(R.string.delete_alert_dialog_question, currentCategory.name))
        message.setSpan(RelativeSizeSpan(1.2F), 0, message.length, 0)

        val alertDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(title)
            .setIcon(R.drawable.ic_delete_swipe_black)
            .setMessage(message)
            .setPositiveButton(getString(R.string.yes_alert_dialog)) { _, _ ->
                viewModel.deleteCategory(currentCategory)
                findNavController().navigate(R.id.action_editCategoryFragment_to_allCategoriesFragment)
            }
            .setNegativeButton(getString(R.string.no_alert_dialog)) { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            .create()

        alertDialog.show()

        alertDialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE)
            .setTextColor(ContextCompat.getColor(requireContext(), R.color.design_default_color_secondary_variant))
        alertDialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_NEGATIVE)
            .setTextColor(ContextCompat.getColor(requireContext(), R.color.design_default_color_secondary_variant))
    }

    /** Load Category */
    private fun loadCategory() {
        viewModel.getViewStateLiveData().observe(viewLifecycleOwner) { category ->
            currentCategory = category
            categoryColor = category.color

            binding.apply {
                tvName.setText(currentCategory.name)
                btnColorPicker.setBackgroundColor(currentCategory.color)
            }
        }
    }

    /** Update Category */
    private fun updateCategory() {
        val categoryUpdated = Category(
            id = currentCategory.id,
            name = binding.tvName.text.toString(),
            color = categoryColor
        )
        viewModel.updateCategory(categoryUpdated)
        findNavController().navigate(R.id.action_editCategoryFragment_to_allCategoriesFragment)
    }

    /** Validate All Inputs Methods */
    private fun confirmAllInputs() {
        if (binding.tvName.text.isNullOrEmpty()) {
            binding.tvName.error = "Fill the field"
            return
        }
        if (categoryColor == 0) {
            Snackbar.make(requireView(), "Pick a color", Snackbar.LENGTH_SHORT).show()
            return
        }
        updateCategory()
    }

    /** Display Color Picker Dialog */
    private fun displayColorPickerDialog() {
        binding.btnColorPicker.setOnClickListener {
            ColorPickerDialog.Builder(requireContext())
                .setColorShape(ColorShape.CIRCLE)
                .setDefaultColor(currentCategory.color)
                .setColorListener { color, _ ->
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