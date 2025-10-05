package io.github.nandindustries.sdk.ui.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import kotlin.math.max
import kotlin.text.iterator

class CurrencyAmountInputVisualTransformation(
    private val fixedCursorAtTheEnd: Boolean = true,
    private val numberOfDecimals: Int = 2,
    // maxValue in minor units (e.g. for 2 decimals, "2500000" represents 25,000.00)
    private val maxValue: Long? = null
) : VisualTransformation {
    private val thousandsSeparator: Char = ','
    private val decimalSeparator: Char = '.'
    private val zero: Char = '0'

    override fun filter(text: AnnotatedString): TransformedText {
        // Use the underlying digits; if a maximum is provided, clamp the value.
        var inputText = text.text

        if (!inputText.all { it.isDigit() }) {
            inputText = inputText.filter { it.isDigit() }
        }

        maxValue?.let { max ->
            val inputNumber = inputText.toLongOrNull() ?: 0L
            if (inputNumber > max) {
                inputText = max.toString()
            }
        }

        val intPart = inputText
            .dropLast(numberOfDecimals)
            .reversed()
            .chunked(3)
            .joinToString(thousandsSeparator.toString())
            .reversed()
            .ifEmpty { zero.toString() }

        val fractionPart = inputText.takeLast(numberOfDecimals).let {
            if (it.length != numberOfDecimals) {
                List(numberOfDecimals - it.length) { zero }
                    .joinToString("") + it
            } else {
                it
            }
        }

        val formattedNumber = "$intPart$decimalSeparator$fractionPart"

        val newText = AnnotatedString(
            text = formattedNumber,
            spanStyles = text.spanStyles,
            paragraphStyles = text.paragraphStyles
        )

        val offsetMapping = if (fixedCursorAtTheEnd) {
            FixedCursorOffsetMapping(
                contentLength = inputText.length,
                formattedContentLength = formattedNumber.length
            )
        } else {
            MovableCursorOffsetMapping(
                unmaskedText = inputText,
                maskedText = formattedNumber,
                decimalDigits = numberOfDecimals
            )
        }

        return TransformedText(newText, offsetMapping)
    }

    private class FixedCursorOffsetMapping(
        private val contentLength: Int,
        private val formattedContentLength: Int,
    ) : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int = formattedContentLength
        override fun transformedToOriginal(offset: Int): Int = contentLength
    }

    private class MovableCursorOffsetMapping(
        private val unmaskedText: String,
        private val maskedText: String,
        private val decimalDigits: Int
    ) : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int =
            when {
                unmaskedText.length <= decimalDigits ->
                    maskedText.length - (unmaskedText.length - offset)

                else -> offset + offsetMaskCount(offset, maskedText)
            }

        override fun transformedToOriginal(offset: Int): Int =
            when {
                unmaskedText.length <= decimalDigits ->
                    max(unmaskedText.length - (maskedText.length - offset), 0)

                else -> offset - maskedText.take(offset).count { !it.isDigit() }
            }

        private fun offsetMaskCount(offset: Int, maskedText: String): Int {
            var maskOffsetCount = 0
            var dataCount = 0
            for (maskChar in maskedText) {
                if (!maskChar.isDigit()) {
                    maskOffsetCount++
                } else if (++dataCount > offset) {
                    break
                }
            }
            return maskOffsetCount
        }
    }
}