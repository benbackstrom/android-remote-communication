package com.example.walkie.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.walkie.R
import kotlinx.android.synthetic.main.main_fragment.*

class WalkieFragment : Fragment() {

    private lateinit var viewModel: WalkieViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val activity = activity ?: return

        viewModel = ViewModelProvider(this).get(WalkieViewModel::class.java)
        viewModel.textUpdater = { text ->
            incoming_text.text = text
        }

        viewModel.doBindService(activity)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onDestroy() {
        val activity = activity
        if (activity != null)
            viewModel.doUnbindService(activity)

        super.onDestroy()
    }

    companion object {
        fun newInstance(): WalkieFragment {
            return WalkieFragment()
        }
    }
}