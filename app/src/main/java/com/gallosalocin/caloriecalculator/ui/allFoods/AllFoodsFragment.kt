package com.gallosalocin.caloriecalculator.ui.allFoods

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
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gallosalocin.caloriecalculator.R
import com.gallosalocin.caloriecalculator.adapters.FoodWithAllDataAdapter
import com.gallosalocin.caloriecalculator.databinding.FragmentAllFoodsBinding
import com.gallosalocin.caloriecalculator.models.Food
import com.gallosalocin.caloriecalculator.models.FoodWithAllData
import com.gallosalocin.caloriecalculator.ui.mainActivity.MainActivity.Companion.dayTag
import com.gallosalocin.caloriecalculator.ui.mainActivity.MainActivity.Companion.globalChoices
import com.gallosalocin.caloriecalculator.ui.mainActivity.MainActivity.Companion.mealTag
import com.gallosalocin.caloriecalculator.utils.Constants.GLOBAL_CHOICE_BOTTOM_DISHES
import com.gallosalocin.caloriecalculator.utils.Constants.GLOBAL_CHOICE_BOTTOM_FOODS
import com.gallosalocin.caloriecalculator.utils.Constants.GLOBAL_CHOICE_NOTHING
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Runnable
import timber.log.Timber
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class AllFoodsFragment : Fragment(R.layout.fragment_all_foods) {

    private var _binding: FragmentAllFoodsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AllFoodsViewModel by viewModels()
    private lateinit var foodAdapter: FoodWithAllDataAdapter

    private lateinit var foodsList: List<FoodWithAllData>
    private lateinit var foodWithAllData: FoodWithAllData
    private lateinit var searchView: SearchView
    private var currentDishId: Int = -1

    private var currentQuery: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAllFoodsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        Timber.d(globalChoices)

        foodsList = ArrayList()
        setupRecyclerView()

        if (globalChoices == GLOBAL_CHOICE_BOTTOM_DISHES) {
            viewModel.getViewStateLiveData().observe(viewLifecycleOwner) {
                currentDishId = it.id
            }
        }

        getAllFoodsLiveData()
        configItemTouchHelper()
    }

    /** Setup toolbar */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.toolbar, menu)
        menu.getItem(2).isVisible = true
        menu.getItem(3).isVisible = true
        menu.getItem(8).isVisible = true


        val searchItem = menu.findItem(R.id.tb_menu_search)
        searchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                if (query != null) {
                    currentQuery = query
                    filter(query)
                }
                return true
            }
        })
    }

    private fun filter(text: String) {
        val filteredFood = ArrayList<FoodWithAllData>()

        foodsList.filterTo(filteredFood) {
            it.food.name.toLowerCase(Locale.ROOT).contains(text.toLowerCase(Locale.ROOT))
        }
        foodAdapter.submitList(filteredFood)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.tb_menu_add -> findNavController().navigate(R.id.action_allFoodsFragment_to_addFoodFragment)
            R.id.tb_menu_sort_category -> {
                val foodListFiltered = foodsList.sortedBy { it.food.categoryId }
                foodAdapter.submitList(foodListFiltered, Runnable {
                    binding.rvAllFoods.scrollToPosition(0)
                })
            }
            R.id.tb_menu_sort_name -> {
                val foodListFiltered = foodsList.sortedBy { it.food.name.toLowerCase(Locale.ROOT) }
                foodAdapter.submitList(foodListFiltered, Runnable {
                    binding.rvAllFoods.layoutManager?.scrollToPosition(0)
                })
            }
//            R.id.tb_menu_sort_carb -> {
//                val foodListFiltered = foodsList.filter { it.food.categoryId == 2 }
//                foodAdapter.submitList(foodListFiltered)
//            }
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
                viewModel.setCurrentFoodId(it.food.id)
                findNavController().navigate(R.id.action_allFoodsFragment_to_foodDetailFragment)
            }
        )

        binding.rvAllFoods.apply {
            adapter = foodAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    /** Get all foods live data */
    private fun getAllFoodsLiveData() {
        viewModel.getAllFoods.observe(viewLifecycleOwner) {
            foodsList = it
            foodAdapter.submitList(foodsList)
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
            .setNegativeButton(getString(R.string.cancel)) { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            .create()

        weightEdited.configEnterButtonSoftKeyboard(selectedFood, alertDialog)
        alertDialog.show()

        val positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
        positiveButton.setOnClickListener {
            if (weightEdited.text.toString() != "" && weightEdited.text.toString() != "0") {
                alertDialog.dismiss()
                updateFood(selectedFood, weightEdited)
                searchView.setQuery("", false)
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

    /** Press enter to update weight */
    private fun AppCompatEditText.configEnterButtonSoftKeyboard(selectedFood: Food, alertDialog: AlertDialog) {
        this.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                alertDialog.dismiss()
                updateFood(selectedFood, this)
                searchView.setQuery("", false)
                setupRecyclerView()
                foodAdapter.submitList(foodsList)
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }

    /** Setup Swipes */
    private fun configItemTouchHelper() {
        when(globalChoices) {
            GLOBAL_CHOICE_NOTHING -> {
                val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
                    0,
                    ItemTouchHelper.LEFT //or ItemTouchHelper.RIGHT
                ) {
                    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                        return false
                    }

                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                        val position = viewHolder.adapterPosition
                        foodWithAllData = foodAdapter.currentList[position]
//                        if (direction == ItemTouchHelper.RIGHT) {
//                            viewModel.setCurrentFoodId(selectedFood.id)
//                            findNavController().navigate(R.id.action_allFoodsFragment_to_foodDetailFragment)
//                        } else {
                                val foodDuplicated = foodWithAllData.food
                                foodDuplicated.id = 0
                                foodDuplicated.dayId = dayTag.toString()
                                foodDuplicated.mealId = mealTag.toString()
                                viewModel.insertFood(foodDuplicated)
                                searchView.setQuery("", false)
                                setupRecyclerView()
                                foodAdapter.submitList(foodsList)
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
                        val itemView = viewHolder.itemView
//                    val editIcon: Drawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_edit)!!
//                    val editIconMargin = (itemView.height - editIcon.intrinsicHeight) / 2
//                    val swipeRightBackground = ColorDrawable(Color.parseColor("#142DE5"))
                        val addIcon: Drawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_add_swipe)!!
                        val swipeLeftBackground = ColorDrawable(Color.parseColor("#00CC00"))
                        val addIconMargin = (itemView.height - addIcon.intrinsicHeight) / 2

                        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

//                        if (dX > 0) {
//                            swipeRightBackground.setBounds(itemView.left, itemView.top, dX.toInt(), itemView.bottom)
//                            editIcon.setBounds(
//                                itemView.left + editIconMargin,
//                                itemView.top + editIconMargin,
//                                itemView.left + editIconMargin + editIcon.intrinsicWidth,
//                                itemView.bottom - editIconMargin
//                            )
//                            swipeRightBackground.draw(canvas)
//                            editIcon.draw(canvas)
//                        } else {
                            swipeLeftBackground.setBounds(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
                            addIcon.setBounds(
                                itemView.right - addIconMargin - addIcon.intrinsicWidth,
                                itemView.top + addIconMargin,
                                itemView.right - addIconMargin,
                                itemView.bottom - addIconMargin
                            )
                            swipeLeftBackground.draw(canvas)
                            addIcon.draw(canvas)
                        }
//                    }
                        super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    }
                }
                ItemTouchHelper(itemTouchHelperCallback).apply {
                    attachToRecyclerView(binding.rvAllFoods)
                }
            }

            GLOBAL_CHOICE_BOTTOM_FOODS -> Timber.d("generalChoices == GENERAL_CHOICE_BOTTOM_FOODS = No Swipe Available")

            GLOBAL_CHOICE_BOTTOM_DISHES -> {
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

                        val foodDuplicated = foodWithAllData.food
                        foodDuplicated.apply {
                            id = 0
                            dayId = "new"
                            dishId = currentDishId
                        }
                        viewModel.insertFood(foodDuplicated)
                        searchView.setQuery("", false)
                        setupRecyclerView()
                        foodAdapter.submitList(foodsList)
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
                        val itemView = viewHolder.itemView
                        val addIcon: Drawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_add_swipe)!!
                        val swipeLeftBackground = ColorDrawable(Color.parseColor("#00CC00"))
                        val addIconMargin = (itemView.height - addIcon.intrinsicHeight) / 2

                        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                            swipeLeftBackground.setBounds(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
                            addIcon.setBounds(
                                itemView.right - addIconMargin - addIcon.intrinsicWidth,
                                itemView.top + addIconMargin,
                                itemView.right - addIconMargin,
                                itemView.bottom - addIconMargin
                            )
                            swipeLeftBackground.draw(canvas)
                            addIcon.draw(canvas)
                        }
                        super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    }
                }
                ItemTouchHelper(itemTouchHelperCallback).apply {
                    attachToRecyclerView(binding.rvAllFoods)
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}