package io.github.nandindustries.demo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.github.nandindustries.demo.presentation.screen.HomeScreen
import io.github.nandindustries.demo.presentation.viewmodel.HomeViewModel
import io.github.nandindustries.demo.ui.theme.MpesaMultiplatformDemoTheme
import io.github.nandindustries.sdk.ui.navigation.MpesaMultiplatformNavigationGraph
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalUuidApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MpesaMultiplatformDemoTheme {
                val navController: NavHostController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "home",
                    modifier = Modifier.fillMaxSize(),
                ) {
                    composable("home") {

                        val viewModel: HomeViewModel by viewModels()
                        HomeScreen(
                            navController = navController,
                            viewModel = viewModel,
                            modifier = Modifier.fillMaxSize(),
                        )
                    }
                    composable("payment/{amount}") { backStackEntry ->
                        val amount = backStackEntry.arguments?.getString("amount")
                        MpesaMultiplatformNavigationGraph(
                            businessName = "Nand Industries",
                            businessLogoUrl = "https://avatars.githubusercontent.com/u/191469385?s=200&v=4",
                            defaultAmount = amount,
                            defaultPhoneNumber = "851234567",
                            transactionReference = "TESTANDROID12345",
                            thirdPartyReference = Uuid.random().toString().take(5),
                            onDismissInputTransactionDetailsStep = {
                                navController.navigate("home") {
                                    popUpTo("home") { inclusive = true }
                                }
                            },
                            modifier = Modifier.fillMaxSize(),
                        )
                    }
                }
            }
        }
    }
}
