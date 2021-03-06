package com.gallosalocin.caloriecalculator.ui.allDishes

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
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gallosalocin.caloriecalculator.R
import com.gallosalocin.caloriecalculator.adapters.DishAdapter
import com.gallosalocin.caloriecalculator.databinding.FragmentAllDishesBinding
import com.gallosalocin.caloriecalculator.models.Dish
import com.gallosalocin.caloriecalculator.ui.mainActivity.MainActivity
import com.gallosalocin.caloriecalculator.ui.mainActivity.MainActivity.Companion.globalChoice
import com.gallosalocin.caloriecalculator.utils.Constants.GLOBAL_CHOICE_BOTTOM_DISHES
import com.gallosalocin.caloriecalculator.utils.Constants.GLOBAL_CHOICE_NOTHING
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class AllDishesFragment : Fragment(R.layout.fragment_all_categories) {

    private var _binding: FragmentAllDishesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AllDishesViewModel by viewModels()
    private lateinit var dishAdapter: DishAdapter
    private lateinit var currentDish: Dish
    private lateinit var currentDishesList: List<Dish>


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAllDishesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        setupRecyclerView()
        getAllDishesLiveData()
        configItemTouchHelper()
    }

    /** Setup toolbar */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.toolbar, menu)
        menu.getItem(3).isVisible = globalChoice != GLOBAL_CHOICE_NOTHING
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.tb_menu_add -> {
                val dish = Dish(name = "")
                viewModel.insertDish(dish)

                findNavController().navigate(R.id.action_allDishesFragment_to_addDishFragment)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /** Setup recyclerview */
    private fun setupRecyclerView() {
        dishAdapter = DishAdapter(
            onItemClickListener = {
                viewModel.setCurrentDishId(it.id)
                findNavController().navigate(R.id.action_allDishesFragment_to_editDishFragment)
            },
            onItemLongClickListener = {
                displayDishNameEditDialog(it)
            }
        )

        binding.rvAllDishes.apply {
            adapter = dishAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    /** Get all dishes live data */
    private fun getAllDishesLiveData() {
        viewModel.getAllDishes.observe(viewLifecycleOwner) { dishesList ->
            currentDishesList = dishesList
            for (dish in currentDishesList) {
                if (dish.name == "") {
                    viewModel.deleteDish(dish)
                }
            }
            dishAdapter.submitList(currentDishesList)
        }
    }

    /** Display Alert Dialog to edit dish name */
    private fun displayDishNameEditDialog(selectedDish: Dish) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_dish_name_edit, null)
        val dishNameEdited = dialogView.findViewById<AppCompatEditText>(R.id.et_dialog_dish_name)
        dishNameEdited.requestFocus()
        val title = SpannableString(getString(R.string.do_you_want_to_change, selectedDish.name))
        title.setSpan(StyleSpan(Typeface.BOLD), 0, title.length, 0)
        title.setSpan(RelativeSizeSpan(1F), 0, title.length, 0)

        val alertDialog = MaterialAlertDialogBuilder(requireContext())
            .setView(dialogView)
            .setTitle(title)
            .setPositiveButton(getString(R.string.ok), null)
            .setNegativeButton(getString(R.string.cancel)) { dialoginterface, _ ->
                dialoginterface.dismiss()
            }
            .create()

        alertDialog.show()

        val positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
        positiveButton.setOnClickListener {
            if (dishNameEdited.text.toString() != "") {
                alertDialog.dismiss()
                updateDish(selectedDish, dishNameEdited)
                setupRecyclerView()
            } else {
                Toast.makeText(requireContext(), getString(R.string.error_fill_field), Toast.LENGTH_SHORT).show()
            }
        }

        alertDialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        positiveButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.design_default_color_secondary_variant))
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
            .setTextColor(ContextCompat.getColor(requireContext(), R.color.design_default_color_secondary_variant))
    }

    /** Update dish */
    private fun updateDish(selectedDish: Dish, nameEdited: AppCompatEditText) {
        val dishUpdated = Dish(
            id = selectedDish.id,
            name = nameEdited.text.toString(),
        )
        viewModel.updateDish(dishUpdated)
    }

    /** Setup Swipe */
    private fun configItemTouchHelper() {
        when (globalChoice) {
            GLOBAL_CHOICE_NOTHING -> {
                val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
                    0,
                    ItemTouchHelper.LEFT
                ) {
                    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                        return false
                    }

                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                        val position = viewHolder.adapterPosition
                        currentDish = dishAdapter.currentList[position]

                        viewModel.setCurrentDishId(currentDish.id)
                        viewModel.getDishFoodListLiveData().observe(viewLifecycleOwner) { foodWithAllDataList ->
                            for (foodWithAllData in foodWithAllDataList) {
                                val foodDuplicated = foodWithAllData.food
                                foodDuplicated.apply {
                                    id = 0
                                    dayId = MainActivity.dayTag.toString()
                                    mealId = MainActivity.mealTag.toString()
                                    dishId = null
                                }
                                viewModel.insertFood(foodDuplicated)
                            }
                            findNavController().navigate(R.id.action_allDishesFragment_to_mealDetailFragment)
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
                    attachToRecyclerView(binding.rvAllDishes)
                }
            }

            GLOBAL_CHOICE_BOTTOM_DISHES -> Timber.d("generalChoices == GENERAL_CHOICE_BOTTOM_DISHES = No swipe available")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}