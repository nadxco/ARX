package com.example.data.api

import com.example.BuildConfig
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@JsonClass(generateAdapter = true)
data class GenerateContentRequest(
    val contents: List<Content>,
    val systemInstruction: Content? = null,
    val generationConfig: GenerationConfig? = null
)

@JsonClass(generateAdapter = true)
data class Content(
    val parts: List<Part>,
    val role: String? = null
)

@JsonClass(generateAdapter = true)
data class Part(
    val text: String? = null
)

@JsonClass(generateAdapter = true)
data class GenerationConfig(
    val temperature: Float? = null
)

@JsonClass(generateAdapter = true)
data class GenerateContentResponse(
    val candidates: List<Candidate>? = null,
    val error: GeminiError? = null
)

@JsonClass(generateAdapter = true)
data class Candidate(
    val content: Content
)

@JsonClass(generateAdapter = true)
data class GeminiError(
    val message: String
)

interface GeminiApiService {
    @POST("v1beta/models/gemini-3.1-pro-preview:generateContent")
    suspend fun generateContent(
        @Query("key") apiKey: String,
        @Body request: GenerateContentRequest
    ): GenerateContentResponse
}

object RetrofitClient {
    private const val BASE_URL = "https://generativelanguage.googleapis.com/"

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
        .readTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
        .writeTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
        .build()

    val service: GeminiApiService by lazy {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
        retrofit.create(GeminiApiService::class.java)
    }
}

suspend fun generateCodeResponse(prompt: String, codeContext: String = ""): String = withContext(Dispatchers.IO) {
    val apiKey = BuildConfig.GEMINI_API_KEY
    if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
        return@withContext "Error: Please set your Gemini API key in the Secrets panel."
    }
    
    val fullPrompt = if (codeContext.isNotBlank()) {
        "Current Files in workspace:\n\n$codeContext\n\nUser Request: $prompt"
    } else {
        prompt
    }

    val sysPrompt = "You are an expert AI coding assistant built into a mobile code editor. You have direct access to the user's files and can rewrite or create them.\n\n" +
        "To modify or create a file, you MUST output a code block with the 'file:' prefix. The app will intercept this and update the files invisibly.\n" +
        "Format exactly like this:\n\n" +
        "```file:main.kt\n" +
        "fun main() {\n" +
        "    println(\"Hello\")\n" +
        "}\n" +
        "```\n\n" +
        "You CAN output multiple file blocks to update several files. ALWAYS provide the FULL complete file content inside the block. Provide brief chat answers outside of the blocks to explain what you've done."

    val request = GenerateContentRequest(
        contents = listOf(Content(parts = listOf(Part(text = fullPrompt)))),
        systemInstruction = Content(parts = listOf(Part(text = sysPrompt))),
        generationConfig = GenerationConfig(temperature = 0.2f)
    )

    try {
        val response = RetrofitClient.service.generateContent(apiKey, request)
        if (response.error != null) {
            "Error: ${response.error.message}"
        } else {
            response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text ?: "No response from AI."
        }
    } catch (e: Exception) {
        "Network Error: ${e.message}"
    }
}
