package com.kralofsky.cipherbox.ciphers

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.kralofsky.cipherbox.*

object PolybusSquare : Cipher() {
    override val name = "Polybus Square"
    override val imageId = R.drawable.polybus
    override val controlLayout = R.layout.cipher_single_key
    override val link = "https://en.wikipedia.org/wiki/Polybius_square"
    override val youtube: String = "a-S0uili1ZY"

    private var alphabet: List<Char> = ('A'..'Z').toList()

    override fun encode(cleartext: String): String = cleartext
            .replace('j', 'i')
            .replace('J', 'I')
            .map {
                if (it.isLetter()) {
                    val index = alphabet.indexOf(it.toUpperCase())
                    (index/5).toAlphabetChar() + "" + (index%5).toAlphabetChar()
                } else {
                    it.toString()
                }
            }
            .joinToString("")

    override fun decode(ciphertext: String): String = ciphertext
        .map { if (it.isLetter()) it.toString() else it+""+it}
        .joinToString("")
        .chunked(2) {
            if(it.length<2) it
            else {
                val a = it[0].toAlphabetInt()
                val b = it[1].toAlphabetInt()
                val index = a * 5 + b

                if (!it[0].isLetter()) it[0].toString()
                else if (a in 0..5 && b in 0..5) alphabet[index].toLowerCase().toString()
                else it
            }
        }
        .joinToString("")

    override fun init(context: AppCompatActivity) {
        context.findViewById<EditText>(R.id.cipher_single_key).addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                val key = p0.toString().mapCharByChar {
                    if (!it.isLetter()) null
                    else it.toUpperCase()
                }
                alphabet =  (key.toCharArray() + ('A'..'Z').toList()).distinct().filter { it != 'J' }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })
    }
}
