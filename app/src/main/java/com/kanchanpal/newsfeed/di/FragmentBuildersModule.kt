package com.kanchanpal.newsfeed.di


import com.kanchanpal.newsfeed.login.LoginFragment
import com.kanchanpal.newsfeed.login.SplashFragment
import com.kanchanpal.newsfeed.news.NewsListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class FragmentBuildersModule {

    @ContributesAndroidInjector
    abstract fun contributeThemeFragment(): NewsListFragment

    @ContributesAndroidInjector
    abstract fun contributeLoginFragment(): LoginFragment

    @ContributesAndroidInjector
    abstract fun contributeSplashFragment(): SplashFragment

//    @ContributesAndroidInjector
//    abstract fun contributeSubscribeFragment(): SubscribeFragment
}
