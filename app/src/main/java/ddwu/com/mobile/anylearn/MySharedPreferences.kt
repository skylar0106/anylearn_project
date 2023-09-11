package ddwu.com.mobile.anylearn

import android.content.Context
import android.content.SharedPreferences

class MySharedPreferences(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    companion object {
        private const val SESSION_ID_KEY = "session_id"
        private const val TOKEN_KEY= "token_key"
    }

    // 세션 ID를 저장하는 함수
    fun saveSessionId(sessionId: String) {
        editor.putString(SESSION_ID_KEY, sessionId)
        editor.apply()
    }

    // 세션 ID를 가져오는 함수
    fun getSessionId(): String? {
        return sharedPreferences.getString(SESSION_ID_KEY, null)
    }

    // 세션 ID를 저장하는 함수
    fun saveTokenKey(sessionId: String) {
        editor.putString(TOKEN_KEY, sessionId)
        editor.apply()
    }

    // 세션 ID를 가져오는 함수
    fun getTokenKey(): String? {
        return sharedPreferences.getString(TOKEN_KEY, null)
    }
}
