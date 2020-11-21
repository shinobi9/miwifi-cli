package shinobi9.miwifi.command

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import shinobi9.miwifi.miwifiClient

class LoginCommand : CliktCommand(name = "login", help = "just login") {

    private val host: String by option(
        names = arrayOf("-h", "--host"),
        help = "host of miwifi router, default 192.168.31.1"
    ).default("192.168.31.1")
    private val pass: String by option(names = arrayOf("-p", "--password"), help = "password to login").default("")
    private val debug by option("-D", "--debug", help = "show request detail").flag("--no-debug", default = false)
    override fun run() {
        miwifiClient.apply {
            routerHost = host
            debugMode = debug
            password = pass
        }.login()
        echo("login success")
    }
}
