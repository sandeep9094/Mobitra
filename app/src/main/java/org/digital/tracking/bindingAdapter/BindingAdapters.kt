package org.digital.tracking.bindingAdapter

import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter

@BindingAdapter("app:setMenuIcon")
fun ImageView.setMenuIcon(@DrawableRes resId: Int) {
    this.setImageDrawable(ContextCompat.getDrawable(this.context, resId))
}

@BindingAdapter("app:setMenuTitle")
fun TextView.setMenuTitle(@StringRes resId: Int) {
    this.setText(resId)
}