package io.github.nandindustries.sdk.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import io.github.nandindustries.sdk.resources.Res
import io.github.nandindustries.sdk.resources.cancel_button
import io.github.nandindustries.sdk.resources.enter_mpesa_number_hint
import io.github.nandindustries.sdk.resources.merchant_payment_label
import io.github.nandindustries.sdk.resources.mpesa_pay_title
import io.github.nandindustries.sdk.resources.pay_now_button
import io.github.nandindustries.sdk.resources.total_amount_mt_label
import io.github.nandindustries.sdk.resources.transaction_details_title
import io.github.nandindustries.sdk.di.SdkInternalDependencies
import io.github.nandindustries.sdk.presentation.uistate.InputTransactionDetailsUiState
import io.github.nandindustries.sdk.presentation.viewmodel.InputTransactionDetailsViewModel
import io.github.nandindustries.sdk.resources.action_close
import io.github.nandindustries.sdk.ui.component.button.PayNowButton
import io.github.nandindustries.sdk.ui.navigation.InputTransactionDetailsScreenDestination
import io.github.nandindustries.sdk.ui.navigation.ProcessTransactionScreenDestination
import io.github.nandindustries.sdk.ui.utils.CurrencyAmountInputVisualTransformation
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal fun InputTransactionDetailsScreen(
    businessName: String,
    businessLogoUrl: String?,
    defaultAmount: String?,
    defaultPhoneNumber: String?,
    transactionReference: String,
    thirdPartyReference: String,
    navController: NavController,
    modifier: Modifier = Modifier,
    onExitScreen: () -> Unit = {},
) {
    val viewModel: InputTransactionDetailsViewModel = viewModel {
        InputTransactionDetailsViewModel(
            defaultAmount = defaultAmount,
            defaultPhoneNumber = defaultPhoneNumber,
            transactionReference = transactionReference,
            thirdPartyReference = thirdPartyReference,
            transactionCompletionPublisher = SdkInternalDependencies.transactionCompletionPublisher,
        )
    }
    val state by viewModel.state.collectAsState()

    with(viewModel) {
        InputTransactionDetailsUi(
            businessName = businessName,
            businessLogoUrl = businessLogoUrl,
            amount = amount.value,
            phoneNumber = phoneNumber.value,
            state = state,
            onAmountValueChange = viewModel::onAmountValueChange,
            onPhoneNumberChange = ::onPhoneNumberChange,
            onContinueButtonClicked = ::onContinueButtonClicked,
            onNavigateBack = ::onNavigateBack,
            modifier = modifier.fillMaxSize(),
        )
    }
    LaunchedEffect(Unit) {
        viewModel.destination.collect { destination ->
            when (destination) {
                is InputTransactionDetailsViewModel.Destination.BackNavigation -> onExitScreen()
                is InputTransactionDetailsViewModel.Destination.ProcessTransaction -> {
                    navController.navigate(
                        route = ProcessTransactionScreenDestination(
                            amount = destination.amount,
                            transactionReference = destination.transactionReference,
                            thirdPartyReference = destination.thirdPartyReference,
                            customerMsisdn = destination.customerMsisdn,
                        ),
                    ) {
                        popUpTo(InputTransactionDetailsScreenDestination) {
                            inclusive = true
                        }
                    }
                }
            }
        }
    }
    BackHandler(true) {
        viewModel.onNavigateBack()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun InputTransactionDetailsUi(
    businessName: String,
    businessLogoUrl: String?,
    amount: String,
    phoneNumber: String,
    state: InputTransactionDetailsUiState,
    onNavigateBack: () -> Unit,
    onAmountValueChange: (String) -> Unit,
    onPhoneNumberChange: (String) -> Unit,
    onContinueButtonClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(Res.string.mpesa_pay_title))
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = stringResource(Res.string.action_close),
                        )
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
        modifier = modifier,
    ) { paddingValues ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .padding(paddingValues),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier =
                    Modifier
                        .fillMaxSize()
                        .weight(1f),
            ) {
                Surface(
                    shape = RoundedCornerShape(65),
                    modifier = Modifier.size(75.dp),
                    color = MaterialTheme.colorScheme.primaryContainer,
                ) {
                    if (businessLogoUrl.isNullOrEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    brush = Brush.linearGradient(
                                        colors = listOf(
                                            MaterialTheme.colorScheme.primary,
                                            MaterialTheme.colorScheme.secondary,
                                        )
                                    )
                                ),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = businessName.take(1).uppercase(),
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.padding(8.dp),
                            )
                        }
                    } else {
                        AsyncImage(
                            model = businessLogoUrl,
                            contentDescription = null,
                            contentScale = ContentScale.FillWidth,
                            modifier = Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.surfaceVariant),
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(Res.string.merchant_payment_label),
                    style = MaterialTheme.typography.bodyLarge,
                )
                Text(
                    text = businessName,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                )
                AssistChip(
                    onClick = {},
                    label = { Text(state.serviceProviderCode) },
                    shape = RoundedCornerShape(100),
                )
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = stringResource(Res.string.transaction_details_title),
                            textAlign = TextAlign.Start,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.fillMaxWidth(),
                        )
                        Spacer(modifier = Modifier.size(18.dp))
                        Text(
                            text = stringResource(Res.string.total_amount_mt_label),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                        )
                        AmountInputField(
                            amount = amount,
                            maxAmount = state.maxAmount,
                            onAmountValueChange = onAmountValueChange,
                            isReadOnly = !state.isEditableAmount,
                        )
                        Spacer(modifier = Modifier.size(18.dp))
                        Text(
                            text = stringResource(Res.string.enter_mpesa_number_hint),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(0.6f),
                            horizontalArrangement = Arrangement.Center,
                        ) {
                            val textStyle = MaterialTheme.typography.titleLarge
                            Text(
                                text = "+258",
                                style =
                                    textStyle.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.primary,
                                    ),
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                BasicTextField(
                                    value = phoneNumber,
                                    onValueChange = onPhoneNumberChange,
                                    textStyle = textStyle.copy(
                                        color = MaterialTheme.colorScheme.onSurface,
                                    ),
                                    maxLines = 1,
                                    keyboardOptions =
                                        KeyboardOptions(
                                            keyboardType = KeyboardType.Phone,
                                            imeAction = ImeAction.Done,
                                        ),
                                    modifier = Modifier,
                                )
                            }
                        }
                        state.phoneNumberValidationErrorMessage?.let {
                            Surface(
                                shape = RoundedCornerShape(30),
                                color = MaterialTheme.colorScheme.errorContainer,
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp),
                            ) {
                                Row(
                                    modifier = Modifier.padding(8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Warning,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.error,
                                    )
                                    Text(
                                        text = stringResource(it),
                                        modifier = Modifier.padding(start = 8.dp),
                                    )
                                }
                            }
                        }
                    }
                }
            }
            Column {
                PayNowButton(
                    text = stringResource(Res.string.pay_now_button),
                    onClick = onContinueButtonClicked,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = state.isContinueButtonEnabled,
                )
                OutlinedButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onNavigateBack,
                ) {
                    Text(stringResource(Res.string.cancel_button))
                }
            }
        }
    }
}

@Composable
fun AmountInputField(
    amount: String,
    maxAmount: Long,
    isReadOnly: Boolean,
    onAmountValueChange: (String) -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            BasicTextField(
                value = amount,
                onValueChange = onAmountValueChange,
                modifier = Modifier.weight(1f).align(Alignment.CenterVertically),
                textStyle =
                    TextStyle(
                        textAlign = TextAlign.Center,
                        fontSize = 42.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                    ),
                keyboardOptions =
                    KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next,
                    ),
                visualTransformation =
                    CurrencyAmountInputVisualTransformation(
                        fixedCursorAtTheEnd = true,
                        numberOfDecimals = 2,
                        maxValue = maxAmount,
                    ),
                maxLines = 1,
                readOnly = isReadOnly,
            )
        }
    }
}

@Preview
@Composable
private fun PaymentUiPreview() {
    InputTransactionDetailsUi(
        amount = "1000",
        phoneNumber = "841234567",
        state = InputTransactionDetailsUiState(
            serviceProviderCode = "171717",
            maxAmount = 1000000,
        ),
        onNavigateBack = {},
        onAmountValueChange = {},
        onPhoneNumberChange = {},
        onContinueButtonClicked = {},
        businessName = "Nand Industries",
        businessLogoUrl = null,
    )
}
