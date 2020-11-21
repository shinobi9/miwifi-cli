package shinobi9.miwifi.command

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import shinobi9.miwifi.miwifiClient

class RebootCommand : CliktCommand(name = "reboot", help = "reboot wifi") {
    private val debug by option("-D", "--debug", help = "show request detail").flag("--no-debug", default = false)
    override fun run() {
        miwifiClient.apply { debugMode = debug }.reboot()
        echo("send reboot request,wifi router will be reboot in second")
    }
}
