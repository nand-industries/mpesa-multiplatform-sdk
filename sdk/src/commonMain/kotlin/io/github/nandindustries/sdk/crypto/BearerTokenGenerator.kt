package io.github.nandindustries.sdk.crypto

import io.github.nandindustries.sdk.config.MpesaMultiplatformSdkInitializer

internal object BearerTokenGenerator {
    private const val BEARER_PREFIX = "Bearer "

    fun generateBearerToken(apiKey: String, base64PublicKey: String): String {
        val helper = MpesaMultiplatformSdkInitializer.getConfig().rsaEncryptHelper
        val encrypted = helper?.encrypt(apiKey, base64PublicKey)
            ?: encrypt(apiKey, base64PublicKey)
        val bearerToken = "$BEARER_PREFIX$encrypted"
        return bearerToken
    }
}