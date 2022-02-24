package com.hardrelice.wyyparser.utils

import java.io.*
import kotlin.experimental.xor

object Uc2Mp3 {
    fun translate(ucPath: String, mp3Path: String){
        println("start!")
        val bytes = ucPath.toFile().readBytes()
        var newBytes = byteArrayOf()
        for (b in bytes){
            newBytes+= b xor 0xa3.toByte()
        }
        mp3Path.toFile().writeBytes(newBytes)
        println("finished!")
    }

    fun translate0(ucPath: String, mp3Path: String){
        val inFile = File(ucPath)
        val outFile = File(mp3Path)

        val dis = DataInputStream(FileInputStream(inFile))
        val dos = DataOutputStream(FileOutputStream(outFile))
        val b = ByteArray(1024)
        var len: Int
        while (dis.read(b).also { len = it } != -1) {
            for (i in 0 until len) {
                b[i] = b[i] xor 0xa3.toByte()
            }
            dos.write(b, 0, len)
        }
    }
}