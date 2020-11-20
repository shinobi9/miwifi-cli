package shinobi9.miwifi.persist

interface Persist<T> {

    fun save(fileName: String, t: T)

    fun load(fileName: String): T
}
