package com.hardrelice.wyyparser.utils

import com.hardrelice.wyyparser.utils.FileHandler.listDirs
import org.json.JSONArray
import org.json.JSONObject
import java.io.FilenameFilter

object Core {
    data class Song(val bitrate: Int, val the163key: ByteArray)
    data class FinalSong(val path: String, val bitrate: Int, val songId: String)

    fun scanCache() {
        println(FileHandler.externalCacheDir)
        val scannedList = FileHandler.externalCacheDir?.toFile()?.listFileNames()
        val cacheList =
            FileHandler.cloudmusicCacheMusic1Root.toFile().list(FilenameFilter { dir, name ->
                return@FilenameFilter name.endsWith(".mp3.uc!")
            })
        val map = hashMapOf<String, Song>()
        if (cacheList == null || cacheList.isEmpty()) {
            println("empty")
            return
        }
        for (file in cacheList) {
            println(file)
            val parsed = Parser.parse(file)
            if (parsed != null) {
                if (parsed.musicId in scannedList!! && parsed.bitrate in FileHandler.getMusicBitrate(
                        parsed.musicId
                    )
                ) continue

                val musicFolder = FileHandler.checkMusicFolder(parsed.musicId)
                val bitrateFile = FileHandler.join(musicFolder, parsed.bitrate).toFile()

//                if(parsed.musicId in map){
//                    if(map[parsed.musicId]!!.bitrate < parsed.bitrate.toInt()) map[parsed.musicId] = Song(parsed.bitrate.toInt(), parsed.the163key)
//                    else continue
//                } else {
//                    map[parsed.musicId] = Song(parsed.bitrate.toInt(), parsed.the163key)
//                }
                val artists = parsed.info["artist"] as JSONArray
                var artistsString = ""
                for (num in 0 until (artists.length())) {
                    val artist = artists[num] as JSONObject
                    artistsString += artist.getString("name")
                    if (num != artists.length() - 1) artistsString += "/"
                }
                println(artistsString)
                println(artistsString.toByteArray())
                println(parsed.info["album"].toString())
                val info = MP3InfoEncoder.generateInfo(
                    hashMapOf(
                        "TSSE" to byteArrayOf(
                            0x00,
                            0x4c,
                            0x61,
                            0x76,
                            0x66,
                            0x35,
                            0x37,
                            0x2e,
                            0x32,
                            0x35,
                            0x2e,
                            0x31,
                            0x30,
                            0x30
                        ),
                        "TPOS" to byteArrayOf(0x01, 0xff.toByte(), 0xfe.toByte(), 0x01, 0x00),
                        "TRCK" to byteArrayOf(0x01, 0xff.toByte(), 0xfe.toByte(), 0x02, 0x00),
                        "TPE1" to artistsString.toMP3ReadableByteArray(),
                        "TIT2" to parsed.info["musicName"].toString().toMP3ReadableByteArray(),
                        "TALB" to parsed.info["album"].toString().toMP3ReadableByteArray(),
                        "COMM" to byteArrayOf(0x00, 0x58, 0x58, 0x58, 0x00) + parsed.the163key
                    )
                )
                bitrateFile.writeBytes(info)
            }
        }
    }

    fun loadCacheToLocal() {
        val cacheList =
            FileHandler.cloudmusicCacheMusic1Root.toFile().list(FilenameFilter { dir, name ->
                return@FilenameFilter name.endsWith(".mp3.uc!")
            })

        var finalMap = hashMapOf<String, FinalSong>()

        for (path in cacheList) {
            Parser.parseFileName(path).let {
                if (it[0] !in finalMap.keys) {
                    finalMap[it[0].toString()] = FinalSong(
                        FileHandler.join(FileHandler.cloudmusicCacheMusic1Root, path),
                        it[1]!!.toInt(),
                        it[0].toString()
                    )
                } else {
                    if (it[1]!!.toInt() > finalMap[it[0].toString()]!!.bitrate) {
                        finalMap[it[0].toString()] =
                            FinalSong(
                                FileHandler.join(FileHandler.cloudmusicCacheMusic1Root, path),
                                it[1]!!.toInt(),
                                it[0].toString()
                            )
                    }
                }
            }
        }
        for (key in finalMap.keys) {
            FileHandler.checkDir(FileHandler.cloudmusicMusicRootParser)
            val fromFile = finalMap[key]!!.path
            val toFile = FileHandler.join(
                FileHandler.cloudmusicMusicRootParser,
                finalMap[key]!!.songId + "-" + finalMap[key]!!.bitrate + ".mp3"
            )
            println(fromFile)
            println(toFile)
            Uc2Mp3.translate0(fromFile, toFile)
        }
    }
}