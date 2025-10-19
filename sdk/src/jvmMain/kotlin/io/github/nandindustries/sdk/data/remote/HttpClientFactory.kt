package io.github.nandindustries.sdk.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.engine.jetty.jakarta.Jetty
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json

actual class HttpClientFactory {
    actual fun create(): HttpClient = HttpClient(Jetty) {
        install(ContentNegotiation) {
            json()
        }
    }
}
