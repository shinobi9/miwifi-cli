@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package shinobi9.miwifi

import io.github.rybalkinsd.kohttp.client.defaultHttpClient
import io.github.rybalkinsd.kohttp.client.fork
import io.github.rybalkinsd.kohttp.dsl.httpGet
import io.github.rybalkinsd.kohttp.dsl.httpPost
import io.github.rybalkinsd.kohttp.ext.url
import io.github.rybalkinsd.kohttp.interceptors.logging.HttpLoggingInterceptor
import io.github.rybalkinsd.kohttp.jackson.ext.toJsonOrNull
import jdk.nashorn.api.scripting.ScriptObjectMirror
import okhttp3.Response
import shinobi9.miwifi.error.LoginFailureException
import shinobi9.miwifi.persist.persistSupport
import java.util.*
import javax.script.Invocable
import javax.script.ScriptEngineManager

class MiwifiClient(
    val routerHost: String = "192.168.31.1",
    val debugMode: Boolean = false,
    val password: String = "",
) {
    companion object {
        const val dataFile = "data.json"
    }

    internal val baseUrl
        get() = "http://$routerHost/cgi-bin/luci"

    internal val client = defaultHttpClient.fork {
        interceptors {
            if (debugMode) +HttpLoggingInterceptor()
        }
    }

    private var token: String? = null

    fun checkLogin(block: () -> Response): Response {
        val readToken = persistSupport.load(dataFile)["token"] as? String
        if (readToken.isNullOrEmpty()) throw LoginFailureException("you should login at first") else token = readToken
        return block()
    }

    fun logout() {
        token = null
        persistSupport.save(dataFile, Properties())
    }

    fun login() {
        val map = transform(password)
        httpPost(client) {
            url("$baseUrl/api/xqsystem/login")
            body {
                form {
                    map.forEach { (t, u) -> t to u }
                }
            }
        }.toJsonOrNull()?.get("token")?.run { token = asText() }
            ?: throw LoginFailureException("login failure , check password!")
        persistSupport.save(dataFile, Properties().apply { setProperty("token", token) })
    }

    private fun String.withToken(): String {
        return token?.let { return@let "$baseUrl/;stok=$token$this" } ?: throw LoginFailureException("未登录")
    }

    private fun transform(password: String): Map<String, Any> {
        val engine = ScriptEngineManager().getEngineByName("nashorn")
        engine.evalResource("/sha1.js")
        engine.evalResource("/aes.js")
        engine.evalResource("/login.js")
        val invocable = engine as Invocable
        return (invocable.invokeFunction("transform", password) as ScriptObjectMirror).toMap<String, Any>()
    }

    fun detail() = checkLogin { httpGet { url("/api/xqnetwork/wifi_detail_all".withToken()) } }

    fun status() = checkLogin { httpGet { url("/api/misystem/status".withToken()) } }

    fun reboot() = checkLogin { httpGet { url("/api/xqsystem/reboot".withToken()) } }

    fun macBindInfo() =
        checkLogin { httpGet { url("/api/xqnetwork/macbind_info".withToken()) } }

    fun wanInfo() = checkLogin { httpGet { url("/api/xqnetwork/wan_info".withToken()) } }

    @Suppress("FunctionName")
    private fun _block(mac: String, block: Boolean) = checkLogin {
        httpGet {
            url("/api/xqsystem/set_mac_filter".withToken())
            param {
                "mac" to mac
                "wan" to if (block) 0 else 1
            }
        }
    }

    fun deviceList() = checkLogin {
        httpGet { url("/api/misystem/devicelist".withToken()) }
    }

    fun block(mac: String) = _block(mac, true)
    fun unblock(mac: String) = _block(mac, false)
}
