package br.com.meu_repositorio_github_dio.ui.repositorios

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.meu_repositorio_github_dio.network.repository.Repository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RepositoriosViewModel(private val repository: Repository) : ViewModel() {

    private val _state = MutableStateFlow<RepositoryState>(RepositoryState.Empty)
    val state: StateFlow<RepositoryState> = _state

    private fun emit(value: RepositoryState) {
        _state.value = value
    }

    fun getAllRepository() {

        viewModelScope.launch {

            try {
                emit(RepositoryState.Loading(true))
                delay(1000)
                val response = repository.getAllRepository()
                with(response) {
                    if (isSuccessful) {
                        emit(RepositoryState.Sucess(body()))
                    } else {
                        emit(RepositoryState.Error("Erro: ${code()}"))
                    }
                }
            } catch (exception: Exception) {
                emit(RepositoryState.Error("Erro: ${exception.message}"))
            }
            emit(RepositoryState.Loading(false))
        }

    }


}