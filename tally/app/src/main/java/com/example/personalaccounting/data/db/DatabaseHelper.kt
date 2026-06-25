package com.example.personalaccounting.data.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(
    context,
    DATABASE_NAME,
    null,
    DATABASE_VERSION
) {
    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "personal_accounting.db"

        const val TABLE_TRANSACTIONS = "transactions"
        const val COLUMN_TRANSACTION_ID = "id"
        const val COLUMN_TRANSACTION_AMOUNT = "amount"
        const val COLUMN_TRANSACTION_TYPE = "type"
        const val COLUMN_TRANSACTION_CATEGORY_ID = "category_id"
        const val COLUMN_TRANSACTION_DATE = "date"
        const val COLUMN_TRANSACTION_NOTE = "note"
        const val COLUMN_TRANSACTION_CREATED_AT = "created_at"
        const val COLUMN_TRANSACTION_UPDATED_AT = "updated_at"

        const val TABLE_CATEGORIES = "categories"
        const val COLUMN_CATEGORY_ID = "id"
        const val COLUMN_CATEGORY_NAME = "name"
        const val COLUMN_CATEGORY_ICON = "icon"
        const val COLUMN_CATEGORY_COLOR = "color"
        const val COLUMN_CATEGORY_TYPE = "type"
        const val COLUMN_CATEGORY_IS_DEFAULT = "is_default"
        const val COLUMN_CATEGORY_CREATED_AT = "created_at"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createCategoriesTable = """
            CREATE TABLE $TABLE_CATEGORIES (
                $COLUMN_CATEGORY_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_CATEGORY_NAME TEXT NOT NULL,
                $COLUMN_CATEGORY_ICON TEXT,
                $COLUMN_CATEGORY_COLOR TEXT,
                $COLUMN_CATEGORY_TYPE TEXT NOT NULL,
                $COLUMN_CATEGORY_IS_DEFAULT INTEGER DEFAULT 0,
                $COLUMN_CATEGORY_CREATED_AT TEXT NOT NULL
            )
        """.trimIndent()

        val createTransactionsTable = """
            CREATE TABLE $TABLE_TRANSACTIONS (
                $COLUMN_TRANSACTION_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_TRANSACTION_AMOUNT REAL NOT NULL,
                $COLUMN_TRANSACTION_TYPE TEXT NOT NULL,
                $COLUMN_TRANSACTION_CATEGORY_ID INTEGER NOT NULL,
                $COLUMN_TRANSACTION_DATE TEXT NOT NULL,
                $COLUMN_TRANSACTION_NOTE TEXT,
                $COLUMN_TRANSACTION_CREATED_AT TEXT NOT NULL,
                $COLUMN_TRANSACTION_UPDATED_AT TEXT NOT NULL,
                FOREIGN KEY ($COLUMN_TRANSACTION_CATEGORY_ID) REFERENCES $TABLE_CATEGORIES($COLUMN_CATEGORY_ID)
            )
        """.trimIndent()

        db.execSQL(createCategoriesTable)
        db.execSQL(createTransactionsTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // v1: destructive upgrade acceptable. From v2+, implement incremental migrations
        // with data preservation (ALTER TABLE, data copy, etc.)
        db.execSQL("DROP TABLE IF EXISTS $TABLE_TRANSACTIONS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CATEGORIES")
        onCreate(db)
    }

    override fun onOpen(db: SQLiteDatabase) {
        super.onOpen(db)
        db.execSQL("PRAGMA foreign_keys=ON")
    }
}
