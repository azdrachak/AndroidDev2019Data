@file:Suppress("unused")

package com.example.androiddata.utilities

import android.app.Application
import android.content.Context
import java.io.File
import java.nio.charset.Charset

class FileHelper {
    companion object {
        fun getTextFromResources(context: Context, resourceId: Int): String {
            return context.resources.openRawResource(resourceId).use { it ->
                it.bufferedReader().use {
                    it.readText()
                }
            }
        }

        fun getTextFromAssets(context: Context, fileName: String): String {
            return context.assets.open(fileName).use { it ->
                it.bufferedReader().use {
                    it.readText()
                }
            }
        }

        fun saveTextToFile(app: Application, json: String?) {
//            val file = File(app.filesDir, "monsters.json")
            val file = File(app.cacheDir, "monsters.json")
            file.writeText(json ?: "", Charsets.UTF_8)
        }

        fun readTextFile(app: Application): String? {
            File(app.cacheDir, "monsters.json").let {
                return if (it.exists()) it.readText() else null
            }
        }
    }
}