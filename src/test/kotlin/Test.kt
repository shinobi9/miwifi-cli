import shinobi9.miwifi.MiwifiClient

fun main() {

    MiwifiClient::class.java.getResource("/aes.js")
        .openStream().reader().readLines()
        .run { println(this) }
}
