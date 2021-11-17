package com.kanchanpal.newsfeed.login

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.kanchanpal.newsfeed.api.NewsListModel
import com.kanchanpal.newsfeed.api.Status
import com.kanchanpal.newsfeed.databinding.FragmentLoginNewBinding
import com.kanchanpal.newsfeed.di.Injectable
import com.kanchanpal.newsfeed.di.injectViewModel
import com.kanchanpal.newsfeed.helper.encrypt
import com.kanchanpal.newsfeed.news.NewsListFragmentDirections
import javax.inject.Inject

class LoginFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: LoginViewModel
    private lateinit var binding: FragmentLoginNewBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginNewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = injectViewModel(viewModelFactory)
        binding.btnLogin.setOnClickListener {
            if (!Patterns.EMAIL_ADDRESS.matcher(binding.etEmail.text.toString()).matches() ) {
                Toast.makeText(context, "Email Salah", Toast.LENGTH_LONG).show()

            } else {
                login(binding.etEmail.text.toString(), binding.etPassword.text.toString())

            }

        }

    }

    private fun login(userName: String, password: String) {
        viewModel.login(userName, password).observe(this, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        binding.clProgressBar.visibility = View.GONE
                        resource.data?.let { user ->
                            Toast.makeText(context, user.token.encrypt(), Toast.LENGTH_LONG).show()
                        }
                        findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToNewsListFragment(NewsListModel()))
                    }
                    Status.FAILED -> {
                        binding.clProgressBar.visibility = View.GONE
                        Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                    }
                    Status.RUNNING -> {
                        binding.clProgressBar.visibility = View.VISIBLE
                        Toast.makeText(context, "loading", Toast.LENGTH_LONG).show()
                    }
                }
            }
        })
    }
}