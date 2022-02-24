package com.hardrelice.wyyparser.utils

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.pm.PackageManager
import android.os.Build


object DynamicPermission {

    const val FINISH = 0
    const val WAIT = 1
    const val OK = 2

    private val PERMISSIONS = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    fun get(activity: Activity, requestCode: Int): Int {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                return OK
            }else{
                if (activity.shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    activity.requestPermissions(PERMISSIONS, requestCode)
                    return WAIT
                } else {
                    val builder = AlertDialog.Builder(activity)
                    builder.setTitle("请给予储存权限") //对话框标题
                        .setMessage("缺乏储存权限软件将无法正常访问音乐目录") //对话框内容
//                    .setIcon(R.drawable.) //对话框图标
                        .setCancelable(false) //点击对话框之外的部分是否取消对话框
                        .setPositiveButton(
                            "退出"
                        ) { dialog, which ->
                            // TODO Auto-generated method stub
                            activity.finish() //结束当前Activity
                        }
                    val dialog: Dialog = builder.create()
                    dialog.show()
                    return FINISH
                }
            }
        }
        return OK
    }




}