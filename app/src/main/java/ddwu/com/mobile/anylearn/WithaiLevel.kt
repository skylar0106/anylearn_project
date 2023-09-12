package ddwu.com.mobile.anylearn

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import okhttp3.*
import android.Manifest
import android.os.Build
import android.util.Log
import ddwu.com.mobile.anylearn.databinding.ActivityWithaiLevelBinding
import okio.ByteString
import ddwu.com.mobile.anylearn.MySharedPreferences


class WithaiLevel : AppCompatActivity() {

    lateinit var wlBinding: ActivityWithaiLevelBinding
    lateinit var client: OkHttpClient
    private lateinit var webSocket: WebSocket
    private lateinit var speechRecognizer: SpeechRecognizer

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

        var level = intent.getStringExtra("level")

        val mySharedPreferences = MySharedPreferences(this)
        val receivedIntent = intent
        val roomId = receivedIntent.getIntExtra("roomId", 0)

        val request: Request = Request.Builder()
            .url("ws://34.81.3.83:8000/ws/chats/$roomId/")
            //.addHeader("Connection", "close")
            .addHeader("Cookie", "sessionid="+mySharedPreferences.getSessionId().toString()) // 쿠키 추가
            .build()
        val webSocketListener: WebSocketListener = MyWebSocketListener()

        webSocket = client.newWebSocket(request, MyWebSocketListener())
        //val message = "Hello, WebSocket!" // 보낼 메시지 내용
        //webSocket.send(message)
        //client.dispatcher.executorService.shutdown()


        // 권한 설정
        requestPermission()

        // RecognizerIntent 생성
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, packageName)    // 여분의 키
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US")         // 언어 설정


        // 완전한 침묵이 감지되면 인식을 종료하는 시간 설정 (예: 3초)
        //intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 4000)

        // <말하기> 버튼 눌러서 음성인식 시작
        wlBinding.mikeImage.setOnClickListener {
            // 새 SpeechRecognizer 를 만드는 팩토리 메서드
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this@WithaiLevel)
            speechRecognizer.setRecognitionListener(recognitionListener)    // 리스너 설정
            speechRecognizer.startListening(intent)                         // 듣기 시작
        }
    }



    // 권한 설정 메소드
    private fun requestPermission() {
        // 버전 체크, 권한 허용했는지 체크
        if (Build.VERSION.SDK_INT >= 23 &&
            ContextCompat.checkSelfPermission(this@WithaiLevel, Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this@WithaiLevel,
                arrayOf(Manifest.permission.RECORD_AUDIO), 0)
        }
    }

    // 리스너 설정
    private val recognitionListener: RecognitionListener = object : RecognitionListener {
        // 말하기 시작할 준비가되면 호출
        override fun onReadyForSpeech(params: Bundle) {
            Toast.makeText(this@WithaiLevel, "음성인식 시작", Toast.LENGTH_SHORT).show()
        }
        // 말하기 시작했을 때 호출
        override fun onBeginningOfSpeech() {
            Toast.makeText(this@WithaiLevel, "잘 듣고 있어요!", Toast.LENGTH_SHORT).show()
        }
        // 입력받는 소리의 크기를 알려줌
        override fun onRmsChanged(rmsdB: Float) {}
        // 말을 시작하고 인식이 된 단어를 buffer에 담음
        override fun onBufferReceived(buffer: ByteArray) {}
        // 말하기를 중지하면 호출
        override fun onEndOfSpeech() {
            Toast.makeText(this@WithaiLevel, "끝!", Toast.LENGTH_SHORT).show()
        }
        // 오류 발생했을 때 호출
        override fun onError(error: Int) {
            val message = when (error) {
                SpeechRecognizer.ERROR_AUDIO -> "오디오 에러"
                SpeechRecognizer.ERROR_CLIENT -> "클라이언트 에러"
                SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "퍼미션 없음"
                SpeechRecognizer.ERROR_NETWORK -> "네트워크 에러"
                SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "네트웍 타임아웃"
                //SpeechRecognizer.ERROR_NO_MATCH -> "찾을 수 없음"
                SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "RECOGNIZER 가 바쁨"
                SpeechRecognizer.ERROR_SERVER -> "서버가 이상함"
                SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "말하는 시간초과"
                else -> "알 수 없는 오류임"
            }
            Toast.makeText(this@WithaiLevel, "에러 발생: $message", Toast.LENGTH_SHORT).show()
        }
        // 인식 결과가 준비되면 호출
        override fun onResults(results: Bundle) {
            // 말을 하면 ArrayList에 단어를 넣고 textView에 단어를 이어줌
            val matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)

            // matches 배열을 하나의 문자열로 결합
            val resultString = matches?.joinToString(" ") // 여기서 " "은 단어 사이에 넣을 공백입니다.

            // 결과를 TextView에 설정
            wlBinding.editRecord.setText(resultString)
            webSocket.send(resultString.toString())

        }
        // 부분 인식 결과를 사용할 수 있을 때 호출
        override fun onPartialResults(partialResults: Bundle) {}
        // 향후 이벤트를 추가하기 위해 예약
        override fun onEvent(eventType: Int, params: Bundle) {}
    }


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





