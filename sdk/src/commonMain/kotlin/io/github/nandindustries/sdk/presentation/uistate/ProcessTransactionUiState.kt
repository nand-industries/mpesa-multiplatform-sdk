package io.github.nandindustries.sdk.presentation.uistate

import io.github.nandindustries.sdk.resources.Res
import io.github.nandindustries.sdk.resources.transaction_processing_info
import io.github.nandindustries.sdk.resources.transaction_processing_subtitle
import io.github.nandindustries.sdk.resources.transaction_processing_title
import org.jetbrains.compose.resources.StringResource

sealed class TransactionState(
    open val title: StringResource,
    open val subtitle: StringResource,
    open val infoText: StringResource,
    open val animationPath: String,
) {
    data object Processing : TransactionState(
        title = Res.string.transaction_processing_title,
        subtitle = Res.string.transaction_processing_subtitle,
        infoText = Res.string.transaction_processing_info,
        animationPath = "files/lottie_processing_transaction.json",
    )

    data class Concluded(
        override val title: StringResource,
        override val subtitle: StringResource,
        override val infoText: StringResource,
        override val animationPath: String,
    ) : TransactionState(
        title = title,
        subtitle = subtitle,
        infoText = infoText,
        animationPath = animationPath,
    )
}
