package com.example.submissionintermediate2.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.example.submissionintermediate2.R
import com.example.submissionintermediate2.util.StringUtil.Companion.isEmailValid

class CustomEditText : AppCompatEditText, View.OnTouchListener {
    constructor(context: Context) : super(context){
        init()
    }
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private lateinit var clearButtonImage: Drawable

    private fun init() {
        clearButtonImage = ContextCompat.getDrawable(context, R.drawable.ic_close_black_24dp) as Drawable
        setOnTouchListener(this)


        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing.
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, after: Int) {
//                if (inputType == InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS && isEmailValid(s)) setError(context.getString(R.string.error_password_min_length))
                if (inputType == 129 && s.count() < 6) setError(context.getString(R.string.error_password_min_length))
                else if (inputType == 33 && !isEmailValid(s)) setError(context.getString(R.string.error_email_not_valid))
                else setError(null)
            }
            override fun afterTextChanged(s: Editable) {
                // Do nothing.
            }
        })
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
    }

    override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
        // Do nothing.
        return false
    }
}