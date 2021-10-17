package com.example.projektanz.ui

import com.example.projektanz.MapContract
import hr.algebra.articles.api.Point

class MapPresenterImpl(
    private var view: MapContract.MapView?,
    private val model: MapContract.MapModel
) : MapContract.MapPresenter, MapContract.MapModel.OnPointsLoadedListener {
    override fun requestToLoadPoints() {
        model.loadPoints(this)
    }

    override fun onPointsLoaded(points: Collection<Point>) {
        view?.addPoints(points)
    }

    override fun onDestroy() {
        view = null
    }

}