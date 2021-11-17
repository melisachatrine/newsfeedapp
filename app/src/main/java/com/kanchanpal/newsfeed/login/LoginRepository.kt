package com.kanchanpal.newsfeed.login

import com.kanchanpal.newsfeed.api.LoginService
import com.kanchanpal.newsfeed.api.User
import javax.inject.Inject

class LoginRepository @Inject constructor(private val service: LoginService) {

    suspend fun login(username: String, password: String): User {
        return service.login(username, password)
    }
}

