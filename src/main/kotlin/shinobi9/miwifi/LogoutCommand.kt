package shinobi9.miwifi

import com.github.ajalt.clikt.core.CliktCommand

class LogoutCommand : CliktCommand(name = "logout", help = "just logout") {
    override fun run() {
        miwifiClient.logout()
        echo("see you")
    }
}
