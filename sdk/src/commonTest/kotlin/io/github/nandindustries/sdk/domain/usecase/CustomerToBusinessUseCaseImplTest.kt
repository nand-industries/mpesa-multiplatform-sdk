package io.github.nandindustries.sdk.domain.usecase

import io.github.nandindustries.sdk.data.remote.mpesa.MpesaClient
import io.github.nandindustries.sdk.data.remote.mpesa.model.CustomerToBusiness
import io.github.nandindustries.sdk.data.remote.mpesa.model.MpesaResponseCodes
import io.github.nandindustries.sdk.domain.entity.CustomerToBusinessTransactionResult
import io.github.nandindustries.sdk.domain.entity.toCustomerToBusinessTransactionResponse
import io.github.nandindustries.sdk.resources.Res
import io.github.nandindustries.sdk.resources.transaction_cancelled_title
import io.github.nandindustries.sdk.resources.transaction_failed_title
import io.github.nandindustries.sdk.resources.transaction_insufficient_balance_title
import io.github.nandindustries.sdk.resources.transaction_internal_error_title
import io.github.nandindustries.sdk.resources.transaction_invalid_amount_title
import io.github.nandindustries.sdk.resources.transaction_invalid_msisdn_title
import io.github.nandindustries.sdk.resources.transaction_success_title
import io.github.nandindustries.sdk.resources.transaction_timeout_title
import io.github.nandindustries.sdk.resources.transaction_unknown_error_title
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

internal class CustomerToBusinessUseCaseImplTest {

    private val fakeClient = FakeMpesaClient()
    private val useCase: CustomerToBusinessUseCase =
        CustomerToBusinessUseCaseImpl(mpesaClient = fakeClient)

    @Test
    fun `Given API error result When invoking use case Then returns unknown error`() = runTest {
        // Given
        fakeClient.result = CustomerToBusiness.Result.ApiError(error = "boom")

        // When
        val result =
            useCase(
                transactionReference = "T123",
                amount = "1000",
                thirdPartyReference = "321",
                customerMsisdn = "258841234567",
            )

        // Then
        val unknown = assertIs<CustomerToBusinessTransactionResult.UnknownError>(result)
        assertEquals(expected = "boom", actual = unknown.error)
        assertEquals(null, unknown.customerToBusinessTransactionResponse)
        assertEquals(Res.string.transaction_unknown_error_title, unknown.titleRes)
    }

    @Test
    fun `Given success response code When invoking use case Then returns successful transaction`() =
        runTest {
            // Given
            val response = responseWithCode(MpesaResponseCodes.SUCCESS)
            fakeClient.result = CustomerToBusiness.Result.Success(response)

            // When
            val result = useCase("T123", "1000", "321", "258841234567")

            // Then
            val success =
                assertIs<CustomerToBusinessTransactionResult.SuccessfulTransaction>(result)
            assertEquals(
                response.toCustomerToBusinessTransactionResponse(),
                success.customerToBusinessTransactionResponse
            )
            assertEquals(Res.string.transaction_success_title, success.titleRes)
        }

    @Test
    fun `Given internal error code When invoking use case Then returns internal error`() =
        runTest {
            // Given
            val response = responseWithCode(MpesaResponseCodes.INTERNAL_ERROR)
            fakeClient.result = CustomerToBusiness.Result.Success(response)

            // When
            val result = useCase(
                transactionReference = "T123",
                amount = "1000",
                thirdPartyReference = "321",
                customerMsisdn = "258841234567",
            )

            // Then
            val internal = assertIs<CustomerToBusinessTransactionResult.InternalError>(result)
            assertEquals(
                expected = response.toCustomerToBusinessTransactionResponse(),
                actual = internal.customerToBusinessTransactionResponse,
            )
            assertEquals(
                expected = Res.string.transaction_internal_error_title,
                actual = internal.titleRes,
            )
        }

    @Test
    fun `Given cancelled code When invoking use case Then returns cancelled transaction`() =
        runTest {
            // Given
            val response = responseWithCode(MpesaResponseCodes.CANCELLED_BY_CUSTOMER)
            fakeClient.result = CustomerToBusiness.Result.Success(response)

            // When
            val result = useCase("T123", "1000", "321", "258841234567")

            // Then
            val cancelled =
                assertIs<CustomerToBusinessTransactionResult.CancelledTransaction>(result)
            assertEquals(
                expected = response.toCustomerToBusinessTransactionResponse(),
                actual = cancelled.customerToBusinessTransactionResponse
            )
            assertEquals(
                expected = Res.string.transaction_cancelled_title,
                actual = cancelled.titleRes,
            )
        }

    @Test
    fun `Given timeout code When invoking use case Then returns timeout transaction`() = runTest {
        // Given
        val response = responseWithCode(MpesaResponseCodes.REQUEST_TIMEOUT)
        fakeClient.result = CustomerToBusiness.Result.Success(response)

        // When
        val result = useCase(
            transactionReference = "T123",
            amount = "1000",
            thirdPartyReference = "321",
            customerMsisdn = "258841234567",
        )

        // Then
        val timeout = assertIs<CustomerToBusinessTransactionResult.TimeoutTransaction>(result)
        assertEquals(
            expected = response.toCustomerToBusinessTransactionResponse(),
            actual = timeout.customerToBusinessTransactionResponse
        )
        assertEquals(
            expected = Res.string.transaction_timeout_title,
            actual = timeout.titleRes,
        )
    }

    @Test
    fun `Given invalid amount code When invoking use case Then returns invalid amount`() =
        runTest {
            // Given
            val response = responseWithCode(MpesaResponseCodes.INVALID_AMOUNT)
            fakeClient.result = CustomerToBusiness.Result.Success(response)

            // When
            val result = useCase(
                transactionReference = "T123",
                amount = "1000",
                thirdPartyReference = "321",
                customerMsisdn = "258841234567",
            )

            // Then
            val invalidAmount = assertIs<CustomerToBusinessTransactionResult.InvalidAmount>(result)
            assertEquals(
                expected = response.toCustomerToBusinessTransactionResponse(),
                actual = invalidAmount.customerToBusinessTransactionResponse,
            )
            assertEquals(Res.string.transaction_invalid_amount_title, invalidAmount.titleRes)
        }

    @Test
    fun `Given insufficient balance code When invoking use case Then returns insufficient balance`() =
        runTest {
            // Given
            val response = responseWithCode(MpesaResponseCodes.INSUFFICIENT_BALANCE)
            fakeClient.result = CustomerToBusiness.Result.Success(response)

            // When
            val result = useCase("T123", "1000", "321", "258841234567")

            // Then
            val insufficient =
                assertIs<CustomerToBusinessTransactionResult.InsufficientBalance>(result)
            assertEquals(
                expected = response.toCustomerToBusinessTransactionResponse(),
                actual = insufficient.customerToBusinessTransactionResponse,
            )
            assertEquals(
                expected = Res.string.transaction_insufficient_balance_title,
                actual = insufficient.titleRes,
            )
        }

    @Test
    fun `Given invalid msisdn code When invoking use case Then returns invalid msisdn`() =
        runTest {
            // Given
            val response = responseWithCode(MpesaResponseCodes.INVALID_MSISDN)
            fakeClient.result = CustomerToBusiness.Result.Success(response)

            // When
            val result = useCase("T123", "1000", "321", "258841234567")

            // Then
            val invalidMsisdn = assertIs<CustomerToBusinessTransactionResult.InvalidMsisdn>(result)
            assertEquals(
                expected = response.toCustomerToBusinessTransactionResponse(),
                actual = invalidMsisdn.customerToBusinessTransactionResponse,
            )
            assertEquals(
                expected = Res.string.transaction_invalid_msisdn_title,
                actual = invalidMsisdn.titleRes,
            )
        }

    @Test
    fun `Given unhandled code When invoking use case Then returns failed`() = runTest {
        // Given
        val response = responseWithCode(MpesaResponseCodes.TRANSACTION_FAILED)
        fakeClient.result = CustomerToBusiness.Result.Success(response)

        // When
        val result = useCase(
            transactionReference = "T123",
            amount = "1000",
            thirdPartyReference = "321",
            customerMsisdn = "258841234567",
        )

        // Then
        val failed = assertIs<CustomerToBusinessTransactionResult.Failed>(result)
        assertEquals(
            expected = response.toCustomerToBusinessTransactionResponse(),
            actual = failed.customerToBusinessTransactionResponse,
        )
        assertEquals(Res.string.transaction_failed_title, failed.titleRes)
    }

    private fun responseWithCode(code: String) =
        CustomerToBusiness.Response(
            conversationId = "conversation",
            transactionID = "transaction",
            description = "description",
            code = code,
            thirdPartyReference = "third",
        )

    private class FakeMpesaClient : MpesaClient {
        var result: CustomerToBusiness.Result =
            CustomerToBusiness.Result.ApiError(error = null)

        override suspend fun performCustomerToBusinessTransaction(
            transactionReference: String,
            amount: String,
            thirdPartyReference: String,
            customerMsisdn: String,
        ): CustomerToBusiness.Result = result
    }
}
