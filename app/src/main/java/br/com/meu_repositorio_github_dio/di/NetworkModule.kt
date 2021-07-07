package br.com.meu_repositorio_github_dio.di

import br.com.meu_repositorio_github_dio.network.api.GithubApi
import br.com.meu_repositorio_github_dio.utils.Constants.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @author RubioAlves
 * Created 07/07/2021 at 15:55
 */
object NetworkModule {

    fun getRetrofit(): GithubApi {

        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val okHttpClient: OkHttpClient = OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GithubApi::class.java)

    }
}