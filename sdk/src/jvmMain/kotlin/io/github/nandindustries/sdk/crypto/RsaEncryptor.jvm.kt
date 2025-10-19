package io.github.nandindustries.sdk.crypto

import java.nio.charset.StandardCharsets
import java.security.KeyFactory
import java.security.spec.X509EncodedKeySpec
import java.util.Base64
import javax.crypto.Cipher

actual fun encrypt(apiKey: String, base64PublicKey: String): String {
    try {
        val keyFactory = KeyFactory.getInstance("RSA")
        val cipher = Cipher.getInstance("RSA")
        val x509PublicKey: ByteArray = Base64.getDecoder().decode(base64PublicKey)
        val publicKeySpec = X509EncodedKeySpec(x509PublicKey)
        val pk = keyFactory.generatePublic(publicKeySpec)
        cipher.init(Cipher.ENCRYPT_MODE, pk)
        val encryptedApiKey: ByteArray? = Base64.getEncoder().encode(
            cipher.doFinal(apiKey.toByteArray(StandardCharsets.UTF_8))
        )
        return "Bearer " + String(encryptedApiKey!!, StandardCharsets.UTF_8)
    } catch (e: Exception) {
        e.printStackTrace()
        return "------"
    }
}
