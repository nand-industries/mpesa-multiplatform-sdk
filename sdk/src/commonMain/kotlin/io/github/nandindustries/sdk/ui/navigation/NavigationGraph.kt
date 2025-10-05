package io.github.nandindustries.sdk.ui.navigation

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import io.github.nandindustries.sdk.presentation.screen.InputTransactionDetailsScreen
import io.github.nandindustries.sdk.presentation.screen.ProcessTransactionScreen
import io.github.nandindustries.sdk.ui.navigation.model.C2BTransactionData
import io.github.nandindustries.sdk.ui.theme.MpesaTheme

@Composable
public fun MpesaMultiplatformNavigationGraph(
    businessName: String,
    transactionReference: String,
    thirdPartyReference: String,
    businessLogoUrl: String? = null,
    defaultAmount: String? = null,
    defaultPhoneNumber: String? = null,
    onDismissInputTransactionDetailsStep: () -> Unit = {},
    darkTheme: Boolean = isSystemInDarkTheme(),
    colorScheme: ColorScheme? = null,
    modifier: Modifier = Modifier,
) {
    val navController: NavHostController = rememberNavController()

    MpesaTheme(darkTheme = darkTheme, colorScheme = colorScheme) {
        NavHost(
            navController = navController,
            startDestination = InputTransactionDetailsScreenDestination,
        ) {
            composable<InputTransactionDetailsScreenDestination> {
                InputTransactionDetailsScreen(
                    businessName = businessName,
                    businessLogoUrl = businessLogoUrl,
                    defaultAmount = defaultAmount,
                    defaultPhoneNumber = defaultPhoneNumber,
                    transactionReference = transactionReference,
                    thirdPartyReference = thirdPartyReference,
                    navController = navController,
                    onExitScreen = onDismissInputTransactionDetailsStep,
                    modifier = modifier,
                )
            }
            composable<ProcessTransactionScreenDestination> { backstackEntry ->
                val c2BTransactionData = C2BTransactionData(
                    amount = backstackEntry.toRoute<ProcessTransactionScreenDestination>().amount,
                    transactionReference = backstackEntry.toRoute<ProcessTransactionScreenDestination>().transactionReference,
                    thirdPartyReference = backstackEntry.toRoute<ProcessTransactionScreenDestination>().thirdPartyReference,
                    customerMsisdn = backstackEntry.toRoute<ProcessTransactionScreenDestination>().customerMsisdn,
                )
                ProcessTransactionScreen(
                    navController = navController,
                    c2BTransactionData = c2BTransactionData,
                    onExitScreen = onDismissInputTransactionDetailsStep,
                    modifier = modifier,
                )
            }
        }
    }
}
