package shinobi9.miwifi

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option

class RebootCommand : CliktCommand(name = "reboot", help = "reboot wifi") {
    private val debug by option("-D", "--debug", help = "show request detail").flag("--no-debug", default = false)
    override fun run() {
        miwifiClient.debugMode = debug
        miwifiClient.reboot()
        echo("send reboot request,wifi router will be reboot in second")
    }
}
