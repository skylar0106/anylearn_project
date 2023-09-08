import android.os.AsyncTask
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class FetchCsrfTokenTask(private val listener: CsrfTokenListener) : AsyncTask<String, Void, String>() {

    interface CsrfTokenListener {
        fun onCsrfTokenFetched(csrfToken: String)
    }

    override fun doInBackground(vararg params: String): String? {
        val baseUrl = params[0]
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(baseUrl + "get-csrf-token/") // CSRF 토큰을 가져오는 엔드포인트 URL로 변경해야 합니다.
            .get()
            .build()

        try {
            val response: Response = client.newCall(request).execute()
            if (response.isSuccessful) {
                return response.body?.string()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    override fun onPostExecute(result: String?) {
        if (result != null) {
            listener.onCsrfTokenFetched(result)
        } else {
            // CSRF 토큰을 가져오지 못한 경우 처리
        }
    }
}
