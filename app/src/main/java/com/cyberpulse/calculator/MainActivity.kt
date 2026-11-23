package com.cyberpulse.calculator

import android.app.Activity
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.Spinner
import android.widget.TextView
import kotlin.random.Random

class MainActivity : Activity() {
    private lateinit var expressionView: TextView
    private lateinit var responseView: TextView
    private lateinit var modeSpinner: Spinner
    private var expression = ""

    private val modes = listOf("Normal", "Savage", "Unhinged", "Veterinarian")
    private val responses = mapOf(
        "Normal" to listOf(
            "I could answer that. But where's the fun in that?",
            "Math is calling. It wants you to try this one.",
            "Interesting question. Absolutely not answering it.",
            "Your brain has a calculator. Please consult it.",
            "I was built for maths, not for doing your homework."
        ),
        "Savage" to listOf(
            "Bro really opened a calculator for THAT?",
            "Didn't you learn this at school?",
            "Use that big brain you keep talking about.",
            "I'm not doing all that. Figure it out.",
            "This is between you and your mathematics teacher.",
            "R2,000 and I might consider it. Just kidding. Maybe."
        ),
        "Unhinged" to listOf(
            "WHAT IS THIS EQUATION. WHO SENT YOU?",
            "I'm closing the mathematics department.",
            "My imaginary lawyer says I don't have to answer.",
            "The numbers are fighting. I'm leaving.",
            "R2,000 for the answer. Cash, vibes, or absolutely nothing.",
            "I have seen enough. The calculator is going on holiday."
        ),
        "Veterinarian" to listOf(
            "This equation needs a vet. Immediately.",
            "The numbers are sick. I cannot help them.",
            "Bro, even the calculator needs medical attention now.",
            "Did you bring this equation from the zoo?",
            "I'm prescribing one textbook and a little common sense.",
            "This maths problem is barking. Please collect it."
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = Color.rgb(10, 10, 18)
        window.navigationBarColor = Color.rgb(10, 10, 18)
        window.decorView.systemUiVisibility = 0
        buildScreen()
    }

    private fun buildScreen() {
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(20), dp(22), dp(20), dp(16))
            background = GradientDrawable(
                GradientDrawable.Orientation.TL_BR,
                intArrayOf(Color.rgb(10, 10, 19), Color.rgb(22, 15, 39), Color.rgb(9, 13, 25))
            )
        }

        val scroll = ScrollView(this)
        val content = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
        }
        scroll.addView(content)

        val brand = text("CALCULATOR", 25f, Color.WHITE, true).apply {
            letterSpacing = 0.12f
            gravity = Gravity.CENTER
        }
        content.addView(brand, matchWrap())

        val subtitle = text("the calculator with absolutely no respect", 12f, 0xFFAAA5C4.toInt()).apply {
            gravity = Gravity.CENTER
            setPadding(0, dp(5), 0, dp(20))
        }
        content.addView(subtitle, matchWrap())

        val modeLabel = text("PERSONALITY MODE", 10f, 0xFFB8A9F5.toInt(), true)
        content.addView(modeLabel, matchWrap())

        modeSpinner = Spinner(this).apply {
            adapter = ArrayAdapter(
                this@MainActivity,
                android.R.layout.simple_spinner_dropdown_item,
                modes
            )
            background = rounded(0xFF24213A.toInt(), 14)
        }
        content.addView(modeSpinner, LinearLayout.LayoutParams(-1, dp(48)).apply {
            topMargin = dp(7)
            bottomMargin = dp(16)
        })

        val display = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER_VERTICAL
            setPadding(dp(18), dp(15), dp(18), dp(15))
            background = rounded(0xFF171526.toInt(), 20, 0xFF51417D.toInt())
            minimumHeight = dp(150)
        }
        expressionView = text("0", 30f, Color.WHITE, true).apply {
            gravity = Gravity.END
            maxLines = 2
        }
        responseView = text("Go on. Ask me something.", 16f, 0xFFE0D9FF.toInt(), true).apply {
            gravity = Gravity.END
            setPadding(0, dp(15), 0, 0)
            minHeight = dp(44)
        }
        display.addView(expressionView, matchWrap())
        display.addView(responseView, matchWrap())
        content.addView(display, matchWrap())

        val keys = listOf(
            listOf("AC", "DEL", "(", ")"),
            listOf("7", "8", "9", "÷"),
            listOf("4", "5", "6", "×"),
            listOf("1", "2", "3", "−"),
            listOf("%", "0", ".", "+"),
            listOf("VIBE CHECK", "=", "=", "=")
        )

        val grid = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(0, dp(16), 0, 0)
        }
        keys.forEachIndexed { rowIndex, row ->
            val line = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                gravity = Gravity.CENTER
            }
            row.forEachIndexed { colIndex, key ->
                if (rowIndex == 5 && colIndex > 0) return@forEachIndexed
                val button = Button(this).apply {
                    text = key
                    textSize = if (key == "VIBE CHECK") 11f else 18f
                    isAllCaps = false
                    setTextColor(Color.WHITE)
                    typeface = Typeface.create("sans-serif-medium", Typeface.BOLD)
                    background = rounded(
                        when {
                            key == "=" || key == "VIBE CHECK" -> 0xFF8058E8.toInt()
                            key in listOf("AC", "DEL") -> 0xFF39304F.toInt()
                            key in listOf("+", "−", "×", "÷", "%", "(", ")") -> 0xFF29243E.toInt()
                            else -> 0xFF1D1B2E.toInt()
                        }, 16
                    )
                    setOnClickListener { onKey(key) }
                }
                val weight = if (key == "VIBE CHECK") 2f else 1f
                line.addView(button, LinearLayout.LayoutParams(0, dp(58), weight).apply {
                    setMargins(dp(4), dp(4), dp(4), dp(4))
                })
            }
            grid.addView(line, matchWrap())
        }
        content.addView(grid)

        val footer = text("NO ANSWERS. JUST ATTITUDE.", 10f, 0xFF817A9C.toInt(), true).apply {
            gravity = Gravity.CENTER
            setPadding(0, dp(18), 0, dp(10))
            letterSpacing = 0.12f
        }
        content.addView(footer, matchWrap())

        root.addView(scroll, LinearLayout.LayoutParams(-1, 0, 1f))
        setContentView(root)
    }

    private fun onKey(key: String) {
        when (key) {
            "AC" -> {
                expression = ""
                responseView.text = "Fresh start. Still not helping."
            }
            "DEL" -> expression = expression.dropLast(1)
            "=", "VIBE CHECK" -> roast()
            else -> {
                if (expression.length < 48) expression += key
            }
        }
        expressionView.text = expression.ifEmpty { "0" }
    }

    private fun roast() {
        if (expression.isBlank()) {
            responseView.text = "You gave me absolutely nothing. Impressive."
            return
        }

        val compact = expression.replace(" ", "")
        val operators = compact.count { it in "+−×÷*/%" }
        val digits = compact.count { it.isDigit() }
        val hasDivisionByZero = compact.contains("÷0") || compact.contains("/0")
        val invalid = compact.any { it !in "0123456789.+−×÷*/%()" }
        val difficult = operators >= 4 || digits >= 12 || compact.length >= 18
        val mode = modeSpinner.selectedItem?.toString() ?: "Normal"

        val custom = when {
            hasDivisionByZero -> listOf(
                "DIVISION BY ZERO? BRO PLEASE.",
                "You cannot divide by zero. Even I have limits.",
                "The laws of mathematics have filed a complaint."
            )
            invalid -> listOf(
                "What language is this equation written in?",
                "I don't speak whatever that was.",
                "Please stop typing in ancient mathematics."
            )
            difficult -> listOf(
                "Nah, find the answer yourself, big brain.",
                "What the actual equation is this? I'm out.",
                "R2,000 and I'll give you the answer. Payment details: absolutely none.",
                "That's a whole university assignment. Bye-bye.",
                "I don't know. Ask someone with a PhD."
            )
            operators == 0 -> listOf(
                "You typed a number. Congratulations?",
                "And what exactly should I do with that?",
                "This is a calculator, not a number museum."
            )
            else -> responses[mode] ?: responses.getValue("Normal")
        }

        responseView.text = custom.random()
    }

    private fun text(value: String, size: Float, color: Int, bold: Boolean = false) =
        TextView(this).apply {
            text = value
            textSize = size
            setTextColor(color)
            if (bold) typeface = Typeface.create("sans-serif", Typeface.BOLD)
        }

    private fun rounded(color: Int, radius: Int, stroke: Int? = null) =
        GradientDrawable().apply {
            setColor(color)
            cornerRadius = dp(radius).toFloat()
            stroke?.let { setStroke(dp(1), it) }
        }

    private fun matchWrap() = LinearLayout.LayoutParams(-1, -2)
    private fun dp(value: Int) = (value * resources.displayMetrics.density).toInt()
}
