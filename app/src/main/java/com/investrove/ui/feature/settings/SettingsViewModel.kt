package com.investrove.ui.feature.settings

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    // inject repo or preferences manager
) : ViewModel() {

    fun setRequireApproval(enabled: Boolean) {
        // Save to datastore/shared preferences later
        println("Require approval: $enabled")
    }
}
