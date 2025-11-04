package com.velaris.mobile.ui.feature.settings.components.sections

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.velaris.mobile.ui.common.SectionCard
import com.velaris.mobile.ui.feature.auth.SessionViewModel
import com.velaris.mobile.ui.navigation.Routes

@Composable
fun AccountSection(viewModel: SessionViewModel, navController: NavController) {
    SectionCard(title = "Account") {
        Button(
            onClick = {
                viewModel.logout()
                navController.navigate(Routes.LOGIN) {
                    popUpTo(0) { inclusive = true }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer
            )
        ) {
            Text("Logout")
        }
    }
}
