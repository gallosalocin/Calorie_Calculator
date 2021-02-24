package com.gallosalocin.caloriecalculator.ui.backupRestore

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.ebner.roomdatabasebackup.core.RoomBackup
import com.gallosalocin.caloriecalculator.R
import com.gallosalocin.caloriecalculator.db.CaloriesCalculatorDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class BackupRestoreViewModel @Inject constructor(
    private val database: CaloriesCalculatorDatabase,
) : ViewModel() {

    fun backupDatabase(context: Context) {
        RoomBackup()
            .context(context)
            .database(database)
            .enableLogDebug(true)
            .useExternalStorage(true)
            .maxFileCount(3)
            .apply {
                onCompleteListener { success, message ->
                    Timber.d("success: $success, message: $message")
                    if (success) {
                        Toast.makeText(context, context.getString(R.string.database_backed_up), Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .backup()
    }

    fun restoreDatabase(context: Context) {
        RoomBackup()
            .context(context)
            .database(database)
            .enableLogDebug(true)
            .useExternalStorage(true)
            .apply {
                onCompleteListener { success, message ->
                    Timber.d("success: $success, message: $message")
                    if (success) {
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .restore()
    }
}