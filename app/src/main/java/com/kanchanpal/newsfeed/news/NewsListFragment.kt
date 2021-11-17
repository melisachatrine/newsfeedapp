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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.kanchanpal.newsfeed.api.Status
import com.kanchanpal.newsfeed.base.ConfirmationDialogFragment
import com.kanchanpal.newsfeed.commonUtil.ConnectivityUtil
import com.kanchanpal.newsfeed.databinding.FragmentNewsListBinding
import com.kanchanpal.newsfeed.di.Injectable
import com.kanchanpal.newsfeed.di.injectViewModel
import com.kanchanpal.newsfeed.helper.UserStorage
import com.kanchanpal.newsfeed.login.SplashFragmentDirections
import kotlinx.android.synthetic.main.fragment_news_list.*
import javax.inject.Inject

class NewsListFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: NewsViewModel
    private var isConnected : Boolean = true
    private lateinit var  binding : FragmentNewsListBinding
    lateinit var userStorage: UserStorage
    val adapter = NewsAdapter()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userStorage = UserStorage(context!!)
        viewModel = injectViewModel(viewModelFactory)

        initAction()
        checkConnection()
    }

    private fun checkConnection(){
        isConnected = ConnectivityUtil.isConnected(context)

        if (!isConnected) {
            tvNoConnection.visibility = View.VISIBLE
            rvNewsList.visibility = View.GONE
            progressBar.visibility = View.GONE
        } else {
            tvNoConnection.visibility = View.GONE
            rvNewsList.visibility = View.VISIBLE

            binding.rvNewsList.adapter = adapter
            subscribeUI(adapter, "firstLoad")

            binding.refreshLayout.setOnRefreshListener() {
                tvNoConnection.visibility = View.GONE
                subscribeUI(adapter, "refreshPage")
                checkConnection()
            }
        }
    }

    private fun initAction(){

        binding.refreshLayout.setOnRefreshListener() {
            tvNoConnection.visibility = View.GONE
            subscribeUI(adapter, "refreshPage")
            checkConnection()
        }

        binding.icLogout.setOnClickListener() {

            val fragment = ConfirmationDialogFragment("Apakah Anda ingin keluar dari aplikasi Indonews?","Ya","Tidak")
            fragment.apply {
                fragment.setOnNoListener {
                    //dismiss here
                }
                fragment.setOnYesListener {
                    //delete all repositories and cache data & direct to login page
                    userStorage.delUser()
                    findNavController().navigate(NewsListFragmentDirections.actionNewsListFragmentToSplashFragment())
                }
            }
            fragment.show(
                childFragmentManager,
                ConfirmationDialogFragment::class.java.canonicalName
            )
        }

        binding.rvNewsList.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if(binding.rvNewsList.canScrollVertically(-1)){
                    ivBackToTop.visibility = View.VISIBLE
                } else {
                    ivBackToTop.visibility = View.GONE
                }
            }
        })

        ivBackToTop.setOnClickListener {
            binding.rvNewsList.scrollToPosition(0)
        }
    }

    private fun subscribeUI(adapter: NewsAdapter, action: String) {

        val data = viewModel.newsList(isConnected, action)
        binding.refreshLayout.finishRefresh()
        data?.networkState?.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.RUNNING -> {
                    progressBar.visibility = View.VISIBLE
                }
                Status.FAILED -> {
                    tvNoConnection.visibility = View.VISIBLE
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
