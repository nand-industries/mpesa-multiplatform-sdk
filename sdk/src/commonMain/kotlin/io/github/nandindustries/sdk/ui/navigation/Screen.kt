package io.github.nandindustries.sdk.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
internal data object InputTransactionDetailsScreenDestination

@Serializable
internal data class ProcessTransactionScreenDestination(
    val amount: String,
    val transactionReference: String,
    val thirdPartyReference: String,
    val customerMsisdn: String,
)