package com.gallosalocin.caloriecalculator.ui.mealDetail

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
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
import com.gallosalocin.caloriecalculator.adapters.FoodAdapter
import com.gallosalocin.caloriecalculator.databinding.FragmentMealDetailBinding
import com.gallosalocin.caloriecalculator.models.Food
import com.gallosalocin.caloriecalculator.models.FoodWithCategory
import com.gallosalocin.caloriecalculator.ui.mainActivity.MainActivity.Companion.dayTag
import com.gallosalocin.caloriecalculator.ui.mainActivity.MainActivity.Companion.mealTag
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MealDetailFragment : Fragment(R.layout.fragment_meal_detail) {

    private var _binding: FragmentMealDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MealDetailViewModel by viewModels()
    private lateinit var foodAdapter: FoodAdapter

    private lateinit var foodWithCategory: FoodWithCategory
    private lateinit var foodWithCategoryList: List<FoodWithCategory>


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMealDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        foodWithCategoryList = ArrayList()
        setupRecyclerView()
        showUserMacrosExpected()

        getMealDetailLiveData()
        configItemTouchHelper()

    }

    // Setup toolbar
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.toolbar, menu)
        menu.getItem(4).isVisible = true
        menu.getItem(3).isVisible = true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.tb_menu_delete -> {
                if (foodWithCategoryList.isEmpty())
                    Snackbar.make(requireView(), getString(R.string.nothing_to_delete), Snackbar.LENGTH_SHORT).show()
                else
                    setupDeleteAllMealFoodsEditDialog()
            }
            R.id.tb_menu_add -> findNavController().navigate(R.id.action_mealDetailFragment_to_allFoodsFragment)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun toolbarChangeTitleMealName(dayTag: Int, mealTag: Int) {
        val dayTagArray = arrayOf(
            getString(R.string.monday_cap), getString(R.string.tuesday_cap), getString(R.string.wednesday_cap), getString(R.string.thursday_cap),
            getString(R.string.friday_cap), getString(R.string.saturday_cap), getString(R.string.sunday_cap)
        )
        val mealTagArray = arrayOf(
            getString(R.string.breakfast), getString(R.string.lunch), getString(R.string.dinner), getString(R.string.snack)
        )

        (requireActivity() as AppCompatActivity).supportActionBar?.title = dayTagArray[dayTag - 1] + " / " + mealTagArray[mealTag - 1]
    }
    
    override fun onStart() {
        super.onStart()
        toolbarChangeTitleMealName(dayTag, mealTag)
    }

    // Setup recyclerview
    private fun setupRecyclerView() {
        foodAdapter = FoodAdapter(
            onItemClickListener = {
                setupWeightEditDialog(it.food)
            },
            onItemLongClickListener = {
                Timber.d("onItemLongClickListener : Click")
            }
        )

        binding.rvMealDetail.apply {
            adapter = foodAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    // Display User Daily Macros Expected
    private fun showUserMacrosExpected() {
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

    // Get meal detail live data sorted by category & display Meal Macros
    private fun getMealDetailLiveData() {
        viewModel.getMealDetail.observe(viewLifecycleOwner) { foodWithCategoryList ->
            this.foodWithCategoryList = foodWithCategoryList
            binding.apply {
                mealCalTotal.text = String.format("%.0f", foodWithCategoryList.sumByDouble { it.food.calories.toDouble() })
                mealFatTotal.text = String.format("%.1f", foodWithCategoryList.sumByDouble { it.food.fats.toDouble() })
                mealCarbTotal.text = String.format("%.1f", foodWithCategoryList.sumByDouble { it.food.carbs.toDouble() })
                mealProtTotal.text = String.format("%.1f", foodWithCategoryList.sumByDouble { it.food.prots.toDouble() })
            }
            foodAdapter.submitList(foodWithCategoryList)
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

    // Setup Swipes
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
                foodWithCategory = foodAdapter.currentList[position]

                if (direction == ItemTouchHelper.LEFT) {
                    viewModel.deleteFood(foodWithCategory.food)
                    Snackbar.make(
                        requireView(), (getString(R.string.successfully_remove_food, foodWithCategory.food.name)),
                        Snackbar.LENGTH_SHORT
                    ).apply {
                        setAction(getString(R.string.undo_snackbar)) {
                            viewModel.insertFood(foodWithCategory.food)
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
            attachToRecyclerView(binding.rvMealDetail)
        }
    }

    // Setup Alert Dialog to edit weight
    private fun setupWeightEditDialog(selectedFood: Food) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_weight_edit, null)

        val weightEdited = dialogView.findViewById<AppCompatEditText>(R.id.et_dialog_weight)
        weightEdited.apply {
            setText("${selectedFood.weight}")
            requestFocus()
            selectAll()
        }

        val dialog = AlertDialog.Builder(requireContext(), R.style.DialogTheme)
            .setView(dialogView)
            .setTitle(selectedFood.name)
            .setCancelable(false)
            .setPositiveButton("Change") { dialoginterface, _ ->
                dialoginterface.dismiss()
                updateFood(selectedFood, weightEdited)
                setupRecyclerView()
                getMealDetailLiveData()
            }
            .setNegativeButton("Cancel") { dialogInterface, _ ->
                dialogInterface.dismiss()
                setupRecyclerView()
                getMealDetailLiveData()
            }.create()

        configEnterButtonSoftKeyboard(selectedFood, weightEdited, dialog)
        dialog.show()

        dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            .setTextColor(ContextCompat.getColor(requireContext(), R.color.design_default_color_secondary_variant))
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
            .setTextColor(ContextCompat.getColor(requireContext(), R.color.design_default_color_secondary_variant))
    }

    // Update Food
    private fun updateFood(selectedFood: Food, weightEdited: AppCompatEditText) {
        val newWeight = weightEdited.text.toString().toInt()
        val foodUpdated = Food(
            id = selectedFood.id,
            name = selectedFood.name,
            categoryId = selectedFood.categoryId,
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

    // Setup Alert Dialog to delete all meal foods
    private fun setupDeleteAllMealFoodsEditDialog() {
        val dialog = AlertDialog.Builder(requireContext(), R.style.DialogTheme)
            .setIcon(R.drawable.ic_delete_black)
            .setTitle(getString(R.string.alert_dialog_delete_meal))
            .setMessage(getString(R.string.alert_dialog_delete_all_message))
            .setCancelable(false)
            .setPositiveButton("Yes") { dialoginterface, _ ->
                dialoginterface.dismiss()
                viewModel.deleteAllMealDetail()
            }
            .setNegativeButton("Cancel") { dialogInterface, _ ->
                dialogInterface.dismiss()
            }.create()
        dialog.show()

        dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            .setTextColor(ContextCompat.getColor(requireContext(), R.color.design_default_color_secondary_variant))
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
            .setTextColor(ContextCompat.getColor(requireContext(), R.color.design_default_color_secondary_variant))
    }

    // Press enter to update weight
    private fun configEnterButtonSoftKeyboard(selectedFood: Food, editText: AppCompatEditText, alertDialog: AlertDialog) {
        editText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                alertDialog.dismiss()
                updateFood(selectedFood, editText)
                setupRecyclerView()
                getMealDetailLiveData()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}