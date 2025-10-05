package io.github.nandindustries.sdk.config

actual val platformSdkInitializationGuideMessage: String = """
    This initialization must be done early in your application's lifecycle, typically in your `Application` class's `onCreate()` method:

    class MyApplication : Application() {
        override fun onCreate() {
            super.onCreate()
            MpesaMultiplatformSdkInitializer.init(...) // Call init here
            // ... rest of your onCreate()
        }
    }

""".trimIndent()