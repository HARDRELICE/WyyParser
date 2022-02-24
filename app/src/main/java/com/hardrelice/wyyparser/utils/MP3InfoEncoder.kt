package com.hardrelice.wyyparser.utils

object MP3InfoEncoder {
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

    data class Frame(
        var tag: Tag? = null,
        var data: ByteArray
    )

    fun genFrameSize(size: Int): ByteArray {

        val first = size / 0x100000000
        val firstLeft = size % 0x100000000
        val second = firstLeft / 0x10000
        val secondLeft = firstLeft % 0x10000
        val third = secondLeft / 0x100
        val forth = secondLeft % 0x100
        return byteArrayOf(first.toByte(), second.toByte(), third.toByte(), forth.toByte())
    }

    fun genHeadSize (size:Int):ByteArray{
        val x = arrayOf(0, 0, 0, 0)
        x[0] = size shr 21
        var left = x[0] shl 21 xor size
        x[1] = left shr 14
        left = x[1] shl 14 xor left
        x[2] = left shr 7
        x[3] = x[2] shl 7 xor left
        println(x.toList())
        return byteArrayOf(x[0].toByte(),x[1].toByte(),x[2].toByte(),x[3].toByte())
    }

    fun generateHeadFrame(size: Int): ByteArray {
        return "ID3".toByteArray()+ byteArrayOf(0x03,0x00,0x00) + genHeadSize(size)
    }

    fun generateFrame(tagName: String, data: ByteArray): ByteArray {
        val tag = Tag(
            name = tagName.toByteArray(),
            size = genFrameSize(data.size),
            none = byteArrayOf(0x00, 0x00)
        )
        val frame = Frame(
            tag = tag,
            data = data
        )
        return tagName.toByteArray() + genFrameSize((data.size)) + byteArrayOf(0x00, 0x00) + data
    }

    fun generateInfo(hashMap: HashMap<String,ByteArray>):ByteArray{
        var frames = byteArrayOf()
        for (obj in hashMap){
            val frame = generateFrame(obj.key, obj.value)
            frames+=frame
        }

        return generateHeadFrame(frames.size)+frames
    }

}