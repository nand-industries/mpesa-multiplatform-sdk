package io.github.nandindustries.sdk.config

actual val platformSdkInitializationGuideMessage: String = """
    This initialization MUST be done early in your application's lifecycle, typically in the entry point of your iOS application, such as in the App struct or the AppDelegate's didFinishLaunchingWithOptions method.`
        @main
        struct iOSApp: App {
            init() {
                MpesaMultiplatformSdkInitializer.library.init(...) // Call init here
                // ... rest of your code
            }
        }
""".trimIndent()