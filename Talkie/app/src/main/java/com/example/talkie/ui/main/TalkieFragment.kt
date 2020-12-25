package com.example.talkie.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.talkie.R
import kotlinx.android.synthetic.main.main_fragment.*

class TalkieFragment : Fragment() {

    private lateinit var viewModel: TalkieViewModel

    companion object {
        fun newInstance() = TalkieFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(TalkieViewModel::class.java)

        val activity = activity ?: return
        viewModel.doBindService(activity)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        button.setOnClickListener {
            viewModel.send(outgoing_edit_text.text.toString())
        }
    }

    override fun onDestroy() {
        val activity = activity
        if (activity != null)
            viewModel.doUnbindService(activity)

        super.onDestroy()
    }
}