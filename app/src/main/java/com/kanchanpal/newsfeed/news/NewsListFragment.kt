package com.kanchanpal.newsfeed.news

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kanchanpal.newsfeed.api.Status
import com.kanchanpal.newsfeed.base.ConfirmationDialogFragment
import com.kanchanpal.newsfeed.commonUtil.ConnectivityUtil
import com.kanchanpal.newsfeed.databinding.FragmentNewsListBinding
import com.kanchanpal.newsfeed.di.Injectable
import com.kanchanpal.newsfeed.di.injectViewModel
import kotlinx.android.synthetic.main.fragment_news_list.*
import javax.inject.Inject

class NewsListFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: NewsViewModel
    private var isConnected : Boolean = true
    private lateinit var  binding : FragmentNewsListBinding
    val adapter = NewsAdapter()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAction()

        viewModel = injectViewModel(viewModelFactory)
        isConnected = ConnectivityUtil.isConnected(context)
        if (!isConnected)
            Toast.makeText(
                context?.applicationContext,
                "No internet connection!",
                Toast.LENGTH_SHORT
            ).show()

        binding.rvNewsList.adapter = adapter
        subscribeUI(adapter)

        binding.refreshLayout.setOnRefreshListener() {
            subscribeUI(adapter)
        }
    }

    private fun initAction(){
        binding.icLogout.setOnClickListener() {

            val fragment = ConfirmationDialogFragment("Apakah Anda ingin keluar dari aplikasi Indonews?","Ya","Tidak")
            fragment.apply {
                fragment.setOnNoListener {
                    //dismiss here
                }
                fragment.setOnYesListener {
                    //delete all repositories and cache data & direct to login page
                }
            }
            fragment.show(
                childFragmentManager,
                ConfirmationDialogFragment::class.java.canonicalName
            )
        }
    }

    private fun subscribeUI(adapter: NewsAdapter) {

        val data = viewModel.newsList(isConnected)
        binding.refreshLayout.finishRefresh()
        data?.networkState?.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.RUNNING -> {
                    progressBar.visibility = View.VISIBLE
                }
                Status.FAILED -> {
                    progressBar.visibility = View.GONE
                    // Handle fail state
                }
                Status.SUCCESS -> {
                    progressBar.visibility = View.GONE
                }
            }
        })
        data?.pagedList?.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

    }
}
