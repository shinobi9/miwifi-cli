import shinobi9.miwifi.core.MiwifiClient

fun main() {
    userDir()
}

fun userDir() {
    System.getenv("LOCALAPPDATA").also { println(it) }
//    System.getProperties().forEach { println(it) }
}

fun resourceFile() {
    MiwifiClient::class.java.getResource("/aes.js")
        .openStream().reader().readLines()
        .run { println(this) }
}
