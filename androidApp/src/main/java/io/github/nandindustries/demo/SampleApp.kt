package io.github.nandindustries.demo

import android.app.Application
import io.github.nandindustries.sdk.config.MpesaMultiplatformSdkInitializer

class SampleApp : Application() {
    override fun onCreate() {
        MpesaMultiplatformSdkInitializer.init(
            productionApiKey = BuildConfig.MPESA_PRODUCTION_API_KEY,
            developmentApiKey = BuildConfig.MPESA_DEVELOPMENT_API_KEY,
            publicKey = BuildConfig.MPESA_PUBLIC_KEY,
            serviceProviderCode = "171717",
            isProduction = false,
        )
        super.onCreate()
    }
}