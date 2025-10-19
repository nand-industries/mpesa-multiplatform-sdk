package io.github.nandindustries.sdk.presentation.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import io.github.alexzhirkevich.compottie.Compottie
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.animateLottieCompositionAsState
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import io.github.alexzhirkevich.compottie.rememberLottiePainter
import io.github.nandindustries.sdk.di.SdkInternalDependencies
import io.github.nandindustries.sdk.presentation.uistate.TransactionState
import io.github.nandindustries.sdk.presentation.viewmodel.ProcessTransactionViewModel
import io.github.nandindustries.sdk.resources.Res
import io.github.nandindustries.sdk.ui.component.counter.TimeoutCountdown
import io.github.nandindustries.sdk.ui.component.transactiondetailscard.TransactionDetailsCardData
import io.github.nandindustries.sdk.ui.component.transactiondetailscard.TransactionDetailsMinimalCard
import io.github.nandindustries.sdk.ui.navigation.model.C2BTransactionData
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ProcessTransactionScreen(
    navController: NavController,
    c2BTransactionData: C2BTransactionData,
    modifier: Modifier = Modifier,
    onExitScreen: () -> Unit = {},
) {
    val viewModel: ProcessTransactionViewModel = viewModel {
        ProcessTransactionViewModel(
            c2BTransactionData = c2BTransactionData,
            customerToBusinessUseCase = SdkInternalDependencies.customerToBusinessUseCase,
            transactionCompletionPublisher = SdkInternalDependencies.transactionCompletionPublisher,
        )
    }
    val state by viewModel.state.collectAsState()
    val transactionDetailsState by viewModel.transactionDetailsData.collectAsState()

    ProcessTransactionUi(
        state = state,
        transactionDetailsCardData = transactionDetailsState,
        onNavigateBack = {
            val wasPopped = navController.popBackStack()
            if (!wasPopped) {
                onExitScreen()
            }
        },
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
@Composable
private fun ProcessTransactionUi(
    state: TransactionState,
    transactionDetailsCardData: TransactionDetailsCardData,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(state.title)) },
                navigationIcon = {
                    if (state !is TransactionState.Processing) {
                        IconButton(onClick = onNavigateBack) {
                            Icon(
                                imageVector = Icons.Rounded.Close,
                                contentDescription = "Close Icon",
                            )
                        }
                    }
                },
                colors = TopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    scrolledContainerColor = MaterialTheme.colorScheme.errorContainer,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
            )
        },
        modifier = modifier.fillMaxSize(),
    ) { paddingValues ->
        Column(
            modifier =
                modifier
                    .verticalScroll(
                        state = rememberScrollState(),
                        enabled = true,
                    )
                    .padding(paddingValues)
                    .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(state.subtitle),
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.fillMaxWidth(),
            )

            key(state) {
                val composition by rememberLottieComposition(state.animationPath) {
                    LottieCompositionSpec.JsonString(
                        jsonString = Res.readBytes(state.animationPath).decodeToString(),
                    )
                }
                val progress by animateLottieCompositionAsState(
                    composition = composition,
                    iterations = Compottie.IterateForever,
                )
                Image(
                    painter =
                        rememberLottiePainter(
                            composition = composition,
                            progress = { progress },
                        ),
                    contentDescription = "Lottie animation",
                    modifier =
                        Modifier
                            .size(150.dp)
                            .padding(18.dp),
                )
            }
            Text(
                text = stringResource(state.infoText),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.fillMaxWidth(),
            )
            AnimatedVisibility(state is TransactionState.Processing) {
                TimeoutCountdown(
                    start = 115,
                    modifier = Modifier.padding(8.dp),
                    onFinished = {},
                )
            }
            TransactionDetailsMinimalCard(
                data = transactionDetailsCardData,
                modifier = Modifier.padding(16.dp),
            )
        }
    }
}

@Preview
@Composable
private fun ProcessTransactionScreenPreview() {
    ProcessTransactionUi(
        state = TransactionState.Processing,
        transactionDetailsCardData = TransactionDetailsCardData(
            amount = 200.0,
            transactionReference = "TRX",
            customerMsisdn = "841234567",
        ),
        onNavigateBack = {},
    )
}
