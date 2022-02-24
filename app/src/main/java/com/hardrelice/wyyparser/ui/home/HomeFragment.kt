package com.hardrelice.wyyparser.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.hardrelice.wyyparser.R
import com.hardrelice.wyyparser.utils.Core
import com.hardrelice.wyyparser.utils.UIDetail
import com.hardrelice.wyyparser.utils.UIHandler
import com.hardrelice.wyyparser.utils.handler
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
//        val textView: TextView = root.findViewById(R.id.text_home)
//        homeViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })
        root.button.setOnClickListener{
            it.isEnabled = false
            it.transitionName = "LOADING"
            Thread{
                Core.loadCacheToLocal()
                handler.send(UIHandler.RUN_ANY, UIDetail(runnable = Runnable {
                    it.isEnabled = true
                    it.transitionName = "EXECUTE"
                }))
            }.start()
        }

        return root
    }
}