package ddwu.com.mobile.anylearn

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ddwu.com.mobile.anylearn.databinding.ActivityWithaiLevelBinding
import okhttp3.*
import okio.ByteString
import java.util.concurrent.TimeUnit

class WithaiLevel : AppCompatActivity() {

    lateinit var wlBinding: ActivityWithaiLevelBinding
    lateinit var client: OkHttpClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        wlBinding = ActivityWithaiLevelBinding.inflate(layoutInflater)
        setContentView(wlBinding.root)

        client = OkHttpClient()

        wlBinding.levelFinishBtn.setOnClickListener {
            val intent = Intent(this, ScriptSaveChoice::class.java)
            startActivity(intent)
        }

        val settingButton: Button = findViewById(R.id.WithaiLevelSetting)
        settingButton.setOnClickListener {
            val intent = Intent(this, SettingPage::class.java)
            startActivity(intent)
        }

        val request: Request = Request.Builder()
            .url("wss://127.0.0.1:8000/ws/chats/5/")
            .build()
        val listener: WebSocketListener = MyWebSocketListener()

        client.newWebSocket(request, listener)
        client.dispatcher.executorService.shutdown()
    }


    inner class MyWebSocketListener : WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: Response) {
            super.onOpen(webSocket, response)
            webSocket.send("{\"type\":\"ticker\", \"symbols\": [\"BTC_KRW\"], \"tickTypes\": [\"30M\"]}")
            webSocket.close(NORMAL_CLOSURE_STATUS, null) //없을 경우 끊임없이 서버와 통신함
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            super.onMessage(webSocket, text)
            // 서버로부터 텍스트 메시지를 수신했을 때 호출되는 코드
        }

        override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
            super.onMessage(webSocket, bytes)
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            super.onClosing(webSocket, code, reason)
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            super.onFailure(webSocket, t, response)
        }

    }
    companion object {
        private const val NORMAL_CLOSURE_STATUS = 1000
    }
}
