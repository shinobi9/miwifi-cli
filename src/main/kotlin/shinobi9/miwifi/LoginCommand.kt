package shinobi9.miwifi

import com.github.ajalt.clikt.core.CliktCommand

class LoginCommand : CliktCommand(name = "login", help = "just login") {
    override fun run() {
        miwifiClient.login()
        echo("login success")
    }
}
