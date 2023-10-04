package com.slack.exercise.search.data.util

import android.content.Context
import java.io.*

object FileUtil {

    fun copyRawResourceToFile(context: Context, resourceId: Int, destinationFile: File) {
        try {
            context.resources.openRawResource(resourceId).use { inputStream ->
                FileOutputStream(destinationFile).use { outputStream ->
                    val buffer = ByteArray(1024)
                    var read: Int
                    while (inputStream.read(buffer).also { read = it } != -1) {
                        outputStream.write(buffer, 0, read)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun writeToFile(file: File, data: List<String>, append: Boolean = false) {
        try {
            if (!file.exists()) {
                file.createNewFile()
            }
            FileWriter(file, append).buffered().use { out ->
                data.forEach { line ->
                    out.write(line)
                    out.newLine()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace() // TODO: Remove exceptions
        }
    }

    fun readFromFile(file: File): List<String> {
        val data = mutableListOf<String>()
        try {
            if (file.exists()) {
                file.bufferedReader().use { reader ->
                    reader.forEachLine { line ->
                        data.add(line)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace() // TODO: Remove exceptions
        }
        return data
    }
}