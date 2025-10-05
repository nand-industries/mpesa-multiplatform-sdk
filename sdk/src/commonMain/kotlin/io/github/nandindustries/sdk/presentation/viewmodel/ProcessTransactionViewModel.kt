package io.github.nandindustries.sdk.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.nandindustries.sdk.domain.entity.CustomerToBusinessTransactionResult
import io.github.nandindustries.sdk.domain.usecase.CustomerToBusinessUseCase
import io.github.nandindustries.sdk.presentation.uistate.TransactionState
import io.github.nandindustries.sdk.reactive.TransactionCompletionPublisher
import io.github.nandindustries.sdk.reactive.model.TransactionCompletionResult
import io.github.nandindustries.sdk.ui.component.transactiondetailscard.TransactionDetailsCardData
import io.github.nandindustries.sdk.ui.navigation.model.C2BTransactionData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

internal class ProcessTransactionViewModel(
    private val c2BTransactionData: C2BTransactionData,
    private val customerToBusinessUseCase: CustomerToBusinessUseCase,
    private val transactionCompletionPublisher: TransactionCompletionPublisher,
) : ViewModel() {
    private val _state: MutableStateFlow<TransactionState> =
        MutableStateFlow(TransactionState.Processing)
    val state: StateFlow<TransactionState>
        get() = _state.asStateFlow()

    private val _transactionDetailsData: MutableStateFlow<TransactionDetailsCardData> =
        MutableStateFlow(
            TransactionDetailsCardData(
                amount = c2BTransactionData.amount.toDouble(),
                transactionReference = c2BTransactionData.transactionReference,
                customerMsisdn = c2BTransactionData.customerMsisdn,
            ),
        )
    val transactionDetailsData: StateFlow<TransactionDetailsCardData>
        get() = _transactionDetailsData.asStateFlow()

    init {
        performC2BTransaction()
    }

    private fun performC2BTransaction() {
        viewModelScope.launch {
            val transactionResult =
                customerToBusinessUseCase(
                    transactionReference = c2BTransactionData.transactionReference,
                    amount = c2BTransactionData.amount,
                    thirdPartyReference = c2BTransactionData.thirdPartyReference,
                    customerMsisdn = c2BTransactionData.customerMsisdn,
                )
            _state.value =
                when (transactionResult) {
                    is CustomerToBusinessTransactionResult.SuccessfulTransaction -> TransactionState.Concluded(
                        title = transactionResult.titleRes,
                        subtitle = transactionResult.subtitleRes,
                        infoText = transactionResult.descriptionRes,
                        animationPath = "files/lottie_successful_transaction.json",
                    )

                    is CustomerToBusinessTransactionResult.CancelledTransaction,
                    is CustomerToBusinessTransactionResult.Failed,
                    is CustomerToBusinessTransactionResult.InsufficientBalance,
                    is CustomerToBusinessTransactionResult.InternalError,
                    is CustomerToBusinessTransactionResult.InvalidAmount,
                    is CustomerToBusinessTransactionResult.InvalidMsisdn,
                    is CustomerToBusinessTransactionResult.TimeoutTransaction,
                    is CustomerToBusinessTransactionResult.UnknownError -> TransactionState.Concluded(
                        title = transactionResult.titleRes,
                        subtitle = transactionResult.subtitleRes,
                        infoText = transactionResult.descriptionRes,
                        animationPath = "files/lottie_failed_transaction.json",
                    )
                }
            transactionCompletionPublisher.broadcastTransactionCompletion(
                TransactionCompletionResult.C2BTransactionCompleted(result = transactionResult),
            )
        }
    }
}