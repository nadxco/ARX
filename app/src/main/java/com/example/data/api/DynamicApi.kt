package com.example.data.api

import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Url
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@JsonClass(generateAdapter = true)
data class OpenAiRequest(
    val model: String,
    val messages: List<OpenAiMessage>,
    val temperature: Float = 0.2f
)

@JsonClass(generateAdapter = true)
data class OpenAiMessage(
    val role: String,
    val content: String
)

@JsonClass(generateAdapter = true)
data class OpenAiResponse(
    val choices: List<OpenAiChoice>? = null,
    val error: OpenAiError? = null
)

@JsonClass(generateAdapter = true)
data class OpenAiChoice(
    val message: OpenAiMessage?
)

@JsonClass(generateAdapter = true)
data class OpenAiError(
    val message: String?
)

@JsonClass(generateAdapter = true)
data class AnthropicRequest(
    val model: String,
    val system: String? = null,
    val messages: List<AnthropicMessage>,
    val max_tokens: Int = 4096,
    val temperature: Float = 0.2f
)

@JsonClass(generateAdapter = true)
data class AnthropicMessage(
    val role: String,
    val content: String
)

@JsonClass(generateAdapter = true)
data class AnthropicResponse(
    val content: List<AnthropicContent>? = null,
    val error: AnthropicError? = null
)

@JsonClass(generateAdapter = true)
data class AnthropicContent(
    val text: String?
)

@JsonClass(generateAdapter = true)
data class AnthropicError(
    val message: String?
)

@JsonClass(generateAdapter = true)
data class PistonFile(
    val name: String,
    val content: String
)

@JsonClass(generateAdapter = true)
data class PistonRequest(
    val language: String,
    val version: String,
    val files: List<PistonFile>
)

@JsonClass(generateAdapter = true)
data class PistonRunResult(
    val stdout: String? = null,
    val stderr: String? = null,
    val output: String? = null,
    val code: Int? = null,
    val signal: String? = null
)

@JsonClass(generateAdapter = true)
data class PistonResponse(
    val language: String? = null,
    val version: String? = null,
    val run: PistonRunResult? = null,
    val compile: PistonRunResult? = null,
    val message: String? = null
)

interface DynamicApiService {
    @POST("https://emacs.piston.rs/api/v2/execute")
    suspend fun executeCodePiston(
        @Header("Content-Type") contentType: String = "application/json",
        @Header("User-Agent") userAgent: String = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36",
        @Body request: PistonRequest
    ): PistonResponse

    @POST
    suspend fun generateContentGemini(
        @Url url: String,
        @Header("x-goog-api-key") apiKey: String,
        @Header("Content-Type") contentType: String = "application/json",
        @Body request: GenerateContentRequest
    ): GenerateContentResponse

    @POST
    suspend fun generateContentOpenAi(
        @Url url: String,
        @Header("Authorization") authHeader: String,
        @Header("Content-Type") contentType: String = "application/json",
        @Body request: OpenAiRequest
    ): OpenAiResponse

    @POST
    @retrofit2.http.Headers("anthropic-version: 2023-06-01")
    suspend fun generateContentAnthropic(
        @Url url: String,
        @Header("x-api-key") apiKey: String,
        @Header("Content-Type") contentType: String = "application/json",
        @Body request: AnthropicRequest
    ): AnthropicResponse
}

object DynamicClient {
    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
        .readTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
        .writeTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
        .build()

    val service: DynamicApiService by lazy {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://dummy.com/") // Replaced by @Url
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
        retrofit.create(DynamicApiService::class.java)
    }
}
