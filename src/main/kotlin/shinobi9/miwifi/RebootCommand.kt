package shinobi9.miwifi

import com.github.ajalt.clikt.core.CliktCommand

class RebootCommand : CliktCommand(name = "reboot", help = "reboot wifi") {
    override fun run() {
        miwifiClient.reboot()
        echo("send reboot request,wifi router will be reboot in second")
    }
}
