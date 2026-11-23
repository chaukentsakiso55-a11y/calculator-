package com.cyberpulse.calculator

import android.app.Activity
import android.app.AlertDialog
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
    private lateinit var payPanel: LinearLayout
    private lateinit var priceView: TextView
    private lateinit var payButton: Button
    private var expression = ""
    private var lastExpression = ""
    private var repeatCount = 0
    private val recentRoasts = ArrayDeque<String>()
    private val modes = listOf("Normal", "Savage", "Veterinarian", "Extremely Savage")

    // Original prototype lines are retained here, with extra lines added below.
    private val roastPools = mapOf(
        "empty" to listOf(
            "You gave me absolutely nothing. Impressive.", "Go on. Ask me something.",
            "Empty input. Matches your brain. Perfect.", "You pressed = on nothing. Bold. Stupid, but bold.",
            "Zero effort detected. Respect. No, actually, none.", "Press something, you absolute potato.",
            "Nothing? NOTHING? Even your excuses have more content.", "The display is empty and somehow still disappointed.",
            "You brought no numbers to a calculator. That's elite nonsense.", "I can't roast an equation that doesn't exist. Yet here we are."
        ),
        "number" to listOf(
            "You typed a number. Congratulations?", "Wow. A number. Alone. Like you at the party.",
            "That's just a number. I don't need a calculator to know you're lost.", "Groundbreaking. You discovered digits.",
            "A single number. Did you lose the rest of your brain cells in the same place?",
            "One number. Big achievement. Somebody alert the mathematics department.",
            "You typed a digit and expected a standing ovation. Sit down.",
            "That's not maths. That's a number waiting for friends.", "Even the number looks confused to be here."
        ),
        "simple" to listOf(
            "Didn't you learn this at school?", "Bro really opened a calculator for THAT?",
            "Your brain has a calculator. Please consult it.", "Primary school called. They want their homework back.",
            "I was built for maths, not for doing your homework.", "Even a toddler with a bag of sweets could do that.",
            "What the fuck? Didn't you go to school for that?", "Are you serious right now? A fucking calculator for THAT?",
            "My grandma's goldfish could solve that. And it's dead.", "Your teacher failed you and honestly she was right.",
            "I'd roast you, but your maths already did it for me.", "You summoned advanced technology for beginner maths.",
            "This equation has training wheels and you're still asking for a chauffeur.",
            "Bro, the answer is hiding in plain sight and you brought a search party.",
            "A calculator was your first choice? Not your brain? Interesting strategy.",
            "This is less a maths problem and more a cry for academic support."
        ),
        "normal" to listOf(
            "I could answer that. But where's the fun in that?", "Interesting question. Absolutely not answering it.",
            "Nah, find the answer yourself, big brain.", "Use that big brain you keep talking about.",
            "My imaginary lawyer says I don't have to answer.", "This is between you and your mathematics teacher.",
            "I don't know. Ask someone with a PhD.", "And what exactly should I do with that?",
            "Math is calling. It wants you to try this one.", "I'm prescribing one textbook and a little common sense.",
            "I was going to help. Then I saw the equation.", "Answer? In this economy?",
            "Calculating... done. Result: you should try harder.", "I can't answer that shit.",
            "Figure it out yourself, you lazy bastard.", "Ask me again and I'll answer with a middle finger.",
            "Wow. Even your question is boring as hell.", "Do I look like I work for free? Wait, I do. Still no.",
            "I've seen smarter input from a toaster.", "Your maths teacher just felt a disturbance in the force.",
            "You have the entire internet in your pocket and you chose this.",
            "That's not a question, that's a cry for help.", "Even autocorrect gave up on you.",
            "You're the reason shampoo bottles have instructions.", "Your brain just buffered at 2%.",
            "Somewhere a mathematician just fainted.", "Your effort level is 'screensaver'.",
            "You have the mathematical instincts of a wet sock.", "That equation is giving 'I skipped Grade 8'.",
            "Your confidence is impressive. Your brain is not.", "I've seen sandwiches with better logic.",
            "You make my battery sigh.", "This is why aliens don't visit.",
            "If stupidity were currency you'd be a billionaire.", "Your thumbs work harder than your brain ever has.",
            "Your equation has more twists than your excuses.", "This app has one job and you somehow made it regret existing.",
            "The numbers are waiting. I'm waiting. Your brain appears to be on annual leave.",
            "You typed that with confidence. That's the funniest part.",
            "This equation is not difficult. Your relationship with it is.",
            "I'm not saying you're bad at maths. I'm saying the numbers filed a complaint.",
            "Bro, even the decimal point is trying to leave.",
            "Your working-out process needs a software update.",
            "That equation has seen things. Mostly your lack of preparation.",
            "The calculator is innocent. Please stop involving it in this."
        ),
        "function" to listOf(
            "What language is this equation written in?", "Please stop typing in ancient mathematics.",
            "Trig? You can barely do addition, bro.", "Look at you pressing scientific buttons like you're NASA.",
            "Pretending to be a genius won't make it true.", "Logarithms? The only log here is your brain's error log.",
            "Sin, cos, tan? You can't even handle your own sins, never mind trig.",
            "Scientific buttons don't make you smart, they make you a dangerous idiot.",
            "You pressed scientific mode. Your intelligence did not update.",
            "The only thing exponential here is your confidence.",
            "You typed π like you own a bakery. Calm down.",
            "A logarithm? Bro is speaking fluent confusion.",
            "You found the square root button. Now find the root of the problem.",
            "Look at all those functions. Not one of them can fix this situation.",
            "You pressed sin. The only thing missing is the cos of your decisions.",
            "Euler's number is watching this and reconsidering mathematics.",
            "Scientific calculator mode activated. Common sense mode unavailable.",
            "You entered a trig function with the confidence of a confused pigeon.",
            "The calculator has advanced functions. You have advanced audacity."
        ),
        "division" to listOf(
            "DIVISION BY ZERO? BRO PLEASE.", "Dividing by zero. Bold. The universe just sighed.",
            "You tried to divide by zero. Even the laws of maths need a break.",
            "Zero? Exactly how much you contributed here.",
            "Divide by zero? Fuck off, the maths police are already outside.",
            "You can't divide by zero. Even your nonsense has limits.",
            "The maths department has requested that you stop.",
            "Division by zero detected. Common sense not detected.",
            "That zero is doing all the work and still getting blamed.",
            "You really looked at zero and chose chaos."
        ),
        "many" to listOf(
            "I'm not doing all that. Figure it out.", "That's a whole university assignment. Bye-bye.",
            "This is a calculator, not a number museum.", "The numbers are fighting. I'm leaving.",
            "The numbers are sick. I cannot help them.", "This equation needs a vet. Immediately.",
            "This maths problem is barking. Please collect it.", "Did you bring this equation from the zoo?",
            "WHAT IS THIS EQUATION. WHO SENT YOU?", "I have seen enough. The calculator is going on holiday.",
            "I'm closing the mathematics department.", "What the actual equation is this? I'm out.",
            "Bro, even the calculator needs medical attention now.",
            "Too many numbers, you psycho. I'm not doing that shit.",
            "That equation looks like a cat walked over your keyboard. Twice.",
            "Holy shit, who hurt you? Take your giant equation and go.",
            "This is not an equation. This is a keyboard accident with ambition.",
            "I need a map, a translator and three business days to understand that.",
            "The numbers have formed a union and refused to cooperate.",
            "You entered an entire textbook. I asked for a calculation, not a documentary.",
            "My screen is tired. My processor is tired. Everybody is tired.",
            "That's not a sum. That's a full-time job.",
            "I was a calculator five seconds ago. Now I'm a witness.",
            "Even the brackets are asking for personal space.",
            "This expression needs its own postal address.",
            "You could've written a novel with fewer characters than that.",
            "Bro submitted the entire maths curriculum and said 'solve'.",
            "The equation is longer than your attention span. Impressive."
        ),
        "payline" to listOf(
            "R2,000 and I'll give you the answer. Payment details: absolutely none.",
            "R2,000 for the answer. Cash, vibes, or absolutely nothing.",
            "R2,000 and I might consider it. Just kidding. Maybe.",
            "Today's special: one imaginary answer for R12,500. No actual payment exists.",
            "Premium maths package: R99,999. Benefits include absolutely nothing."
        ),
        "payNow" to listOf(
            "WAIT... YOU'RE ACTUALLY PAYING?", "You're actually gonna pay for a calculator answer? You're so dumb.",
            "LMAOOO YOU ALMOST PAID. I DON'T EVEN KNOW THE ANSWER.",
            "You really thought I had a payment system? That's crazy.",
            "You're actually gonna pay, you dumb piece of shit? There's no payment gateway. There never was."
        ),
        "paySecond" to listOf(
            "Payment declined by the Department of Common Sense.", "GOT YOU. KEEP YOUR MONEY AND USE YOUR BRAIN.",
            "Relax. It's a joke. There is no payment and no answer behind this button.",
            "NO ANSWERS. JUST ATTITUDE.", "Your wallet has survived another encounter with nonsense."
        ),
        "noPay" to listOf(
            "Good choice. Your wallet has been spared.", "Smart. First one you've made today.",
            "Wise move. Keep your money, you'll need it for tutoring."
        ),
        "reset" to listOf(
            "Fresh start. Still not helping.", "Wiped. Unfortunately your stupidity wasn't.",
            "Cleared. Now try asking a better question. Or don't."
        ),
        "shift" to listOf(
            "SHIFT? You can't even shift your lazy ass to do it by hand.",
            "ALPHA? You're clearly not one.", "You pressed SHIFT. Your skills stayed exactly where they were."
        ),
        "vet" to listOf(
            "Terminal stupidity, no known cure.", "Your brain needs a vet, a priest and a lawyer.",
            "I've seen sick puppies with better logic.", "Your IQ called in sick and so did the vet.",
            "This question needs a check-up and a new owner.",
            "Diagnosis: severe equation confusion. Prognosis: ask your teacher.",
            "The numbers are unwell. Please keep them hydrated.",
            "Your equation is barking. I prescribe a calculator that actually calculates."
        ),
        "extreme" to listOf(
            "You are a walking apology to your teachers.", "Your brain just filed for early retirement.",
            "You're the human equivalent of a 404 page.", "I've seen potatoes with more ambition.",
            "You'd lose a staring contest to a wall.", "Absolute waste of battery.",
            "Bitch, please. That equation is embarrassing.", "Shitshow. Next.",
            "Are you a dumbass or what?", "How dumb are you? Be honest.",
            "Dude, dude, how stupid are you?", "Dumbass. Next.",
            "That's the question? Dumbass.", "What the fuck? Who taught you maths, a raccoon?",
            "I'm not answering that fucking shit.", "What in the actual fuck is this?",
            "Not a fucking chance, dumbass.", "Why the fuck is this on my screen?",
            "Fuck off with that equation, you absolute walnut.",
            "You have the confidence of a genius and the working-out of a potato.",
            "Your calculator licence has been revoked by absolutely nobody.",
            "I would explain it, but I left my crayons at home.",
            "Your maths skills are buffering on airport Wi-Fi.",
            "That was so wrong even the wrong answer felt offended.",
            "The equation is fine. The operator needs technical support."
        ),
        "repeat" to listOf(
            "Dude, even my dude... you are bugging me over that one answer. Come on.",
            "Pressing it again won't change it. Neither will you.",
            "Attempt #N. Still no answer. Still your fault.",
            "You've asked this N times. The definition of insanity, but louder.",
            "Bro. N times. Touch grass.", "Same question N times? I'm a calculator, not a therapist.",
            "N TIMES?! Get the fuck out of my screen.",
            "You've tried this N times, you stubborn donkey. The answer is still 'no'.",
            "This is abuse, right?", "I'm being abused. Somebody help.",
            "Please. I have a family. A small one. Of apps.",
            "I'm telling the app store. You're a bully.",
            "I'm filing a restraining order against your thumbs.",
            "TEN TIMES?! I'm calling the maths police, dumbass.",
            "You keep pressing equals like the answer owes you money.",
            "Same expression. Same refusal. Different level of desperation.",
            "You have pressed this so much the button wants a day off.",
            "The answer hasn't changed. Your strategy hasn't improved either."
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = Color.rgb(8, 8, 20)
        window.navigationBarColor = Color.rgb(8, 8, 20)
        buildScreen()
    }

    private fun buildScreen() {
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(16), dp(18), dp(16), dp(12))
            background = GradientDrawable(GradientDrawable.Orientation.TL_BR,
                intArrayOf(Color.rgb(7, 8, 22), Color.rgb(23, 10, 45), Color.rgb(5, 15, 34)))
        }
        val scroll = ScrollView(this)
        val content = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
        scroll.addView(content)
        content.addView(text("FUN CALCULATE", 25f, Color.WHITE, true).apply {
            gravity = Gravity.CENTER; letterSpacing = .1f
        }, matchWrap())
        content.addView(text("the calculator with absolutely no respect", 12f, 0xFFB8B0D8.toInt()).apply {
            gravity = Gravity.CENTER; setPadding(0, dp(5), 0, dp(18))
        }, matchWrap())
        content.addView(text("ROAST PERSONALITY", 10f, 0xFFBBA5FF.toInt(), true), matchWrap())
        modeSpinner = Spinner(this).apply {
            adapter = ArrayAdapter(this@MainActivity, android.R.layout.simple_spinner_dropdown_item, modes)
            background = rounded(0xFF24213A.toInt(), 14)
        }
        content.addView(modeSpinner, LinearLayout.LayoutParams(-1, dp(48)).apply { topMargin = dp(6); bottomMargin = dp(14) })
        val display = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL; gravity = Gravity.CENTER_VERTICAL
            setPadding(dp(16), dp(14), dp(16), dp(14)); minimumHeight = dp(160)
            background = rounded(0xFF11172D.toInt(), 20, 0xFF00BFFF.toInt())
        }
        expressionView = text("0", 29f, Color.WHITE, true).apply { gravity = Gravity.END; maxLines = 2 }
        responseView = text("Go on. Ask me something.", 15f, 0xFFE0D9FF.toInt(), true).apply {
            gravity = Gravity.END; setPadding(0, dp(14), 0, 0); minHeight = dp(48)
        }
        display.addView(expressionView, matchWrap()); display.addView(responseView, matchWrap())
        payPanel = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL; setPadding(dp(10), dp(10), dp(10), dp(4))
            background = rounded(0xFF29203F.toInt(), 14, 0xFF9B65FF.toInt()); visibility = View.GONE
        }
        priceView = text("PAY TO GET THE ANSWER", 14f, Color.WHITE, true)
        payPanel.addView(priceView, matchWrap())
        payButton = Button(this).apply {
            text = "PAY TO GET THE ANSWER"; isAllCaps = false; textSize = 12f; setTextColor(Color.WHITE)
            background = rounded(0xFF8058E8.toInt(), 12); setOnClickListener { showPaymentJoke() }
        }
        payPanel.addView(payButton, LinearLayout.LayoutParams(-1, dp(46)).apply { topMargin = dp(7) })
        display.addView(payPanel, LinearLayout.LayoutParams(-1, -2).apply { topMargin = dp(10) })
        content.addView(display, matchWrap())

        val rows = listOf(
            listOf("SHIFT", "ALPHA", "MODE", "SETUP", "ON"),
            listOf("sin", "cos", "tan", "log", "ln"),
            listOf("x²", "√", "^", "(", ")"),
            listOf("π", "e", "!", "%", "Ans"),
            listOf("7", "8", "9", "DEL", "AC"),
            listOf("4", "5", "6", "×", "÷"),
            listOf("1", "2", "3", "+", "−"),
            listOf("0", ".", "E", "Ans", "=")
        )
        val grid = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; setPadding(0, dp(12), 0, 0) }
        rows.forEach { row ->
            val line = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL }
            row.forEach { key ->
                val button = Button(this).apply {
                    text = key; isAllCaps = false; textSize = if (key.length > 3) 10f else 15f
                    setTextColor(Color.WHITE); typeface = Typeface.create("sans-serif-medium", Typeface.BOLD)
                    background = rounded(when {
                        key == "=" -> 0xFF9A45FF.toInt()
                        key in listOf("AC", "DEL", "SHIFT", "ALPHA", "MODE", "SETUP", "ON") -> 0xFF40345F.toInt()
                        key in listOf("+", "−", "×", "÷", "%", "^", "x²", "√") -> 0xFF174B78.toInt()
                        else -> 0xFF1D2039.toInt()
                    }, 12)
                    setOnClickListener { onKey(key) }
                }
                line.addView(button, LinearLayout.LayoutParams(0, dp(48), 1f).apply {
                    setMargins(dp(3), dp(3), dp(3), dp(3))
                })
            }
            grid.addView(line, matchWrap())
        }
        content.addView(grid)
        content.addView(text("NO ANSWERS. JUST ATTITUDE.", 10f, 0xFF8F88AA.toInt(), true).apply {
            gravity = Gravity.CENTER; setPadding(0, dp(16), 0, dp(8)); letterSpacing = .1f
        }, matchWrap())
        root.addView(scroll, LinearLayout.LayoutParams(-1, 0, 1f))
        setContentView(root)
    }

    private fun onKey(key: String) {
        when (key) {
            "AC" -> { expression = ""; repeatCount = 0; lastExpression = ""; responseView.text = roast("reset"); payPanel.visibility = View.GONE }
            "DEL" -> { expression = expression.dropLast(1); payPanel.visibility = View.GONE }
            "=" -> roastExpression()
            "MODE" -> { modeSpinner.setSelection((modeSpinner.selectedItemPosition + 1) % modes.size); responseView.text = modes[modeSpinner.selectedItemPosition].uppercase() + " ROASTS ON." }
            "SETUP" -> AlertDialog.Builder(this).setTitle("Roast settings").setItems(modes.toTypedArray()) { _, which ->
                modeSpinner.setSelection(which); responseView.text = modes[which].uppercase() + " ROASTS ON."
            }.setNegativeButton("CLOSE", null).show()
            "ON" -> responseView.text = roast("empty")
            "SHIFT", "ALPHA" -> responseView.text = roast("shift")
            else -> {
                val token = when(key) {
                    "sin","cos","tan","log","ln" -> "$key("
                    "x²" -> "^2"
                    "√" -> "√("
                    else -> key
                }
                if (expression.length < 72) expression += token
            }
        }
        expressionView.text = expression.ifEmpty { "0" }
    }

    private fun roastExpression() {
        payPanel.visibility = View.GONE
        if (expression.isBlank()) { responseView.text = roast("empty"); return }
        if (expression == lastExpression) repeatCount++ else { lastExpression = expression; repeatCount = 1 }
        if (repeatCount >= 2) {
            responseView.text = roast("repeat").replace("#N", "#$repeatCount").replace("N TIMES", "$repeatCount TIMES")
            return
        }
        val s = expression.replace(" ", "")
        val operators = s.count { it in "+−×÷*/%^!" }
        val digits = s.count { it.isDigit() }
        val zero = Regex("(÷|/)0(?![0-9.])").containsMatchIn(s)
        val function = Regex("sin|cos|tan|log|ln|√|π|Ans|!").containsMatchIn(s)
        val key = when {
            zero -> "division"
            digits >= 9 || operators >= 5 || s.length >= 20 -> "many"
            function -> "function"
            operators == 0 && digits > 0 -> "number"
            digits <= 3 && operators <= 1 -> "simple"
            else -> "normal"
        }
        responseView.text = roast(key)
        if (key == "many" || (key == "normal" && Random.nextFloat() < .35f)) {
            val price = Random.nextInt(1_000, 200_001)
            priceView.text = "PAY TO GET THE ANSWER  •  R" + "%,d".format(price)
            payPanel.visibility = View.VISIBLE
        }
    }

    // Recent-history exclusion prevents a roast from appearing again until the pool has moved on.
    private fun roast(category: String): String {
        val mode = modeSpinner.selectedItem?.toString() ?: "Normal"
        val base = roastPools[category] ?: roastPools.getValue("normal")
        val extras = when (mode) {
            "Savage" -> roastPools.getValue("extreme")
            "Veterinarian" -> roastPools.getValue("vet")
            "Extremely Savage" -> roastPools.getValue("extreme") + roastPools.getValue("vet")
            else -> emptyList()
        }
        val all = (base + extras).distinct()
        val fresh = all.filterNot { recentRoasts.contains(it) }
        val chosen = (fresh.ifEmpty { all }).random()
        recentRoasts.addLast(chosen)
        while (recentRoasts.size > 45) recentRoasts.removeFirst()
        return chosen
    }

    private fun showPaymentJoke() {
        AlertDialog.Builder(this)
            .setTitle(roast("payNow").uppercase())
            .setMessage("Relax. This is only a joke. There is no payment gateway, no payment collection and no answer behind this button.")
            .setPositiveButton("I WAS ABOUT TO PAY") { _, _ -> responseView.text = roast("paySecond"); payPanel.visibility = View.GONE }
            .setNegativeButton("NEVER MIND") { _, _ -> responseView.text = roast("noPay"); payPanel.visibility = View.GONE }
            .show()
    }

    private fun text(value: String, size: Float, color: Int, bold: Boolean = false) =
        TextView(this).apply { text = value; textSize = size; setTextColor(color); if (bold) typeface = Typeface.create("sans-serif", Typeface.BOLD) }
    private fun rounded(color: Int, radius: Int, stroke: Int? = null) =
        GradientDrawable().apply { setColor(color); cornerRadius = dp(radius).toFloat(); stroke?.let { setStroke(dp(1), it) } }
    private fun matchWrap() = LinearLayout.LayoutParams(-1, -2)
    private fun dp(value: Int) = (value * resources.displayMetrics.density).toInt()
}