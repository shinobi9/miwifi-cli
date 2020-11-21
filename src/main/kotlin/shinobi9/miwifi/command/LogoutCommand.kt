package shinobi9.miwifi.command

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import shinobi9.miwifi.miwifiClient

class LogoutCommand : CliktCommand(name = "logout", help = "just logout") {
    private val debug by option("-D", "--debug", help = "show request detail").flag("--no-debug", default = false)
    override fun run() {
        miwifiClient.apply { debugMode = debug }.logout()
        echo("see you")
    }
}
