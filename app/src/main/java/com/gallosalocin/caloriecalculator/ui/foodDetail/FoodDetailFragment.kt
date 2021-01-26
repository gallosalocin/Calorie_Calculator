package com.gallosalocin.caloriecalculator.ui.foodDetail

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.gallosalocin.caloriecalculator.R
import com.gallosalocin.caloriecalculator.databinding.FragmentFoodDetailBinding
import com.gallosalocin.caloriecalculator.models.Food
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FoodDetailFragment : Fragment(R.layout.fragment_food_detail) {

    private var _binding: FragmentFoodDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FoodDetailViewModel by viewModels()
    private lateinit var currentFood: Food


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentFoodDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        loadFood()
    }

    // Setup toolbar
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.toolbar, menu)
        menu.getItem(2).isVisible = true
        menu.getItem(7).isVisible = true

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.tb_menu_edit -> {
                viewModel.setCurrentFoodId(currentFood.id)
                findNavController().navigate(R.id.action_foodDetailFragment_to_editFoodFragment)
            }
            R.id.tb_menu_delete -> displayAlertDialogToDelete()
        }
        return super.onOptionsItemSelected(item)
    }

    // Display Alert Dialog to delete
    private fun displayAlertDialogToDelete() {
        AlertDialog.Builder(requireContext(), R.style.DialogTheme)
                .setCancelable(false)
                .setTitle(getString(R.string.title_alert_dialog))
                .setIcon(R.drawable.ic_delete_swipe_black)
                .setMessage(getString(R.string.delete_alert_dialog_question, currentFood.name))
                .setPositiveButton(getString(R.string.yes_alert_dialog)) { _, _ ->
                    viewModel.deleteFood(currentFood)
                    findNavController().navigate(R.id.action_foodDetailFragment_to_allFoodsFragment)
                }
                .setNegativeButton(getString(R.string.no_alert_dialog)) { dialogInterface, _ ->
                    dialogInterface.dismiss()
                }.create().show()
    }

    // Load food
    private fun loadFood() {
        viewModel.getViewStateLiveData().observe(viewLifecycleOwner) { currentFoodWithCategory ->
            currentFood = currentFoodWithCategory.food

            binding.apply {
                tvResultName.text = currentFood.name
                tvResultCategory.text = currentFoodWithCategory.category.name
                tvResultCalories.text = String.format("%.0f", ((100 * currentFood.calories) / currentFood.weight))
                tvResultFats.text = String.format("%.1fg", ((100 * currentFood.fats) / currentFood.weight))
                tvResultCarbs.text = String.format("%.1fg", ((100 * currentFood.carbs) / currentFood.weight))
                tvResultProts.text = String.format("%.1fg", ((100 * currentFood.prots) / currentFood.weight))
                tvResultNote.text = currentFood.note
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}