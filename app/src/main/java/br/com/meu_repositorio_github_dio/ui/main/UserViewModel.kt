package br.com.meu_repositorio_github_dio.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.meu_repositorio_github_dio.network.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author RubioAlves
 * Created 06/07/2021 at 14:50
 */
@HiltViewModel
class UserViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private val _state = MutableStateFlow<UIState>(UIState.Empty)
    val state: StateFlow<UIState> = _state

    private fun emit(value: UIState) {
        _state.value = value
    }

    fun getUser() {

        viewModelScope.launch {

            try {
                emit(UIState.Loading(true))
                delay(1000)
                val response = repository.getUser()
                with(response) {
                    if (isSuccessful) {
                        emit(UIState.Sucess(body()))
                    } else {
                        emit(UIState.Error("Erro: ${code()}"))
                    }
                }
            } catch (exception: Exception) {
                emit(UIState.Error("Erro: ${exception.message}"))
            }
            emit(UIState.Loading(false))
        }

    }

}