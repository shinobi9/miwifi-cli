package shinobi9.miwifi

import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.options.versionOption
import shinobi9.miwifi.command.*
import shinobi9.miwifi.core.MiwifiClient

val miwifiClient: MiwifiClient = MiwifiClient()

fun main(args: Array<String>): Unit = MiwifiCommand()
    .subcommands(
        RebootCommand(),
        StatusCommand(),
        LoginCommand(),
        LogoutCommand(),
        DeviceCommand().subcommands(DeviceCommand.DeviceListCommand())
    )
    .versionOption("0.1", names = setOf("--version", "-v"), message = { "miwifi-cli 0.0.1" })
    .main(args)
