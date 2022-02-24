package com.hardrelice.wyyparser.utils

import java.io.File
import java.nio.charset.Charset

object MP3InfoDecoder {

    data class Tag(
        var name: ByteArray = ByteArray(4),
        var size: ByteArray = ByteArray(4),
        var none: ByteArray = ByteArray(2)
    )

    data class Head(
        var name: ByteArray = ByteArray(3),
        var version: ByteArray = ByteArray(2),
        var flag: ByteArray = ByteArray(1),
        var size: ByteArray = ByteArray(4)
    )

    fun getHeadSize(byte: ByteArray): Int {
        return (byte[0].toInt() shl 21) or (byte[1].toInt() shl 14) or (byte[2].toInt() shl 7) or byte[3].toInt()
    }

    fun getFrameSize(byte: ByteArray): Long {
        return ((byte[0].toInt() and 0xff) * 0x100000000 + (byte[1].toInt() and 0xff) * 0x10000 + (byte[2].toInt() and 0xff) * 0x100 + (byte[3].toInt() and 0xff))
    }

    fun getTagName(byte: ByteArray): String {
        var str = ""
        for (b in byte) {
            str += b.toChar()
        }
        return str
    }


    fun decode(filePath: String): HashMap<String, Any> {
        val file = File(filePath)
        val bytes = file.inputStream()
        val info = ByteArray(10)
        bytes.read(info)
        val head = Head(
            info.sliceArray(0 until 3),
            info.sliceArray(3 until 5),
            info.sliceArray(5 until 6),
            info.sliceArray(6 until 10),
        )

        val frame = ByteArray(getHeadSize(head.size))
        bytes.read(frame)
        val frameInputStream = frame.inputStream()
        var x = 10
        var rawInfoMap = hashMapOf<String, Any>()
        while (frameInputStream.read(info) != -1) {
            val tag = Tag(
                info.sliceArray(0 until 4),
                info.sliceArray(4 until 8),
                info.sliceArray(9 until 10)
            )

            if (getFrameSize(tag.size).toInt() == 0) break
            val tagName = getTagName(tag.name)
            val data = ByteArray(getFrameSize(tag.size).toInt())
            frameInputStream.read(data)
            when (tagName) {
                // 注释
                "COMM" -> {
                    rawInfoMap[tagName] =
                        data.slice(5 until data.size).toByteArray()
                            .toString(Charset.forName("utf-8"))
                }
                // 专辑名称, 歌曲名, 作者
                "TALB", "TIT2", "TPE1" -> {
                    var finalString = ""
                    var first = false
                    val sliced = data.slice(3 until data.size)
                    println(data.toUByteArray())
                    for (i in 0 until sliced.size / 2) {
                        val char = sliced[i * 2 + 1]*0x100 + sliced[i * 2]
                        println(char and 0xffff)
                        println(char.toChar())
                        finalString += char.toChar()
                    }
                    rawInfoMap[tagName] = finalString
                }
                // mp3编码器
                "TSSE" -> {
                    rawInfoMap[tagName] = data
                }
                // 未知
                "TPOS" -> {
                    rawInfoMap[tagName] = data
                }
                // 未知
                "TRCK" -> {
                    rawInfoMap[tagName] = data
                }
                // 封面图片
                "APIC" -> {
                    var zeros = 0
                    var pointer = 0
                    println(0.toByte())
                    for (b in data) {
                        println(b)
                        if (b == 0.toByte()) zeros += 1
                        else zeros = 0
                        if (zeros == 3) {
                            rawInfoMap[tagName] = data.slice(pointer until data.size).toByteArray()
                            break
                        }
                        pointer += 1
                    }
//                    println(data.size)
//                    rawInfoMap[tagName] = data.slice(13 until data.size).toByteArray()
                }
            }
            x -= 1
        }
        return rawInfoMap
    }
}