package com.hardrelice.wyyparser.utils

import android.util.JsonWriter
import com.google.gson.JsonArray
import org.json.JSONArray
import org.json.JSONObject
import java.io.Writer
import java.nio.charset.Charset

object Parser {

    data class Parsed(
        val the163key: ByteArray,
        val musicId: String,
        val bitrate: String,
        val mp3DocId: String,
        val info:HashMap<String,Any>
    )

    val key = byteArrayOf(
        0x23,
        0x31,
        0x34,
        0x6C,
        0x6A,
        0x6B,
        0x5F,
        0x21,
        0x5C,
        0x5D,
        0x26,
        0x30,
        0x55,
        0x3C,
        0x27,
        0x28
    )

    fun parseFileName(filePath: String): List<String?> {
        val fileName = FileHandler.getBaseName(filePath)
        val arr = fileName.split('-')
        var songId: String? = null
        var md5key: String? = null
        var quality: String? = null
        if (arr.size > 0) {
            songId = arr[0]
        }
        if (arr.size > 1) {
            quality = arr[1]
        }
        if (arr.size > 2) {
            md5key = arr[2].replace(".mp3.uc!", "")
        }
        return listOf(songId, quality, md5key)
    }

    fun getSongInfo(musicId: String, bitrate: String, mp3DocId: String): HashMap<String, Any> {
        val song = Requests.get("https://music.163.com/api/song/detail?ids=[$musicId]").json.getJSONArray("songs").getJSONObject(0)
        val artistsArray = mutableListOf<Array<Any>>()
//        for (num in 0 until (song.getJSONArray("artists").length())) {
//            val artist = song.getJSONArray("artists")[num] as JSONObject
//            JSONArray(arrayOf(artist["name"] as String, artist["id"] as Int))
//        }
        println(song.getJSONArray("artists"))
        return hashMapOf<String, Any>(
            "album" to song.getJSONObject("album").get("name"),
            "albumId" to song.getJSONObject("album").get("id"),
            "albumPic" to song.getJSONObject("album").get("picUrl"),
            "albumPicDocId" to song.getJSONObject("album").get("pic"),
            "alias" to song["alias"],
            "artist" to song.getJSONArray("artists"),
            "musicId" to song["id"],
            "musicName" to song["name"],
            "mvId" to song["mvid"],
            "transNames" to "",
            "format" to "mp3",
            "bitrate" to bitrate,
            "duration" to song["duration"],
            "mp3DocId" to mp3DocId
        )
    }

    fun generateArtistsList(artists: JSONArray): String {
        var string = "["
        for (num in 0 until (artists.length())) {
            val artist = artists[num] as JSONObject
            string += """["${artist.getString("name")}",${artist.getInt("id")}]"""
            if (num != artists.length() - 1) string += ","
        }
        string += "]"
        return string
    }

    fun generate163Key(
        musicId: String,
        musicName: String,
        artist: JSONArray,
        albumId: String,
        albumName: String,
        albumPicDocId: String,
        albumPic: String,
        bitrate: String,
        mp3DocId: String,
        duration: String,
        mvId: String,
        format: String
    ): ByteArray {
        val artistsList = generateArtistsList(artist)
        val padded = Cryptor.pad(
            """music:{"musicId":$musicId,"musicName":"$musicName","artist":$artistsList,"albumId":$albumId,"album":"$albumName","albumPicDocId":"$albumPicDocId","albumPic":"$albumPic","bitrate":$bitrate,"mp3DocId":"$mp3DocId","duration":$duration,"mvId":$mvId,"alias":[],"transNames":[],"format":"$format"}""".toByteArray(
                Charset.defaultCharset()
            )
        )
        val data = Cryptor.encryptAES(padded, key).toByteArray(Charset.defaultCharset())
        return "163 key(Don't modify):".toByteArray(Charset.defaultCharset()) + data
    }

    fun parse(filePath: String): Parsed? {
        if(!filePath.endsWith(".mp3.uc!")) return null
        val fileName = FileHandler.getBaseName(filePath)
        val detail = parseFileName(fileName)
        if (null in detail) return null
        val musicId = detail[0] as String
        val bitrate = detail[1] as String
        val mp3DocId = detail[2] as String
        val info = getSongInfo(musicId, bitrate, mp3DocId)
        return Parsed(generate163Key(
            musicId = musicId,
            musicName = info["musicName"].toString(),
            artist = info["artist"] as JSONArray,
            albumId = info["albumId"].toString(),
            albumName = info["album"].toString(),
            albumPicDocId = info["albumPicDocId"].toString(),
            albumPic = info["albumPic"].toString(),
            bitrate = info["bitrate"].toString(),
            mp3DocId = info["mp3DocId"].toString(),
            duration = info["duration"].toString(),
            mvId = info["mvId"].toString(),
            format = info["format"].toString()
        ),musicId,bitrate, mp3DocId,info)
    }

}