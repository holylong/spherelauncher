package com.holy.spherelib.packutils

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log
import androidx.annotation.NonNull

class KHooyPackageManager{

    companion object{

        // 通过packName得到PackageInfo，作为参数传入即可
        private fun isSystemApp(pi: PackageInfo): Boolean {
            val isSysApp =
                pi.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM === 1
            val isSysUpd =
                pi.applicationInfo.flags and ApplicationInfo.FLAG_UPDATED_SYSTEM_APP === 1
            return isSysApp || isSysUpd
        }

        fun bitMapScale(
            bitmap: Bitmap,
            scale: Float
        ): Bitmap? {
            val matrix = Matrix()
            matrix.postScale(scale, scale) //长和宽放大缩小的比例
            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        }

        fun scaleBitmap(@NonNull src:Bitmap, disw:Int, dish:Int, filter: Boolean):Bitmap?{
            //精确缩放到指定大小
            Log.d(TAG, "sx:" + src.width + " sy:" + src.height + " width:" + disw + " height:" + dish)
            return Bitmap.createScaledBitmap(src, disw, dish, filter)
        }

        private val TAG = KHooyPackageManager.javaClass.simpleName
        open fun createScaledBitmap(@NonNull src: Bitmap, dstWidth :Int, dstHeight:Int,
                                    filter:Boolean):Bitmap {
            var m = Matrix()

            val width = src.width
            val height = src.height
            if (width != dstWidth || height != dstHeight) {
                val sx = dstWidth / width.toFloat()
                val sy = dstHeight / height.toFloat()
                Log.d(TAG, "sx:" + sx + " sy:" + sy + " width:" + dstWidth + " height:" + dstHeight)
                m.setScale(sx, sy)
            }
            return Bitmap.createBitmap(src, 0, 0, width, height, m, filter);
        }

        fun getAppIcons(ctx: Context):List<Bitmap> {
            var packages = ArrayList<Bitmap>()
            try {
                var packageInfos = ctx.packageManager.getInstalledPackages(PackageManager.GET_ACTIVITIES or PackageManager.GET_SERVICES)
                for (info in packageInfos) {
                    var bm = ctx.packageManager.getApplicationIcon(info.applicationInfo) as BitmapDrawable
                    packages.add(bm.bitmap)
                }
            } catch (t:Throwable) {
                t.printStackTrace();
            }
            return packages;
        }

        /**
         * 获取所有app列表
         */
        fun getAppInfos(ctx: Context?, size:Int, withText:Boolean, withSystemApps:Boolean):List<AppItem>{
            var packages = ArrayList<AppItem>()
            try {
                //var packageInfos = ctx.packageManager.getInstalledPackages(PackageManager.GET_ACTIVITIES or PackageManager.GET_SERVICES)
                var packageInfos = ctx?.packageManager?.getInstalledPackages(PackageManager.GET_ACTIVITIES)
                Log.d("==", "app size:" + packageInfos?.size)
                var  inx = 0
                if (packageInfos != null) {
                    for (info in packageInfos) {
                        if(!withSystemApps)      //如果不需要systemapp，就不进行系统app判断  判断是否是如果是就跳过
                                if(isSystemApp(info))continue

                        Log.d("==", " index:" + inx++ + " name:" + info.packageName)
                        //var icon = ctx?.packageManager?.getApplicationIcon(info.applicationInfo)
                        var icon = info.applicationInfo.loadIcon(ctx?.packageManager)
                        Log.d("==", "load icon")
                        if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) and (icon is Drawable)){
                            Log.d("==", "drawble转化成图片")
                            var bitmap = icon?.intrinsicWidth?.let { Bitmap.createBitmap(it, icon.intrinsicHeight, Bitmap.Config.ARGB_8888) };

                            var canvas = bitmap?.let { Canvas(it) };
                            if (icon != null) {
                                if (canvas != null) {
                                    icon.setBounds(0, 0, canvas.getWidth(), canvas.getHeight())
                                }
                            };
                            if (canvas != null) {
                                icon?.draw(canvas)
                            };
                            var name = info.packageName
                            Log.d("==" , "插入到用户数据 width:" + bitmap?.width + " height:" + bitmap?.height)

                            if(icon != null)
                                bitmap?.let {
                                    bitmap-> scaleBitmap(bitmap, size, size, true)?.let {
                                        Log.d("==>", "width:" + it.width + " height:" + it.height)
                                        AppItem(it, name, icon)?.let {
                                            packages.add(it)
                                        }
                                    }
                                }
                        }else{
                            Log.d("==", "直接获取图片")
                            var bm = ctx?.packageManager?.getApplicationIcon(info.applicationInfo) as BitmapDrawable
                            var name = info.packageName
                            //icon?.let { AppItem(bm.bitmap, name, it) }?.let { packages.add(it) }
                            icon?.let { AppItem(createScaledBitmap(bm.bitmap, 120, 120, true), name, it) }?.let { packages.add(it) }
                        }
                    }
                }
            } catch (t:Throwable) {
                t.printStackTrace();
            }
            return packages;
        }
    }
}