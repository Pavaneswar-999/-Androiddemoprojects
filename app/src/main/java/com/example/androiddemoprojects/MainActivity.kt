package com.example.androiddemoprojects

import android.os.Bundle
import android.util.Log
import android.view.animation.OvershootInterpolator
import android.view.HapticFeedbackConstants
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    private lateinit var inputLayout1: TextInputLayout
    private lateinit var inputLayout2: TextInputLayout
    private lateinit var editNumber1: TextInputEditText
    private lateinit var editNumber2: TextInputEditText
    private lateinit var textResult: TextView
    private lateinit var cardResult: MaterialCardView

    private val df = DecimalFormat("#,##0.####")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG, "onCreate: initializing views")

        bindViews()
        setupListeners()
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart: activity becoming visible")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: activity in foreground")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause: activity losing focus")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop: activity no longer visible")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "onRestart: activity restarting")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: activity destroyed")
    }

    private fun bindViews() {
        inputLayout1 = findViewById(R.id.inputLayout1)
        inputLayout2 = findViewById(R.id.inputLayout2)
        editNumber1 = findViewById(R.id.editNumber1)
        editNumber2 = findViewById(R.id.editNumber2)
        textResult = findViewById(R.id.textResult)
        cardResult = findViewById(R.id.cardResult)
    }

    private fun setupListeners() {
        findViewById<Button>(R.id.buttonAdd).setOnClickListener { 
            it.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
            calculate('＋') 
        }
        findViewById<Button>(R.id.buttonSubtract).setOnClickListener { 
            it.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
            calculate('－') 
        }
        findViewById<Button>(R.id.buttonMultiply).setOnClickListener { 
            it.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
            calculate('×') 
        }
        findViewById<Button>(R.id.buttonDivide).setOnClickListener {
            it.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
            calculate('÷') 
        }

        editNumber1.addTextChangedListener { inputLayout1.error = null }
        editNumber2.addTextChangedListener { inputLayout2.error = null }
    }

    private fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        currentFocus?.clearFocus()
    }

    private fun calculate(operator: Char) {
        hideKeyboard()
        val num1String = editNumber1.text?.toString()?.trim()
        val num2String = editNumber2.text?.toString()?.trim()

        val num1 = num1String?.toDoubleOrNull()
        val num2 = num2String?.toDoubleOrNull()

        var hasError = false

        if ((num1String.isNullOrEmpty()) || (num1 == null)) {
            inputLayout1.error = getString(R.string.error_invalid_number)
            hasError = true
        }
        if ((num2String.isNullOrEmpty()) || (num2 == null)) {
            inputLayout2.error = getString(R.string.error_invalid_number)
            hasError = true
        }

        if (hasError) return

        val n1 = num1!!
        val n2 = num2!!

        val result = when (operator) {
            '＋' -> n1 + n2
            '－' -> n1 - n2
            '×' -> n1 * n2
            '÷' -> {
                if (n2 == 0.0) {
                    inputLayout2.error = getString(R.string.error_div_zero)
                    Log.w(TAG, "calculate: division by zero blocked")
                    return
                }
                n1 / n2
            }
            else -> return
        }

        Log.d(TAG, "calculate: $n1 $operator $n2 = $result")
        showResult(n1, operator, n2, result)
    }

    private fun showResult(num1: Double, operator: Char, num2: Double, result: Double) {
        textResult.text = getString(
            R.string.result_format,
            df.format(num1),
            operator.toString(),
            df.format(num2),
            df.format(result),
        )

        cardResult.visibility = View.VISIBLE
        cardResult.alpha = 0f
        cardResult.scaleX = 0.92f
        cardResult.scaleY = 0.92f
        cardResult.animate()
            .alpha(1f)
            .scaleX(1f)
            .scaleY(1f)
            .setDuration(220)
            .setInterpolator(OvershootInterpolator())
            .start()
    }
}