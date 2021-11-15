package com.kanchanpal.newsfeed.base

import android.app.Dialog
import com.kanchanpal.newsfeed.R
import kotlinx.android.synthetic.main.confirmation_dialog_fragment.*

class ConfirmationDialogFragment(val string: String, val yesString: String? = null, val noString: String? = null) : BaseDialogFragment(){

    private var onYesListener: () -> Unit = {}
    private var onNoListener: () -> Unit = {}

    override fun setupDialogStyle(dialog: Dialog) {}

    override fun loadArguments() {
        setStyle(STYLE_NORMAL, R.style.DefaultDialog_Fullscreen_Transparent)
    }

    override fun setup() {
        initView()
        initAction()
    }

    override fun getLayout(): Int = R.layout.confirmation_dialog_fragment

    private fun initView(){
        tvLabel.text = string
        if(null != yesString){
            btnYes.text = yesString
        }
        if(null != noString){
            btnNo.text = noString
        }
    }

    private fun initAction(){
        clMain.setOnClickListener { dismiss() }
        btnYes.setOnClickListener {
            onYesListener()
            dismiss()
        }
        btnNo.setOnClickListener {
            onNoListener()
            dismiss()
        }

    }

    fun setOnYesListener(listener: ()-> Unit){
        this.onYesListener = listener
    }

    fun setOnNoListener(listener: ()-> Unit){
        this.onNoListener = listener
    }
}