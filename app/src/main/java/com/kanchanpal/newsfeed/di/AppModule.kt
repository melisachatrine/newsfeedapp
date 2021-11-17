package com.kanchanpal.newsfeed.di

import android.app.Application
import com.kanchanpal.newsfeed.api.LoginService
import com.kanchanpal.newsfeed.api.NewsService
import com.kanchanpal.newsfeed.data.AppDatabase
import com.kanchanpal.newsfeed.data.newsSet.NewsRemoteDataSource
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class, CoreDataModule::class])
class AppModule {

    @Singleton
    @Provides
    fun provideNewsService(@NewsApi okhttpClient: OkHttpClient,
                           converterFactory: GsonConverterFactory
    ) = provideService(okhttpClient, converterFactory, NewsService::class.java)

    @Singleton
    @Provides
    fun provideLoginService(@LoginApi okhttpClient: OkHttpClient,
                            converterFactory: GsonConverterFactory
    ) = provideLoginService(okhttpClient, converterFactory, LoginService::class.java)

    @Singleton
    @Provides
    fun provideNewsRemoteDataSource(newsService: NewsService)
            = NewsRemoteDataSource(newsService)

    @NewsApi
    @Provides
    fun providePrivateOkHttpClient(
        upstreamClient: OkHttpClient
    ): OkHttpClient {
        return upstreamClient.newBuilder().build()
    }

    @LoginApi
    @Provides
    fun provideLoginOkHttpClient(
        upstreamClient: OkHttpClient
    ): OkHttpClient {
        return upstreamClient.newBuilder().build()
    }

    @Singleton
    @Provides
    fun provideDb(app: Application) = AppDatabase.getInstance(app)

    @Singleton
    @Provides
    fun provideNewsSetDao(db: AppDatabase) = db.getNewsListDao()


    @CoroutineScopeIO
    @Provides
    fun provideCoroutineScopeIO() = CoroutineScope(Dispatchers.IO)


    private fun createRetrofit(
        okhttpClient: OkHttpClient,
        converterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(NewsService.ENDPOINT)
            .client(okhttpClient)
            .addConverterFactory(converterFactory)
            .build()
    }

    private fun createLoginRetrofit(
        okhttpClient: OkHttpClient,
        converterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(LoginService.ENDPOINT)
            .client(okhttpClient)
            .addConverterFactory(converterFactory)
            .build()
    }

    private fun <T> provideService(okhttpClient: OkHttpClient,
                                   converterFactory: GsonConverterFactory, clazz: Class<T>): T {
        return createRetrofit(okhttpClient, converterFactory).create(clazz)
    }

    private fun <T> provideLoginService(okhttpClient: OkHttpClient,
                                        converterFactory: GsonConverterFactory, clazz: Class<T>): T {
        return createLoginRetrofit(okhttpClient, converterFactory).create(clazz)
    }
}
