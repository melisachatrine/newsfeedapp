package com.kanchanpal.newsfeed.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.kanchanpal.newsfeed.api.NewsListModel
import com.kanchanpal.newsfeed.api.User
import com.kanchanpal.newsfeed.databinding.FragmentLoginNewBinding
import com.kanchanpal.newsfeed.databinding.FragmentSplashBinding
import com.kanchanpal.newsfeed.di.Injectable
import com.kanchanpal.newsfeed.helper.UserStorage

class SplashFragment: Fragment(), Injectable {
    private lateinit var binding: FragmentSplashBinding
    lateinit var userStorage: UserStorage

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userStorage = UserStorage(context!!)
        if(userStorage.getUser() != null){
            Log.e("userstorage",userStorage.getUser().toString())
            //to newslist
            findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToNewsListFragment(NewsListModel()))

        }else{
            //to login
            findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToLoginFragment())
        }
    }
}