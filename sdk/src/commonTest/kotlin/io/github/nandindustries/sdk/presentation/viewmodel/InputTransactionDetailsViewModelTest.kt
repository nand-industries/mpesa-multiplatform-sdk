package io.github.nandindustries.sdk.presentation.viewmodel

import io.github.nandindustries.sdk.config.MpesaMultiplatformSdkInitializer
import io.github.nandindustries.sdk.reactive.TransactionCompletionPublisher
import io.github.nandindustries.sdk.reactive.model.TransactionCompletionResult
import io.github.nandindustries.sdk.resources.Res
import io.github.nandindustries.sdk.resources.error_incomplete_phone_number
import io.github.nandindustries.sdk.resources.error_phone_prefix
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
internal class InputTransactionDetailsViewModelTest {

    private lateinit var viewModel: InputTransactionDetailsViewModel

    @BeforeTest
    fun setUp() {
        MpesaMultiplatformSdkInitializer.init(
            productionApiKey = "prod",
            developmentApiKey = "dev",
            publicKey = "public",
            isProduction = false,
            serviceProviderCode = "171717",
        )
        buildViewModel()
    }

    @Test
    fun `When viewModel is built Then it exposes initial state`() =
        runTest {
            // When
            buildViewModel()

            // Then
            val state = viewModel.state.value
            assertEquals(10_000_000L, state.maxAmount)
            assertTrue(state.isEditableAmount)
            assertFalse(state.isContinueButtonEnabled)
            assertEquals("", viewModel.amount.value)
            assertEquals("", viewModel.phoneNumber.value)
        }

    @Test
    fun `Given alpha amount When changing amount Then clears amount and button is disabled`() =
        runTest {
            // Given
            val alphaAmount = "abc"

            // When
            viewModel.onAmountValueChange(alphaAmount)

            // Then
            assertEquals("", viewModel.amount.value)
            assertFalse(viewModel.state.value.isContinueButtonEnabled)
        }

    @Test
    fun `Given amount above limit When changing amount Then clamps to maximum`() = runTest {
        // Given
        val bigAmount = "999999999999"

        // When
        viewModel.onAmountValueChange(bigAmount)

        // Then
        assertEquals("10000000", viewModel.amount.value)
    }

    @Test
    fun `Given invalid phone prefix When changing phone Then shows prefix error`() = runTest {
        // Given
        val invalidPhonePrefix = "820000000"

        // When
        viewModel.onPhoneNumberChange(invalidPhonePrefix)

        // Then
        assertEquals(invalidPhonePrefix, viewModel.phoneNumber.value)
        assertEquals(
            Res.string.error_phone_prefix,
            viewModel.state.value.phoneNumberValidationErrorMessage
        )
        assertFalse(viewModel.state.value.isContinueButtonEnabled)
    }

    @Test
    fun `Given short phone number with valid prefix When changing phone Then shows incomplete error`() =
        runTest {
            // Given
            val shortValidPrefixPhoneNumber = "84123"

            // When
            viewModel.onPhoneNumberChange(shortValidPrefixPhoneNumber)

            // Then
            assertEquals(
                expected = shortValidPrefixPhoneNumber,
                actual = viewModel.phoneNumber.value,
            )
            assertEquals(
                Res.string.error_incomplete_phone_number,
                viewModel.state.value.phoneNumberValidationErrorMessage,
            )
        }

    @Test
    fun `Given valid amount and phone When continue button is clicked Then navigates to ProcessTransaction`() =
        runTest {
            // Given
            val validAmount = "600.0"
            val validPhoneNumber = "841234567"
            buildViewModel(
                defaultAmount = validAmount,
                defaultPhoneNumber = validPhoneNumber,
            )
            assertTrue(viewModel.state.value.isContinueButtonEnabled)

            // When
            viewModel.onContinueButtonClicked()

            // Then
            val destination = viewModel.destination.first()
            assertIs<InputTransactionDetailsViewModel.Destination.ProcessTransaction>(destination)
            assertEquals(
                expected = validAmount,
                actual = destination.amount,
            )
            assertEquals(
                expected = "258$validPhoneNumber",
                actual = destination.customerMsisdn,
            )
            assertEquals(
                expected = TEST_TRANSACTION_REFERENCE,
                actual = destination.transactionReference,
            )
            assertEquals(
                expected = TEST_THIRD_PARTY_REFERENCE,
                actual = destination.thirdPartyReference,
            )
        }

    @Ignore
    @Test
    fun `Given valid data When navigating back Then broadcasts cancellation`() = runTest {
        // Given
        val validAmount = "600"
        val validPhoneNumber = "841234567"
        val publisher = TransactionCompletionPublisher()
        buildViewModel(
            defaultAmount = validAmount,
            defaultPhoneNumber = validPhoneNumber,
            transactionCompletionPublisher = publisher,
        )

        // When
        viewModel.onNavigateBack()
        advanceUntilIdle()

        // Then
        val completion = publisher.result.first()
        assertEquals(
            expected = TransactionCompletionResult.C2BTransactionCancelledBeforeStarted,
            actual = completion,
        )
        val destination = viewModel.destination.first()
        assertEquals(
            expected = InputTransactionDetailsViewModel.Destination.BackNavigation,
            actual = destination,
        )
    }

    @Test
    fun `Given valid defaults Then locks amount field no validation error and continue button is enabled`() =
        runTest {
            // Given
            val validAmount = "600"
            val validPhoneNumber = "841234567"
            val publisher = TransactionCompletionPublisher()
            buildViewModel(
                defaultAmount = validAmount,
                defaultPhoneNumber = validPhoneNumber,
                transactionCompletionPublisher = publisher,
            )

            // Then
            assertFalse(viewModel.state.value.isEditableAmount)
            assertEquals(null, viewModel.state.value.phoneNumberValidationErrorMessage)
            assertTrue(viewModel.state.value.isContinueButtonEnabled)
        }

    @Test
    fun `Given invalid default amount Then throws exception on viewmodel creation`() = runTest {
        // Given
        val invalidAmount = "--invalid-amount--"

        // Then
        assertFailsWith<IllegalArgumentException> {
            buildViewModel(defaultAmount = invalidAmount)
        }
    }

    @Test
    fun `Given invalid default phone number Then throws exception on viewmodel creation`() =
        runTest {
            // Given
            val invalidPhoneNumber = "a"

            // Then
            assertFailsWith<IllegalArgumentException> {
                buildViewModel(defaultPhoneNumber = invalidPhoneNumber)
            }
        }


    private fun buildViewModel(
        defaultAmount: String? = null,
        defaultPhoneNumber: String? = null,
        transactionCompletionPublisher: TransactionCompletionPublisher? = null,
    ) {
        viewModel = InputTransactionDetailsViewModel(
            transactionCompletionPublisher = transactionCompletionPublisher
                ?: TransactionCompletionPublisher(),
            defaultAmount = defaultAmount,
            defaultPhoneNumber = defaultPhoneNumber,
            transactionReference = TEST_TRANSACTION_REFERENCE,
            thirdPartyReference = TEST_THIRD_PARTY_REFERENCE,
        )
    }

    companion object {
        private const val TEST_TRANSACTION_REFERENCE = "transaction-reference"
        private const val TEST_THIRD_PARTY_REFERENCE = "third-party-reference"
    }
}