package ddwu.com.mobile.anylearn

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import ddwu.com.mobile.anylearn.databinding.ActivityWithaiLevelBinding
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okio.ByteString
import java.util.*


class WithaiLevel : AppCompatActivity() {

    lateinit var wlBinding: ActivityWithaiLevelBinding
    lateinit var client: OkHttpClient

    lateinit var cThis: Context//context 설정
    var LogTT = "[STT]" //LOG타이틀

    //음성 인식용
    lateinit var SttIntent: Intent
    lateinit var mRecognizer: SpeechRecognizer

    //음성 출력용
    lateinit var tts: TextToSpeech


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        wlBinding = ActivityWithaiLevelBinding.inflate(layoutInflater)
        setContentView(wlBinding.root)

        client = OkHttpClient()

        wlBinding.levelFinishBtn.setOnClickListener {
            val intent = Intent(this, ScriptSaveChoice::class.java)
            startActivity(intent)
        }

        wlBinding.WithaiLevelSetting.setOnClickListener {
            val intent = Intent(this, SettingPage::class.java)
            startActivity(intent)
        }


        val request: Request = Request.Builder()
            .url("ws://35.229.205.158:8000/ws/chats/2/")
            .addHeader("Connection", "close")
            .addHeader("Cookie", "sessionid=0qipsz241yko1eek60tqvu9if8yklcz2") // 쿠키 추가
            .build()
        val webSocketListener: WebSocketListener = MyWebSocketListener()

        client.newWebSocket(request, webSocketListener)
        client.dispatcher.executorService.shutdown()

        //음성파트
        cThis = this

        //음성인식
//        SttIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
//        SttIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, applicationContext.packageName)
//        SttIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR") //한국어 사용
//
//        mRecognizer = SpeechRecognizer.createSpeechRecognizer(cThis)
//        mRecognizer.setRecognitionListener(recognitionListener)

        //음성출력 생성, 리스너 초기화

        //음성출력 생성, 리스너 초기화
        tts = TextToSpeech(cThis) { status ->
            if (status != TextToSpeech.ERROR) {
                tts.language = Locale.KOREAN
            }
        }
    }



//    private val recognitionListener = object : RecognitionListener {
//        override fun onReadyForSpeech(bundle: Bundle) {
//            txtSystem.setText("onReadyForSpeech...........\r\n${txtSystem.text}")
//        }
//
//        override fun onBeginningOfSpeech() {
//            txtSystem.setText("지금부터 말을 해주세요...........\r\n${txtSystem.text}")
//        }
//
//        override fun onRmsChanged(v: Float) {}
//
//        override fun onBufferReceived(bytes: ByteArray) {
//            txtSystem.setText("onBufferReceived...........\r\n${txtSystem.text}")
//        }
//
//        override fun onEndOfSpeech() {
//            txtSystem.setText("onEndOfSpeech...........\r\n${txtSystem.text}")
//        }
//
//        override fun onError(i: Int) {
//            txtSystem.setText("천천히 다시 말해 주세요...........\r\n${txtSystem.text}")
//        }
//
//        override fun onResults(results: Bundle) {
//            val key = SpeechRecognizer.RESULTS_RECOGNITION
//            val mResult = results.getStringArrayList(key)
//            val rs = mResult?.toTypedArray()
//            txtInMsg.setText("${rs?.get(0)}\r\n${txtInMsg.text}")
//            rs?.get(0)?.let { FuncVoiceOrderCheck(it) }
//            mRecognizer.startListening(SttIntent)
//        }
//
//        override fun onPartialResults(bundle: Bundle) {
//            txtSystem.setText("onPartialResults...........\r\n${txtSystem.text}")
//        }
//
//        override fun onEvent(i: Int, bundle: Bundle) {
//            txtSystem.setText("onEvent...........\r\n${txtSystem.text}")
//        }
//    }
//
//
    inner class MyWebSocketListener : WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: Response) {
            super.onOpen(webSocket, response)
            //webSocket.close(NORMAL_CLOSURE_STATUS, null) //없을 경우 끊임없이 서버와 통신함
            Log.d("Socket","Open")
            val responseBody = response.body?.string()
            //val jsonObject = JSONObject(responseBody)

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
            Log.d("Socket","Error : " + t.message)
        }

    }
    companion object {
        private const val NORMAL_CLOSURE_STATUS = 1000
    }
}

//private fun SpeechRecognizer.setRecognitionListener(listener: RecognitionListener) {
//
//}




