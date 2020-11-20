package shinobi9.miwifi

import com.github.ajalt.clikt.core.subcommands
import shinobi9.miwifi.DeviceCommand.DeviceListCommand

lateinit var miwifiClient: MiwifiClient

fun main(args: Array<String>): Unit = MiwifiCommand()
    .subcommands(
        RebootCommand(),
        StatusCommand(),
        LoginCommand(),
        LogoutCommand(),
        DeviceCommand().subcommands(DeviceListCommand())
    )
    .main(args)
