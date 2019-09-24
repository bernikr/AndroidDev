package com.kralofsky.cipherbox

object CaesarCipher : Cipher() {
    override fun encode(cleartext: String): String {
        return cleartext.mapCharByChar { it + 'b' }
    }

    override fun decode(ciphertext: String): String {
        return ciphertext.mapCharByChar { it + 'b'.inverse() }
    }

    override val name = "Caesar Cipher"
    override val description = ""
    override val imageId = R.mipmap.ic_launcher_round
}
