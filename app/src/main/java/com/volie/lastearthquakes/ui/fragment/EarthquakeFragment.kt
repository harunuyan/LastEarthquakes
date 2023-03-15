package com.volie.lastearthquakes.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.volie.lastearthquakes.databinding.FragmentEarthquakeBinding
import com.volie.lastearthquakes.ui.adapter.EarthquakeAdapter
import com.volie.lastearthquakes.util.Status
import com.volie.lastearthquakes.viewmodel.EarthquakeViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class EarthquakeFragment
@Inject constructor() : Fragment() {
    private var _mBinding: FragmentEarthquakeBinding? = null
    private val mBinding get() = _mBinding!!
    private val mViewModel: EarthquakeViewModel by viewModels()
    private val mAdapter: EarthquakeAdapter by lazy {
        EarthquakeAdapter {
            val action =
                EarthquakeFragmentDirections.actionEarthquakeFragmentToEarthquakeMapsFragment(it)
            findNavController().navigate(action)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _mBinding = FragmentEarthquakeBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        mViewModel.getEarthquakes()
        pullToRefresh()
        initObserver()
    }

    private fun pullToRefresh() {
        mBinding.swipeRefreshLayout.setOnRefreshListener {
            mBinding.swipeRefreshLayout.isRefreshing = false
            mViewModel.refreshEarthquakes()
        }
    }

    private fun setupRecyclerView() {
        mBinding.recyclerView.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    fun initObserver() {
        mViewModel.news.observe(viewLifecycleOwner) { resource ->
            when (resource.status) {
                Status.SUCCESS -> {
                    with(mBinding) {
                        recyclerView.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        tvError.visibility = View.GONE
                    }
                    resource.data?.let {
                        mAdapter.submitList(it.data)
                    }
                }
                Status.ERROR -> {
                    with(mBinding) {
                        progressBar.visibility = View.GONE
                        tvError.visibility = View.VISIBLE
                    }
                }
                Status.LOADING -> {
                    with(mBinding) {
                        progressBar.visibility = View.VISIBLE
                        recyclerView.visibility = View.GONE
                        tvError.visibility = View.GONE
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _mBinding = null
    }
}