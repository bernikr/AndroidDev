package com.kralofsky.cipherbox


fun String.mapCharByChar(transform: (Char) -> Char) : String {
    return this.map(transform).joinToString("") { it.toString() }
}

fun String.mapCharByCharIndexed(transform: (index: Int, Char) -> Char) : String {
    return this.mapIndexed(transform).joinToString("") { it.toString() }
}

operator fun Char.plus(other: Char) : Char {
    return this.transformCaseIndependent {
        ((it.toAlphabetInt() + other.toAlphabetInt())%26).toAlphabetChar()
    }
}

fun Char.inverse(): Char {
    return this.transformCaseIndependent {
        ((26-it.toAlphabetInt()) % 26).toAlphabetChar()
    }
}

fun Char.toAlphabetInt(): Int {
    if (!this.isLetter()) return 0
    return this.toUpperCase().toInt() - 65
}

fun Int.toAlphabetChar(): Char {
    return (this + 65).toChar()
}

fun Char.transformCaseIndependent(transform: (Char) -> Char) : Char {
    if (!this.isLetter()) return this
    val upperCase = this.isUpperCase()
    val v = transform.invoke(this.toUpperCase())
    return if (upperCase) v.toUpperCase() else v.toLowerCase()
}
