package com.github.mahendranv.podroom

import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.URL
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

/**
 * Given a flow that emits List, this helper collects the emit (list of elements) once and returns first element.
 */
suspend fun <T> Flow<List<T>>.pickFirstElement(): T? {
    val list = firstOrNull() // collects flow of list
    return list?.firstOrNull()
}

fun downloadContent(url: String, file: File) {
    val src = URL(url)
    val output = FileOutputStream(file)
    try {
        val inputStream = src.openStream()
        inputStream.use { input ->
            output.use { output ->
                input.copyTo(output)
            }
        }
        println("File downloaded successfully.")
    } catch (e: IOException) {
        println("Error downloading file: ${e.message}")
    }
}