package com.volie.lastearthquakes.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.volie.lastearthquakes.R
import com.volie.lastearthquakes.databinding.FragmentEarthquakeMapsBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class EarthquakeMapsFragment
@Inject constructor() : Fragment() {
    private var _mBinding: FragmentEarthquakeMapsBinding? = null
    private val mBinding get() = _mBinding!!
    private lateinit var args: EarthquakeMapsFragmentArgs

    private val callback = OnMapReadyCallback { googleMap ->
        setLocationInfo(googleMap)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _mBinding = FragmentEarthquakeMapsBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            args = EarthquakeMapsFragmentArgs.fromBundle(it)
        }

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        setDetails()
    }

    private fun setDetails() {
        val args = args.earthquake
        with(mBinding.include) {
            txtName.text = args.name
            txtDate.text = args.date
            cardMag.setCardBackgroundColor(args.magnitudeColor)
            rootInclude.setBackgroundColor(args.magnitudeColorLight)
            txtTime.text = args.time
            txtDepth.text = args.depth
            txtMag.text = args.magnitudeText
        }
    }

    private fun setLocationInfo(googleMap: GoogleMap) {
        val lng = args.earthquake.geoJson.coordinates[0]
        val lat = args.earthquake.geoJson.coordinates[1]
        val earthquake = LatLng(lat, lng)
        with(googleMap) {
            addMarker(MarkerOptions().position(earthquake).title(args.earthquake.name))
            moveCamera(CameraUpdateFactory.newLatLngZoom(earthquake, 10f))
            mapType = GoogleMap.MAP_TYPE_HYBRID
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _mBinding = null
    }
}