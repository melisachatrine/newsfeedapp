package com.kanchanpal.newsfeed.helper

import android.util.Base64
import com.kanchanpal.newsfeed.api.NewsListModel
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

private var randomNumber = ""
var listFavorites: ArrayList<NewsListModel> = arrayListOf()

fun addlist(item: NewsListModel) {
    listFavorites.add(item)
}

fun removelist(item: NewsListModel) {
    listFavorites.remove(item)
}

fun String.encrypt() :  String?
{
    val secretKey = "newsappsecretkey"

    randomNumber = ""
    generateRandomString()
    val iv = randomNumber

    randomNumber = ""
    generateRandomString()
    val salt = randomNumber

    val ivB64: ByteArray
    val saltB64: ByteArray
    val valueB64: ByteArray
    val resultb64: ByteArray

    ivB64 = iv.toByteArray(charset("UTF-8"))
    val strIvB64 = Base64.encodeToString(ivB64, Base64.NO_WRAP)

    saltB64 = salt.toByteArray(charset("UTF-8"))
    val strSaltB64 = Base64.encodeToString(saltB64, Base64.NO_WRAP)

    valueB64 = this.toByteArray(charset("UTF-8"))
    val strValueB64 = Base64.encodeToString(valueB64, Base64.DEFAULT)

    try {
        val ivParameterSpec = IvParameterSpec(Base64.decode(strIvB64, Base64.NO_WRAP))

        val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
        val spec =  PBEKeySpec(secretKey.toCharArray(), Base64.decode(strSaltB64, Base64.NO_WRAP), 1000, 256)
        val tmp = factory.generateSecret(spec)
        val secretKey =  SecretKeySpec(tmp.encoded, "AES")

        val cipher = Cipher.getInstance("AES/CBC/PKCS7Padding")
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec)

        val strAES = Base64.encodeToString(cipher.doFinal(strValueB64.toByteArray(Charsets.UTF_8)), Base64.DEFAULT)
        val result = strIvB64.toString()+"::"+strSaltB64.toString()+"::"+strAES

        resultb64 = result.toByteArray(charset("UTF-8"))
        val strEncrypt = Base64.encodeToString(resultb64, Base64.NO_WRAP)

        return strEncrypt
    } catch (e: Exception) {
        println("Error while encrypting: $e")
    }
    return null
}

fun generateRandomString(): String? {
    try {
        val chars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
        for (i in 0..15) {
            randomNumber += chars[Math.floor(Math.random() * chars.length).toInt()]
        }
        return randomNumber
    } catch (e : Exception) {
        println("Error while generate random number: $e");
    }
    return null
}