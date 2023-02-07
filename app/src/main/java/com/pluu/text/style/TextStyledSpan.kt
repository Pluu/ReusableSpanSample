package com.pluu.text.style

import android.graphics.Typeface
import android.text.Spannable
import android.text.TextPaint
import android.text.style.BackgroundColorSpan
import android.text.style.ClickableSpan
import android.text.style.StyleSpan
import android.view.View
import androidx.annotation.ColorInt

data class TextStyledSpan(
    @ColorInt val background: Int? = null,
    val isBold: Boolean = false,
    val onClickAction: (() -> Unit)? = null,
    @ColorInt val clickableColor: Int? = null
)

fun Spannable.setSpanStyle(
    span: TextStyledSpan,
    start: Int,
    end: Int = Int.MIN_VALUE
) {
    setBackground(span.background, start, end)
    setBold(span.isBold, start, end)
    setClick(span.onClickAction, span.clickableColor, start, end)
}

internal fun Spannable.setBackground(@ColorInt color: Int?, start: Int, end: Int) {
    color?.let {
        setSpan(BackgroundColorSpan(color), start, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
    }
}

internal fun Spannable.setBold(isBold: Boolean, start: Int, end: Int) {
    if (isBold) {
        setSpan(StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
    }
}

internal fun Spannable.setClick(
    clickAction: (() -> Unit)?,
    @ColorInt color: Int?,
    start: Int,
    end: Int
) {
    clickAction?.let {
        setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                clickAction.invoke()
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                color?.let {
                    ds.color = color
                }
            }
        }, start, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
    }
}

