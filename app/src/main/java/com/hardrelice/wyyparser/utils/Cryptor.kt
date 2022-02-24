package com.hardrelice.wyyparser.utils

import android.util.Base64
import java.nio.charset.Charset
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

object Cryptor {


    fun pad(bytes:ByteArray):ByteArray {
        return bytes + ((16 - bytes.size % 16) times (16 - bytes.size % 16).toChar()).toByteArray(
            Charset.forName("utf-8"))
    }

    //加密
    fun encryptAES(input: ByteArray, password: ByteArray): String {
        //创建cipher对象
        val cipher = Cipher.getInstance("AES")
        //初始化cipher
        //通过秘钥工厂生产秘钥
        val keySpec: SecretKeySpec = SecretKeySpec(password, "AES")
        cipher.init(Cipher.ENCRYPT_MODE, keySpec)
        //加密、解密
        val encrypt = cipher.doFinal(input)
        val baseBytes = Base64.encode(encrypt, Base64.DEFAULT)
        var string = ""
        for (byte in baseBytes) {
            string += byte.toChar()
        }
        return string
    }

    //解密
    fun decryptAES(input: ByteArray, password: String): String {
        //创建cipher对象
        val cipher = Cipher.getInstance("AES")
        //初始化cipher
        //通过秘钥工厂生产秘钥
        val keySpec: SecretKeySpec = SecretKeySpec(password.toByteArray(), "AES")
        cipher.init(Cipher.DECRYPT_MODE, keySpec)
        //加密、解密
        val encrypt = cipher.doFinal(Base64.decode(input, Base64.DEFAULT))
        return String(encrypt)
    }

}