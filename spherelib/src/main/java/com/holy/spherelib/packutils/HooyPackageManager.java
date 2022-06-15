package com.holy.spherelib.packutils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class HooyPackageManager {
    public  static List<Bitmap> getAppIcons(Context ctx){
        List<Bitmap> packages = new ArrayList<Bitmap>();
        try {
            List<PackageInfo> packageInfos = ctx.getPackageManager().getInstalledPackages(PackageManager.GET_ACTIVITIES |
                    PackageManager.GET_SERVICES);
            for (PackageInfo info : packageInfos) {
                Drawable icon = ctx.getPackageManager().getApplicationIcon(info.applicationInfo);
                BitmapDrawable bd = (BitmapDrawable) icon;
                Bitmap bm = bd.getBitmap();
                packages.add(bm);
            }
        } catch (Throwable t) {
            t.printStackTrace();;
        }
        return packages;
    }

    private List<String> getPkgList() {
        List<String> packages = new ArrayList<String>();
        try {
            Process p = Runtime.getRuntime().exec("pm list packages");
            InputStreamReader isr = new InputStreamReader(p.getInputStream(), "utf-8");
            BufferedReader br = new BufferedReader(isr);
            String line = br.readLine();
            while (line != null) {
                line = line.trim();
                if (line.length() > 8) {
                    String prefix = line.substring(0, 8);
                    if (prefix.equalsIgnoreCase("package:")) {
                        line = line.substring(8).trim();
                        if (!TextUtils.isEmpty(line)) {
                            packages.add(line);
                        }
                    }
                }
                line = br.readLine();
            }
            br.close();
            p.destroy();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return packages;
    }

    private List<String> getPkgListNew(Context ctx) {
        List<String> packages = new ArrayList<String>();
        try {
            List<PackageInfo> packageInfos = ctx.getPackageManager().getInstalledPackages(PackageManager.GET_ACTIVITIES |
                    PackageManager.GET_SERVICES);
            for (PackageInfo info : packageInfos) {
                String pkg = info.packageName;
                packages.add(pkg);
            }
        } catch (Throwable t) {
            t.printStackTrace();;
        }
        return packages;
    }

    // 通过packName得到PackageInfo，作为参数传入即可
    private boolean isSystemApp(PackageInfo pi) {
        boolean isSysApp = (pi.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 1;
        boolean isSysUpd = (pi.applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) == 1;
        return isSysApp || isSysUpd;
    }


}
