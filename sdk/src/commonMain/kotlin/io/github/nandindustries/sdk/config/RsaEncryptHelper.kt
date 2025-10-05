package io.github.nandindustries.sdk.config

/**
 * Platform-specific helper interface for when RSA encryption cannot be written in platform source sets for now.
 */
interface RsaEncryptHelper {

    /**
     * Encrypts the provided API key using RSA encryption with the given public key.
     *
     * @param apiKey The API key to encrypt.
     * @param base64PublicKey The public key in Base64 format used for encryption.
     * @return The encrypted API key as a Base64-encoded string.
     */
    fun encrypt(apiKey: String, base64PublicKey: String): String
}
