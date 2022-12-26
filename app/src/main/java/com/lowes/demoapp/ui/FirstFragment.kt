package com.lowes.demoapp.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.lowes.demoapp.databinding.FragmentFirstBinding
import com.lowes.demoapp.domain.model.Album
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
    private lateinit var albumsAdapter : AlbumsRecyclerAdapter
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
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG,"onViewCreated")
        binding.search.setOnClickListener {
            var query = binding.queryinputtext.text
            Log.d(TAG,"query : $query")
            if(query.isNotEmpty())
                viewModel.doSearch(query.toString())
            else
                Log.e(TAG,"Empty query")
        }
        binding.newSearch.setOnClickListener {
            viewModel.getNewReleases()
        }
        binding.search.isEnabled = false
        binding.newSearch.isEnabled = false
        initRecyclerView()
        observeViewModel()
    }
    private fun initRecyclerView(){
        Log.d(TAG,"initRecyclerView")
        binding.recyclerView.apply{
            layoutManager = LinearLayoutManager(this@FirstFragment.context)
            albumsAdapter = AlbumsRecyclerAdapter(ArrayList<Album>())
            adapter = albumsAdapter
        }
    }
    private fun observeViewModel() {
        Log.d(TAG,"observeViewModel")
        with(lifecycleScope) {
            viewModel.accessTokenInitialized.onEach { result ->
                Log.d(TAG,"access token init result:$result")
                when(result){
                    true -> {
                        Log.d(TAG, "token present")
                        binding.search.isEnabled = true
                        binding.newSearch.isEnabled = true
                    }
                    false -> {
                        Log.d(TAG, "token absent")
                        binding.search.isEnabled = false
                        binding.newSearch.isEnabled = false
                        viewModel.getAcessToken()
                    }
                }

            }.launchIn(this)

            viewModel.newReleases.onEach { albums ->
                Log.d(TAG,"got updated albums: size: ${albums.size}")
                albumsAdapter.updateAlbums(albums)
                albumsAdapter.notifyDataSetChanged()
            }.launchIn(this)

            viewModel.searchQuery.onEach { query ->
                Log.d(TAG,"got search query")
                binding.queryinputtext.setText(query)
            }.launchIn(this)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}