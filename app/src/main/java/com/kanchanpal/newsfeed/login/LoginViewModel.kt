package com.kanchanpal.newsfeed.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.kanchanpal.newsfeed.api.Data
import com.kanchanpal.newsfeed.api.NewsListModel
import com.kanchanpal.newsfeed.api.Resource
import com.kanchanpal.newsfeed.api.User
import com.kanchanpal.newsfeed.data.newsSet.NewsRepository
import com.kanchanpal.newsfeed.di.CoroutineScopeIO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val repository: LoginRepository,
    @CoroutineScopeIO private val ioCoroutineScope: CoroutineScope
) : ViewModel() {


    fun login(userName: String, password: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            emit(Resource.success(data = repository.login(userName, password)))
        } catch (e: Exception) {
            emit(Resource.error(data = null, message = e.message ?: "Error Occurred!"))
        }

    }

    override fun onCleared() {
        super.onCleared()
        ioCoroutineScope.cancel()
    }
}