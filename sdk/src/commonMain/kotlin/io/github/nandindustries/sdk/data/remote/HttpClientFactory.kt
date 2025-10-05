package io.github.nandindustries.sdk.data.remote

import io.ktor.client.HttpClient

expect class HttpClientFactory() {
    fun create(): HttpClient
}
