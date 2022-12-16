package com.qinxin.common.util

import android.util.DisplayMetrics
import android.util.TypedValue
import com.qinxin.common.App

object PixelUtil {
    /** Convert from DIP to PX  */
    fun toPixelFromDIP(value: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            value,
            App.instance?.resources?.displayMetrics
        )
    }

    /** Convert from DIP to PX  */
    fun toPixelFromDIP(value: Double): Float {
        return toPixelFromDIP(value.toFloat())
    }

    /** Convert from SP to PX  */
    /** Convert from SP to PX  */
    @JvmOverloads
    fun toPixelFromSP(value: Float, maxFontScale: Float = Float.NaN): Float {
        val displayMetrics: DisplayMetrics = App.instance?.resources!!.displayMetrics
        var scaledDensity = displayMetrics.scaledDensity
        val currentFontScale = scaledDensity / displayMetrics.density
        if (maxFontScale >= 1 && maxFontScale < currentFontScale) {
            scaledDensity = displayMetrics.density * maxFontScale
        }
        return value * scaledDensity
    }

    /** Convert from SP to PX  */
    fun toPixelFromSP(value: Double): Float {
        return toPixelFromSP(value.toFloat())
    }

    /** Convert from PX to DP  */
    fun toDIPFromPixel(value: Float): Float {
        return value / App.instance?.resources?.displayMetrics!!.density
    }
}
