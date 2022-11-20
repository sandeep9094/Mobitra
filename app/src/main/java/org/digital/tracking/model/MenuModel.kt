package org.digital.tracking.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

/**
 * @param iconResId Icon drawable resource id
 * @param titleResId Menu name string resource id, i.e. R.id.app_name in strings.xml
 */
data class MenuModel(
    @DrawableRes
    val iconResId: Int,
    @StringRes
    val titleResId: Int
)
