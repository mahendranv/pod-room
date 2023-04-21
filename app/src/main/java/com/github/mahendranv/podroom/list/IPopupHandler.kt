package com.github.mahendranv.podroom.list

interface IPopupHandler<T> {

    fun getActions(): List<String>

    fun onActionClicked(position: Int, item: T)
}