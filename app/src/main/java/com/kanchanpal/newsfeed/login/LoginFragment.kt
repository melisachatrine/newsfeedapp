package com.kanchanpal.newsfeed.login

import android.app.Activity
import android.os.Bundle
import android.text.method.SingleLineTransformationMethod
import android.util.Patterns
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.kanchanpal.newsfeed.R
import com.kanchanpal.newsfeed.api.NewsListModel
import com.kanchanpal.newsfeed.api.Status
import com.kanchanpal.newsfeed.databinding.FragmentLoginNewBinding
import com.kanchanpal.newsfeed.di.Injectable
import com.kanchanpal.newsfeed.di.injectViewModel
import com.kanchanpal.newsfeed.helper.AsteriskPasswordTransformationMethod
import com.kanchanpal.newsfeed.helper.UserStorage
import com.kanchanpal.newsfeed.helper.encrypt
import com.kanchanpal.newsfeed.helper.isValidEmail
import com.kanchanpal.newsfeed.news.NewsListFragmentDirections
import kotlinx.android.synthetic.main.fragment_login_new.*
import javax.inject.Inject

class LoginFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: LoginViewModel
    private lateinit var binding: FragmentLoginNewBinding
    lateinit var userStorage: UserStorage


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
        userStorage = UserStorage(context!!)
        viewModel = injectViewModel(viewModelFactory)

        etPassword.transformationMethod = AsteriskPasswordTransformationMethod.getInstance()

        initAction()

    }

    private fun initAction(){
        etPassword.setOnTouchListener { _, event ->
            val drawableRight = 2
            if (event.action == MotionEvent.ACTION_DOWN) {
                if (event.rawX >= (etPassword.right - etPassword.compoundDrawables[drawableRight].bounds.width())) {
                    if (etPassword.transformationMethod == AsteriskPasswordTransformationMethod.getInstance()) {
                        etPassword.transformationMethod = SingleLineTransformationMethod.getInstance()
                        etPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_eye, 0)
                    } else {
                        etPassword.transformationMethod = AsteriskPasswordTransformationMethod.getInstance()
                        etPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_invisibility, 0)
                    }
                    return@setOnTouchListener true
                }
            }
            return@setOnTouchListener false
        }

        binding.btnLogin.setOnClickListener {

            val imm = context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager?
            imm?.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)


            var email = binding.etEmail.text.toString().trim()
            var password = binding.etPassword.text.toString().trim()

            if (email.isValidEmail() && password.isNotEmpty()) {
                login(binding.etEmail.text.toString(), binding.etPassword.text.toString())
            } else {
                if(email.isEmpty()) {
                    Toast.makeText(context, "Email tidak boleh kosong.", Toast.LENGTH_LONG).show()
                }
                else if (!email.isValidEmail()) {
                    Toast.makeText(context, "Format email salah.", Toast.LENGTH_LONG).show()
                }
                else if (password.isEmpty()) {
                    Toast.makeText(context, "Password tidak boleh kosong.", Toast.LENGTH_LONG).show()
                }
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
                            userStorage.setUser(user)
                        }
                        findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToNewsListFragment(NewsListModel()))
                    }
                    Status.FAILED -> {
                        binding.clProgressBar.visibility = View.GONE
                        Toast.makeText(context, "Email/Password yang Anda Masukkan Salah", Toast.LENGTH_LONG).show()
                    }
                    Status.RUNNING -> {
                        binding.clProgressBar.visibility = View.VISIBLE
                    }
                }
            }
        })
    }
}