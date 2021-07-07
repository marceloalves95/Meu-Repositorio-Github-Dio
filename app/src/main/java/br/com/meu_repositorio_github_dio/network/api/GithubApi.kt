package br.com.meu_repositorio_github_dio.network.api

import br.com.meu_repositorio_github_dio.network.domain.MeusRepositorios
import br.com.meu_repositorio_github_dio.network.domain.User
import retrofit2.Response
import retrofit2.http.GET

/**
 * @author RubioAlves
 * Created 06/07/2021 at 09:33
 */
interface GithubApi {

    @GET("users/marceloalves95/repos")
    suspend fun getAllRepository():Response<MutableList<MeusRepositorios>>
    @GET("users/marceloalves95")
    suspend fun getUser():Response<User>
}