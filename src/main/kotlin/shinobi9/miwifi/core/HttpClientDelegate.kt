package shinobi9.miwifi.core

import io.github.rybalkinsd.kohttp.client.defaultHttpClient
import io.github.rybalkinsd.kohttp.client.fork
import io.github.rybalkinsd.kohttp.interceptors.logging.HttpLoggingInterceptor
import okhttp3.OkHttpClient
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class HttpClientDelegate : ReadOnlyProperty<MiwifiClient, OkHttpClient> {
    private val nonDebugClient = defaultHttpClient
    private val debugClient = defaultHttpClient.fork {
        interceptors {
            +HttpLoggingInterceptor()
        }
    }

    override fun getValue(thisRef: MiwifiClient, property: KProperty<*>): OkHttpClient {
        return if (thisRef.debugMode)
            debugClient
        else
            nonDebugClient
    }
}
