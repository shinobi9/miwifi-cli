@file:Suppress(
    "ClassName", "JoinDeclarationAndAssignment", "MemberVisibilityCanBePrivate",
    "PARAMETER_NAME_CHANGED_ON_OVERRIDE"
)

package shinobi9.miwifi.persist

import com.fasterxml.jackson.module.kotlin.readValue
import shinobi9.miwifi.error.UnsupportedOperatingSystemException
import shinobi9.miwifi.prettyObjectMapper
import sun.awt.OSInfo
import sun.awt.OSInfo.OSType.*
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

object persistSupport : Persist<Properties> {
    val osType: OSInfo.OSType
    val persistRoot: String
    const val dir = "miwifi-cli"

    init {
        osType = OSInfo.getOSType()
        val root = directlySpecifyByOSType()
        if (root.isEmpty()) throw UnsupportedOperatingSystemException("""your os is ${System.getProperty("os.name")} . unsupported yet""")
        persistRoot = root
    }

    private const val pathForLinux = "/var/lib"
    private val pathForWin
        get() = System.getenv("LOCALAPPDATA").orEmpty()
    private const val pathForMac = "/var/lib"

    private fun directlySpecifyByOSType(): String {
        return when (osType) {
            LINUX -> pathForLinux.apply { checkExists() }
            WINDOWS -> pathForWin.apply { checkExists() }
            MACOSX -> pathForMac.apply { checkExists() }
            else -> ""
        }
    }

    private fun String.checkExists() {
        if (!Files.exists(Paths.get(this)))
            throw NullPointerException("directory not found $this")
    }

    val parent: String
        get() = "$persistRoot${File.separator}$dir"

    private fun File.mkdirIfNotExist() = run {
        if (!exists()) {
            if (!mkdir()) throw AccessDeniedException(this)
        }
    }

    private fun File.touchIfNotExist() = run {
        if (!exists()) {
            if (!createNewFile()) throw AccessDeniedException(this)
        }
    }

    override fun save(fileName: String, properties: Properties) {
        File(parent).mkdirIfNotExist()
        val file = File("$parent${File.separator}$fileName")
        file.touchIfNotExist()
        file.writeBytes(prettyObjectMapper.writeValueAsBytes(properties))
    }

    override fun load(fileName: String): Properties {
        File(parent).mkdirIfNotExist()
        val file = File("$parent${File.separator}$fileName")
        file.touchIfNotExist()
        return prettyObjectMapper.readValue(file)
    }
}
