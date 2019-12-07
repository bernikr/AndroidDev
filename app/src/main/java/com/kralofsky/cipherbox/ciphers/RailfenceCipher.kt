package com.kralofsky.cipherbox.ciphers

import android.os.Build
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.kralofsky.cipherbox.*
import kotlin.math.floor

object RailfenceCipher : Cipher() {
    override val name = "Railfence Cipher"
    override val imageId = R.drawable.railfence
    override val controlLayout = R.layout.cipher_slider
    override val link = "https://en.wikipedia.org/wiki/Rail_fence_cipher"

    var key = 1

    override fun encode(cleartext: String): String = RailFenceCipher(key).getEncryptedData(cleartext)

    override fun decode(ciphertext: String): String = RailFenceCipher(key).getDecryptedData(ciphertext)

    override fun init(context: AppCompatActivity) {
        val slider = context.findViewById<SeekBar>(R.id.cipher_slider_seekbar)
        slider.max = 10


        slider.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                key = p1+1
                context.findViewById<TextView>(R.id.cipher_slider_currentValue).text = key.toString()
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })
    }


    // From https://exercism.io/tracks/kotlin/exercises/rail-fence-cipher/solutions/665bb2dd30364a7d9b268333040de6c5
    class RailFenceCipher(private val railCount: Int) {
        private val cycleLength = (railCount * 2) - 2

        fun getEncryptedData(data: String): String {
            if (railCount==1 || railCount > 2*(data.length-1) || data.isEmpty()) return data
            return data
                .mapIndexed { i, char -> Pair(getRail(i), char) }
                .sortedBy { it.first }
                .map { it.second }
                .joinToString("")
        }

        fun getDecryptedData(encrypted: String): String {
            if (railCount==1 || railCount > 2*(encrypted.length-1) || encrypted.isEmpty()) return encrypted
            return placeDataOnRails(encrypted).let { rails ->
                encrypted.indices.joinToString("") { index ->
                    getRail(index).let { r ->
                        rails[r].take(1).also { rails[r] = rails[r].drop(1) }
                    }
                }
            }
        }

        private fun getRail(index: Int) = (index % cycleLength).let {
            if (it >= railCount) cycleLength - it else it
        }

        private fun placeDataOnRails(encrypted: String) = mutableListOf<String>().apply {
            var start = 0

            getRailCounts(encrypted).forEach {
                add(encrypted.substring(start, start + it))
                start += it
            }
        }

        private fun getRailCounts(data: String) = data
            .mapIndexed { i, char -> Pair(getRail(i), char) }
            .groupingBy { it.first }
            .eachCount()
            .map { it.value }
    }
}
