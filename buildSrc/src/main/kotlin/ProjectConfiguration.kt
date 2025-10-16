object ProjectConfiguration {
    object MpesaMultiplatformSdk {
        const val versionName = "1.0.0"
        const val packageOfResClass = "io.github.nandindustries.sdk.resources"
        const val namespace = "io.github.nandindustries.android"
        const val baseName = "MpesaMultiplatformSdk"
        const val minSdk = 21
        const val compileSdk = 36
        const val documentation = "https://github.com/nand-industries/mpesa-multiplatform-sdk"

        object Publishing {
            const val artifactId = "mpesa-multiplatform-sdk"
            const val name = "M-Pesa Multiplatform SDK"
            const val group = "io.github.nand-industries"
            const val description =
                "Compose Multiplatform SDK for interacting with Vodacom M-Pesa APIs."
            const val inceptionYear = "2025"
            const val repositorySlug = "nand-industries/mpesa-multiplatform-sdk"
            const val cocoaPodName = "MpesaMultiplatformSdkCocoaPod"

            object License {
                const val name = "The Apache License, Version 2.0"
                const val licenseType = "{ :type => 'Apache-2.0' }"
                const val url = "https://www.apache.org/licenses/LICENSE-2.0.txt"
                const val distribution = "https://www.apache.org/licenses/LICENSE-2.0.txt"
            }
        }

        object iOS {
            const val deploymentTarget = "12.0"
        }
    }

    object Compiler {
        const val jdkVersion = 21
    }
}
