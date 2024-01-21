package com.example.sideproject_youtube_mvp.contract

interface BaseContract {
    interface Presenter<T : View> {
        var view: T?

        fun onDestroy() {
            view = null
        }
    }

    interface View
}