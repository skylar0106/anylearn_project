package ddwu.com.mobile.anylearn

import android.content.Context
import android.content.SharedPreferences

class MySharedPreferences(context: Context) {
    private val appContext = context.applicationContext
    private val sharedPreferences: SharedPreferences = appContext.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    private val SESSION_ID_KEY = "session_id"
    private val CSRF_TOKEN_KEY = "csrf_token"
    private val COOKIE_TOKEN = "cookie_token"

    // 세션 ID를 저장하는 함수
    fun saveSessionId(sessionId: String) {
        editor.putString(SESSION_ID_KEY, sessionId)
        editor.apply()
    }

    // 세션 ID를 가져오는 함수
    fun getSessionId(): String? {
        return sharedPreferences.getString(SESSION_ID_KEY, null)
    }

    // csrfToken을 저장하는 함수
    fun saveCsrfToken(csrfToken: String) {
        editor.putString(CSRF_TOKEN_KEY, csrfToken)
        editor.apply()
    }

    // csrfToken을 가져오는 함수
    fun getCsrfToken(): String? {
        return sharedPreferences.getString(CSRF_TOKEN_KEY, null)
    }

    // cookieToken을 저장하는 함수
    fun saveCookieToken(cookieToken: String) {
        editor.putString(COOKIE_TOKEN, cookieToken)
        editor.apply()
    }

    // cookieToken을 가져오는 함수
    fun getCookieToken(): String? {
        return sharedPreferences.getString(COOKIE_TOKEN, null)
    }
}