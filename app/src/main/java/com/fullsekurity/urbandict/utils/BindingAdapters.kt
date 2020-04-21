package com.fullsekurity.urbandict.utils

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.cardview.widget.CardView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fullsekurity.urbandict.logger.LogUtils
import com.fullsekurity.urbandict.recyclerview.RecyclerViewViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.squareup.picasso.Picasso
import java.lang.reflect.Field

@BindingAdapter("background_from_res_int")
fun setBackgroundr(view: EditText, resInt: Int) {
    view.setBackgroundResource(resInt)
    view.requestLayout()
}

@BindingAdapter("image_background")
fun setBackgroundImage(imageView: ImageView, resource: Drawable) {
    imageView.setImageDrawable(resource)
}

@BindingAdapter("layout_marginTop")
fun setMarginTop(v: View, topMargin: Int) {
    v.layoutParams ?: return
    val layoutParams = v.layoutParams as ViewGroup.MarginLayoutParams
    layoutParams.topMargin = topMargin
    v.layoutParams = layoutParams
    v.requestLayout()
}

@BindingAdapter("layout_marginLeft")
fun setMarginLeft(v: View, leftMargin: Int) {
    val layoutParams = v.layoutParams as ViewGroup.MarginLayoutParams
    layoutParams.leftMargin = leftMargin
    v.layoutParams = layoutParams
    v.requestLayout()
}

@BindingAdapter("layout_marginRight")
fun setMarginRight(v: View, rightMargin: Int) {
    val layoutParams = v.layoutParams as ViewGroup.MarginLayoutParams
    layoutParams.rightMargin = rightMargin
    v.layoutParams = layoutParams
    v.requestLayout()
}

@BindingAdapter("textview_gravity")
fun setTextViewGravity(v: TextView, gravity: Int) {
    v.gravity = gravity
    v.requestLayout()
}

@BindingAdapter("text_font")
fun setFont(view: TextView, font: String) {
    val typeface = FontFamily.newInstance().getFontResId(view.context, font)
    view.typeface = typeface
}

@BindingAdapter("text_font_text_input_layout")
fun setFontTextInputLayout(textInputLayout: TextInputLayout, font: String) {
    val typeface = FontFamily.newInstance().getFontResId(textInputLayout.context, font)
    textInputLayout.typeface = typeface
}

@BindingAdapter("edit_text_font")
fun setEditTextFont(view: EditText, font: String) {
    val typeface = FontFamily.newInstance().getFontResId(view.context, font)
    view.typeface = typeface
}

@BindingAdapter("text_color")
fun setTextColor(view: TextView, color: String) {
    view.setTextColor(Color.parseColor(color))
    // If color is ever needed as a drawable: Converters.convertColorToDrawable(0x00ff00)
}

@BindingAdapter("edit_text_color")
fun setEditTextColor(view: EditText, color: String) {
    view.setTextColor(Color.parseColor(color))
    // If color is ever needed as a drawable: Converters.convertColorToDrawable(0x00ff00)
}

@BindingAdapter("edit_text_underline_color")
fun setEditTextUnderlineColor(view: TextInputEditText, color: String) {
    val colorStateList = ColorStateList.valueOf(Color.parseColor(color))
    view.supportBackgroundTintList = colorStateList
}

@BindingAdapter("password_toggle_tint")
fun setPasswordToggleTint(view: TextInputLayout, color: String) {
    val colorStateList = ColorStateList.valueOf(Color.parseColor(color))
    view.setPasswordVisibilityToggleTintList(colorStateList)
}

@BindingAdapter("background_color")
fun setBackgroundColor(view: LinearLayout, color: String) {
    view.setBackgroundColor(Color.parseColor(color))
}

@BindingAdapter("text_size")
fun setTextSize(view: TextView, size: Float) {
    view.textSize = size
}

@BindingAdapter("edit_text_size")
fun setEditTextSize(view: EditText, size: Float) {
    view.textSize = size
}

@BindingAdapter("layout_width_dynamic")
fun setLayoutWidth(v: View, width: Int) {
    v.layoutParams.width = width
    v.requestLayout()
}

@BindingAdapter("layout_height_dynamic")
fun setLayoutHeight(v: View, height: Int) {
    v.layoutParams.height = height
    v.requestLayout()
}

@BindingAdapter("text_hint_color")
fun setTextHintColor(view: EditText, color: String) {
    view.setHintTextColor(Color.parseColor(color))
}

@BindingAdapter("edit_text_cursor")
fun setEditTextCursor(view: EditText, resInt: Int) {
    val f: Field = TextView::class.java.getDeclaredField("mCursorDrawableRes")
    f.isAccessible = true
    f.set(view, resInt)
}

@BindingAdapter("set_hint")
fun setHint(view: TextView, hint: String) {
    view.hint = hint
}

@BindingAdapter("recyclerViewViewModel")
fun setRecyclerViewViewModel(recyclerView: RecyclerView, viewModel: RecyclerViewViewModel?) {
    viewModel?.setupRecyclerView(recyclerView)
}

// TextInputLayout: Hint color ABOVE the EditText box when the editText IS focused
@BindingAdapter("focused_upper_text_hint_color")
fun setUpperHintColor(textInputLayout: TextInputLayout, color: String) {
    try {
        val field = TextInputLayout::class.java.getDeclaredField("focusedTextColor")
        field.isAccessible = true
        val states = arrayOf(intArrayOf())
        val colors = intArrayOf(Color.parseColor(color))
        val myList = ColorStateList(states, colors)
        field.set(textInputLayout, myList)
        val method = textInputLayout::class.java.getDeclaredMethod("updateLabelState", Boolean::class.javaPrimitiveType, Boolean::class.javaPrimitiveType)
        method.isAccessible = true
        method.invoke(textInputLayout, true, true)
    } catch (e: Exception) {
        LogUtils.E(LogUtils.FilterTags.withTags(LogUtils.TagFilter.THM), e)
    }
}

// TextInputLayout: Hint color INSIDE the EditText box when the editText IS NOT focused
@BindingAdapter("unfocused_edit_text_hint_color")
fun setUnfocusedUpperHintColor(textInputLayout: TextInputLayout, color: String) {
    try {
        val field = TextInputLayout::class.java.getDeclaredField("defaultHintTextColor")
        field.isAccessible = true
        val states = arrayOf(intArrayOf())
        val colors = intArrayOf(Color.parseColor(color))
        val myList = ColorStateList(states, colors)
        field.set(textInputLayout, myList)
        val method = textInputLayout::class.java.getDeclaredMethod("updateLabelState", Boolean::class.javaPrimitiveType, Boolean::class.javaPrimitiveType)
        method.isAccessible = true
        method.invoke(textInputLayout, true, true)
    } catch (e: Exception) {
        LogUtils.E(LogUtils.FilterTags.withTags(LogUtils.TagFilter.THM), e)
    }
}