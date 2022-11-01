package com.example.submissionintermediate2.util

import android.util.Patterns

class StringUtil {
    companion object {
        fun isEmailValid(email: CharSequence?): Boolean {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }
    }
}