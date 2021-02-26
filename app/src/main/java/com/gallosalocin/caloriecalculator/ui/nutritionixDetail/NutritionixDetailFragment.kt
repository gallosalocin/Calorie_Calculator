package com.gallosalocin.caloriecalculator.ui.nutritionixDetail

import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import coil.load
import coil.transform.CircleCropTransformation
import com.gallosalocin.caloriecalculator.R
import com.gallosalocin.caloriecalculator.data.network.dto.FoodNamePost
import com.gallosalocin.caloriecalculator.data.network.dto.FoodNutrients
import com.gallosalocin.caloriecalculator.databinding.FragmentNutritionixDetailBinding
import com.gallosalocin.caloriecalculator.models.Category
import com.gallosalocin.caloriecalculator.models.Food
import com.gallosalocin.caloriecalculator.utils.NetworkResult
import com.gallosalocin.caloriecalculator.utils.Utils.Companion.validateInputEditText
import com.gallosalocin.caloriecalculator.utils.Utils.Companion.validateSpinnerCategory
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class NutritionixDetailFragment : Fragment(R.layout.fragment_nutritionix_detail) {

    private var _binding: FragmentNutritionixDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NutritionixDetailViewModel by viewModels()

    private var categoryList: List<Category> = ArrayList()
    private var spinnerCategorySelected: Category? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentNutritionixDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        configSpinner()

        getCurrentFood()
    }

    /** Get current food */
    private fun getCurrentFood() {
        viewModel.getCurrentCommonFoodNameLiveData().observe(viewLifecycleOwner) { foodName ->
            val foodNamePost = FoodNamePost(foodName)
            requestApiData(foodNamePost)
        }
    }

    /** Request Api data food nutrients */
    private fun requestApiData(foodNamePost: FoodNamePost) {
        viewModel.pushFoodNutrients(foodNamePost)
        viewModel.foodNutrientsResponse.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    val foodResultsList: List<FoodNutrients> = it.data!!.foods
                    val currentFood = foodResultsList[0]
                    loadFood(currentFood)
                }
                is NetworkResult.Error -> {
                    Toast.makeText(requireContext(), "Error : ${it.message}", Toast.LENGTH_SHORT).show()
                    Timber.d("NetworkResult.Error : ${it.message}")
                }
                is NetworkResult.Loading -> {
                    Timber.d("NetworkResult.Loading : loading...")
                }
            }
        }
    }

    /** Load food */
    private fun loadFood(currentFood: FoodNutrients) {
            binding.apply {
                ivImage.load(currentFood.photo.thumb) {
                    placeholder(R.drawable.ic_placeholder)
                    transformations(CircleCropTransformation())
                    error(R.drawable.ic_image_error)
                }
                etResultName.setText(currentFood.name)
                tvResultCalories.text = String.format("%.0f", ((100 * currentFood.calories) / currentFood.weight))
                tvResultFats.text = String.format("%.1f", ((100 * currentFood.fats) / currentFood.weight))
                tvResultCarbs.text = String.format("%.1f", ((100 * currentFood.carbs) / currentFood.weight))
                tvResultProts.text = String.format("%.1f", ((100 * currentFood.prots) / currentFood.weight))
            }
    }

    /** Setup toolbar */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.toolbar, menu)
        menu.getItem(5).isVisible = true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.tb_menu_save -> confirmAllInputs()
        }
        return super.onOptionsItemSelected(item)
    }

    /** Save food */
    private fun saveFood() {
        val food = Food(
            name = binding.etResultName.text.toString(),
            categoryId = spinnerCategorySelected!!.id,
            calories = binding.tvResultCalories.text.toString().replace(',', '.').toFloat(),
            fats = binding.tvResultFats.text.toString().replace(',', '.').toFloat(),
            carbs = binding.tvResultCarbs.text.toString().replace(',', '.').toFloat(),
            prots = binding.tvResultProts.text.toString().replace(',', '.').toFloat(),
        )
        viewModel.insertFood(food)
        findNavController().navigate(R.id.action_nutritionixDetailFragment_to_nutritionixSearchFragment)
    }

    /** Validate All Inputs Methods */
    private fun confirmAllInputs() {
        if (!binding.spinnerCategory.validateSpinnerCategory(getString(R.string.choose_a_category))
            or (!binding.etResultName.validateInputEditText(getString(R.string.error_fill_field)))
        ) {
            return
        }
        saveFood()
    }

    /** Config Category Spinner */
    private fun configSpinner() {
        viewModel.getAllCategories.observe(viewLifecycleOwner) { allCategories ->
            categoryList = allCategories

            val adapter = ArrayAdapter(requireContext(), R.layout.spinner_custom_layout_nutritionix, categoryList)
            adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
            binding.spinnerCategory.adapter = adapter

            binding.spinnerCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(p0: AdapterView<*>?) {
                }

                override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    spinnerCategorySelected = adapter.getItem(position)
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}