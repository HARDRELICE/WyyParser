package com.hardrelice.wyyparser.utils

import java.io.BufferedOutputStream
import java.io.BufferedReader
import java.io.FileOutputStream
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

fun test(){
    val map = MP3InfoDecoder.decode("/storage/emulated/0/test.mp3")
    for (key in map){
        println(key.toString())
        val data = map[key] as ByteArray
        if(key.toString()!="APIC") println(data.toUByteArray())
        else println(data.toUByteArray().slice(0 until 128))
    }
}

val headers = hashMapOf<String, String>(
    "X-Real-IP" to "211.161.244.70",
    "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.120 Safari/537.36"
)

fun call(url: String) {
    val filePath="storage/emulated/0/test.txt"
    val conn = URL(url).openConnection() as HttpsURLConnection
    for(key in headers){
        conn.setRequestProperty(key.toString(), headers[key].toString())
    }
    conn.setHostnameVerifier { _, session -> true }
    conn.connect()
    val ips = conn.inputStream
    val br = BufferedReader(InputStreamReader(ips, "UTF-8"))
    val stb = StringBuffer()
    println(br.readText())
//    conn.inputStream.use { input ->
//        BufferedOutputStream(FileOutputStream(filePath)).use { output ->
//            input.copyTo(output) //将文件复制到本地 其中copyTo使用方法可参考我的Io流笔记
//        }
//    }

    return
}



fun main(){
    println(call("https://www.baidu.com"))
}