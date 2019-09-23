package com.kralofsky.cipherbox

object CaesarCipher : Cipher() {
    override fun encode(cleartext: String): String {
        return cleartext.mapCharByChar { it + 'a' }
    }

    override fun decode(ciphertext: String): String {
        return ciphertext.mapCharByChar { it - 'a' }
    }

    override val name = "Caesar Cipher"
    override val description = ""
    override val imageId = R.mipmap.ic_launcher_round
}

fun String.mapCharByChar(transform: (Char) -> Char) : String {
    return this.map(transform).joinToString("") { it.toString() }
}

operator fun Char.plus(other: Char) : Char {
    return this.transformCaseIndependent {
        (it.toAlphabetInt() + other.toAlphabetInt()).toAlphabetChar()
    }
}

operator fun Char.minus(other: Char) : Char {
    return this.transformCaseIndependent {
        (it.toAlphabetInt() - other.toAlphabetInt()).toAlphabetChar()
    }
}

fun Char.toAlphabetInt(): Int {
    if (!this.isLetter()) return 0
    return this.toUpperCase().toInt() - 64
}

fun Int.toAlphabetChar(): Char {
    return (this + 64).toChar()
}

fun Char.transformCaseIndependent(transform: (Char) -> Char) : Char {
    if (!this.isLetter()) return this
    val upperCase = this.isUpperCase()
    val v = transform.invoke(this.toUpperCase())
    return if (upperCase) v.toUpperCase() else v.toLowerCase()
}