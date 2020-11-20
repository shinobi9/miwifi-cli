package shinobi9.miwifi

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option

class LogoutCommand : CliktCommand(name = "logout", help = "just logout") {
    private val debug by option("-D", "--debug", help = "show request detail").flag("--no-debug", default = false)
    override fun run() {
        miwifiClient.debugMode = debug
        miwifiClient.logout()
        echo("see you")
    }
}
