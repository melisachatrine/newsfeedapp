package com.kanchanpal.newsfeed.subscribe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.kanchanpal.newsfeed.databinding.FragmentSubscribeBinding

class SubscribeFragment : Fragment() {

    private lateinit var binding: FragmentSubscribeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSubscribeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAction()
    }

    private fun initAction(){
        binding.btnDaftar.setOnClickListener(){
            Toast.makeText(context, "Terima kasih! Tim kami akan segera menghubungi Anda.", Toast.LENGTH_LONG).show()
        }

        binding.ivNavigation.setOnClickListener(){
            activity?.supportFragmentManager?.popBackStack()
        }
    }

}