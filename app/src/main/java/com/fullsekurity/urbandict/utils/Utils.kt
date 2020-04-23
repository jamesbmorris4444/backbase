package com.fullsekurity.urbandict.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.fullsekurity.urbandict.repository.storage.City

class Utils {

    companion object {
        fun cityComparison(city: City): String {
            return city.name
        }
    }

}