package com.volie.lastearthquakes.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import coil.load
import com.google.android.material.elevation.SurfaceColors
import com.volie.lastearthquakes.R
import com.volie.lastearthquakes.databinding.FragmentEarthquakeRiskMapBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class EarthquakeRiskMapFragment
@Inject constructor() : Fragment() {
    private var _mBinding: FragmentEarthquakeRiskMapBinding? = null
    private val mBinding get() = _mBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _mBinding = FragmentEarthquakeRiskMapBinding.inflate(inflater, container, false)
        setStatusAndNavBarColor()
        mBinding.imgRiskMap.load(
            "https://www.afad.gov.tr/kurumlar/afad.gov.tr/24212/pics/image-b592cc237f473.png"
        ) {
            crossfade(true)
            placeholder(R.drawable.img_risk_map)
        }
        return mBinding.root
    }

    private fun setStatusAndNavBarColor() {
        val window = activity?.window
        val color = SurfaceColors.SURFACE_2.getColor(requireContext())
        window!!.statusBarColor = color
        window.navigationBarColor = color
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _mBinding = null
    }

}