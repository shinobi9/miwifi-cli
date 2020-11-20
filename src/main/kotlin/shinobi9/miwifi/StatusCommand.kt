package shinobi9.miwifi

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option

class StatusCommand : CliktCommand(name = "status", help = "show wifi status") {
    private val debug by option("-D", "--debug", help = "show request detail").flag("--no-debug", default = false)
    override fun run() {
        miwifiClient.debugMode = debug
        echo(miwifiClient.status().toPrettyJsonString())
    }
}
