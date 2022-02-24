package com.hardrelice.wyyparser.utils

import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.core.net.toUri
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.lang.ref.WeakReference


class UIHandler(activity: Activity) : Handler(Looper.getMainLooper()) {
    private var mActivity: WeakReference<Activity> = WeakReference(activity)
    var activity = activity

    companion object {
        const val RUN_ANY: Int = 0
        const val UPDATE_PROGRESS_BAR: Int = 1
        const val SET_IMAGE: Int = 2
        const val SET_IMAGE_VIEW: Int = 3
        const val SET_FRAGMENT: Int = 4
        const val UPDATE_FRAGMENT: Int = 5
    }

    fun send(what:Int, detail: UIDetail){
        val msg = Message()
        msg.what = what
        msg.obj = detail
        this.sendMessage(msg)
    }

    fun UpdateProgressBar(viewId: Int, progress: Int) {
        val progressBar: ProgressBar = activity.findViewById(viewId)
        progressBar.progress = progress
        println(progressBar.progress)
    }

    fun SetImage(view: ImageView, imgPath: String, ratio: Float) {
        try {
            val screenWidth: Int = view.width
            val lp: ViewGroup.LayoutParams = view.layoutParams
            lp.width = screenWidth
            lp.height = (screenWidth/ratio).toInt()
            println("height ${lp.height}")
//                ViewGroup.LayoutParams.WRAP_CONTENT
            view.layoutParams = lp
            Glide.with(activity.applicationContext).load(imgPath).into(view)
        } catch (e: Exception) {
            Log.e("SetImage", "glide exception")
        }
    }

    fun SetImageView(view: View, imgPath: String, ratio: Float) {
        val illustView = view as ImageView
        try {
            val screenWidth: Int = view.width
            val lp: ViewGroup.LayoutParams = illustView.layoutParams
            lp.width = screenWidth
            lp.height = (screenWidth/ratio).toInt()
//                ViewGroup.LayoutParams.WRAP_CONTENT
            illustView.layoutParams = lp

//            illustView.setMaxWidth(screenWidth)
//            illustView.setMaxHeight((screenWidth/ratio).toInt())
//            Glide.with(activity.applicationContext).load(imgPath).into(illustView)
            illustView.setImageURI(imgPath.toUri())
        } catch (e: Exception) {
            Log.e("SetImage", "glide exception")
        }
    }

    override fun handleMessage(msg: Message) {
        super.handleMessage(msg)
        var uiDetail: UIDetail = msg.obj as UIDetail
        println(Thread.currentThread().name)
        when (msg.what) {
            RUN_ANY -> {
                post {
                    uiDetail.runnable?.run()
                }
            }
            UPDATE_PROGRESS_BAR -> {
                post {
                    var percent = 0
                    if (uiDetail.int != 0) {
                        percent = uiDetail.int
                    } else {
                        percent = uiDetail.float.toInt()
                    }
                    UpdateProgressBar(uiDetail.id, percent)
                }
            }
            SET_IMAGE -> {
                post {
                    SetImage(uiDetail.view!! as ImageView, uiDetail.string, uiDetail.float)
                }
            }
            SET_IMAGE_VIEW -> {
                post {
                    SetImageView(uiDetail.view!!, uiDetail.string, uiDetail.float)
                }
            }
//            SET_FRAGMENT -> {
//                post {
//                    val view = uiDetail.view as RecyclerView
//                    val rank = uiDetail.obj as List<RankItem>
//                    view.adapter =
//                        RankAdapter(rank, uiDetail.activity as FragmentActivity)
//                }
//            }
//            UPDATE_FRAGMENT -> {
//                post {
//                    val view = uiDetail.view as RecyclerView
//                    val rank = uiDetail.obj as List<RankItem>
//                    var adapter = view.adapter as RankAdapter
//                    adapter!!.addRangeItem(uiDetail.int, rank.size, rank)
//                    view.adapter!!.notifyItemRangeInserted(uiDetail.int, rank.size)
//                }
//            }


        }
    }
}