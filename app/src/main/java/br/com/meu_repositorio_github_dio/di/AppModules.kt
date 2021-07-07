package br.com.meu_repositorio_github_dio.di

import br.com.meu_repositorio_github_dio.network.api.GithubApi
import br.com.meu_repositorio_github_dio.network.repository.Repository
import br.com.meu_repositorio_github_dio.ui.main.UserViewModel
import br.com.meu_repositorio_github_dio.ui.repositorios.RepositoriosViewModel
import br.com.meu_repositorio_github_dio.utils.Constants.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @author RubioAlves
 * Created 06/07/2021 at 09:15
 */
object AppModules {

    val appModules = module {
        single {
            NetworkModule.getRetrofit()
        }
        single {
            Repository(get())
        }
        viewModel {
            RepositoriosViewModel(get())
        }
        viewModel {
            UserViewModel(get())
        }

    }

}