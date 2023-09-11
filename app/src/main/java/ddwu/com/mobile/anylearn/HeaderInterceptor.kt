import ddwu.com.mobile.anylearn.MySharedPreferences
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class CsrfTokenInterceptor(private val mySharedPreferences: MySharedPreferences) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        // 서버 응답에서 Set-Cookie 헤더를 가져온 후 csrftoken을 추출합니다.
        val cookies = response.headers("Set-Cookie")
        var csrfToken: String? = null
        var sessionId: String? = null
        for (cookie in cookies) {
            if (cookie.startsWith("csrftoken=")) {
                csrfToken = cookie.split(";")[0].substring("csrftoken=".length)
                break
            }
            if (cookie.startsWith("sessionid=")) {
                sessionId = cookie.split(";")[0].substring("sessionid=".length)
                break
            }
        }

        // 추출한 csrftoken을 저장 (예: SharedPreferences에 저장)
        if (csrfToken != null) {
            mySharedPreferences.saveCsrfToken(csrfToken)
        }

        // 추출한 sessionId 저장 (예: SharedPreferences에 저장)
        if (sessionId != null) {
            mySharedPreferences.saveCsrfToken(sessionId)
        }

        return response
    }
}