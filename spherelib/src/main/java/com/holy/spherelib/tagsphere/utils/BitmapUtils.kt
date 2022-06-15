package com.holy.spherelib.tagsphere.utils

import android.graphics.Bitmap
import android.graphics.Matrix
import androidx.annotation.NonNull

object BitmapUtils{
    open fun createScaledBitmap(@NonNull src: Bitmap, dstWidth :Int, dstHeight:Int,
                                filter:Boolean):Bitmap {
        var m = Matrix()

        val width = src.getWidth()
        val height = src.getHeight()
        if (width != dstWidth || height != dstHeight) {
            val sx = dstWidth / width.toFloat()
            val sy = dstHeight / height.toFloat()
            m.setScale(sx, sy)
        }
        return Bitmap.createBitmap(src, 0, 0, width, height, m, filter);
    }
}