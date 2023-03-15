package com.volie.lastearthquakes.ui.fragment

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.volie.lastearthquakes.R
import com.volie.lastearthquakes.databinding.FragmentEarthquakeBinding
import com.volie.lastearthquakes.ui.adapter.EarthquakeAdapter
import com.volie.lastearthquakes.util.Status
import com.volie.lastearthquakes.viewmodel.EarthquakeViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class EarthquakeFragment
@Inject constructor() : Fragment(), SearchView.OnQueryTextListener {
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
        setupMenu()
        setupRecyclerView()
        mViewModel.getEarthquakes()
        pullToRefresh()
        initObserver()
    }

    private fun setupMenu() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.earthquake_fargment_search_menu, menu)

                val search = menu.findItem(R.id.menu_search)
                val searchView = search.actionView as SearchView
                searchView.isSubmitButtonEnabled = true
                searchView.setOnQueryTextListener(this@EarthquakeFragment)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    android.R.id.home -> requireActivity()
                        .onBackPressedDispatcher
                        .onBackPressed()
                    R.id.menu_sort_last -> mViewModel.getEarthquakes()
                    R.id.menu_sort_highMag -> mViewModel.sortHighMag()
                    R.id.menu_sort_lowMag -> mViewModel.sortLowMag()
                }
                return true
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
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
        mViewModel.earthquakes.observe(viewLifecycleOwner) { resource ->
            when (resource.status) {
                Status.SUCCESS -> {
                    with(mBinding) {
                        recyclerView.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        tvError.visibility = View.GONE
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
            resource.data?.let {
                mAdapter.submitList(it.data)
            }
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText != null) {
            mViewModel.searchDatabase(newText)
        }
        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _mBinding = null
    }
}