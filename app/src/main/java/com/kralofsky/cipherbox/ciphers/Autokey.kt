package com.kralofsky.cipherbox.ciphers

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.kralofsky.cipherbox.*

object Autokey : Cipher() {
    override val name = "Autokey Cipher"
    override val imageId = R.drawable.vigenere_portrait
    override val controlLayout = R.layout.cipher_single_key
    override val link = "https://en.wikipedia.org/wiki/Autokey_cipher"
    override val youtube = "QJcQY9GY850"

    private var key: String = ""

    override fun encode(cleartext: String): String =
        (key + cleartext.clean())
            .zip(cleartext.clean())
            .map {it.first + it.second}
            .joinToString("")
            .clean()

    override fun decode(ciphertext: String): String {
        var dynamicKey = key

        return ciphertext.clean().mapIndexed { i, it ->
            val c = it + dynamicKey[i].inverse()
            dynamicKey += c
            c
        }.joinToString("")
    }


    override fun init(context: AppCompatActivity) {
        context.findViewById<EditText>(R.id.cipher_single_key).addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                key = p0.toString().mapCharByChar {
                    if (!it.isLetter()) null
                    else it.toUpperCase()
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })
    }

}
