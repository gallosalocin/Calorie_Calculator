package com.gallosalocin.caloriecalculator.ui.dayChoice

import android.os.Bundle
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.gallosalocin.caloriecalculator.R
import com.gallosalocin.caloriecalculator.databinding.FragmentDayBinding
import com.gallosalocin.caloriecalculator.ui.mainActivity.MainActivity.Companion.dayTag
import com.gallosalocin.caloriecalculator.ui.mainActivity.MainActivity.Companion.globalChoice
import com.gallosalocin.caloriecalculator.utils.Constants.GLOBAL_CHOICE_NOTHING
import com.google.android.material.snackbar.Snackbar

class DayChoiceFragment : Fragment(R.layout.fragment_day) {

    private var _binding: FragmentDayBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)

        globalChoice = GLOBAL_CHOICE_NOTHING
        dayTag = 0

        binding.ivShowBottomSheet.setOnClickListener { findNavController().navigate(R.id.action_dayFragment_to_bottomSheetFragment) }

        pressBackTwiceToQuit()
        dayChoice()
    }

    /** Press back twice to quit app */
    private fun pressBackTwiceToQuit() {
        var backPressedTime: Long = 0
        val backSnackBar = Snackbar.make(requireView(), getString(R.string.press_back_again_to_exit), Snackbar.LENGTH_SHORT)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (backPressedTime + 2000 > System.currentTimeMillis()) {
                    backSnackBar.dismiss()
                    activity?.finish()
                } else {
                    backSnackBar.show()
                }
                backPressedTime = System.currentTimeMillis()
            }
        })
    }

    /** Setup toolbar */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.toolbar, menu)
        menu.getItem(7).isVisible = true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.tb_menu -> findNavController().navigate(R.id.action_dayFragment_to_bottomSheetFragment)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun dayChoice() {
        binding.tvMonday.setOnClickListener {
            dayTag = 1
            findNavController().navigate(R.id.action_dayFragment_to_mealFragment)
            requireView().performHapticFeedback(HapticFeedbackConstants.CONTEXT_CLICK)
        }
        binding.tvTuesday.setOnClickListener {
            dayTag = 2
            findNavController().navigate(R.id.action_dayFragment_to_mealFragment)
            requireView().performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
        }
        binding.tvWednesday.setOnClickListener {
            dayTag = 3
            findNavController().navigate(R.id.action_dayFragment_to_mealFragment)
            requireView().performHapticFeedback(HapticFeedbackConstants.CONTEXT_CLICK)
        }
        binding.tvThursday.setOnClickListener {
            dayTag = 4
            findNavController().navigate(R.id.action_dayFragment_to_mealFragment)
            requireView().performHapticFeedback(HapticFeedbackConstants.CONTEXT_CLICK)
        }
        binding.tvFriday.setOnClickListener {
            dayTag = 5
            findNavController().navigate(R.id.action_dayFragment_to_mealFragment)
            requireView().performHapticFeedback(HapticFeedbackConstants.CONTEXT_CLICK)
        }
        binding.tvSaturday.setOnClickListener {
            dayTag = 6
            findNavController().navigate(R.id.action_dayFragment_to_mealFragment)
            requireView().performHapticFeedback(HapticFeedbackConstants.CONTEXT_CLICK)
        }
        binding.tvSunday.setOnClickListener {
            dayTag = 7
            findNavController().navigate(R.id.action_dayFragment_to_mealFragment)
            requireView().performHapticFeedback(HapticFeedbackConstants.CONTEXT_CLICK)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}