package shinobi9.miwifi

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.github.rybalkinsd.kohttp.jackson.ext.toJson
import okhttp3.Response
import shinobi9.miwifi.core.MiwifiClient
import java.net.URL
import javax.script.ScriptEngine

internal val prettyObjectMapper: ObjectMapper =
    jacksonObjectMapper().configure(SerializationFeature.INDENT_OUTPUT, true)

internal fun String.resource(): URL = MiwifiClient::class.java.getResource(this)
internal fun ScriptEngine.evalResource(path: String): Any? = eval(path.resource().openStream().reader())
internal fun Response.toPrettyJsonString(): String {
    return prettyObjectMapper.writeValueAsString(toJson())
}
