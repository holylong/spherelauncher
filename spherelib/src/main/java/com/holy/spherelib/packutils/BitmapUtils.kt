package com.holy.spherelib.packutils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.PixelFormat
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable

class BitmapUtils {

    fun drawableToBitmap(ctx: Context, id:Int):Bitmap{
        var res = ctx.resources
        return BitmapFactory.decodeResource(res, id);
    }

    fun bitmapToDrawable(bmp : Bitmap):Drawable{
        return BitmapDrawable(bmp)
    }

    fun drawableToBitmap(drawable:Drawable):Bitmap {
        var bitmap = Bitmap.createBitmap(
                drawable.intrinsicWidth,
                drawable.intrinsicHeight,
        if(drawable.opacity !=PixelFormat.OPAQUE)
                 Bitmap.Config.ARGB_8888
                else Bitmap.Config.RGB_565)

        var canvas = Canvas(bitmap);

        //canvas.setBitmap(bitmap);

        drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight);
        drawable.draw(canvas);
        return bitmap;
    }
}