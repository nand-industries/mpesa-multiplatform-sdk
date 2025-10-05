package io.github.nandindustries.sdk.ui.navigation.model

import kotlinx.serialization.Serializable

@Serializable
data class C2BTransactionData(
    val amount: String,
    val transactionReference: String,
    val thirdPartyReference: String,
    val customerMsisdn: String,
)
