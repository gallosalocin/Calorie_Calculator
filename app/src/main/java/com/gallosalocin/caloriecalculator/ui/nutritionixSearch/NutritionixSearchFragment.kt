package com.gallosalocin.caloriecalculator.ui.nutritionixSearch

import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.gallosalocin.caloriecalculator.R
import com.gallosalocin.caloriecalculator.adapters.CommonFoodsAdapter
import com.gallosalocin.caloriecalculator.data.network.dto.CommonFoods
import com.gallosalocin.caloriecalculator.databinding.FragmentNutritionixSearchBinding
import com.gallosalocin.caloriecalculator.utils.NetworkListener
import com.gallosalocin.caloriecalculator.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.flow.collect
import timber.log.Timber


@AndroidEntryPoint
class NutritionixSearchFragment : Fragment(R.layout.fragment_nutritionix_search) {

    private var _binding: FragmentNutritionixSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NutritionixSearchViewModel by viewModels()
    private lateinit var commonFoodsAdapter: CommonFoodsAdapter

    private lateinit var searchView: SearchView
    private lateinit var networkListener: NetworkListener
    private lateinit var commonFoodsList: List<CommonFoods>
    private var isCache = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentNutritionixSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        setupRecyclerView()

        if (isCache) {
            commonFoodsAdapter.submitList(commonFoodsList)
        } else {
            displayErrorMessage(true, R.drawable.ic_search, getString(R.string.you_have_not_done_any_search_yet))
        }

        viewModel.readBackOnline.observe(viewLifecycleOwner) {
            viewModel.backOnline = it
        }

        lifecycleScope.launchWhenStarted { checkNetworkAvailabilityRealTime() }
    }

    /** Setup toolbar */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.toolbar, menu)
        menu.getItem(2).isVisible = true

        val searchItem = menu.findItem(R.id.tb_menu_search)
        searchView = searchItem.actionView as SearchView

        searchView.setOnCloseListener {
            displayErrorMessage(true, R.drawable.ic_search, getString(R.string.you_have_not_done_any_search_yet))
            displayRecyclerView(false)
            false
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                if (query != null) {
                    requestApiData(query)
//                    hideSoftKeyboard()
                }
                return true
            }
        })
    }

    /** Setup recyclerview */
    private fun setupRecyclerView() {
        commonFoodsAdapter = CommonFoodsAdapter {
            viewModel.setCurrentCommonFoodName(it.foodName)
            findNavController().navigate(R.id.action_nutritionixSearchFragment_to_nutritionixDetailFragment)
        }

        binding.rvCommonFoods.apply {
            adapter = commonFoodsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    /** Check network availability in real time */
    private suspend fun checkNetworkAvailabilityRealTime() {
        networkListener = NetworkListener()
        networkListener.checkNetworkAvailability(requireContext()).collect {
            Timber.d("NetworkListener : $it")
            viewModel.networkStatus = it
            viewModel.showNetworkStatus()
        }
    }

    /** Request Api data instant search*/
    private fun requestApiData(query: String) {
        viewModel.getInstantSearch(query)
        viewModel.instantSearchResponse.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    displayErrorMessage(false)
                    displayRecyclerView(true)
                    binding.rvCommonFoods.hideShimmer()
                    isCache = true
                    commonFoodsList = it.data!!.common
                    commonFoodsAdapter.submitList(commonFoodsList, Runnable {
                        binding.rvCommonFoods.scrollToPosition(0)
                    })
                }
                is NetworkResult.Error -> {
                    displayErrorMessage(true, R.drawable.ic_sad, it.message)
                    displayRecyclerView(false)
                }
                is NetworkResult.Loading -> {
                    displayErrorMessage(false)
                    displayRecyclerView(true)
                    binding.rvCommonFoods.showShimmer()
                }
            }
        }
    }

    /** Display or hide error message */
    private fun displayErrorMessage(isVisible: Boolean, image: Int = 0, message: String? = null) {
        binding.apply {
            ivNoResult.setImageResource(image)
            ivNoResult.isVisible = isVisible
            tvNoSearchYet.isVisible = isVisible
            tvNoSearchYet.text = message
        }
    }

    /** Display or hide recycler view */
    private fun displayRecyclerView(isVisible: Boolean) {
        binding.rvCommonFoods.isVisible = isVisible
    }

    /** Hide keyboard */
    private fun hideSoftKeyboard() {
        (requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
            .hideSoftInputFromWindow(view?.windowToken, 0)
    }
}