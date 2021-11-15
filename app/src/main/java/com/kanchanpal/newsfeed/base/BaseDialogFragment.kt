package com.kanchanpal.newsfeed.base

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager

abstract class BaseDialogFragment : androidx.fragment.app.DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadArguments()
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        setupDialogStyle(dialog)
        return dialog
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return if (0 != getLayout()) {
            inflater.inflate(getLayout(), container, false)
        } else super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initInjection()
        super.onViewCreated(view, savedInstanceState)
        initSubscription()
        internalSetup()
        setup()
    }

    abstract fun setupDialogStyle(dialog: Dialog)

    abstract fun loadArguments()

    open fun initInjection(){}

    open fun internalSetup(){}

    open fun initSubscription() {}


    override fun show(manager: FragmentManager, tag: String?) {
        try {
            super.show(manager, tag);
        } catch (ignored: IllegalStateException) {

        }
    }

    override fun dismiss() {
        if (isStateSaved) {
            dismissAllowingStateLoss()
        } else {
            super.dismiss()
        }
    }

    abstract fun setup()
    abstract fun getLayout():Int

}