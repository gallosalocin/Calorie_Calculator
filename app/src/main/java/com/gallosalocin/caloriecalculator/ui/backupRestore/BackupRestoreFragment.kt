package com.gallosalocin.caloriecalculator.ui.backupRestore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.gallosalocin.caloriecalculator.R
import com.gallosalocin.caloriecalculator.databinding.FragmentBackupRestoreBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BackupRestoreFragment : Fragment(R.layout.fragment_backup_restore) {

    private var _binding: FragmentBackupRestoreBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BackupRestoreViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentBackupRestoreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)


        binding.btnBackup.setOnClickListener {
            viewModel.backupDatabase(requireContext())
        }

        binding.btnRestore.setOnClickListener {
            viewModel.restoreDatabase(requireContext())
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}