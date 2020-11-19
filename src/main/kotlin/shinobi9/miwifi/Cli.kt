package shinobi9.miwifi

import com.github.ajalt.clikt.core.subcommands
import shinobi9.miwifi.DeviceCommand.*

lateinit var miwifiClient: MiwifiClient

fun main(args: Array<String>): Unit = MiwifiCommand()
    .subcommands(RebootCommand(), StatusCommand(), DeviceCommand().subcommands(DeviceListCommand()))
    .main(args)
