package com.hardrelice.wyyparser.utils

import java.io.File

object FileHandler {
    const val storageRoot = "/storage/emulated/0/"
    const val neteaseRoot = storageRoot + "netease/"
    const val cloudmusicRoot = neteaseRoot + "cloudmusic/"
    const val cloudmusicMusicRoot = cloudmusicRoot + "Music/"
    const val cloudmusicMusicRootParser = cloudmusicRoot + "Music/Parser/"
    const val cloudmusicCacheRoot = cloudmusicRoot + "Cache/"
    const val cloudmusicCacheDownloadRoot = cloudmusicCacheRoot + "Download/"
    const val cloudmusicCacheMusic1Root = cloudmusicCacheRoot + "Music1/"
    const val cloudmusicCacheLyricRoot = cloudmusicCacheRoot + "Lyric/"

    val externalCacheDir = ContextEverywhere.ApplicationContext?.externalCacheDir?.absolutePath
    fun String.listDirs():Array<String>? {
        return File(this).list()
    }

    fun join(vararg dirs: String): String {
        var retDir = ""
        for (dir in dirs) {
            retDir += "/$dir"
        }
        return retDir
    }

    fun checkDir(path: String): Boolean {
        var ret = true
        if (!File(path).exists()) {
            File(path).mkdir().also { ret = it }
        }
        return ret
    }

    fun getBaseName(path: String):String {
        var pathArr = path.split('/')
        var pt = pathArr.size-1
        while(pathArr[pt]==""){
            pt-=1
        }
        return pathArr[pt]
    }

    fun checkMusicFolder(musicId: String):String{
        if(!join(externalCacheDir!!, musicId).toFile().exists()){
            join(externalCacheDir, musicId).toFile().mkdirs()
        }
        return join(externalCacheDir, musicId)
    }


    fun getMusicBitrate(musicId:String):Array<String>{
        return join(externalCacheDir!!, musicId).toFile().listFileNames()
    }


}