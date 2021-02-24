package com.gallosalocin.caloriecalculator.ui.allFoods

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
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
import com.gallosalocin.caloriecalculator.adapters.FoodAdapter
import com.gallosalocin.caloriecalculator.databinding.FragmentAllFoodsBinding
import com.gallosalocin.caloriecalculator.models.Food
import com.gallosalocin.caloriecalculator.models.FoodWithCategory
import com.gallosalocin.caloriecalculator.ui.mainActivity.MainActivity.Companion.dayTag
import com.gallosalocin.caloriecalculator.ui.mainActivity.MainActivity.Companion.isBottomChoice
import com.gallosalocin.caloriecalculator.ui.mainActivity.MainActivity.Companion.mealTag
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Runnable
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class AllFoodsFragment : Fragment(R.layout.fragment_all_foods) {

    private var _binding: FragmentAllFoodsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AllFoodsViewModel by viewModels()
    private lateinit var foodAdapter: FoodAdapter

    private lateinit var foodsList: List<FoodWithCategory>
    private lateinit var foodWithCategory: FoodWithCategory
    private lateinit var selectedFood: Food
    private lateinit var searchView: SearchView

    private var currentQuery: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAllFoodsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        foodsList = ArrayList()

        setupRecyclerView()

        getAllFoodsLiveData()
        configItemTouchHelper()
    }

    // Setup toolbar
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.toolbar, menu)
        menu.getItem(3).isVisible = true
        menu.getItem(2).isVisible = true
        menu.getItem(7).isVisible = true


        val searchItem = menu.findItem(R.id.tb_menu_search)
        searchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    currentQuery = query
                    filter(query)
                }
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
        val filteredFood = ArrayList<FoodWithCategory>()

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

    // Setup recyclerview
    private fun setupRecyclerView() {
        foodAdapter = FoodAdapter(
            onItemClickListener = {
                setupWeightEditDialog(it.food)
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

    // Get all foods live data
    private fun getAllFoodsLiveData() {
        viewModel.getAllFoods.observe(viewLifecycleOwner) {
            foodsList = it
            foodAdapter.submitList(foodsList)
        }
    }

    // Setup Swipes
    private fun configItemTouchHelper() {
        if (!isBottomChoice) {
            val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT //or ItemTouchHelper.RIGHT
            ) {
                override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.adapterPosition
                    foodWithCategory = foodAdapter.currentList[position]

                    foodWithCategory.food.apply {
                        selectedFood = this
//                        if (direction == ItemTouchHelper.RIGHT) {
//                            viewModel.setCurrentFoodId(selectedFood.id)
//                            findNavController().navigate(R.id.action_allFoodsFragment_to_foodDetailFragment)
//                        } else {
                            if (dayTag != 0) {
                                val foodDuplicated = foodWithCategory.food
                                foodDuplicated.id = System.currentTimeMillis().toInt()
                                foodDuplicated.dayId = dayTag.toString()
                                foodDuplicated.mealId = mealTag.toString()
                                viewModel.insertFood(foodDuplicated)
                                searchView.setQuery("", false)
                                setupRecyclerView()
                                foodAdapter.submitList(foodsList)
                            } else {
                                searchView.setQuery("", false)
                                setupRecyclerView()
                                foodAdapter.submitList(foodsList)
                            }
//                        }
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
                searchView.setQuery("", false)
            }
            .setNegativeButton("Cancel") { dialogInterface, _ ->
                dialogInterface.dismiss()
                searchView.setQuery(currentQuery, true)
            }.create()

        dialog.show()

        configEnterButtonSoftKeyboard(selectedFood, weightEdited, dialog)
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

    // Press enter to update weight
    private fun configEnterButtonSoftKeyboard(selectedFood: Food, editText: AppCompatEditText, alertDialog: AlertDialog) {
        editText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                updateFood(selectedFood, editText)
                alertDialog.dismiss()
                searchView.setQuery("", false)
                setupRecyclerView()
                foodAdapter.submitList(foodsList)
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