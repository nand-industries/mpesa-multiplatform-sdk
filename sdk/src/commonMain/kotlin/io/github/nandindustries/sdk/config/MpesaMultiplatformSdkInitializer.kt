package io.github.nandindustries.sdk.config

import io.github.nandindustries.sdk.MpesaMultiplatformSdk
import io.github.nandindustries.sdk.di.DefaultSdkDependencyContainer

object MpesaMultiplatformSdkInitializer {
    private lateinit var config: Config

    fun init(
        productionApiKey: String,
        developmentApiKey: String,
        publicKey: String,
        isProduction: Boolean,
        serviceProviderCode: String,
        rsaEncryptHelper: RsaEncryptHelper? = null,
    ) {
        config = Config(
            productionApiKey = productionApiKey,
            developmentApiKey = developmentApiKey,
            publicKey = publicKey,
            isProduction = isProduction,
            serviceProviderCode = serviceProviderCode,
            rsaEncryptHelper = rsaEncryptHelper,
        )
        MpesaMultiplatformSdk.replaceDependencyContainer(DefaultSdkDependencyContainer())
    }

    internal fun getConfig(): Config {
        if (!this::config.isInitialized) {
            throw SdkNotInitializedException()
        }
        return config
    }

    internal data class Config(
        val productionApiKey: String,
        val developmentApiKey: String,
        val publicKey: String,
        val serviceProviderCode: String,
        val isProduction: Boolean,
        val rsaEncryptHelper: RsaEncryptHelper?,
    )

    internal class SdkNotInitializedException : Exception(exceptionMessage)

    private val exceptionMessage: String = """
    M-Pesa Multiplatform Sdk is not initialized.

    Before using any payment-related functionality, you MUST call the `MpesaMultiplatformSdkInitializer.init()` method.

    Correct usage:

    MpesaMultiplatformSdkInitializer.init(
        productionApiKey = "YOUR_PRODUCTION_API_KEY",
        developmentApiKey = "YOUR_DEVELOPMENT_API_KEY",
        publicKey = "YOUR_PUBLIC_KEY",
        serviceProviderCode = "YOUR_SERVICE_PROVIDER_CODE",
        isProduction = true // Or your logic to determine the environment
    )

    """ + platformSdkInitializationGuideMessage + """
        See the library's documentation for more details and platform-specific examples: https://github.com/nand-industries/mpesa-multiplatform-sdk
    """.trimIndent()
}
