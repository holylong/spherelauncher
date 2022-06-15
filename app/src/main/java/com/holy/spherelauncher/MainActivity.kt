package com.holy.spherelauncher

import android.app.Activity
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.holy.spherelauncher.R
import com.holy.spherelauncher.VectorDrawableWithNameTagItem
import com.holy.spherelib.packutils.AppItem
import com.holy.spherelib.packutils.KHooyPackageManager
import com.holy.spherelib.tagsphere.OnTagTapListener
import com.holy.spherelib.tagsphere.item.TagItem
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : Activity() , OnTagTapListener {

    companion object{
        val TAG = MainActivity.javaClass.simpleName
        var appInfos:List<AppItem>?=null
    }

    private var mPopup: PopupWindow? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        supportActionBar?.hide()
        setContentView(R.layout.activity_main)
        initAppView()
    }

    private fun initAppView(){
        val tags = mutableListOf<VectorDrawableWithNameTagItem>()
        //var appInfos = parentFragment?.activity?.let { KHooyPackageManager.getAppInfos(it) }
        //var appInfos = KHooyPackageManager.getAppInfos(activity?.applicationContext)
        appInfos = KHooyPackageManager.getAppInfos(applicationContext, 600, false, false)
        val tmpAppInfos = appInfos
        if (tmpAppInfos != null) {
            Log.d("==", "get all app size:" + tmpAppInfos.size)
            tmpAppInfos.forEach{
                Log.d("==", "app icon width:" + it.icon.width + " height:" + it.icon.height)
                //if(id < 20)
                tags.add(VectorDrawableWithNameTagItem(BitmapDrawable(it.icon), it.name))
            }

            Log.d("==", "tags size:" + tags.size)
            tagSphereView.addTagList(tags)
            tagSphereView.setRadius(1.75f)  //设置半径
        }else{
            Log.d("==", "is null")
        }

        tagSphereView.setOnTagTapListener(this)
    }

    override fun onTap(tagItem: TagItem) {
        Log.d(TAG, "Desktop tap:" + (tagItem as VectorDrawableWithNameTagItem).text)
        val intent = packageManager.getLaunchIntentForPackage((tagItem as VectorDrawableWithNameTagItem).text)
        startActivity(intent)
    }
}