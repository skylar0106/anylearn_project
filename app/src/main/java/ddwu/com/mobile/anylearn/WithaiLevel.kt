package ddwu.com.mobile.anylearn

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import ddwu.com.mobile.anylearn.databinding.ActivityWithaiLevelBinding
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import kotlin.properties.Delegates


class WithaiLevel : AppCompatActivity() {

    lateinit var wlBinding: ActivityWithaiLevelBinding
    lateinit var client: OkHttpClient
//    private lateinit var webSocket: WebSocket
    private lateinit var speechRecognizer: SpeechRecognizer
    private lateinit var tts : TextToSpeech

    // WebSocket 객체를 싱글톤으로 생성
    companion object {
        private lateinit var webSocket: WebSocket

        private var roomIdValue: Int = 0

        var roomId: Int
            get() = roomIdValue
            set(value) {
                roomIdValue = value
            }
        fun getWebSocketInstance(): WebSocket {
            return webSocket
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        wlBinding = ActivityWithaiLevelBinding.inflate(layoutInflater)
        setContentView(wlBinding.root)

        client = OkHttpClient()

        wlBinding.levelFinishBtn.setOnClickListener {
            val endJson = JSONObject()
            endJson.put("type", " end-conversation")
            endJson.put("message", "end")
            webSocket.send(endJson.toString())

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
        roomId = receivedIntent.getIntExtra("roomId", 0)


        val request: Request = Request.Builder()
            .url("ws://34.81.3.83:8000/ws/chats/$roomId/")
            //.addHeader("Connection", "close")
            .addHeader("Cookie", "sessionid="+mySharedPreferences.getSessionId().toString()) // 쿠키 추가
            .build()
        val webSocketListener: WebSocketListener = MyWebSocketListener()

        // tts 세팅
        tts = TextToSpeech(this) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts.language = Locale.ENGLISH
            } else {
                Log.e("TTS", "TTS 엔진 초기화에 실패했습니다.")
            }
        }

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

            // JSON 객체 생성
            val json = JSONObject()
            json.put("type", "user-message")
            json.put("message", resultString)

            // JSON 데이터를 문자열로 변환하여 서버로 전송
            webSocket.send(json.toString())

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
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            super.onMessage(webSocket, text)

            // 서버로부터 받은 JSON 형식의 데이터 파싱
            try {
                val jsonObject = JSONObject(text)
                val type = jsonObject.optString("type")
                val message = jsonObject.optString("message")

                // 받은 데이터를 UI에 표시하거나 필요한 작업 수행
                val handler = Handler(Looper.getMainLooper())

                // 백그라운드 스레드에서 UI 업데이트 예약
                handler.post {
                    // 영어 한국어 부분 추출
                    val (englishText, koreanText) = extractText(message)
                    Log.d("ai conversation", englishText)
                    Log.d("ai conversation", koreanText)

                    // 영어 부분이 존재하면 TTS 실행
                    if (englishText.isNotEmpty()) {
                        // UI 업데이트 코드 작성
                        wlBinding.englishSubtitle.text = englishText
                        speakText(englishText)
                    }

                    // 한글 부분을 원하는 변수에 저장 (예: koreanVariable)
                    if (koreanText.isNotEmpty()) {
                        // UI 업데이트 코드 작성
                        wlBinding.koreanSubtitle.text = koreanText
                    }
                }

            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }

        // 문자열에서 영어 부분과 한글 부분을 추출하는 메서드
        private fun extractText(text: String): Pair<String, String> {
            val startIndex = text.indexOf("(")
            val endIndex = text.indexOf(")")
            if (startIndex != -1 && endIndex != -1 && startIndex < endIndex) {
                val englishText = text.substring(0, startIndex).trim()
                val koreanText = text.substring(startIndex + 1, endIndex).trim()
                return Pair(englishText, koreanText)
            }
            return Pair(text, "")
        }

        // TTS 실행하는 메서드
        private fun speakText(text: String) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
            tts.setPitch(1.0f)
            tts.setSpeechRate(1.0f)
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            super.onClosing(webSocket, code, reason)
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            super.onFailure(webSocket, t, response)
            Log.d("Socket","Error : " + t.message)
        }

    }
    override fun onDestroy() {
        tts.stop()
        tts.shutdown()
        super.onDestroy()
    }
}





