[![Maven Central Version](https://img.shields.io/maven-central/v/io.github.nand-industries/mpesa-multiplatform-sdk?color=orange)](https://central.sonatype.com/artifact/io.github.nand-industries/mpesa-multiplatform-sdk)
[![Kotlin](https://img.shields.io/badge/kotlin-2.2.21-blue.svg?logo=kotlin)](http://kotlinlang.org)
[![Compose](https://img.shields.io/badge/compose-1.9.3-blue.svg?logo=jetpackcompose)](https://www.jetbrains.com/lp/compose-multiplatform)
[![License](https://img.shields.io/badge/License-Apache%202.0-green.svg)](https://opensource.org/licenses/Apache-2.0)

# M‑Pesa Multiplatform SDK

> **Unofficial, open‑source Compose Multiplatform SDK** that streamlines checkout (C2B) integration
> with the **Vodacom Mozambique M‑Pesa API** for **Android** and **iOS** (with room to extend to Web, Desktop and JVM Systems).

<p align="center">
  <img src="/documentation/logo_center_mpesa_multiplatform_sdk.png" alt="M-Pesa Multiplatform SDK" width="200"/>
</p>

It wraps authentication, HTTP orchestration, UI flows, and reactive transaction reporting behind a
single API so Android and iOS apps share the same logic **and** presentation layer.
This project also serves as an experiment on [Compose Multiplatform](https://www.jetbrains.com/compose-multiplatform/)
capabilities for building libraries targeting native mobile platforms.

If you plan to go live, read the official [Vodacom M-Pesa API docs](https://developer.mpesa.vm.co.mz/documentation/) carefully and validate your flows in sandbox
and production.
Also, make sure to take a look at [M-Pesa Requirements Checker](https://mpesa.ivanbila.dev/).

---

## Table of contents

1. [Quick start](#quick-start)
2. [Usage](#usage)
    * [Get credentials](#Get-credentials)
    * [Install the dependency](#install-the-dependency)
    * [Initialize the SDK](#initialize-the-sdk)
    * [Start the checkout flow (C2B)](#start-the-checkout-flow-c2b)
    * [Observe transaction results](#observe-transaction-results)
3. [Security considerations](#security-considerations)
4. [Build & run](#build--run)
5. [Project structure](#project-structure)
6. [Contributing](#contributing)
7. [License](#license)

---

## Usage
In order to integrate this SDK to your mobile application you need to do the following steps:

### Get credentials

You need an **API Key** and a **Public Key** to initialize the SDK and connect to the M‑Pesa API.
Create an account and generate credentials in the [Vodacom Developer Portal](https://developer.mpesa.vm.co.mz/accounts/login/?next=/accounts/signup/). Make sure you understand
sandbox vs production behavior before shipping.

### Install the dependency

> **Note:** Distribution is being finalized. The snippets below show how it will look once
> published. Until then, use the included sample apps or consume the SDK via source.

**Gradle (Android)**

```kts
repositories {
    mavenCentral()
}

dependencies {
    implementation("io.github.nand-industries:mpesa-multiplatform-sdk:<version>")
}
```

**SwiftPM (iOS)**

```swift
.package(url: "https://github.com/nand-industries/mpesa-multiplatform-sdk.git", from: "<version>")
```

### Initialize the SDK

Call `MpesaMultiplatformSdkInitializer` **once** before presenting any checkout UI, ideally during
app startup or DI graph creation.

**Android**

```kotlin
import io.github.nandindustries.sdk.config.MpesaMultiplatformSdkInitializer

MpesaMultiplatformSdkInitializer.init(
    productionApiKey = BuildConfig.MPESA_PRODUCTION_API_KEY,
    developmentApiKey = BuildConfig.MPESA_DEVELOPMENT_API_KEY,
    publicKey = BuildConfig.MPESA_PUBLIC_KEY,
    serviceProviderCode = "171717",
    isProduction = BuildConfig.DEBUG.not(),
    // Optional: rsaEncryptHelper = YourAndroidRsaEncryptHelper()
)
```

**iOS**

```swift
import MpesaMultiplatformSdk

MpesaMultiplatformSdkInitializer().doInit(
    productionApiKey: productionApiKey,
    developmentApiKey: developmentApiKey,
    publicKey: publicApiKey,
    isProduction: false,
    serviceProviderCode: "171717",
    rsaEncryptHelper:  /* required on iOS — provide your own */
)
```

> On **Android**, providing a custom RSA helper is optional. On **iOS**, you **must** implement and
> pass an `RsaEncryptHelper`. See the iOS demo app for a minimal implementation.

### Start the checkout flow (C2B)

![Checkout sample](/documentation/mpesa-multiplatform-checkout-experience.png "Checkout experience sample")

The SDK renders an input screen followed by a processing screen and wraps the C2B API under the
hood. You trigger it from your host app.

**Android (Compose)**

```kotlin
import io.github.nandindustries.sdk.ui.navigation.MpesaMultiplatformNavigationGraph

MpesaMultiplatformNavigationGraph(
    businessName = "Sample Shop",
    businessLogoUrl = "yourbusinesslogo.xyz",
    transactionReference = "T123456",
    thirdPartyReference = "12345",
    defaultAmount = "499.50",      // Optional — if omitted (not editable), user must enter
    defaultPhoneNumber = "841234567", // Optional — if omitted (editable), user must enter
    onDismissInputTransactionDetailsStep = { /* Close sheet */ },
)
```

**iOS (SwiftUI/UIKit host)**

```swift
let controller = MainViewController(
    businessName: "Sample Shop",
    businessLogoUrl: "yourbusinesslogo.xyz",
    transactionReference: "T123456",
    thirdPartyReference: "12345",
    defaultAmount: "499.50",
    defaultPhoneNumber: "841234567"
)
present(controller, animated: true)
```

> `MainViewController` delegates rendering to the shared Compose navigation graph, so the UI matches
> Android.

### Observe transaction results

Subscribe to the shared stream to be notified when the user cancels the flow or when M‑Pesa
responds. `CustomerToBusinessUseCase` maps M‑Pesa response codes to strongly typed outcomes with
localized messaging.

**Android**

```kotlin
val transactionStream = MpesaMultiplatformSdk.transactionCompletionStream
lifecycleScope.launch {
    transactionStream.onTransactionCompletionResult().collect { result ->
        when (result) {
            is TransactionCompletionResult.C2BTransactionCompleted -> {
                if (result.result is CustomerToBusinessUseCase.Result.SuccessfulTransaction) {
                    // Handle success
                } else {
                    // Handle failure
                }
            }
            is TransactionCompletionResult.C2BTransactionCancelledBeforeStarted -> {
                // Handle cancellation
            }
        }
    }
}
```

**iOS**

```swift
TransactionCompletionObserverKt.observeTransactionCompletion { result in
    if result is TransactionCompletionResultC2BTransactionCancelledBeforeStarted {
        // Handle cancellation
    } else if let completed = result as? TransactionCompletionResultC2BTransactionCompleted {
        if TransactionCompletionResultExtensionsKt.isSuccessfulC2BTransaction(result: completed) {
            // Handle success
        } else {
            // Handle failure
        }
    }
}
```

---

## Security considerations

* **RSA encryption:** API keys are encrypted with `RSA/ECB/PKCS1Padding` before being sent as bearer
  tokens. You can override via `RsaEncryptHelper` per‑platform.
* **Env isolation:** The SDK switches between sandbox (`api.sandbox.vm.co.mz`) and production (
  `api.vm.co.mz`) via the `isProduction` flag.
* **Input hardening:** Phone numbers are constrained to Vodacom prefixes (84/85) and exactly nine
  digits. Amounts are coerced to configured min/max before submission.
* **Localized responses:** Response codes map to user‑friendly titles/subtitles/descriptions to
  avoid leaking raw API codes.

> Always store API keys securely (encrypted at rest or fetched from your backend) and inject them at
> runtime. **Never** commit real keys.

---

## Build & run

### Prerequisites

* Kotlin **1.9+** with Compose Multiplatform plugin (toolchain 21 configured).
* Android Studio **Giraffe+** or IntelliJ IDEA with KMP support.
* Xcode **15+** for iOS builds.

### Project structure

```
mpesa-multiplatform/
├── androidApp/                 # Android sample (Compose)
├── iosApp/                     # iOS sample project (SwiftUi)
├── sdk/                        # Kotlin Multiplatform library module
│   ├── src/commonMain/         # Shared business logic, UI, resources
│   ├── src/androidMain/        # Android-specific HTTP & crypto wiring
│   └── src/iosMain/            # iOS-specific HTTP & crypto wiring
└── build.gradle.kts
```

### Running the samples

Add these lines to **local secrets** and replace with your values:

* **Android:** `local.properties`
* **iOS:** `iosApp/Configuration/Config.xcconfig`

```properties
MPESA_PRODUCTION_API_KEY=replace
MPESA_DEVELOPMENT_API_KEY=replace
MPESA_PUBLIC_KEY=replace
```

Run:

* Android: `./gradlew :androidApp:installDebug`
* iOS: open `iosApp` in Xcode and run the `iosApp` scheme
* Shared tests: `./gradlew :sdk:check`

---

## Contributing

1. **Check for existing Issues:** Before proposing a new idea or reporting a bug,
   search the project’s issue tracker to ensure no duplicate or similar issues already exist.
   Use relevant keywords and filters to review open and closed issues.
2. **Propose:** Open an issue describing your idea or bug before starting work.
3. **Branching:** Use `feature/<short-description>`, `fix/<short-description>`,
   `chore/<short-description>`, `refactor/<short-description>`
   or `security/<short-description>`from `main`.
4. **Code Style:** Follow Kotlin official style and idiomatic Compose. Use resource strings for
   user‑facing text (`Res.string.*`).
5. **Testing:** Put unit tests under `sdk/src/commonTest` when changing business logic. Run
   `./gradlew :sdk:check` before pushing.
6. **Security:** Never commit real credentials. Use env vars or local secrets.
7. **Verify Samples:** Ensure both sample apps build and run.
8. **PRs:** Include a clear summary, screenshots for UI changes, and note any breaking API changes.
   Ensure CI passes before requesting review.

---

## License

[Apache License](./LICENSE)
