package io.github.nandindustries.sdk.data.remote.mpesa.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

internal object CustomerToBusiness {
    const val REQUEST_PATH = "c2bPayment/singleStage/"

    @Serializable
    data class Request(
        val transactionReference: String,
        val amount: String,
        val thirdPartyReference: String,
        val customerMsisdn: String,
        val serviceProviderCode: String,
    )

    @Serializable
    data class Response(
        @SerialName("output_ConversationID") val conversationId: String,
        @SerialName("output_TransactionID") val transactionID: String,
        @SerialName("output_ResponseDesc") val description: String,
        @SerialName("output_ResponseCode") val code: String,
        @SerialName("output_ThirdPartyReference") val thirdPartyReference: String,
    )

    sealed interface Result {
        data class Success(
            val response: Response,
        ) : Result

        data class ApiError(
            val error: String?,
        ) : Result
    }
}
