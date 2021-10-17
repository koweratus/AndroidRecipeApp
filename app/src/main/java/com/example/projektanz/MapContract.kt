package com.example.projektanz

import hr.algebra.articles.api.Point

interface MapContract {
    interface MapModel {
        interface OnPointsLoadedListener {
            fun onPointsLoaded(points: Collection<Point>)
        }
        fun loadPoints(listener: OnPointsLoadedListener)

    }
    interface MapView {
        fun addPoints(points: Collection<Point>)
    }
    interface MapPresenter {
        fun requestToLoadPoints()
        fun onDestroy()
    }
}