package com.github.mahendranv.podroom.list

interface IPopupHandler {

    fun getActions(): List<String>

    fun onActionClicked(position: Int, item: Any)
}