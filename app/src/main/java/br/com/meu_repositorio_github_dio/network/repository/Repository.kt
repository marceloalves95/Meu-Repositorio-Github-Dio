package br.com.meu_repositorio_github_dio.network.repository

import br.com.meu_repositorio_github_dio.network.api.GithubApi

/**
 * @author RubioAlves
 * Created 06/07/2021 at 09:42
 */
class Repository(private val api:GithubApi) {

    suspend fun getAllRepository() = api.getAllRepository()
    suspend fun getUser() = api.getUser()

}