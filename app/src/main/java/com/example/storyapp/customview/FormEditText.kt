package com.example.storyapp.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.util.Patterns
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.example.storyapp.R

class FormEditText : AppCompatEditText, View.OnTouchListener{
    private lateinit var clearButtonImage: Drawable

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }

    private fun showClearButton() {
        setButtonDrawables(endOfTheText = clearButtonImage)
    }
    private fun hideClearButton() {
        setButtonDrawables()
    }

    private fun setButtonDrawables(
        startOfTheText: Drawable? = null,
        topOfTheText: Drawable? = null,
        endOfTheText: Drawable? = null,
        bottomOfTheText: Drawable? = null
    ){
        setCompoundDrawablesWithIntrinsicBounds(
            startOfTheText,
            topOfTheText,
            endOfTheText,
            bottomOfTheText
        )
    }
    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        if (compoundDrawables[2] != null) {
            val clearButtonStart: Float
            val clearButtonEnd: Float
            var isClearButtonClicked = false
            if (layoutDirection == View.LAYOUT_DIRECTION_RTL) {
                clearButtonEnd = (clearButtonImage.intrinsicWidth + paddingStart).toFloat()
                when {
                    event.x < clearButtonEnd -> isClearButtonClicked = true
                }
            } else {
                clearButtonStart = (width - paddingEnd - clearButtonImage.intrinsicWidth).toFloat()
                when {
                    event.x > clearButtonStart -> isClearButtonClicked = true
                }
            }
            if (isClearButtonClicked) {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        clearButtonImage = ContextCompat.getDrawable(context, R.drawable.outline_cancel_24) as Drawable
                        showClearButton()
                        return true
                    }
                    MotionEvent.ACTION_UP -> {
                        clearButtonImage = ContextCompat.getDrawable(context, R.drawable.outline_cancel_24) as Drawable
                        when {
                            text != null -> text?.clear()
                        }
                        hideClearButton()
                        return true
                    }
                    else -> return false
                }
            } else return false
        }
        return false
    }

    private fun init() {
        clearButtonImage = ContextCompat.getDrawable(context, R.drawable.outline_cancel_24) as Drawable
        setOnTouchListener(this)

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.toString().isNotEmpty()) showClearButton() else hideClearButton()

                if (inputType and InputType.TYPE_TEXT_VARIATION_PASSWORD != 0 && s.toString().length < 8) {
                    Log.d("form edit text", "password chencking")
                    setError(
                        context.getString(R.string.invalid_password),
                        null
                    )
                } else if (inputType and InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS != 0 && !Patterns.EMAIL_ADDRESS.matcher(s).matches()){
                    setError(
                        context.getString(R.string.invalid_email),
                        null
                    )
                }
                else {
                    error = null
                }
            }
            override fun afterTextChanged(s: Editable) {
                if (inputType and InputType.TYPE_TEXT_VARIATION_PASSWORD != 0 && s.toString().length < 8) {
                    Log.d("form edit text", "password chencking")
                    setError(
                        context.getString(R.string.invalid_password),
                        null
                    )
                } else if (inputType and InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS != 0 && !Patterns.EMAIL_ADDRESS.matcher(s).matches()){
                    setError(
                        context.getString(R.string.invalid_email),
                        null
                    )
                }
                else {
                    error = null
                }
            }
        })
    }
}