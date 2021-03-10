package com.gallosalocin.caloriecalculator.ui.mealDetail

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
import com.gallosalocin.caloriecalculator.databinding.FragmentMealDetailBinding
import com.gallosalocin.caloriecalculator.models.Food
import com.gallosalocin.caloriecalculator.models.FoodWithAllData
import com.gallosalocin.caloriecalculator.ui.mainActivity.MainActivity.Companion.dayTag
import com.gallosalocin.caloriecalculator.ui.mainActivity.MainActivity.Companion.mealTag
import com.gallosalocin.caloriecalculator.utils.Constants.DAY_TAG_FRIDAY
import com.gallosalocin.caloriecalculator.utils.Constants.DAY_TAG_MONDAY
import com.gallosalocin.caloriecalculator.utils.Constants.DAY_TAG_SATURDAY
import com.gallosalocin.caloriecalculator.utils.Constants.DAY_TAG_SUNDAY
import com.gallosalocin.caloriecalculator.utils.Constants.DAY_TAG_THURSDAY
import com.gallosalocin.caloriecalculator.utils.Constants.DAY_TAG_TUESDAY
import com.gallosalocin.caloriecalculator.utils.Constants.DAY_TAG_WEDNESDAY
import com.gallosalocin.caloriecalculator.utils.Constants.MEAL_TAG_BREAKFAST
import com.gallosalocin.caloriecalculator.utils.Constants.MEAL_TAG_DINNER
import com.gallosalocin.caloriecalculator.utils.Constants.MEAL_TAG_LUNCH
import com.gallosalocin.caloriecalculator.utils.Constants.MEAL_TAG_SNACK
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MealDetailFragment : Fragment(R.layout.fragment_meal_detail) {

    private var _binding: FragmentMealDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MealDetailViewModel by viewModels()
    private lateinit var foodAdapter: FoodWithAllDataAdapter

    private lateinit var foodWithAllData: FoodWithAllData
    private lateinit var foodWithAllDataList: List<FoodWithAllData>


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMealDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        foodWithAllDataList = ArrayList()
        setupRecyclerView()
        showUserMacrosExpected()

        getMealDetailLiveData()
        configItemTouchHelper()

    }

    /** Setup toolbar */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.toolbar, menu)
        menu.getItem(3).isVisible = true
        menu.getItem(4).isVisible = true
        menu.getItem(5).isVisible = true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.tb_menu_delete -> {
                if (foodWithAllDataList.isEmpty())
                    Snackbar.make(requireView(), getString(R.string.nothing_to_delete), Snackbar.LENGTH_SHORT).show()
                else
                    displayAlertDialogToDelete()
            }
            R.id.tb_menu_add -> findNavController().navigate(R.id.action_mealDetailFragment_to_allFoodsFragment)
            R.id.tb_menu_add_dish -> findNavController().navigate(R.id.action_mealDetailFragment_to_allDishesFragment)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun toolbarChangeTitleMealName(dayTag: String, mealTag: String) {
        var day = ""
        var meal = ""
        when (dayTag) {
            DAY_TAG_MONDAY -> day = getString(R.string.monday_cap)
            DAY_TAG_TUESDAY -> day = getString(R.string.tuesday_cap)
            DAY_TAG_WEDNESDAY -> day = getString(R.string.wednesday_cap)
            DAY_TAG_THURSDAY -> day = getString(R.string.thursday_cap)
            DAY_TAG_FRIDAY -> day = getString(R.string.friday_cap)
            DAY_TAG_SATURDAY -> day = getString(R.string.saturday_cap)
            DAY_TAG_SUNDAY -> day = getString(R.string.sunday_cap)
        }
        when (mealTag) {
            MEAL_TAG_BREAKFAST -> meal = getString(R.string.breakfast)
            MEAL_TAG_LUNCH -> meal = getString(R.string.lunch)
            MEAL_TAG_DINNER -> meal = getString(R.string.dinner)
            MEAL_TAG_SNACK -> meal = getString(R.string.snack)
        }
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "$day  /  $meal"
    }

    override fun onStart() {
        super.onStart()
        toolbarChangeTitleMealName(dayTag, mealTag)
    }

    /** Setup recyclerview */
    private fun setupRecyclerView() {
        foodAdapter = FoodWithAllDataAdapter(
            onItemClickListener = {
                displayWeightEditDialog(it.food)
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

    /** Display User Daily Macros Expected */
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

    /** Display User Daily Macros */
    private fun showDayDetailMacros() {
        viewModel.getDayDetail.observe(viewLifecycleOwner) { foodWithAllData ->
            binding.apply {
                dayCalTotal.text = String.format("%.0f", foodWithAllData.sumByDouble { it.food.calories.toDouble() })
                dayFatTotal.text = String.format("%.1f", foodWithAllData.sumByDouble { it.food.fats.toDouble() })
                dayCarbTotal.text = String.format("%.1f", foodWithAllData.sumByDouble { it.food.carbs.toDouble() })
                dayProtTotal.text = String.format("%.1f", foodWithAllData.sumByDouble { it.food.prots.toDouble() })
            }
            checkIfDayMacrosBiggerExpectedDayMacros()
        }
    }

    /** Get meal detail live data sorted by category & display Meal Macros */
    private fun getMealDetailLiveData() {
        viewModel.getMealDetail.observe(viewLifecycleOwner) { foodWithAllData ->
            foodWithAllDataList = foodWithAllData
            binding.apply {
                mealCalTotal.text = String.format("%.0f", foodWithAllData.sumByDouble { it.food.calories.toDouble() })
                mealFatTotal.text = String.format("%.1f", foodWithAllData.sumByDouble { it.food.fats.toDouble() })
                mealCarbTotal.text = String.format("%.1f", foodWithAllData.sumByDouble { it.food.carbs.toDouble() })
                mealProtTotal.text = String.format("%.1f", foodWithAllData.sumByDouble { it.food.prots.toDouble() })
            }
            foodAdapter.submitList(foodWithAllDataList)
        }
    }

    /** Check if dayMacrosTotal are bigger than expectedDayMacrosTotal */
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
            attachToRecyclerView(binding.rvMealDetail)
        }
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
            if (weightEdited.text.toString().trim() != "" && weightEdited.text.toString() != "0") {
                alertDialog.dismiss()
                updateFood(selectedFood, weightEdited)
                setupRecyclerView()
                getMealDetailLiveData()
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

    /** Display Alert Dialog to delete all meal foods */
    private fun displayAlertDialogToDelete() {
        val title = SpannableString(getString(R.string.alert_dialog_delete_meal))
        title.setSpan(StyleSpan(Typeface.BOLD), 0, title.length, 0)
        title.setSpan(RelativeSizeSpan(1.2F), 0, title.length, 0)

        val message = SpannableString(getString(R.string.alert_dialog_delete_all_message))
        message.setSpan(RelativeSizeSpan(1.2F), 0, message.length, 0)

        val alertDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(title)
            .setIcon(R.drawable.ic_delete_swipe_black)
            .setMessage(message)
            .setPositiveButton(getString(R.string.yes_alert_dialog)) { dialoginterface, _ ->
                dialoginterface.dismiss()
                viewModel.deleteAllMealDetail()
            }
            .setNegativeButton(getString(R.string.cancel)) { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            .create()

        alertDialog.show()

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
            .setTextColor(ContextCompat.getColor(requireContext(), R.color.design_default_color_secondary_variant))
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
            .setTextColor(ContextCompat.getColor(requireContext(), R.color.design_default_color_secondary_variant))
    }

    /** Press enter to update weight */
    private fun AppCompatEditText.configEnterButtonSoftKeyboard(selectedFood: Food, alertDialog: AlertDialog) {
        this.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                alertDialog.dismiss()
                updateFood(selectedFood, this)
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