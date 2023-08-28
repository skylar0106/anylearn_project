import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class CsrfTokenInterceptor(private val csrfToken: String) : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest: Request = chain.request()
        val newRequest = originalRequest.newBuilder()
            .header("X-CSRFToken", csrfToken)
            .build()
        return chain.proceed(newRequest)
    }
}