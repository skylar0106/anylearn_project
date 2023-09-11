package ddwu.com.mobile.anylearn

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// RetrofitConfig.kt
object RetrofitConfig {
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://34.81.3.83:8000/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(
            OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
                .build()
        )
        .build()

    val webSocketClient: OkHttpClient = OkHttpClient.Builder().build()
}
