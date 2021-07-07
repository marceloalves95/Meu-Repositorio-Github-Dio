# Meu-Repositorio-Github-Dio
> Esse projeto foi proposto no Bootcamping Inter Android Developer. Ele é um projeto de criação de um aplicativo que mostra de forma elegante os seus repositórios no Github. Este projeto utiliza as seguintes bibliotecas: Retrofit, Glide, Injeção de dependência com Koin &amp; Dagger Hilt.

#### Branches

Esse projeto está dividido em duas branches:

- [Meu-Repositorio-Github-Dio com Koin](https://github.com/marceloalves95/Meu-Repositorio-Github-Dio/tree/Meu-Repositorio-Github-Dio-Koin)
- [Meu-Repositorio-Github-Dio com DaggerHilt](https://github.com/marceloalves95/Meu-Repositorio-Github-Dio/tree/Meu-Repositorio-Github-Dio-Dagger-Hilt)

Para mais detalhes sobre a implementação de cada injeção de dependência, escolha a branch acima ou digite o código abaixo para baixar a branch especifica:

```
//Branch Cartao-Visita-Dio-Koin
git clone https://github.com/marceloalves95/Meu-Repositorio-Github-Dio.git --branch Meu-Repositorio-Github-Dio-Koin
//Branch Cartao-Visita-Dio-DaggerHilter
git clone https://github.com/marceloalves95/Meu-Repositorio-Github-Dio.git --branch Meu-Repositorio-Github-Dio-Dagger-Hilt
```
#### Instalação com o DaggerHilt

#### Dependência

Para o uso das bibliotecas usadas nesse projeto, inclua o seguinte código no `build.gradle` do projeto e atualize o `Gradle`:

```groovy
buildscript {

    ext{
        //Retrofit
        retrofit_version="2.9.0"
        okhttp3_logging_interceptor_version="4.9.0"
        //Glide
        glide_version="4.12.0"
        //Dagger Hilt
        hiltVersion = '2.37'
    }
    dependencies{
    .......
        classpath "com.google.dagger:hilt-android-gradle-plugin:$hiltVersion"
       
    }
    .......
}
```

Inclua as seguintes dependências adicionando o código no `build.gradle` do módulo do projeto e atualize o `Gradle`:

```groovy
plugins {
    id 'kotlin-kapt'
    //DaggerHilt
    id 'dagger.hilt.android.plugin'
    ........
}
dependencies {
    
    //Glide
    kapt "com.github.bumptech.glide:compiler:$glide_version"
    implementation "com.github.bumptech.glide:glide:$glide_version"
    annotationProcessor "com.github.bumptech.glide:glide:$glide_version"
    //Retrofit
    implementation "com.squareup.retrofit2:retrofit:$retrofit_version"
    implementation "com.squareup.retrofit2:converter-gson:$retrofit_version"
    implementation "com.squareup.okhttp3:logging-interceptor:$okhttp3_logging_interceptor_version"
    //Dagger Hilt
    kapt "com.google.dagger:hilt-compiler:$hiltVersion"
    implementation "com.google.dagger:hilt-android:$hiltVersion"
    annotationProcessor "com.google.dagger:hilt-compiler:$hiltVersion"
    testAnnotationProcessor "com.google.dagger:hilt-compiler:$hiltVersion"
    testImplementation "com.google.dagger:hilt-android-testing:$hiltVersion"
    androidTestImplementation "com.google.dagger:hilt-android-testing:$hiltVersion"
    androidTestAnnotationProcessor "com.google.dagger:hilt-compiler:2.37"
    
    ..........
}
```

##### Aplicação

Crie uma classe chamado `Application`:

```kotlin
@HiltAndroidApp
class Application:Application()
```

##### AndroidManifest

Acrescente na linha no `AndroidManifest.xml`

```xml
<uses-permission android:name="android.permission.INTERNET" />

    <application
        android:name=".di.Application"
        .....
        android:theme="@style/Theme.MeuTema">
        <activity
            android:name="br.com.meu_repositorio_github_dio.ui.main.MainActivity"
            android:label="@string/app_name"
            android:theme="@style/Theme.MeuTema.NoActionBar">
       .....
</application>
```

##### Módulo

Crie um módulo com uma classe `Singleton` chamado `NetworkModule`:

```kotlin
@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

    @Singleton
    @Provides
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
```

##### API

Crie uma interface chamada `GithubApi.kt`

```kotlin
interface GithubApi {

    @GET("users/marceloalves95/repos")
    suspend fun getAllRepository():Response<MutableList<MeusRepositorios>>
    @GET("users/marceloalves95")
    suspend fun getUser():Response<User>
}
```

##### Repository

Crie uma classe chamada `Repository.kt`

```kotlin
@Singleton
class Repository @Inject constructor(private val api:GithubApi) {

    suspend fun getAllRepository() = api.getAllRepository()
    suspend fun getUser() = api.getUser()

}
```

##### ViewModel

Crie um classe chamada `RepositoriosViewModel.kt` e `UserViewModel.kt`:

```kotlin
@HiltViewModel
class RepositoriosViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

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
```

```kotlin
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
```

#### AndroidEntryPoint

Para cada Activity ou Fragment do seu projeto adicione a seguinte linha antes do seu arquivo

```kotlin
@AndroidEntryPoint
class MainActivit{
.....
}
```
