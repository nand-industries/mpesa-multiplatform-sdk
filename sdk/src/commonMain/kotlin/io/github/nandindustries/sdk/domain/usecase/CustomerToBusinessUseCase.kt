package io.github.nandindustries.sdk.domain.usecase

import io.github.nandindustries.sdk.data.remote.mpesa.model.MpesaResponseCodes
import io.github.nandindustries.sdk.domain.entity.CustomerToBusinessTransactionResult
import io.github.nandindustries.sdk.domain.entity.toCustomerToBusinessTransactionResponse
import io.github.nandindustries.sdk.data.remote.mpesa.MpesaClient
import io.github.nandindustries.sdk.data.remote.mpesa.model.CustomerToBusiness

internal fun interface CustomerToBusinessUseCase {

    suspend operator fun invoke(
        transactionReference: String,
        amount: String,
        thirdPartyReference: String,
        customerMsisdn: String,
    ): CustomerToBusinessTransactionResult
}

internal class CustomerToBusinessUseCaseImpl(
    private val mpesaClient: MpesaClient,
) : CustomerToBusinessUseCase {

    override suspend fun invoke(
        transactionReference: String,
        amount: String,
        thirdPartyReference: String,
        customerMsisdn: String,
    ): CustomerToBusinessTransactionResult =
        when (val transactionResult = mpesaClient.performCustomerToBusinessTransaction(
            transactionReference = transactionReference,
            amount = amount,
            thirdPartyReference = thirdPartyReference,
            customerMsisdn = customerMsisdn,
        )) {
            is CustomerToBusiness.Result.ApiError -> CustomerToBusinessTransactionResult.UnknownError(transactionResult.error)
            is CustomerToBusiness.Result.Success -> handleSuccess(transactionResult)
        }

    private fun handleSuccess(
        transactionResult: CustomerToBusiness.Result.Success,
    ): CustomerToBusinessTransactionResult = when (transactionResult.response.code) {
        MpesaResponseCodes.SUCCESS -> CustomerToBusinessTransactionResult.SuccessfulTransaction(
            customerToBusinessTransactionResponse = transactionResult.response.toCustomerToBusinessTransactionResponse(),
        )

        MpesaResponseCodes.INTERNAL_ERROR -> CustomerToBusinessTransactionResult.InternalError(
            customerToBusinessTransactionResponse = transactionResult.response.toCustomerToBusinessTransactionResponse(),
        )

        MpesaResponseCodes.CANCELLED_BY_CUSTOMER -> CustomerToBusinessTransactionResult.CancelledTransaction(
            customerToBusinessTransactionResponse = transactionResult.response.toCustomerToBusinessTransactionResponse(),
        )

        MpesaResponseCodes.REQUEST_TIMEOUT -> CustomerToBusinessTransactionResult.TimeoutTransaction(
            customerToBusinessTransactionResponse = transactionResult.response.toCustomerToBusinessTransactionResponse(),
        )

        MpesaResponseCodes.INVALID_AMOUNT -> CustomerToBusinessTransactionResult.InvalidAmount(
            customerToBusinessTransactionResponse = transactionResult.response.toCustomerToBusinessTransactionResponse(),
        )

        MpesaResponseCodes.INSUFFICIENT_BALANCE -> CustomerToBusinessTransactionResult.InsufficientBalance(
            customerToBusinessTransactionResponse = transactionResult.response.toCustomerToBusinessTransactionResponse(),
        )

        MpesaResponseCodes.INVALID_MSISDN -> CustomerToBusinessTransactionResult.InvalidMsisdn(
            customerToBusinessTransactionResponse = transactionResult.response.toCustomerToBusinessTransactionResponse(),
        )

        else -> CustomerToBusinessTransactionResult.Failed(
            customerToBusinessTransactionResponse = transactionResult.response.toCustomerToBusinessTransactionResponse(),
        )
    }
}
