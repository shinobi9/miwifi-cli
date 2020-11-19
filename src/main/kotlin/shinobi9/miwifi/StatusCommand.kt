package shinobi9.miwifi

import com.github.ajalt.clikt.core.CliktCommand

class StatusCommand : CliktCommand(name = "status", help = "show wifi status") {
    override fun run() {
        echo(miwifiClient.status().toPrettyJsonString())
    }
}
