package com.kralofsky.cipherbox

object CaesarCipher : Cipher() {
    override val name = "Caesar Cipher"
    override val description = ""
    override val imageId = R.mipmap.ic_launcher_round
    override val controlLayout = R.layout.cipherc_caesar

    override fun encode(cleartext: String): String = cleartext.mapCharByChar { it + 'b' }

    override fun decode(ciphertext: String): String = ciphertext.mapCharByChar { it + 'b'.inverse() }
}
