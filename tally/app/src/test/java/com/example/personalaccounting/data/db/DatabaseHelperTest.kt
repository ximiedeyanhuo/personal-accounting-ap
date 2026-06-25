package com.example.personalaccounting.data.db

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@RunWith(AndroidJUnit4::class)
class DatabaseHelperTest {
    private lateinit var dbHelper: DatabaseHelper

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        dbHelper = DatabaseHelper(context)
    }

    @After
    @Throws(IOException::class)
    fun tearDown() {
        dbHelper.close()
    }

    @Test
    fun testDatabaseCreation() {
        val db = dbHelper.writableDatabase
        assertTrue(db.isOpen)
        db.close()
    }

    @Test
    fun testTablesExist() {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery(
            "SELECT name FROM sqlite_master WHERE type='table' AND name IN ('transactions', 'categories')",
            null
        )
        assertTrue(cursor.count >= 2)
        cursor.close()
    }
}
