package io.github.nandindustries.sdk.crypto

import kotlinx.cinterop.ExperimentalForeignApi

@OptIn(ExperimentalForeignApi::class)
actual fun encrypt(apiKey: String, base64PublicKey: String): String {
    throw Exception("Not implemented for iOS yet, please use the MpesaMultiplatformSdkInitializer to initialize the SDK with a valid RSA Encrypt Helper.")
}
