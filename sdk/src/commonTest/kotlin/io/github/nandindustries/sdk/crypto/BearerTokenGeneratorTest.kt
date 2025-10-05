package io.github.nandindustries.sdk.crypto

import io.github.nandindustries.sdk.config.MpesaMultiplatformSdkInitializer
import io.github.nandindustries.sdk.config.RsaEncryptHelper
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class BearerTokenGeneratorTest {

    @Test
    fun `Given helper provided When generating token Then uses helper encryption`() = runTest {
        // Given
        val helper = RecordingEncryptHelper()
        MpesaMultiplatformSdkInitializer.init(
            productionApiKey = "prod",
            developmentApiKey = "dev",
            publicKey = "public",
            isProduction = false,
            serviceProviderCode = "code",
            rsaEncryptHelper = helper,
        )

        // When
        val token =
            BearerTokenGenerator.generateBearerToken(apiKey = "secret", base64PublicKey = "public")

        // Then
        assertTrue(helper.invoked)
        assertEquals("secret", helper.apiKey)
        assertEquals("Bearer encrypted-secret", token)
    }

    private class RecordingEncryptHelper : RsaEncryptHelper {
        var invoked: Boolean = false
        var apiKey: String? = null

        override fun encrypt(apiKey: String, base64PublicKey: String): String {
            invoked = true
            this.apiKey = apiKey
            return "encrypted-$apiKey"
        }
    }
}
