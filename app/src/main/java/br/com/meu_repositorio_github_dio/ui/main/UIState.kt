package br.com.meu_repositorio_github_dio.ui.main

import br.com.meu_repositorio_github_dio.network.domain.User

sealed class UIState{
    class Sucess(val response: User?) : UIState()
    class Loading(val isLoading: Boolean) : UIState()
    class Error(val message: String) : UIState()
    object Empty : UIState()
}