package io.github.nandindustries.sdk.presentation.viewmodel

import io.github.nandindustries.sdk.domain.entity.CustomerToBusinessTransactionResponse
import io.github.nandindustries.sdk.domain.entity.CustomerToBusinessTransactionResult
import io.github.nandindustries.sdk.domain.usecase.CustomerToBusinessUseCase
import io.github.nandindustries.sdk.presentation.uistate.TransactionState
import io.github.nandindustries.sdk.reactive.TransactionCompletionPublisher
import io.github.nandindustries.sdk.reactive.model.TransactionCompletionResult
import io.github.nandindustries.sdk.ui.navigation.model.C2BTransactionData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@OptIn(ExperimentalCoroutinesApi::class)
internal class ProcessTransactionViewModelTest {

    private val transactionData =
        C2BTransactionData(
            amount = "1000",
            transactionReference = "T123",
            thirdPartyReference = "321",
            customerMsisdn = "258841234567",
        )

    @Test
    fun `Given transaction data When initializing view model Then exposes transaction details`() =
        runTest {
            // Given
            val dispatcher = StandardTestDispatcher(testScheduler)
            Dispatchers.setMain(dispatcher)
            val useCaseResult = successfulResult()
            val useCase = FakeCustomerToBusinessUseCase(useCaseResult)
            val publisher = TransactionCompletionPublisher()

            try {
                // When
                val viewModel =
                    ProcessTransactionViewModel(
                        c2BTransactionData = transactionData,
                        customerToBusinessUseCase = useCase,
                        transactionCompletionPublisher = publisher,
                    )

                // Then
                assertIs<TransactionState.Processing>(viewModel.state.value)
                val details = viewModel.transactionDetailsData.value
                assertEquals(transactionData.amount.toDouble(), details.amount)
                assertEquals(transactionData.transactionReference, details.transactionReference)
                assertEquals(transactionData.customerMsisdn, details.customerMsisdn)
                advanceUntilIdle()
            } finally {
                Dispatchers.resetMain()
            }
        }

    @Ignore
    @Test
    fun `Given successful use case result When initializing view model Then publishes successful completion`() =
        runTest {
            // Given
            val dispatcher = StandardTestDispatcher(testScheduler)
            Dispatchers.setMain(dispatcher)
            val useCaseResult = successfulResult()
            val useCase = FakeCustomerToBusinessUseCase(useCaseResult)
            val publisher = TransactionCompletionPublisher()

            try {
                val viewModel =
                    ProcessTransactionViewModel(
                        c2BTransactionData = transactionData,
                        customerToBusinessUseCase = useCase,
                        transactionCompletionPublisher = publisher,
                    )

                // When
                advanceUntilIdle()

                // Then
                val concluded = assertIs<TransactionState.Concluded>(viewModel.state.value)
                assertEquals(useCaseResult.titleRes, concluded.title)
                assertEquals("files/lottie_successful_transaction.json", concluded.animationPath)
                val completion = publisher.result.first()
                val completed =
                    assertIs<TransactionCompletionResult.C2BTransactionCompleted>(completion)
                assertEquals(useCaseResult, completed.result)
            } finally {
                Dispatchers.resetMain()
            }
        }

    @Ignore
    @Test
    fun `Given failure use case result When initializing view model Then publishes failed completion`() =
        runTest {
            // Given
            val dispatcher = StandardTestDispatcher(testScheduler)
            Dispatchers.setMain(dispatcher)
            val response = responseWithCode("INS-6")
            val useCaseResult = CustomerToBusinessTransactionResult.Failed(response)
            val useCase = FakeCustomerToBusinessUseCase(useCaseResult)
            val publisher = TransactionCompletionPublisher()

            try {
                val viewModel =
                    ProcessTransactionViewModel(
                        c2BTransactionData = transactionData,
                        customerToBusinessUseCase = useCase,
                        transactionCompletionPublisher = publisher,
                    )

                // When
                advanceUntilIdle()

                // Then
                val concluded = assertIs<TransactionState.Concluded>(viewModel.state.value)
                assertEquals(useCaseResult.titleRes, concluded.title)
                assertEquals("files/lottie_failed_transaction.json", concluded.animationPath)
                val completion = publisher.result.first()
                val completed =
                    assertIs<TransactionCompletionResult.C2BTransactionCompleted>(completion)
                assertEquals(useCaseResult, completed.result)
            } finally {
                Dispatchers.resetMain()
            }
        }

    private fun successfulResult(): CustomerToBusinessTransactionResult.SuccessfulTransaction =
        CustomerToBusinessTransactionResult.SuccessfulTransaction(
            customerToBusinessTransactionResponse = responseWithCode(code = "INS-0")
        )

    private fun responseWithCode(code: String) =
        CustomerToBusinessTransactionResponse(
            conversationId = "conversation",
            transactionId = "transaction",
            description = "description",
            code = code,
            thirdPartyReference = "third",
        )

    private class FakeCustomerToBusinessUseCase(
        private val result: CustomerToBusinessTransactionResult,
    ) : CustomerToBusinessUseCase {

        override suspend fun invoke(
            transactionReference: String,
            amount: String,
            thirdPartyReference: String,
            customerMsisdn: String,
        ): CustomerToBusinessTransactionResult = result
    }
}