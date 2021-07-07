package br.com.meu_repositorio_github_dio.di

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

/**
 * @author RubioAlves
 * Created 06/07/2021 at 09:13
 */
class Application:Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@Application)
            modules(AppModules.appModules)
        }

    }
}