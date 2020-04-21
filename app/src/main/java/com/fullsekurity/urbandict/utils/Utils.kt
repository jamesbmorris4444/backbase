package com.fullsekurity.urbandict.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.fullsekurity.urbandict.repository.storage.City

class Utils {

    companion object {
        fun hideKeyboard(view: View?) {
            if (view == null) return
            val inputManager = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(view.windowToken, 0)
        }

        fun showKeyboard(view: View?) {
            if (view == null) return
            val inputManager = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.showSoftInput(view, InputMethodManager.SHOW_FORCED)
        }

        fun cityComparison(city: City): String {
            return city.name
        }

        fun newPatternOfSubpatterns(patternOfSubpatterns: String, index: Int, newPattern: String): String {
            // patternOfSubpatterns = P|P|P|...|P
            // if there are N subpatterns then index = 0 to N-1
            // example of usage: newPatternOfSubpatterns("aaaa|bbbb|cccc|dddd", 2, "xxxxxxxx")
            // will return the string value: "aaaa|bbbb|xxxxxxxx|dddd"
            val split: MutableList<String> = patternOfSubpatterns.split('|').toMutableList()
            val stringBuilder = StringBuilder()
            split[index] = newPattern
            for (newIndex in split.indices) {
                stringBuilder.append(split[newIndex])
                if (newIndex < split.size - 1) {
                    stringBuilder.append('|')
                }
            }
            return stringBuilder.toString()
        }

        fun getPatternOfSubpatterns(patternOfSubpatterns: String, index: Int): String {
            // patternOfSubpatterns = P|P|P|...|P|
            // if there are N subpatterns then index = 0 to N-1
            // example of usage: getPatternOfSubpatterns("aaaa|bbbb|cccc|dddd", 2)
            // will return the string value: "cccc"
            val split: MutableList<String> = patternOfSubpatterns.split('|').toMutableList()
            return split[index]
        }

    }

}