package com.example.projektanz.ui.fragments.maps

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.projektanz.MapContract
import com.example.projektanz.R
import com.example.projektanz.models.MapModelImpl
import com.example.projektanz.ui.MapPresenterImpl
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import hr.algebra.articles.api.Point
import java.lang.Exception

private const val INIT_ZOOM_LEVEL = 15f

class MapFragment: Fragment(), OnMapReadyCallback, MapContract.MapView {

    private lateinit var mMap: GoogleMap
    private lateinit var presenter: MapContract.MapPresenter

    private val markers = mutableMapOf<Marker, Point>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map_activity) as SupportMapFragment
        mapFragment.getMapAsync(this)
        initPresenter()
    }

    private fun initPresenter() {
        presenter = MapPresenterImpl(this, MapModelImpl())
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        configureMap()
        presenter.requestToLoadPoints()
        setAdapter()
    }

    private fun setAdapter() {
        mMap.setInfoWindowAdapter(object: GoogleMap.InfoWindowAdapter {
            override fun getInfoWindow(marker: Marker): View? {
                val view = LayoutInflater.from(context).inflate(R.layout.info_window, null)
                val ivInfo = view.findViewById<ImageView>(R.id.ivInfo)
                val tvInfo = view.findViewById<TextView>(R.id.tvInfo)
                val point = markers[marker]
                if (point != null) {
                    tvInfo.text = point.getInfo()
                    Picasso.get()
                        .load(point.image)
                        .into(ivInfo, object: Callback {
                            override fun onSuccess() {
                                if (marker.isInfoWindowShown) {
                                    marker.hideInfoWindow()
                                    marker.showInfoWindow()
                                }
                            }

                            override fun onError(e: Exception) {
                                Log.d(javaClass.name, e.message, e)
                            }

                        })
                }

                return view
            }

            override fun getInfoContents(p0: Marker) = null

        })
    }

    private fun configureMap() {
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
    }

    private fun addMarker(point: Point) {
        val marker = mMap.addMarker(MarkerOptions().position(point.getLatLng()).title(point.title))
        markers[marker] = point
        animateCamera(point.getLatLng())
    }
    private fun animateCamera(latLng: LatLng)
            = mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, INIT_ZOOM_LEVEL))

    override fun addPoints(points: Collection<Point>) {
        points.forEach {addMarker(it)}
    }

}
