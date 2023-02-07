package com.pluu.reusablespan.sample

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.BackgroundColorSpan
import android.text.style.CharacterStyle
import android.text.style.ClickableSpan
import android.text.style.StyleSpan
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.set
import com.pluu.reusablespan.sample.databinding.ActivityMainBinding
import com.pluu.text.style.TextStyledSpan
import com.pluu.text.style.setSpanStyle

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val originText = """
            Everything you need to build on Android
            
            Android Studio is Android's official IDE. It is purpose-built for Android to accelerate your development and help you build the highest-quality apps for every Android device.
        """.trimIndent()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bindDefaultSpan()
        bindModifySpan()
        bindTextStyledSpan()
    }

    ///////////////////////////////////////////////////////////////////////////
    // Sample 1
    ///////////////////////////////////////////////////////////////////////////

    private fun bindDefaultSpan() {
        val spans = buildList {
            this += BackgroundColorSpan(Color.DKGRAY)
            this += StyleSpan(Typeface.BOLD)
            this += object : ClickableSpan() {
                override fun onClick(widget: View) {
                    Toast.makeText(this@MainActivity, "Click", Toast.LENGTH_SHORT).show()
                }

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.color = Color.YELLOW
                }
            }
        }

        binding.tv1.movementMethod = LinkMovementMethod.getInstance()
        binding.tv1.text = createDefault(originText, "Android", *spans.toTypedArray())
    }

    private fun createDefault(
        origin: String,
        findText: String,
        vararg spans: CharacterStyle
    ): CharSequence {
        val result = SpannableString(origin)
        findText.toRegex().findAll(result)
            .forEach {
                for (span in spans) {
                    result[it.range.first, it.range.last + 1] = span
                }
            }
        return result
    }

    ///////////////////////////////////////////////////////////////////////////
    // Sample 2
    ///////////////////////////////////////////////////////////////////////////

    private fun bindModifySpan() {
        binding.tv2.movementMethod = LinkMovementMethod.getInstance()
        binding.tv2.text = createModify(
            originText,
            "Android",
            applier = { start, end ->
                setSpan(
                    BackgroundColorSpan(Color.DKGRAY),
                    start, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE
                )
                setSpan(
                    StyleSpan(Typeface.BOLD),
                    start, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE
                )
                setSpan(
                    object : ClickableSpan() {
                        override fun onClick(widget: View) {
                            Toast.makeText(this@MainActivity, "Click", Toast.LENGTH_SHORT).show()
                        }

                        override fun updateDrawState(ds: TextPaint) {
                            super.updateDrawState(ds)
                            ds.color = Color.YELLOW
                        }
                    }, start, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE
                )
            }
        )
    }

    private fun createModify(
        origin: String,
        findText: String,
        applier: Spannable.(start: Int, end: Int) -> Unit
    ): CharSequence {
        val result = SpannableString(origin)
        findText.toRegex().findAll(result)
            .forEach {
                result.applier(it.range.first, it.range.last + 1)
            }
        return result
    }

    ///////////////////////////////////////////////////////////////////////////
    // Sample 3
    ///////////////////////////////////////////////////////////////////////////

    private fun bindTextStyledSpan() {
        binding.tv3.movementMethod = LinkMovementMethod.getInstance()
        binding.tv3.text = createTextStyledSpan(
            originText,
            "Android",
            TextStyledSpan(
                background = Color.DKGRAY,
                isBold = true,
                onClickAction = {
                    Toast.makeText(this, "Click", Toast.LENGTH_SHORT).show()
                },
                clickableColor = Color.YELLOW
            )
        )
    }

    private fun createTextStyledSpan(
        origin: String,
        findText: String,
        styledSpan: TextStyledSpan
    ): CharSequence {
        val result = SpannableString(origin)
        findText.toRegex().findAll(result)
            .forEach {
                result.setSpanStyle(styledSpan, it.range.first, it.range.last + 1)
            }
        return result
    }
}

