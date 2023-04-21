package com.github.mahendranv.podroom.list

/**
 * Contract to convert given object to UI string.
 */
interface IStringifier<T> {

    /**
     * Prepares a [CharSequence] from given object.
     */
    fun prepareUiString(item: T): CharSequence
}