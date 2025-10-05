package io.github.nandindustries.sdk.data.remote.mpesa

import io.github.nandindustries.sdk.config.MpesaMultiplatformSdkInitializer
import io.github.nandindustries.sdk.crypto.BearerTokenGenerator
import io.github.nandindustries.sdk.data.remote.HttpClientFactory
import io.github.nandindustries.sdk.data.remote.mpesa.model.CustomerToBusiness
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.http.encodedPath
import io.ktor.http.isSuccess

internal interface MpesaClient {
    suspend fun performCustomerToBusinessTransaction(
        transactionReference: String,
        amount: String,
        thirdPartyReference: String,
        customerMsisdn: String,
    ): CustomerToBusiness.Result
}

internal class MpesaClientImpl(
    private val httpClientFactory: HttpClientFactory,
) : MpesaClient {

    override suspend fun performCustomerToBusinessTransaction(
        transactionReference: String,
        amount: String,
        thirdPartyReference: String,
        customerMsisdn: String,
    ): CustomerToBusiness.Result {
        val result = try {
            httpClientFactory.create().post {
                buildMpesaRequestUrl(CustomerToBusiness.REQUEST_PATH)
                contentType(ContentType.Application.Json)
                buildMpesaRequestHeaders()
                setBody(
                    CustomerToBusiness.Request(
                        transactionReference = transactionReference,
                        amount = amount.toDouble().toString(),
                        thirdPartyReference = thirdPartyReference,
                        customerMsisdn = customerMsisdn,
                        serviceProviderCode = MpesaMultiplatformSdkInitializer
                            .getConfig()
                            .serviceProviderCode,
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return CustomerToBusiness.Result.ApiError(e.message)
        }
        return if (result.status.isSuccess()) {
            CustomerToBusiness.Result.Success(result.body<CustomerToBusiness.Response>())
        } else {
            CustomerToBusiness.Result.ApiError(result.status.value.toString())
        }
    }

    private fun HttpRequestBuilder.buildMpesaRequestHeaders() {
        headers {
            append(HttpHeaders.Origin, MPESA_CORS)
            val bearer = BearerTokenGenerator.generateBearerToken(
                apiKey = if (MpesaMultiplatformSdkInitializer.getConfig().isProduction) {
                    MpesaMultiplatformSdkInitializer.getConfig().productionApiKey
                } else {
                    MpesaMultiplatformSdkInitializer.getConfig().developmentApiKey
                },
                base64PublicKey = MpesaMultiplatformSdkInitializer.getConfig().publicKey,
            )
            append(
                name = HttpHeaders.Authorization,
                value = bearer,
            )
        }
    }

    private fun HttpRequestBuilder.buildMpesaRequestUrl(requestPath: String) {
        url {
            protocol = URLProtocol.HTTPS
            host = getMpesaHost()
            port = MPESA_REQUESTS_PORT
            encodedPath = getMpesaRequestEncodedPath(requestPath)
        }
    }

    private fun getMpesaHost(): String =
        if (MpesaMultiplatformSdkInitializer.getConfig().isProduction) {
            MPESA_PRODUCTION_HOST
        } else {
            MPESA_DEVELOPMENT_HOST
        }

    private fun getMpesaRequestEncodedPath(requestPath: String): String =
        MPESA_REQUESTS_PATH + requestPath

    companion object {
        private const val MPESA_CORS = "developer.mpesa.vm.co.mz"
        private const val MPESA_REQUESTS_PORT = 18352
        private const val MPESA_PRODUCTION_HOST = "api.vm.co.mz"
        private const val MPESA_DEVELOPMENT_HOST = "api.sandbox.vm.co.mz"
        private const val MPESA_REQUESTS_PATH = "/ipg/v1x/"
    }
}
