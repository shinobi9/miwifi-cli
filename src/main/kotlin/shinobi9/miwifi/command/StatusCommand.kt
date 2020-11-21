package shinobi9.miwifi.command

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import shinobi9.miwifi.miwifiClient
import shinobi9.miwifi.toPrettyJsonString

class StatusCommand : CliktCommand(name = "status", help = "show wifi status") {
    private val debug by option("-D", "--debug", help = "show request detail").flag("--no-debug", default = false)
    override fun run() {
        echo(miwifiClient.apply { debugMode = debug }.status().toPrettyJsonString())
    }
}
