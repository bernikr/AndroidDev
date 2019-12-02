package com.kralofsky.cipherbox.ciphers

import androidx.appcompat.app.AppCompatActivity
import com.kralofsky.cipherbox.*

object Atbash : Cipher() {
    override val name = "Atbash"
    override val description = ""
    override val imageId = R.drawable.alef
    override val link = "https://en.wikipedia.org/wiki/Atbash"


    override fun encode(cleartext: String): String = cleartext.mapLetters {
        ('Z'.toAlphabetInt() - it.toAlphabetInt()).toAlphabetChar()
    }

    override fun decode(ciphertext: String): String = encode(ciphertext)

    override fun init(context: AppCompatActivity) {}
}
