package com.lowes.demoapp.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.lowes.demoapp.R
import com.lowes.demoapp.databinding.FragmentFirstBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

private const val TAG = "FirstFragment"
/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG,"onResume")
        viewModel.doInit()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG,"onViewCreated")
        binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
        observeViewModel()
    }

    private fun observeViewModel() {
        Log.d(TAG,"observeViewModel")
        with(lifecycleScope) {
            viewModel.accessTokenInitialized.onEach { result ->
                Log.d(TAG,"access token init result:$result")
                when(result){
                    true ->
                        Log.d(TAG,"token present")
                    false ->
                        Log.d(TAG,"token absent")
                }

            }.launchIn(this)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}