package com.kralofsky.cipherbox.ciphers

import androidx.appcompat.app.AppCompatActivity
import com.kralofsky.cipherbox.*

object Rot13 : Cipher() {
    override val name = "ROT13"
    override val imageId = R.drawable.rot13
    override val link = "https://en.wikipedia.org/wiki/ROT13"


    override fun encode(cleartext: String): String = cleartext.mapLetters {
        it + 13.toAlphabetChar()
    }

    override fun decode(ciphertext: String): String = encode(ciphertext)

    override fun init(context: AppCompatActivity) {}
}
