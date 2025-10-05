import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.ComposeUIViewController
import io.github.nandindustries.sdk.ui.navigation.MpesaMultiplatformNavigationGraph
import platform.UIKit.UIViewController

fun MainViewController(
    businessName: String,
    businessLogoUrl: String?,
    transactionReference: String,
    thirdPartyReference: String,
    defaultAmount: String?,
    defaultPhoneNumber: String?,
    onDismissInputTransactionDetailsStep: () -> Unit = {},
): UIViewController = ComposeUIViewController {
    MpesaMultiplatformNavigationGraph(
        businessName = businessName,
        businessLogoUrl = businessLogoUrl,
        transactionReference = transactionReference,
        thirdPartyReference = thirdPartyReference,
        defaultAmount = defaultAmount,
        defaultPhoneNumber = defaultPhoneNumber,
        onDismissInputTransactionDetailsStep = onDismissInputTransactionDetailsStep,
        modifier = Modifier.fillMaxSize(),
    )
}
