package com.hardrelice.wyyparser.utils

import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

object Requests {
    val headers = hashMapOf("X-Real-IP" to "211.161.244.70",
        "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.120 Safari/537.36")

    class Response(val content: String){
        val json = JSONObject(this.content)
    }
    fun call(url: String, headers: Map<String, String> = this.headers): String {
        val conn = URL(url).openConnection() as HttpsURLConnection
        for(key in headers){
            conn.setRequestProperty(key.toString(), headers[key])
        }
        conn.setHostnameVerifier { _, session -> true }
        conn.connect()
        if(conn.responseCode==200) {
            val ips = conn.inputStream
            val br = BufferedReader(InputStreamReader(ips, "UTF-8"))
            val stb = StringBuffer()
            br.readText().let {
                println(it)
                return it }
        }
        return ""
    }

    fun get(url: String, headers: Map<String, String> = this.headers):Response{
        return Response(call(url, headers))
    }

}