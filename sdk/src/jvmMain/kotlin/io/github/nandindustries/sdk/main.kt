package io.github.nandindustries.sdk

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import io.github.nandindustries.sdk.config.MpesaMultiplatformSdkInitializer
import io.github.nandindustries.sdk.ui.navigation.MpesaMultiplatformNavigationGraph
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
fun main() = application {
    // Initialize SDK
    MpesaMultiplatformSdkInitializer.init(
        productionApiKey = "none",
        developmentApiKey = "p4puyzwstpw7z663g5dn7ygbs6k0ls03",
        publicKey = "MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAmptSWqV7cGUUJJhUBxsMLonux24u+FoTlrb+4Kgc6092JIszmI1QUoMohaDDXSVueXx6IXwYGsjjWY32HGXj1iQhkALXfObJ4DqXn5h6E8y5/xQYNAyd5bpN5Z8r892B6toGzZQVB7qtebH4apDjmvTi5FGZVjVYxalyyQkj4uQbbRQjgCkubSi45Xl4CGtLqZztsKssWz3mcKncgTnq3DHGYYEYiKq0xIj100LGbnvNz20Sgqmw/cH+Bua4GJsWYLEqf/h/yiMgiBbxFxsnwZl0im5vXDlwKPw+QnO2fscDhxZFAwV06bgG0oEoWm9FnjMsfvwm0rUNYFlZ+TOtCEhmhtFp+Tsx9jPCuOd5h2emGdSKD8A6jtwhNa7oQ8RtLEEqwAn44orENa1ibOkxMiiiFpmmJkwgZPOG/zMCjXIrrhDWTDUOZaPx/lEQoInJoE2i43VN/HTGCCw8dKQAwg0jsEXau5ixD0GUothqvuX3B9taoeoFAIvUPEq35YulprMM7ThdKodSHvhnwKG82dCsodRwY428kg2xM/UjiTENog4B6zzZfPhMxFlOSFX4MnrqkAS+8Jamhy1GgoHkEMrsT5+/ofjCx0HjKbT5NuA2V/lmzgJLl3jIERadLzuTYnKGWxVJcGLkWXlEPYLbiaKzbJb2sYxt+Kt5OxQqC1MCAwEAAQ==",
        serviceProviderCode = "171717",
        isProduction = false,
    )

    Window(
        onCloseRequest = ::exitApplication,
        title = "MpesaMultiplatformDesktopDemo",
    ) {
        MpesaMultiplatformNavigationGraph(
            businessName = "Nand Industries",
            businessLogoUrl = "https://avatars.githubusercontent.com/u/191469385?s=200&v=4",
            defaultAmount = "500",
            defaultPhoneNumber = "851234567",
            transactionReference = "TESTDESKTOP12345",
            thirdPartyReference = Uuid.random().toString().take(5),
            onDismissInputTransactionDetailsStep = {},
            modifier = Modifier.fillMaxSize(),
            darkTheme = false,
        )
    }
}
