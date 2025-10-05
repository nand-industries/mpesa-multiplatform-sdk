package io.github.nandindustries.sdk.domain.entity

import io.github.nandindustries.sdk.data.remote.mpesa.model.CustomerToBusiness

data class CustomerToBusinessTransactionResponse(
    val conversationId: String,
    val transactionId: String,
    val description: String,
    val code: String,
    val thirdPartyReference: String,
)

internal fun CustomerToBusiness.Response.toCustomerToBusinessTransactionResponse() =
    CustomerToBusinessTransactionResponse(
        conversationId = conversationId,
        transactionId = transactionID,
        description = description,
        code = code,
        thirdPartyReference = thirdPartyReference,
    )
