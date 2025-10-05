package io.github.nandindustries.sdk.crypto

import android.util.Base64
import java.security.KeyFactory
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher

actual fun encrypt(apiKey: String, base64PublicKey: String): String {
    val keyFactory = KeyFactory.getInstance("RSA")
    val decodedKey = Base64.decode(base64PublicKey, Base64.NO_WRAP)
    val publicKeySpec = X509EncodedKeySpec(decodedKey)
    val publicKey = keyFactory.generatePublic(publicKeySpec)

    val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
    cipher.init(Cipher.ENCRYPT_MODE, publicKey)

    val encrypted = cipher.doFinal(apiKey.toByteArray(Charsets.UTF_8))
    return Base64.encodeToString(encrypted, Base64.NO_WRAP)
}
