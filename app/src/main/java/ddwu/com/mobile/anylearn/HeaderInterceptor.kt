import ddwu.com.mobile.anylearn.MySharedPreferences
import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor(private val mySharedPreferences: MySharedPreferences) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        // 서버 응답에서 Set-Cookie 헤더를 가져온 후 csrftoken을 추출합니다.
        val cookies = response.headers("Set-Cookie")
        val csrfTokens = response.headers("X-Csrftoken")
        val sessionIds = response.headers("Session-Id")

        var csrfToken: String? = null
        var sessionId: String? = null

        // csrfTokens 리스트에서 첫 번째 값을 추출하고 대괄호를 제거합니다.
        for (token in csrfTokens) {
            csrfToken = token.removeSurrounding("[", "]")
            break
        }

        // sessionIds 리스트에서 첫 번째 값을 추출하고 대괄호를 제거합니다.
        for (sessionIdValue in sessionIds) {
            sessionId = sessionIdValue.removeSurrounding("[", "]")
            break
        }

        // 추출한 csrfToken 저장 (예: SharedPreferences에 저장)
        if (csrfToken != null) {
            mySharedPreferences.saveCsrfToken(csrfToken)
        }

        // 추출한 sessionId 저장 (예: SharedPreferences에 저장)
        if (sessionId != null) {
            mySharedPreferences.saveSessionId(sessionId)
        }

        return response
    }
}