package shinobi9.miwifi

import com.github.ajalt.clikt.core.CliktCommand
import com.jakewharton.picnic.BorderStyle
import com.jakewharton.picnic.TextAlignment
import com.jakewharton.picnic.table
import io.github.rybalkinsd.kohttp.jackson.ext.toJson

class DeviceCommand : CliktCommand(name = "device", help = "device operation") {
    override fun run() = Unit

    class DeviceListCommand : CliktCommand(name = "list", help = "show devices in table") {
        override fun run() {
            val deviceList = miwifiClient.deviceList().toJson()["list"]
            table {
                style {
                    borderStyle = BorderStyle.Hidden
                }
                header {
                    cellStyle {
                        alignment = TextAlignment.BottomCenter
                        border = true
                    }
                    row("mac", "ip", "status", "name")
                }
                cellStyle {
                    paddingLeft = 1
                    paddingRight = 1
                    border = true
                }
                body {
                    cellStyle {
                        alignment = TextAlignment.MiddleRight
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
                    cellStyle {
                        border = true
                    }
                    row("count", deviceList.size())
                }
            }.also { echo(it) }
        }
    }
}
