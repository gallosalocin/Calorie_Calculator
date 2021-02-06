package com.gallosalocin.caloriecalculator.ui.allCategories

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.gallosalocin.caloriecalculator.R
import com.gallosalocin.caloriecalculator.adapters.CategoryAdapter
import com.gallosalocin.caloriecalculator.databinding.FragmentAllCategoriesBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllCategoriesFragment : Fragment(R.layout.fragment_all_categories) {

    private var _binding: FragmentAllCategoriesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AllCategoriesViewModel by viewModels()
    private lateinit var categoryAdapter: CategoryAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAllCategoriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        setupRecyclerView()
        getAllCategoriesLiveData()
    }

    // Setup toolbar
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.toolbar, menu)
        menu.getItem(3).isVisible = true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.tb_menu_add -> findNavController().navigate(R.id.action_allCategoriesFragment_to_addCategoryFragment)
        }
        return super.onOptionsItemSelected(item)
    }

    // Setup recyclerview
    private fun setupRecyclerView() {
        categoryAdapter = CategoryAdapter {
            viewModel.setCurrentCategoryId(it.id)
            findNavController().navigate(R.id.action_allCategoriesFragment_to_editCategoryFragment)
        }

        binding.rvAllCategories.apply {
            adapter = categoryAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    // Get all categories live data
    private fun getAllCategoriesLiveData() {
        viewModel.getAllCategories.observe(viewLifecycleOwner) {
            val categoriesList = it.drop(1)
            categoryAdapter.submitList(categoriesList)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}