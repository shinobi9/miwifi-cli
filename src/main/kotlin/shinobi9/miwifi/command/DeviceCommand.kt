package shinobi9.miwifi.command

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.NoOpCliktCommand
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.jakewharton.picnic.BorderStyle
import com.jakewharton.picnic.TextAlignment
import com.jakewharton.picnic.table
import io.github.rybalkinsd.kohttp.jackson.ext.toJson
import shinobi9.miwifi.miwifiClient

class DeviceCommand : NoOpCliktCommand(name = "device", help = "device operation") {
    class DeviceListCommand : CliktCommand(name = "list", help = "show devices in table") {
        private val debug by option("-D", "--debug", help = "show request detail").flag("--no-debug", default = false)
        override fun run() {
            val deviceList = miwifiClient.apply { debugMode = debug }.deviceList().toJson()["list"]
            table {
                style {
                    borderStyle = BorderStyle.Hidden
                }
                header {
                    cellStyle {
                        alignment = TextAlignment.BottomCenter
                        border = false
                    }
                    row("MAC", "IP", "STATUS", "NAME")
                }
                cellStyle {
                    paddingLeft = 1
                    paddingRight = 1
                    border = false
                }
                body {
                    cellStyle {
                        alignment = TextAlignment.MiddleLeft
                    }
                    deviceList.forEach { dev ->
                        val mac = dev["mac"].asText()
                        val ip = dev["ip"][0]["ip"].asText()
                        val blockStatus = dev["authority"]["wan"].asInt().let { if (it == 0) "block" else "health" }
                        val name = dev["name"].asText()
                        row {
                            cell(mac)
                            cell(ip)
                            cell(blockStatus)
                            cell(name)
                        }
                    }
                }

                footer {
                    cellStyle { borderTop = true }
                    row("count", deviceList.size())
                }
            }.also { echo(it) }
        }
    }
}
