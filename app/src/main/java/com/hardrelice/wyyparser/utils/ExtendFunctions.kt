package com.hardrelice.wyyparser.utils

import java.io.File

fun String.toFile():File{
    return File(this)
}

fun String.toMP3ReadableByteArray():ByteArray{
    fun reverse(char:Char):ByteArray{
        val second = char.toInt() shr 8
        val first = (second shl 8) xor char.toInt()
        return byteArrayOf(first.toByte(), second.toByte())
    }

    var temp = byteArrayOf()
    for (char in this){
        temp+=reverse(char)
    }
    return temp
}

fun File.listFileNames():Array<String>{
    if(this.list()==null){
        return arrayOf()
    }else {
        this.list().let {
            val paths = mutableListOf<String>()
            for (path in it) {
                paths.add(FileHandler.getBaseName(path))
            }
            return paths.toTypedArray()
        }
    }
}