object ProjectConfigurations {
    object MpesaMultiplatformSdk {
        const val versionName = "1.0.0"
        const val packageOfResClass = "io.github.nandindustries.sdk.resources"
        const val namespace = "io.github.nandindustries.android"
        const val baseName = "sdk"
        const val minSdk = 21
        const val compileSdk = 36

        object Publishing {
            const val artifactId = "mpesa-multiplatform-sdk"
            const val name = "M-Pesa Multiplatform SDK"

            const val group = "io.github.nand-industries"
            const val description =
                "Compose Multiplatform SDK for interacting with Vodacom M-Pesa APIs."
            const val inceptionYear = "2025"
            const val repositorySlug = "nand-industries/mpesa-multiplatform-sdk"

            object License {
                const val name = "The Apache License, Version 2.0"
                const val url = "https://www.apache.org/licenses/LICENSE-2.0.txt"
                const val distribution = "https://www.apache.org/licenses/LICENSE-2.0.txt"
            }
        }
    }

    object Compiler {
        const val jdkVersion = 21
    }
}
