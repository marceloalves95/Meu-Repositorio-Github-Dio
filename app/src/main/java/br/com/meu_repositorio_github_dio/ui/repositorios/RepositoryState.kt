package br.com.meu_repositorio_github_dio.ui.repositorios

import br.com.meu_repositorio_github_dio.network.domain.MeusRepositorios

sealed class RepositoryState{
    class Sucess(val response: MutableList<MeusRepositorios>?) : RepositoryState()
    class Loading(val isLoading: Boolean) : RepositoryState()
    class Error(val message: String) : RepositoryState()
    object Empty : RepositoryState()
}