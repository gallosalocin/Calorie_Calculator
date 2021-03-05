package com.gallosalocin.caloriecalculator.ui.editDish

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gallosalocin.caloriecalculator.R
import com.gallosalocin.caloriecalculator.adapters.FoodWithAllDataAdapter
import com.gallosalocin.caloriecalculator.databinding.FragmentEditDishBinding
import com.gallosalocin.caloriecalculator.models.Dish
import com.gallosalocin.caloriecalculator.models.Food
import com.gallosalocin.caloriecalculator.models.FoodWithAllData
import com.gallosalocin.caloriecalculator.ui.addDish.AddDishFragment.Companion.isDish
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditDishFragment : Fragment(R.layout.fragment_edit_dish) {

    private var _binding: FragmentEditDishBinding? = null
    private val binding get() = _binding!!

    private val viewModel: EditDishViewModel by viewModels()
    private lateinit var foodAdapter: FoodWithAllDataAdapter

    private lateinit var foodWithAllData: FoodWithAllData
    private lateinit var currentDish: Dish
    private lateinit var currentRecipeFoodWithAllDataList: List<FoodWithAllData>


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentEditDishBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        currentRecipeFoodWithAllDataList = ArrayList()
        setupRecyclerView()

        getCurrentDish()
        getCurrentDishListFood()
        configItemTouchHelper()

    }

    /** Setup toolbar */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.toolbar, menu)
        menu.getItem(3).isVisible = true
        menu.getItem(5).isVisible = true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.tb_menu_delete -> displayAlertDialogToDelete()
            R.id.tb_menu_add -> {
                isDish = true
                viewModel.setCurrentDishId(currentDish.id)
                findNavController().navigate(R.id.action_editDishFragment_to_allFoodsFragment)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /** Setup recyclerview */
    private fun setupRecyclerView() {
        foodAdapter = FoodWithAllDataAdapter(
            onItemClickListener = {
                displayWeightEditDialog(it.food)
            },
            onItemLongClickListener = {
                Toast.makeText(requireContext(), "longClick : ${it.food}", Toast.LENGTH_SHORT).show()
            }
        )

        binding.rvFoodsDish.apply {
            adapter = foodAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    /** Get current dish */
    private fun getCurrentDish() {
        viewModel.getDishLiveData().observe(viewLifecycleOwner) { dish ->
            currentDish = dish
            changeToolbarTitle()
        }
    }

    /** Get current dish list food */
    private fun getCurrentDishListFood() {
        viewModel.getDishFoodListLiveData().observe(viewLifecycleOwner) { foodWithAllDataList ->
            currentRecipeFoodWithAllDataList = foodWithAllDataList
            binding.apply {
                dishTotalCal.text = String.format("%.0f", currentRecipeFoodWithAllDataList.sumByDouble { it.food.calories.toDouble() })
                dishTotalFat.text = String.format("%.1f", currentRecipeFoodWithAllDataList.sumByDouble { it.food.fats.toDouble() })
                dishTotalCarb.text = String.format("%.1f", currentRecipeFoodWithAllDataList.sumByDouble { it.food.carbs.toDouble() })
                dishTotalProt.text = String.format("%.1f", currentRecipeFoodWithAllDataList.sumByDouble { it.food.prots.toDouble() })
            }
            foodAdapter.submitList(currentRecipeFoodWithAllDataList)
        }
    }

    /** Setup Swipe to remove food */
    private fun configItemTouchHelper() {
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT
        ) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                foodWithAllData = foodAdapter.currentList[position]

                if (direction == ItemTouchHelper.LEFT) {
                    viewModel.deleteFood(foodWithAllData.food)
                    Snackbar.make(
                        requireView(), (getString(R.string.successfully_remove_food, foodWithAllData.food.name)),
                        Snackbar.LENGTH_SHORT
                    ).apply {
                        setAction(getString(R.string.undo_snackbar)) {
                            viewModel.insertFood(foodWithAllData.food)
                        }
                        show()
                    }
                }
            }

            override fun onChildDraw(
                canvas: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                val removeIcon: Drawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_remove_swipe)!!
                val swipeLeftBackground = ColorDrawable(Color.parseColor("#FF9900"))
                val itemView = viewHolder.itemView
                val removeIconMargin = (itemView.height - removeIcon.intrinsicHeight) / 2

                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

                    if (dX < 0) {
                        swipeLeftBackground.setBounds(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
                        removeIcon.setBounds(
                            itemView.right - removeIconMargin - removeIcon.intrinsicWidth,
                            itemView.top + removeIconMargin,
                            itemView.right - removeIconMargin,
                            itemView.bottom - removeIconMargin
                        )
                        swipeLeftBackground.draw(canvas)
                        removeIcon.draw(canvas)
                    }
                }
                super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
        }
        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.rvFoodsDish)
        }
    }

    /** Display Alert Dialog to delete */
    private fun displayAlertDialogToDelete() {
        val title = SpannableString(getString(R.string.title_alert_dialog_dish))
        title.setSpan(StyleSpan(Typeface.BOLD), 0, title.length, 0)
        title.setSpan(RelativeSizeSpan(1.2F), 0, title.length, 0)

        val message = SpannableString(getString(R.string.delete_alert_dialog_question, currentDish.name))
        message.setSpan(RelativeSizeSpan(1.2F), 0, message.length, 0)

        val alertDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(title)
            .setIcon(R.drawable.ic_delete_swipe_black)
            .setMessage(message)
            .setPositiveButton(getString(R.string.yes_alert_dialog)) { _, _ ->
                viewModel.deleteDish(currentDish)
                findNavController().navigate(R.id.action_editDishFragment_to_allDishesFragment)
            }
            .setNegativeButton(getString(R.string.no_alert_dialog)) { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            .create()

        alertDialog.show()

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
            .setTextColor(ContextCompat.getColor(requireContext(), R.color.design_default_color_secondary_variant))
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
            .setTextColor(ContextCompat.getColor(requireContext(), R.color.design_default_color_secondary_variant))
    }

    /** Display Alert Dialog to edit weight */
    private fun displayWeightEditDialog(selectedFood: Food) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_weight_edit, null)
        val weightEdited = dialogView.findViewById<AppCompatEditText>(R.id.et_dialog_weight)
        weightEdited.apply {
            setText("${selectedFood.weight}")
            requestFocus()
            selectAll()
        }
        val title = SpannableString(selectedFood.name)
        title.setSpan(StyleSpan(Typeface.BOLD), 0, title.length, 0)
        title.setSpan(RelativeSizeSpan(1.2F), 0, title.length, 0)

        val alertDialog = MaterialAlertDialogBuilder(requireContext())
            .setView(dialogView)
            .setTitle(title)
            .setPositiveButton(getString(R.string.ok), null)
            .setNegativeButton(getString(R.string.cancel)) { dialoginterface, _ ->
                dialoginterface.dismiss()
            }
            .create()

        weightEdited.configEnterButtonSoftKeyboard(selectedFood, alertDialog)
        alertDialog.show()

        val positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
        positiveButton.setOnClickListener {
            if (weightEdited.text.toString() != "" && weightEdited.text.toString() != "0") {
                alertDialog.dismiss()
                updateFood(selectedFood, weightEdited)
                setupRecyclerView()
                getCurrentDishListFood()
            } else {
                Toast.makeText(requireContext(), getString(R.string.error_fill_field_weight), Toast.LENGTH_SHORT).show()
            }
        }

        alertDialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        positiveButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.design_default_color_secondary_variant))
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
            .setTextColor(ContextCompat.getColor(requireContext(), R.color.design_default_color_secondary_variant))
    }

    /** Update Food */
    private fun updateFood(selectedFood: Food, weightEdited: AppCompatEditText) {
        val newWeight = weightEdited.text.toString().toInt()
        val foodUpdated = Food(
            id = selectedFood.id,
            name = selectedFood.name,
            categoryId = selectedFood.categoryId,
            dishId = selectedFood.dishId,
            calories = ((newWeight.toFloat() / 100) * ((100 * selectedFood.calories) / selectedFood.weight)),
            fats = ((newWeight.toFloat() / 100) * ((100 * selectedFood.fats) / selectedFood.weight)),
            carbs = ((newWeight.toFloat() / 100) * ((100 * selectedFood.carbs) / selectedFood.weight)),
            prots = ((newWeight.toFloat() / 100) * ((100 * selectedFood.prots) / selectedFood.weight)),
            note = selectedFood.note,
            dayId = selectedFood.dayId,
            mealId = selectedFood.mealId,
            weight = newWeight
        )
        viewModel.updateFood(foodUpdated)
    }

    /** Press enter to update weight */
    private fun AppCompatEditText.configEnterButtonSoftKeyboard(selectedFood: Food, alertDialog: AlertDialog) {
        this.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                alertDialog.dismiss()
                updateFood(selectedFood, this)
                setupRecyclerView()
                getCurrentDishListFood()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }

    /** Change toolbar title */
    private fun changeToolbarTitle() {
        (requireActivity() as AppCompatActivity).supportActionBar?.title = currentDish.name
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}