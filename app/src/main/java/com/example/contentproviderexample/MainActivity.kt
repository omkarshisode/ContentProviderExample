package com.example.contentproviderexample

import android.content.ContentUris
import android.media.Image
import android.os.Bundle
import android.provider.MediaStore.Images.Media
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Calendar

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val projection = arrayOf(Media._ID, Media.DISPLAY_NAME)
        val selection = "${Media.DATE_TAKEN} >= ?"
        val millisSecYesterday = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, - 1)
        }.timeInMillis
        val selectionArg = arrayOf(millisSecYesterday.toString())
        val sortOrder = "${Media.DATE_TAKEN} DESC"
        contentResolver.query(
            Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArg,
            sortOrder
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndex(Media._ID)
            val nameColumn = cursor.getColumnIndex(Media.DISPLAY_NAME)

            val images = mutableListOf<Image>()
            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val name = cursor.getColumnName(nameColumn)
                val contentUri = ContentUris.withAppendedId(Media.EXTERNAL_CONTENT_URI, id)
            }
        }
    }
}