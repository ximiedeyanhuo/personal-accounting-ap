# Personal Accounting App Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use compose:subagent (recommended) or compose:execute to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Build a personal accounting Android app with basic expense tracking, custom categories, and category expense statistics.

**Architecture:** MVVM architecture with SQLite direct operations, Jetpack Compose UI, and Material Design 2.

**Tech Stack:** Kotlin, Jetpack Compose, SQLite, Material Design 2, StateFlow, ViewModel, Navigation Compose.

## Global Constraints
- Use SQLite direct operations (no Room)
- Material Design 2 only
- MVVM architecture
- Kotlin + Jetpack Compose
- Local storage only (no cloud sync)
- Custom categories (no predefined categories)

---

## Task 1: Project Setup

**Covers:** [S1, S6]

**Files:**
- Create: `app/build.gradle.kts`
- Create: `build.gradle.kts`
- Create: `settings.gradle.kts`
- Create: `app/src/main/AndroidManifest.xml`
- Create: `app/src/main/java/com/example/personalaccounting/MainActivity.kt`

**Interfaces:**
- Consumes: None
- Produces: Basic Android project structure with Compose dependencies

- [ ] **Step 1: Create Android project structure**

Create basic Android project with Kotlin and Jetpack Compose support.

- [ ] **Step 2: Configure build.gradle.kts**

```kotlin
// app/build.gradle.kts
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.personalaccounting"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.personalaccounting"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation(platform("androidx.compose:compose-bom:2024.02.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material:material") // Material Design 2
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.navigation:navigation-compose:2.7.7")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")
    implementation("androidx.compose.runtime:runtime-livedata")
    
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.02.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}
```

- [ ] **Step 3: Create MainActivity with Compose**

```kotlin
// app/src/main/java/com/example/personalaccounting/MainActivity.kt
package com.example.personalaccounting

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.personalaccounting.ui.theme.PersonalAccountingTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PersonalAccountingTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PersonalAccountingTheme {
        Greeting("Android")
    }
}
```

- [ ] **Step 4: Create theme files**

```kotlin
// app/src/main/java/com/example/personalaccounting/ui/theme/Color.kt
package com.example.personalaccounting.ui.theme

import androidx.compose.ui.graphics.Color

val Purple200 = Color(0xFFBB86FC)
val Purple500 = Color(0xFF6200EE)
val Purple700 = Color(0xFF3700B3)
val Teal200 = Color(0xFF03DAC5)
val Teal700 = Color(0xFF018786)
```

```kotlin
// app/src/main/java/com/example/personalaccounting/ui/theme/Theme.kt
package com.example.personalaccounting.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    primary = Purple200,
    primaryVariant = Purple700,
    secondary = Teal200
)

private val LightColorPalette = lightColors(
    primary = Purple500,
    primaryVariant = Purple700,
    secondary = Teal200
)

@Composable
fun PersonalAccountingTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        content = content
    )
}
```

- [ ] **Step 5: Commit**

```bash
git add app/build.gradle.kts build.gradle.kts settings.gradle.kts app/src/main/AndroidManifest.xml app/src/main/java/com/example/personalaccounting/
git commit -m "feat: set up basic Android project with Compose"
```

---

## Task 2: Database Layer - DatabaseHelper

**Covers:** [S3, S6]

**Files:**
- Create: `app/src/main/java/com/example/personalaccounting/data/db/DatabaseHelper.kt`
- Create: `app/src/main/java/com/example/personalaccounting/domain/model/Transaction.kt`
- Create: `app/src/main/java/com/example/personalaccounting/domain/model/Category.kt`

**Interfaces:**
- Consumes: None
- Produces: DatabaseHelper class for SQLite operations, Transaction and Category models

- [ ] **Step 1: Write the failing test**

```kotlin
// app/src/test/java/com/example/personalaccounting/data/db/DatabaseHelperTest.kt
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
```

- [ ] **Step 2: Run test to verify it fails**

Run: `./gradlew test`
Expected: FAIL with "DatabaseHelper not found"

- [ ] **Step 3: Create Transaction model**

```kotlin
// app/src/main/java/com/example/personalaccounting/domain/model/Transaction.kt
package com.example.personalaccounting.domain.model

import java.time.LocalDate
import java.time.LocalDateTime

data class Transaction(
    val id: Long = 0,
    val amount: Double,
    val type: TransactionType,
    val categoryId: Long,
    val date: LocalDate,
    val note: String = "",
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)

enum class TransactionType {
    INCOME,
    EXPENSE
}
```

- [ ] **Step 4: Create Category model**

```kotlin
// app/src/main/java/com/example/personalaccounting/domain/model/Category.kt
package com.example.personalaccounting.domain.model

import java.time.LocalDateTime

data class Category(
    val id: Long = 0,
    val name: String,
    val icon: String = "",
    val color: String = "",
    val type: TransactionType,
    val isDefault: Boolean = false,
    val createdAt: LocalDateTime = LocalDateTime.now()
)
```

- [ ] **Step 5: Create DatabaseHelper**

```kotlin
// app/src/main/java/com/example/personalaccounting/data/db/DatabaseHelper.kt
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
        
        // Transactions table
        const val TABLE_TRANSACTIONS = "transactions"
        const val COLUMN_TRANSACTION_ID = "id"
        const val COLUMN_TRANSACTION_AMOUNT = "amount"
        const val COLUMN_TRANSACTION_TYPE = "type"
        const val COLUMN_TRANSACTION_CATEGORY_ID = "category_id"
        const val COLUMN_TRANSACTION_DATE = "date"
        const val COLUMN_TRANSACTION_NOTE = "note"
        const val COLUMN_TRANSACTION_CREATED_AT = "created_at"
        const val COLUMN_TRANSACTION_UPDATED_AT = "updated_at"
        
        // Categories table
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
        // Create categories table first (referenced by transactions)
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
        
        // Create transactions table
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
        // Drop tables if they exist and recreate
        db.execSQL("DROP TABLE IF EXISTS $TABLE_TRANSACTIONS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CATEGORIES")
        onCreate(db)
    }
    
    override fun onOpen(db: SQLiteDatabase) {
        super.onOpen(db)
        db.execSQL("PRAGMA foreign_keys=ON")
    }
}
```

- [ ] **Step 6: Run test to verify it passes**

Run: `./gradlew test`
Expected: PASS

- [ ] **Step 7: Commit**

```bash
git add app/src/main/java/com/example/personalaccounting/domain/model/ app/src/main/java/com/example/personalaccounting/data/db/ app/src/test/java/com/example/personalaccounting/data/db/
git commit -m "feat: add DatabaseHelper and domain models"
```

---

## Task 3: Database Layer - DAOs

**Covers:** [S3, S6]

**Files:**
- Create: `app/src/main/java/com/example/personalaccounting/data/db/TransactionDao.kt`
- Create: `app/src/main/java/com/example/personalaccounting/data/db/CategoryDao.kt`

**Interfaces:**
- Consumes: DatabaseHelper, Transaction, Category models
- Produces: DAO classes for database operations

- [ ] **Step 1: Write the failing test**

```kotlin
// app/src/test/java/com/example/personalaccounting/data/db/TransactionDaoTest.kt
package com.example.personalaccounting.data.db

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.personalaccounting.domain.model.Category
import com.example.personalaccounting.domain.model.Transaction
import com.example.personalaccounting.domain.model.TransactionType
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@RunWith(AndroidJUnit4::class)
class TransactionDaoTest {
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var transactionDao: TransactionDao
    private lateinit var categoryDao: CategoryDao
    private var testCategoryId: Long = 0
    
    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        dbHelper = DatabaseHelper(context)
        transactionDao = TransactionDao(dbHelper)
        categoryDao = CategoryDao(dbHelper)
        
        // Create a test category
        val category = Category(
            name = "Food",
            type = TransactionType.EXPENSE,
            isDefault = true
        )
        testCategoryId = categoryDao.insert(category)
    }
    
    @After
    fun tearDown() {
        dbHelper.close()
    }
    
    @Test
    fun testInsertTransaction() {
        val transaction = Transaction(
            amount = 100.0,
            type = TransactionType.EXPENSE,
            categoryId = testCategoryId,
            date = LocalDate.now(),
            note = "Lunch"
        )
        val id = transactionDao.insert(transaction)
        assertTrue(id > 0)
    }
    
    @Test
    fun testGetTransactionById() {
        val transaction = Transaction(
            amount = 50.0,
            type = TransactionType.INCOME,
            categoryId = testCategoryId,
            date = LocalDate.now(),
            note = "Salary"
        )
        val id = transactionDao.insert(transaction)
        val retrieved = transactionDao.getById(id)
        
        assertEquals(50.0, retrieved?.amount)
        assertEquals(TransactionType.INCOME, retrieved?.type)
    }
}
```

- [ ] **Step 2: Run test to verify it fails**

Run: `./gradlew test`
Expected: FAIL with "TransactionDao not found"

- [ ] **Step 3: Create TransactionDao**

```kotlin
// app/src/main/java/com/example/personalaccounting/data/db/TransactionDao.kt
package com.example.personalaccounting.data.db

import com.example.personalaccounting.domain.model.Transaction
import com.example.personalaccounting.domain.model.TransactionType
import java.time.LocalDate
import java.time.LocalDateTime

class TransactionDao(private val dbHelper: DatabaseHelper) {
    
    fun insert(transaction: Transaction): Long {
        val db = dbHelper.writableDatabase
        val values = android.content.ContentValues().apply {
            put(DatabaseHelper.COLUMN_TRANSACTION_AMOUNT, transaction.amount)
            put(DatabaseHelper.COLUMN_TRANSACTION_TYPE, transaction.type.name)
            put(DatabaseHelper.COLUMN_TRANSACTION_CATEGORY_ID, transaction.categoryId)
            put(DatabaseHelper.COLUMN_TRANSACTION_DATE, transaction.date.toString())
            put(DatabaseHelper.COLUMN_TRANSACTION_NOTE, transaction.note)
            put(DatabaseHelper.COLUMN_TRANSACTION_CREATED_AT, transaction.createdAt.toString())
            put(DatabaseHelper.COLUMN_TRANSACTION_UPDATED_AT, transaction.updatedAt.toString())
        }
        return db.insert(DatabaseHelper.TABLE_TRANSACTIONS, null, values)
    }
    
    fun getById(id: Long): Transaction? {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            DatabaseHelper.TABLE_TRANSACTIONS,
            null,
            "${DatabaseHelper.COLUMN_TRANSACTION_ID} = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )
        
        return if (cursor.moveToFirst()) {
            cursorToTransaction(cursor)
        } else {
            null
        }.also {
            cursor.close()
        }
    }
    
    fun getAll(): List<Transaction> {
        val transactions = mutableListOf<Transaction>()
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            DatabaseHelper.TABLE_TRANSACTIONS,
            null,
            null,
            null,
            null,
            null,
            "${DatabaseHelper.COLUMN_TRANSACTION_DATE} DESC"
        )
        
        while (cursor.moveToNext()) {
            transactions.add(cursorToTransaction(cursor))
        }
        cursor.close()
        return transactions
    }
    
    fun getByDateRange(startDate: LocalDate, endDate: LocalDate): List<Transaction> {
        val transactions = mutableListOf<Transaction>()
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            DatabaseHelper.TABLE_TRANSACTIONS,
            null,
            "${DatabaseHelper.COLUMN_TRANSACTION_DATE} BETWEEN ? AND ?",
            arrayOf(startDate.toString(), endDate.toString()),
            null,
            null,
            "${DatabaseHelper.COLUMN_TRANSACTION_DATE} DESC"
        )
        
        while (cursor.moveToNext()) {
            transactions.add(cursorToTransaction(cursor))
        }
        cursor.close()
        return transactions
    }
    
    fun getByType(type: TransactionType): List<Transaction> {
        val transactions = mutableListOf<Transaction>()
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            DatabaseHelper.TABLE_TRANSACTIONS,
            null,
            "${DatabaseHelper.COLUMN_TRANSACTION_TYPE} = ?",
            arrayOf(type.name),
            null,
            null,
            "${DatabaseHelper.COLUMN_TRANSACTION_DATE} DESC"
        )
        
        while (cursor.moveToNext()) {
            transactions.add(cursorToTransaction(cursor))
        }
        cursor.close()
        return transactions
    }
    
    fun update(transaction: Transaction): Int {
        val db = dbHelper.writableDatabase
        val values = android.content.ContentValues().apply {
            put(DatabaseHelper.COLUMN_TRANSACTION_AMOUNT, transaction.amount)
            put(DatabaseHelper.COLUMN_TRANSACTION_TYPE, transaction.type.name)
            put(DatabaseHelper.COLUMN_TRANSACTION_CATEGORY_ID, transaction.categoryId)
            put(DatabaseHelper.COLUMN_TRANSACTION_DATE, transaction.date.toString())
            put(DatabaseHelper.COLUMN_TRANSACTION_NOTE, transaction.note)
            put(DatabaseHelper.COLUMN_TRANSACTION_UPDATED_AT, LocalDateTime.now().toString())
        }
        return db.update(
            DatabaseHelper.TABLE_TRANSACTIONS,
            values,
            "${DatabaseHelper.COLUMN_TRANSACTION_ID} = ?",
            arrayOf(transaction.id.toString())
        )
    }
    
    fun delete(id: Long): Int {
        val db = dbHelper.writableDatabase
        return db.delete(
            DatabaseHelper.TABLE_TRANSACTIONS,
            "${DatabaseHelper.COLUMN_TRANSACTION_ID} = ?",
            arrayOf(id.toString())
        )
    }
    
    private fun cursorToTransaction(cursor: android.database.Cursor): Transaction {
        return Transaction(
            id = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TRANSACTION_ID)),
            amount = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TRANSACTION_AMOUNT)),
            type = TransactionType.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TRANSACTION_TYPE))),
            categoryId = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TRANSACTION_CATEGORY_ID)),
            date = LocalDate.parse(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TRANSACTION_DATE))),
            note = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TRANSACTION_NOTE)) ?: "",
            createdAt = LocalDateTime.parse(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TRANSACTION_CREATED_AT))),
            updatedAt = LocalDateTime.parse(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TRANSACTION_UPDATED_AT)))
        )
    }
}
```

- [ ] **Step 4: Create CategoryDao**

```kotlin
// app/src/main/java/com/example/personalaccounting/data/db/CategoryDao.kt
package com.example.personalaccounting.data.db

import com.example.personalaccounting.domain.model.Category
import com.example.personalaccounting.domain.model.TransactionType

class CategoryDao(private val dbHelper: DatabaseHelper) {
    
    fun insert(category: Category): Long {
        val db = dbHelper.writableDatabase
        val values = android.content.ContentValues().apply {
            put(DatabaseHelper.COLUMN_CATEGORY_NAME, category.name)
            put(DatabaseHelper.COLUMN_CATEGORY_ICON, category.icon)
            put(DatabaseHelper.COLUMN_CATEGORY_COLOR, category.color)
            put(DatabaseHelper.COLUMN_CATEGORY_TYPE, category.type.name)
            put(DatabaseHelper.COLUMN_CATEGORY_IS_DEFAULT, if (category.isDefault) 1 else 0)
            put(DatabaseHelper.COLUMN_CATEGORY_CREATED_AT, category.createdAt.toString())
        }
        return db.insert(DatabaseHelper.TABLE_CATEGORIES, null, values)
    }
    
    fun getById(id: Long): Category? {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            DatabaseHelper.TABLE_CATEGORIES,
            null,
            "${DatabaseHelper.COLUMN_CATEGORY_ID} = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )
        
        return if (cursor.moveToFirst()) {
            cursorToCategory(cursor)
        } else {
            null
        }.also {
            cursor.close()
        }
    }
    
    fun getAll(): List<Category> {
        val categories = mutableListOf<Category>()
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            DatabaseHelper.TABLE_CATEGORIES,
            null,
            null,
            null,
            null,
            null,
            "${DatabaseHelper.COLUMN_CATEGORY_NAME} ASC"
        )
        
        while (cursor.moveToNext()) {
            categories.add(cursorToCategory(cursor))
        }
        cursor.close()
        return categories
    }
    
    fun getByType(type: TransactionType): List<Category> {
        val categories = mutableListOf<Category>()
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            DatabaseHelper.TABLE_CATEGORIES,
            null,
            "${DatabaseHelper.COLUMN_CATEGORY_TYPE} = ?",
            arrayOf(type.name),
            null,
            null,
            "${DatabaseHelper.COLUMN_CATEGORY_NAME} ASC"
        )
        
        while (cursor.moveToNext()) {
            categories.add(cursorToCategory(cursor))
        }
        cursor.close()
        return categories
    }
    
    fun update(category: Category): Int {
        val db = dbHelper.writableDatabase
        val values = android.content.ContentValues().apply {
            put(DatabaseHelper.COLUMN_CATEGORY_NAME, category.name)
            put(DatabaseHelper.COLUMN_CATEGORY_ICON, category.icon)
            put(DatabaseHelper.COLUMN_CATEGORY_COLOR, category.color)
            put(DatabaseHelper.COLUMN_CATEGORY_TYPE, category.type.name)
            put(DatabaseHelper.COLUMN_CATEGORY_IS_DEFAULT, if (category.isDefault) 1 else 0)
        }
        return db.update(
            DatabaseHelper.TABLE_CATEGORIES,
            values,
            "${DatabaseHelper.COLUMN_CATEGORY_ID} = ?",
            arrayOf(category.id.toString())
        )
    }
    
    fun delete(id: Long): Int {
        val db = dbHelper.writableDatabase
        return db.delete(
            DatabaseHelper.TABLE_CATEGORIES,
            "${DatabaseHelper.COLUMN_CATEGORY_ID} = ?",
            arrayOf(id.toString())
        )
    }
    
    private fun cursorToCategory(cursor: android.database.Cursor): Category {
        return Category(
            id = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CATEGORY_ID)),
            name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CATEGORY_NAME)),
            icon = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CATEGORY_ICON)) ?: "",
            color = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CATEGORY_COLOR)) ?: "",
            type = TransactionType.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CATEGORY_TYPE))),
            isDefault = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CATEGORY_IS_DEFAULT)) == 1,
            createdAt = java.time.LocalDateTime.parse(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CATEGORY_CREATED_AT)))
        )
    }
}
```

- [ ] **Step 5: Run test to verify it passes**

Run: `./gradlew test`
Expected: PASS

- [ ] **Step 6: Commit**

```bash
git add app/src/main/java/com/example/personalaccounting/data/db/ app/src/test/java/com/example/personalaccounting/data/db/
git commit -m "feat: add TransactionDao and CategoryDao"
```

---

## Task 4: Repository Layer

**Covers:** [S2, S6]

**Files:**
- Create: `app/src/main/java/com/example/personalaccounting/data/repository/TransactionRepository.kt`
- Create: `app/src/main/java/com/example/personalaccounting/data/repository/CategoryRepository.kt`

**Interfaces:**
- Consumes: TransactionDao, CategoryDao, Transaction, Category models
- Produces: Repository classes for business logic

- [ ] **Step 1: Write the failing test**

```kotlin
// app/src/test/java/com/example/personalaccounting/data/repository/TransactionRepositoryTest.kt
package com.example.personalaccounting.data.repository

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.personalaccounting.data.db.DatabaseHelper
import com.example.personalaccounting.data.db.TransactionDao
import com.example.personalaccounting.data.db.CategoryDao
import com.example.personalaccounting.domain.model.Category
import com.example.personalaccounting.domain.model.Transaction
import com.example.personalaccounting.domain.model.TransactionType
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@RunWith(AndroidJUnit4::class)
class TransactionRepositoryTest {
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var transactionRepository: TransactionRepository
    private lateinit var categoryRepository: CategoryRepository
    private var testCategoryId: Long = 0
    
    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        dbHelper = DatabaseHelper(context)
        val transactionDao = TransactionDao(dbHelper)
        val categoryDao = CategoryDao(dbHelper)
        
        transactionRepository = TransactionRepository(transactionDao)
        categoryRepository = CategoryRepository(categoryDao)
        
        // Create a test category
        val category = Category(
            name = "Food",
            type = TransactionType.EXPENSE,
            isDefault = true
        )
        testCategoryId = categoryRepository.insert(category)
    }
    
    @After
    fun tearDown() {
        dbHelper.close()
    }
    
    @Test
    fun testAddTransaction() {
        val transaction = Transaction(
            amount = 100.0,
            type = TransactionType.EXPENSE,
            categoryId = testCategoryId,
            date = LocalDate.now(),
            note = "Lunch"
        )
        val id = transactionRepository.add(transaction)
        assertTrue(id > 0)
    }
    
    @Test
    fun testGetMonthlyExpenses() {
        // Add multiple transactions
        val transaction1 = Transaction(
            amount = 50.0,
            type = TransactionType.EXPENSE,
            categoryId = testCategoryId,
            date = LocalDate.now(),
            note = "Breakfast"
        )
        val transaction2 = Transaction(
            amount = 30.0,
            type = TransactionType.EXPENSE,
            categoryId = testCategoryId,
            date = LocalDate.now(),
            note = "Snack"
        )
        
        transactionRepository.add(transaction1)
        transactionRepository.add(transaction2)
        
        val monthlyExpenses = transactionRepository.getMonthlyExpenses(LocalDate.now().year, LocalDate.now().monthValue)
        assertEquals(2, monthlyExpenses.size)
    }
}
```

- [ ] **Step 2: Run test to verify it fails**

Run: `./gradlew test`
Expected: FAIL with "TransactionRepository not found"

- [ ] **Step 3: Create TransactionRepository**

```kotlin
// app/src/main/java/com/example/personalaccounting/data/repository/TransactionRepository.kt
package com.example.personalaccounting.data.repository

import com.example.personalaccounting.data.db.TransactionDao
import com.example.personalaccounting.domain.model.Transaction
import com.example.personalaccounting.domain.model.TransactionType
import java.time.LocalDate

class TransactionRepository(private val transactionDao: TransactionDao) {
    
    fun add(transaction: Transaction): Long {
        return transactionDao.insert(transaction)
    }
    
    fun getById(id: Long): Transaction? {
        return transactionDao.getById(id)
    }
    
    fun getAll(): List<Transaction> {
        return transactionDao.getAll()
    }
    
    fun getByDateRange(startDate: LocalDate, endDate: LocalDate): List<Transaction> {
        return transactionDao.getByDateRange(startDate, endDate)
    }
    
    fun getByType(type: TransactionType): List<Transaction> {
        return transactionDao.getByType(type)
    }
    
    fun update(transaction: Transaction): Int {
        return transactionDao.update(transaction)
    }
    
    fun delete(id: Long): Int {
        return transactionDao.delete(id)
    }
    
    fun getMonthlyTransactions(year: Int, month: Int): List<Transaction> {
        val startDate = LocalDate.of(year, month, 1)
        val endDate = startDate.plusMonths(1).minusDays(1)
        return transactionDao.getByDateRange(startDate, endDate)
    }
    
    fun getMonthlyExpenses(year: Int, month: Int): List<Transaction> {
        return getMonthlyTransactions(year, month).filter { it.type == TransactionType.EXPENSE }
    }
    
    fun getMonthlyIncome(year: Int, month: Int): List<Transaction> {
        return getMonthlyTransactions(year, month).filter { it.type == TransactionType.INCOME }
    }
    
    fun getMonthlyTotalExpense(year: Int, month: Int): Double {
        return getMonthlyExpenses(year, month).sumOf { it.amount }
    }
    
    fun getMonthlyTotalIncome(year: Int, month: Int): Double {
        return getMonthlyIncome(year, month).sumOf { it.amount }
    }
    
    fun getMonthlyBalance(year: Int, month: Int): Double {
        return getMonthlyTotalIncome(year, month) - getMonthlyTotalExpense(year, month)
    }
}
```

- [ ] **Step 4: Create CategoryRepository**

```kotlin
// app/src/main/java/com/example/personalaccounting/data/repository/CategoryRepository.kt
package com.example.personalaccounting.data.repository

import com.example.personalaccounting.data.db.CategoryDao
import com.example.personalaccounting.domain.model.Category
import com.example.personalaccounting.domain.model.TransactionType

class CategoryRepository(private val categoryDao: CategoryDao) {
    
    fun insert(category: Category): Long {
        return categoryDao.insert(category)
    }
    
    fun getById(id: Long): Category? {
        return categoryDao.getById(id)
    }
    
    fun getAll(): List<Category> {
        return categoryDao.getAll()
    }
    
    fun getByType(type: TransactionType): List<Category> {
        return categoryDao.getByType(type)
    }
    
    fun update(category: Category): Int {
        return categoryDao.update(category)
    }
    
    fun delete(id: Long): Int {
        return categoryDao.delete(id)
    }
    
    fun createDefaultCategories() {
        val defaultCategories = listOf(
            Category(name = "Food", type = TransactionType.EXPENSE, isDefault = true),
            Category(name = "Transport", type = TransactionType.EXPENSE, isDefault = true),
            Category(name = "Shopping", type = TransactionType.EXPENSE, isDefault = true),
            Category(name = "Entertainment", type = TransactionType.EXPENSE, isDefault = true),
            Category(name = "Salary", type = TransactionType.INCOME, isDefault = true),
            Category(name = "Investment", type = TransactionType.INCOME, isDefault = true)
        )
        
        defaultCategories.forEach { category ->
            categoryDao.insert(category)
        }
    }
}
```

- [ ] **Step 5: Run test to verify it passes**

Run: `./gradlew test`
Expected: PASS

- [ ] **Step 6: Commit**

```bash
git add app/src/main/java/com/example/personalaccounting/data/repository/
git commit -m "feat: add TransactionRepository and CategoryRepository"
```

---

## Task 5: Statistics Service

**Covers:** [S4, S6]

**Files:**
- Create: `app/src/main/java/com/example/personalaccounting/domain/service/StatisticsService.kt`
- Create: `app/src/main/java/com/example/personalaccounting/domain/model/CategoryExpense.kt`

**Interfaces:**
- Consumes: TransactionRepository, CategoryRepository
- Produces: StatisticsService for expense statistics, CategoryExpense model

- [ ] **Step 1: Write the failing test**

```kotlin
// app/src/test/java/com/example/personalaccounting/domain/service/StatisticsServiceTest.kt
package com.example.personalaccounting.domain.service

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.personalaccounting.data.db.CategoryDao
import com.example.personalaccounting.data.db.DatabaseHelper
import com.example.personalaccounting.data.db.TransactionDao
import com.example.personalaccounting.data.repository.CategoryRepository
import com.example.personalaccounting.data.repository.TransactionRepository
import com.example.personalaccounting.domain.model.Category
import com.example.personalaccounting.domain.model.Transaction
import com.example.personalaccounting.domain.model.TransactionType
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate
import kotlin.test.assertEquals

@RunWith(AndroidJUnit4::class)
class StatisticsServiceTest {
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var statisticsService: StatisticsService
    private lateinit var transactionRepository: TransactionRepository
    private lateinit var categoryRepository: CategoryRepository
    private var foodCategoryId: Long = 0
    private var transportCategoryId: Long = 0
    
    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        dbHelper = DatabaseHelper(context)
        val transactionDao = TransactionDao(dbHelper)
        val categoryDao = CategoryDao(dbHelper)
        
        transactionRepository = TransactionRepository(transactionDao)
        categoryRepository = CategoryRepository(categoryDao)
        statisticsService = StatisticsService(transactionRepository, categoryRepository)
        
        // Create test categories
        val foodCategory = Category(
            name = "Food",
            type = TransactionType.EXPENSE,
            isDefault = true
        )
        val transportCategory = Category(
            name = "Transport",
            type = TransactionType.EXPENSE,
            isDefault = true
        )
        
        foodCategoryId = categoryRepository.insert(foodCategory)
        transportCategoryId = categoryRepository.insert(transportCategory)
        
        // Add test transactions
        val transaction1 = Transaction(
            amount = 100.0,
            type = TransactionType.EXPENSE,
            categoryId = foodCategoryId,
            date = LocalDate.now(),
            note = "Lunch"
        )
        val transaction2 = Transaction(
            amount = 50.0,
            type = TransactionType.EXPENSE,
            categoryId = foodCategoryId,
            date = LocalDate.now(),
            note = "Dinner"
        )
        val transaction3 = Transaction(
            amount = 30.0,
            type = TransactionType.EXPENSE,
            categoryId = transportCategoryId,
            date = LocalDate.now(),
            note = "Bus"
        )
        
        transactionRepository.add(transaction1)
        transactionRepository.add(transaction2)
        transactionRepository.add(transaction3)
    }
    
    @After
    fun tearDown() {
        dbHelper.close()
    }
    
    @Test
    fun testGetCategoryExpenses() {
        val categoryExpenses = statisticsService.getCategoryExpenses(
            LocalDate.now().year,
            LocalDate.now().monthValue
        )
        
        assertEquals(2, categoryExpenses.size)
        
        val foodExpense = categoryExpenses.find { it.categoryId == foodCategoryId }
        assertEquals(150.0, foodExpense?.totalAmount)
        assertEquals(2, foodExpense?.transactionCount)
        
        val transportExpense = categoryExpenses.find { it.categoryId == transportCategoryId }
        assertEquals(30.0, transportExpense?.totalAmount)
        assertEquals(1, transportExpense?.transactionCount)
    }
}
```

- [ ] **Step 2: Run test to verify it fails**

Run: `./gradlew test`
Expected: FAIL with "StatisticsService not found"

- [ ] **Step 3: Create CategoryExpense model**

```kotlin
// app/src/main/java/com/example/personalaccounting/domain/model/CategoryExpense.kt
package com.example.personalaccounting.domain.model

data class CategoryExpense(
    val categoryId: Long,
    val categoryName: String,
    val totalAmount: Double,
    val transactionCount: Int,
    val percentage: Double = 0.0
)
```

- [ ] **Step 4: Create StatisticsService**

```kotlin
// app/src/main/java/com/example/personalaccounting/domain/service/StatisticsService.kt
package com.example.personalaccounting.domain.service

import com.example.personalaccounting.data.repository.CategoryRepository
import com.example.personalaccounting.data.repository.TransactionRepository
import com.example.personalaccounting.domain.model.CategoryExpense
import com.example.personalaccounting.domain.model.TransactionType

class StatisticsService(
    private val transactionRepository: TransactionRepository,
    private val categoryRepository: CategoryRepository
) {
    
    fun getCategoryExpenses(year: Int, month: Int): List<CategoryExpense> {
        val expenses = transactionRepository.getMonthlyExpenses(year, month)
        val categories = categoryRepository.getAll()
        
        val categoryMap = categories.associateBy { it.id }
        
        val expensesByCategory = expenses.groupBy { it.categoryId }
        
        val totalExpenses = expenses.sumOf { it.amount }
        
        return expensesByCategory.map { (categoryId, transactions) ->
            val category = categoryMap[categoryId]
            val totalAmount = transactions.sumOf { it.amount }
            val percentage = if (totalExpenses > 0) (totalAmount / totalExpenses) * 100 else 0.0
            
            CategoryExpense(
                categoryId = categoryId,
                categoryName = category?.name ?: "Unknown",
                totalAmount = totalAmount,
                transactionCount = transactions.size,
                percentage = percentage
            )
        }.sortedByDescending { it.totalAmount }
    }
    
    fun getCategoryIncome(year: Int, month: Int): List<CategoryExpense> {
        val income = transactionRepository.getMonthlyIncome(year, month)
        val categories = categoryRepository.getAll()
        
        val categoryMap = categories.associateBy { it.id }
        
        val incomeByCategory = income.groupBy { it.categoryId }
        
        val totalIncome = income.sumOf { it.amount }
        
        return incomeByCategory.map { (categoryId, transactions) ->
            val category = categoryMap[categoryId]
            val totalAmount = transactions.sumOf { it.amount }
            val percentage = if (totalIncome > 0) (totalAmount / totalIncome) * 100 else 0.0
            
            CategoryExpense(
                categoryId = categoryId,
                categoryName = category?.name ?: "Unknown",
                totalAmount = totalAmount,
                transactionCount = transactions.size,
                percentage = percentage
            )
        }.sortedByDescending { it.totalAmount }
    }
    
    fun getMonthlySummary(year: Int, month: Int): MonthlySummary {
        val totalIncome = transactionRepository.getMonthlyTotalIncome(year, month)
        val totalExpense = transactionRepository.getMonthlyTotalExpense(year, month)
        val balance = totalIncome - totalExpense
        
        return MonthlySummary(
            year = year,
            month = month,
            totalIncome = totalIncome,
            totalExpense = totalExpense,
            balance = balance
        )
    }
}

data class MonthlySummary(
    val year: Int,
    val month: Int,
    val totalIncome: Double,
    val totalExpense: Double,
    val balance: Double
)
```

- [ ] **Step 5: Run test to verify it passes**

Run: `./gradlew test`
Expected: PASS

- [ ] **Step 6: Commit**

```bash
git add app/src/main/java/com/example/personalaccounting/domain/service/ app/src/main/java/com/example/personalaccounting/domain/model/CategoryExpense.kt app/src/test/java/com/example/personalaccounting/domain/service/
git commit -m "feat: add StatisticsService and CategoryExpense model"
```

---

## Task 6: Navigation Setup

**Covers:** [S4, S6]

**Files:**
- Create: `app/src/main/java/com/example/personalaccounting/ui/navigation/Screen.kt`
- Create: `app/src/main/java/com/example/personalaccounting/ui/navigation/NavGraph.kt`
- Modify: `app/src/main/java/com/example/personalaccounting/MainActivity.kt`

**Interfaces:**
- Consumes: None
- Produces: Navigation setup for app screens

- [ ] **Step 1: Write the failing test**

```kotlin
// app/src/test/java/com/example/personalaccounting/ui/navigation/NavGraphTest.kt
package com.example.personalaccounting.ui.navigation

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.compose.rememberNavController
import org.junit.Rule
import org.junit.Test

class NavGraphTest {
    @get:Rule
    val composeTestRule = createComposeRule()
    
    @Test
    fun testNavGraphSetup() {
        composeTestRule.setContent {
            val navController = rememberNavController()
            NavGraph(navController = navController)
        }
        // Just verify it doesn't crash
    }
}
```

- [ ] **Step 2: Run test to verify it fails**

Run: `./gradlew test`
Expected: FAIL with "NavGraph not found"

- [ ] **Step 3: Create Screen sealed class**

```kotlin
// app/src/main/java/com/example/personalaccounting/ui/navigation/Screen.kt
package com.example.personalaccounting.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object AddTransaction : Screen("add_transaction")
    object EditTransaction : Screen("edit_transaction/{transactionId}") {
        fun createRoute(transactionId: Long) = "edit_transaction/$transactionId"
    }
    object CategoryManagement : Screen("category_management")
    object Statistics : Screen("statistics")
    object Settings : Screen("settings")
}
```

- [ ] **Step 4: Create NavGraph**

```kotlin
// app/src/main/java/com/example/personalaccounting/ui/navigation/NavGraph.kt
package com.example.personalaccounting.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.personalaccounting.ui.screens.addedit.AddEditTransactionScreen
import com.example.personalaccounting.ui.screens.category.CategoryManagementScreen
import com.example.personalaccounting.ui.screens.home.HomeScreen
import com.example.personalaccounting.ui.screens.statistics.StatisticsScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onAddTransaction = {
                    navController.navigate(Screen.AddTransaction.route)
                },
                onEditTransaction = { transactionId ->
                    navController.navigate(Screen.EditTransaction.createRoute(transactionId))
                },
                onNavigateToStatistics = {
                    navController.navigate(Screen.Statistics.route)
                },
                onNavigateToCategories = {
                    navController.navigate(Screen.CategoryManagement.route)
                }
            )
        }
        
        composable(Screen.AddTransaction.route) {
            AddEditTransactionScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        
        composable(
            route = Screen.EditTransaction.route,
            arguments = listOf(
                navArgument("transactionId") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val transactionId = backStackEntry.arguments?.getLong("transactionId") ?: 0L
            AddEditTransactionScreen(
                transactionId = transactionId,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        
        composable(Screen.CategoryManagement.route) {
            CategoryManagementScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        
        composable(Screen.Statistics.route) {
            StatisticsScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
```

- [ ] **Step 5: Update MainActivity**

```kotlin
// app/src/main/java/com/example/personalaccounting/MainActivity.kt
package com.example.personalaccounting

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.personalaccounting.ui.navigation.NavGraph
import com.example.personalaccounting.ui.theme.PersonalAccountingTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PersonalAccountingTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val navController = rememberNavController()
                    NavGraph(navController = navController)
                }
            }
        }
    }
}
```

- [ ] **Step 6: Run test to verify it passes**

Run: `./gradlew test`
Expected: PASS

- [ ] **Step 7: Commit**

```bash
git add app/src/main/java/com/example/personalaccounting/ui/navigation/ app/src/main/java/com/example/personalaccounting/MainActivity.kt
git commit -m "feat: add navigation setup"
```

---

## Task 7: Home Screen

**Covers:** [S4, S6]

**Files:**
- Create: `app/src/main/java/com/example/personalaccounting/ui/screens/home/HomeScreen.kt`
- Create: `app/src/main/java/com/example/personalaccounting/ui/screens/home/HomeViewModel.kt`
- Create: `app/src/main/java/com/example/personalaccounting/ui/components/TransactionItem.kt`

**Interfaces:**
- Consumes: TransactionRepository, CategoryRepository, StatisticsService
- Produces: HomeScreen composable, HomeViewModel

- [ ] **Step 1: Write the failing test**

```kotlin
// app/src/test/java/com/example/personalaccounting/ui/screens/home/HomeViewModelTest.kt
package com.example.personalaccounting.ui.screens.home

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.personalaccounting.data.db.CategoryDao
import com.example.personalaccounting.data.db.DatabaseHelper
import com.example.personalaccounting.data.db.TransactionDao
import com.example.personalaccounting.data.repository.CategoryRepository
import com.example.personalaccounting.data.repository.TransactionRepository
import com.example.personalaccounting.domain.model.Category
import com.example.personalaccounting.domain.model.Transaction
import com.example.personalaccounting.domain.model.TransactionType
import com.example.personalaccounting.domain.service.StatisticsService
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate
import kotlin.test.assertEquals

@RunWith(AndroidJUnit4::class)
class HomeViewModelTest {
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var viewModel: HomeViewModel
    private lateinit var transactionRepository: TransactionRepository
    private lateinit var categoryRepository: CategoryRepository
    private lateinit var statisticsService: StatisticsService
    private var testCategoryId: Long = 0
    
    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        dbHelper = DatabaseHelper(context)
        val transactionDao = TransactionDao(dbHelper)
        val categoryDao = CategoryDao(dbHelper)
        
        transactionRepository = TransactionRepository(transactionDao)
        categoryRepository = CategoryRepository(categoryDao)
        statisticsService = StatisticsService(transactionRepository, categoryRepository)
        
        viewModel = HomeViewModel(
            transactionRepository,
            categoryRepository,
            statisticsService
        )
        
        // Create a test category
        val category = Category(
            name = "Food",
            type = TransactionType.EXPENSE,
            isDefault = true
        )
        testCategoryId = categoryRepository.insert(category)
    }
    
    @After
    fun tearDown() {
        dbHelper.close()
    }
    
    @Test
    fun testLoadTransactions() {
        val transaction = Transaction(
            amount = 100.0,
            type = TransactionType.EXPENSE,
            categoryId = testCategoryId,
            date = LocalDate.now(),
            note = "Lunch"
        )
        transactionRepository.add(transaction)
        
        viewModel.loadTransactions()
        
        assertEquals(1, viewModel.transactions.value.size)
    }
    
    @Test
    fun testLoadMonthlySummary() {
        val transaction = Transaction(
            amount = 100.0,
            type = TransactionType.EXPENSE,
            categoryId = testCategoryId,
            date = LocalDate.now(),
            note = "Lunch"
        )
        transactionRepository.add(transaction)
        
        viewModel.loadMonthlySummary()
        
        assertEquals(100.0, viewModel.monthlySummary.value?.totalExpense)
    }
}
```

- [ ] **Step 2: Run test to verify it fails**

Run: `./gradlew test`
Expected: FAIL with "HomeViewModel not found"

- [ ] **Step 3: Create HomeViewModel**

```kotlin
// app/src/main/java/com/example/personalaccounting/ui/screens/home/HomeViewModel.kt
package com.example.personalaccounting.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.personalaccounting.data.repository.CategoryRepository
import com.example.personalaccounting.data.repository.TransactionRepository
import com.example.personalaccounting.domain.model.Category
import com.example.personalaccounting.domain.model.Transaction
import com.example.personalaccounting.domain.service.MonthlySummary
import com.example.personalaccounting.domain.service.StatisticsService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

class HomeViewModel(
    private val transactionRepository: TransactionRepository,
    private val categoryRepository: CategoryRepository,
    private val statisticsService: StatisticsService
) : ViewModel() {
    
    private val _transactions = MutableStateFlow<List<Transaction>>(emptyList())
    val transactions: StateFlow<List<Transaction>> = _transactions.asStateFlow()
    
    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories.asStateFlow()
    
    private val _monthlySummary = MutableStateFlow<MonthlySummary?>(null)
    val monthlySummary: StateFlow<MonthlySummary?> = _monthlySummary.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    init {
        loadTransactions()
        loadCategories()
        loadMonthlySummary()
    }
    
    fun loadTransactions() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _transactions.value = transactionRepository.getAll()
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun loadCategories() {
        viewModelScope.launch {
            _categories.value = categoryRepository.getAll()
        }
    }
    
    fun loadMonthlySummary() {
        viewModelScope.launch {
            val now = LocalDate.now()
            _monthlySummary.value = statisticsService.getMonthlySummary(now.year, now.monthValue)
        }
    }
    
    fun deleteTransaction(transactionId: Long) {
        viewModelScope.launch {
            transactionRepository.delete(transactionId)
            loadTransactions()
            loadMonthlySummary()
        }
    }
    
    fun getCategoryById(categoryId: Long): Category? {
        return _categories.value.find { it.id == categoryId }
    }
}
```

- [ ] **Step 4: Create TransactionItem composable**

```kotlin
// app/src/main/java/com/example/personalaccounting/ui/components/TransactionItem.kt
package com.example.personalaccounting.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.personalaccounting.domain.model.Category
import com.example.personalaccounting.domain.model.Transaction
import com.example.personalaccounting.domain.model.TransactionType
import java.time.format.DateTimeFormatter

@Composable
fun TransactionItem(
    transaction: Transaction,
    category: Category?,
    onTransactionClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val amountColor = when (transaction.type) {
        TransactionType.INCOME -> Color(0xFF4CAF50)
        TransactionType.EXPENSE -> Color(0xFFF44336)
    }
    
    val amountPrefix = when (transaction.type) {
        TransactionType.INCOME -> "+"
        TransactionType.EXPENSE -> "-"
    }
    
    val formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")
    
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = category?.name ?: "Unknown",
                style = MaterialTheme.typography.subtitle1,
                fontWeight = FontWeight.Medium
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = transaction.date.format(formatter),
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
            )
            
            if (transaction.note.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = transaction.note,
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                )
            }
        }
        
        Text(
            text = "$amountPrefix$%.2f".format(transaction.amount),
            style = MaterialTheme.typography.subtitle1,
            fontWeight = FontWeight.Bold,
            color = amountColor
        )
    }
}
```

- [ ] **Step 5: Create HomeScreen**

```kotlin
// app/src/main/java/com/example/personalaccounting/ui/screens/home/HomeScreen.kt
package com.example.personalaccounting.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.personalaccounting.ui.components.TransactionItem

@Composable
fun HomeScreen(
    onAddTransaction: () -> Unit,
    onEditTransaction: (Long) -> Unit,
    onNavigateToStatistics: () -> Unit,
    onNavigateToCategories: () -> Unit,
    viewModel: HomeViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val transactions by viewModel.transactions.collectAsState()
    val monthlySummary by viewModel.monthlySummary.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Personal Accounting") },
                actions = {
                    IconButton(onClick = onNavigateToStatistics) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Statistics"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddTransaction) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Transaction"
                )
            }
        }
    ) { padding ->
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                // Monthly Summary Card
                monthlySummary?.let { summary ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        elevation = 4.dp
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "Monthly Summary",
                                style = MaterialTheme.typography.h6,
                                fontWeight = FontWeight.Bold
                            )
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(
                                        text = "Income",
                                        style = MaterialTheme.typography.body2,
                                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                                    )
                                    Text(
                                        text = "$%.2f".format(summary.totalIncome),
                                        style = MaterialTheme.typography.h6,
                                        color = androidx.compose.ui.graphics.Color(0xFF4CAF50)
                                    )
                                }
                                
                                Column {
                                    Text(
                                        text = "Expense",
                                        style = MaterialTheme.typography.body2,
                                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                                    )
                                    Text(
                                        text = "$%.2f".format(summary.totalExpense),
                                        style = MaterialTheme.typography.h6,
                                        color = androidx.compose.ui.graphics.Color(0xFFF44336)
                                    )
                                }
                                
                                Column {
                                    Text(
                                        text = "Balance",
                                        style = MaterialTheme.typography.body2,
                                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                                    )
                                    Text(
                                        text = "$%.2f".format(summary.balance),
                                        style = MaterialTheme.typography.h6,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
                
                // Transactions List
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(transactions) { transaction ->
                        val category = viewModel.getCategoryById(transaction.categoryId)
                        TransactionItem(
                            transaction = transaction,
                            category = category,
                            onTransactionClick = onEditTransaction
                        )
                        Divider()
                    }
                }
            }
        }
    }
}
```

- [ ] **Step 6: Run test to verify it passes**

Run: `./gradlew test`
Expected: PASS

- [ ] **Step 7: Commit**

```bash
git add app/src/main/java/com/example/personalaccounting/ui/screens/home/ app/src/main/java/com/example/personalaccounting/ui/components/TransactionItem.kt
git commit -m "feat: add HomeScreen and HomeViewModel"
```

---

## Task 8: Add/Edit Transaction Screen

**Covers:** [S4, S6]

**Files:**
- Create: `app/src/main/java/com/example/personalaccounting/ui/screens/addedit/AddEditTransactionScreen.kt`
- Create: `app/src/main/java/com/example/personalaccounting/ui/screens/addedit/AddEditTransactionViewModel.kt`

**Interfaces:**
- Consumes: TransactionRepository, CategoryRepository
- Produces: AddEditTransactionScreen composable, AddEditTransactionViewModel

- [ ] **Step 1: Write the failing test**

```kotlin
// app/src/test/java/com/example/personalaccounting/ui/screens/addedit/AddEditTransactionViewModelTest.kt
package com.example.personalaccounting.ui.screens.addedit

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.personalaccounting.data.db.CategoryDao
import com.example.personalaccounting.data.db.DatabaseHelper
import com.example.personalaccounting.data.db.TransactionDao
import com.example.personalaccounting.data.repository.CategoryRepository
import com.example.personalaccounting.data.repository.TransactionRepository
import com.example.personalaccounting.domain.model.Category
import com.example.personalaccounting.domain.model.TransactionType
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@RunWith(AndroidJUnit4::class)
class AddEditTransactionViewModelTest {
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var viewModel: AddEditTransactionViewModel
    private lateinit var transactionRepository: TransactionRepository
    private lateinit var categoryRepository: CategoryRepository
    private var testCategoryId: Long = 0
    
    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        dbHelper = DatabaseHelper(context)
        val transactionDao = TransactionDao(dbHelper)
        val categoryDao = CategoryDao(dbHelper)
        
        transactionRepository = TransactionRepository(transactionDao)
        categoryRepository = CategoryRepository(categoryDao)
        
        viewModel = AddEditTransactionViewModel(
            transactionRepository,
            categoryRepository
        )
        
        // Create a test category
        val category = Category(
            name = "Food",
            type = TransactionType.EXPENSE,
            isDefault = true
        )
        testCategoryId = categoryRepository.insert(category)
    }
    
    @After
    fun tearDown() {
        dbHelper.close()
    }
    
    @Test
    fun testSaveTransaction() {
        viewModel.updateAmount("100.0")
        viewModel.updateType(TransactionType.EXPENSE)
        viewModel.updateCategoryId(testCategoryId)
        viewModel.updateDate(LocalDate.now())
        viewModel.updateNote("Lunch")
        
        viewModel.saveTransaction()
        
        assertTrue(viewModel.isSuccess.value)
    }
    
    @Test
    fun testValidation() {
        // Empty amount should fail
        viewModel.updateAmount("")
        viewModel.updateType(TransactionType.EXPENSE)
        viewModel.updateCategoryId(testCategoryId)
        viewModel.updateDate(LocalDate.now())
        
        viewModel.saveTransaction()
        
        assertEquals("Amount is required", viewModel.errorMessage.value)
    }
}
```

- [ ] **Step 2: Run test to verify it fails**

Run: `./gradlew test`
Expected: FAIL with "AddEditTransactionViewModel not found"

- [ ] **Step 3: Create AddEditTransactionViewModel**

```kotlin
// app/src/main/java/com/example/personalaccounting/ui/screens/addedit/AddEditTransactionViewModel.kt
package com.example.personalaccounting.ui.screens.addedit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.personalaccounting.data.repository.CategoryRepository
import com.example.personalaccounting.data.repository.TransactionRepository
import com.example.personalaccounting.domain.model.Category
import com.example.personalaccounting.domain.model.Transaction
import com.example.personalaccounting.domain.model.TransactionType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime

class AddEditTransactionViewModel(
    private val transactionRepository: TransactionRepository,
    private val categoryRepository: CategoryRepository
) : ViewModel() {
    
    private val _amount = MutableStateFlow("")
    val amount: StateFlow<String> = _amount.asStateFlow()
    
    private val _type = MutableStateFlow(TransactionType.EXPENSE)
    val type: StateFlow<TransactionType> = _type.asStateFlow()
    
    private val _categoryId = MutableStateFlow<Long?>(null)
    val categoryId: StateFlow<Long?> = _categoryId.asStateFlow()
    
    private val _date = MutableStateFlow(LocalDate.now())
    val date: StateFlow<LocalDate> = _date.asStateFlow()
    
    private val _note = MutableStateFlow("")
    val note: StateFlow<String> = _note.asStateFlow()
    
    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()
    
    private val _isSuccess = MutableStateFlow(false)
    val isSuccess: StateFlow<Boolean> = _isSuccess.asStateFlow()
    
    private var editingTransactionId: Long? = null
    
    init {
        loadCategories()
    }
    
    fun loadTransaction(transactionId: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val transaction = transactionRepository.getById(transactionId)
                transaction?.let {
                    editingTransactionId = it.id
                    _amount.value = it.amount.toString()
                    _type.value = it.type
                    _categoryId.value = it.categoryId
                    _date.value = it.date
                    _note.value = it.note
                }
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun loadCategories() {
        viewModelScope.launch {
            _categories.value = categoryRepository.getAll()
        }
    }
    
    fun updateAmount(amount: String) {
        _amount.value = amount
        _errorMessage.value = null
    }
    
    fun updateType(type: TransactionType) {
        _type.value = type
        loadCategories()
    }
    
    fun updateCategoryId(categoryId: Long?) {
        _categoryId.value = categoryId
    }
    
    fun updateDate(date: LocalDate) {
        _date.value = date
    }
    
    fun updateNote(note: String) {
        _note.value = note
    }
    
    fun saveTransaction() {
        val amountValue = _amount.value.toDoubleOrNull()
        if (amountValue == null || amountValue <= 0) {
            _errorMessage.value = "Amount is required and must be positive"
            return
        }
        
        if (_categoryId.value == null) {
            _errorMessage.value = "Category is required"
            return
        }
        
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val transaction = Transaction(
                    id = editingTransactionId ?: 0,
                    amount = amountValue,
                    type = _type.value,
                    categoryId = _categoryId.value!!,
                    date = _date.value,
                    note = _note.value,
                    createdAt = if (editingTransactionId != null) LocalDateTime.now() else LocalDateTime.now(),
                    updatedAt = LocalDateTime.now()
                )
                
                if (editingTransactionId != null) {
                    transactionRepository.update(transaction)
                } else {
                    transactionRepository.add(transaction)
                }
                
                _isSuccess.value = true
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun resetSuccess() {
        _isSuccess.value = false
    }
}
```

- [ ] **Step 4: Create AddEditTransactionScreen**

```kotlin
// app/src/main/java/com/example/personalaccounting/ui/screens/addedit/AddEditTransactionScreen.kt
package com.example.personalaccounting.ui.screens.addedit

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.personalaccounting.domain.model.TransactionType
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun AddEditTransactionScreen(
    transactionId: Long? = null,
    onNavigateBack: () -> Unit,
    viewModel: AddEditTransactionViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val amount by viewModel.amount.collectAsState()
    val type by viewModel.type.collectAsState()
    val categoryId by viewModel.categoryId.collectAsState()
    val date by viewModel.date.collectAsState()
    val note by viewModel.note.collectAsState()
    val categories by viewModel.categories.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val isSuccess by viewModel.isSuccess.collectAsState()
    
    var showDatePicker by remember { mutableStateOf(false) }
    
    LaunchedEffect(transactionId) {
        transactionId?.let {
            viewModel.loadTransaction(it)
        }
    }
    
    LaunchedEffect(isSuccess) {
        if (isSuccess) {
            onNavigateBack()
            viewModel.resetSuccess()
        }
    }
    
    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = date.toEpochDay() * 86400000L
        )
        
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val selectedDate = LocalDate.ofEpochDay(millis / 86400000L)
                            viewModel.updateDate(selectedDate)
                        }
                        showDatePicker = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (transactionId != null) "Edit Transaction" else "Add Transaction") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = androidx.compose.material.icons.Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { padding ->
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                // Amount Input
                OutlinedTextField(
                    value = amount,
                    onValueChange = { viewModel.updateAmount(it) },
                    label = { Text("Amount") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth(),
                    isError = errorMessage != null
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Type Selection
                Text(
                    text = "Type",
                    style = MaterialTheme.typography.subtitle1
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = type == TransactionType.EXPENSE,
                        onClick = { viewModel.updateType(TransactionType.EXPENSE) },
                        text = { Text("Expense") },
                        modifier = Modifier.weight(1f)
                    )
                    
                    FilterChip(
                        selected = type == TransactionType.INCOME,
                        onClick = { viewModel.updateType(TransactionType.INCOME) },
                        text = { Text("Income") },
                        modifier = Modifier.weight(1f)
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Category Selection
                Text(
                    text = "Category",
                    style = MaterialTheme.typography.subtitle1
                )
                
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    categories.forEach { category ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = categoryId == category.id,
                                onClick = { viewModel.updateCategoryId(category.id) }
                            )
                            
                            Spacer(modifier = Modifier.width(8.dp))
                            
                            Text(
                                text = category.name,
                                style = MaterialTheme.typography.body1
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Date Selection
                OutlinedTextField(
                    value = date.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")),
                    onValueChange = {},
                    label = { Text("Date") },
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        IconButton(onClick = { showDatePicker = true }) {
                            Icon(
                                imageVector = androidx.compose.material.icons.Icons.Default.DateRange,
                                contentDescription = "Select Date"
                            )
                        }
                    }
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Note Input
                OutlinedTextField(
                    value = note,
                    onValueChange = { viewModel.updateNote(it) },
                    label = { Text("Note (optional)") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Error Message
                errorMessage?.let { error ->
                    Text(
                        text = error,
                        color = MaterialTheme.colors.error,
                        style = MaterialTheme.typography.body2,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
                
                // Save Button
                Button(
                    onClick = { viewModel.saveTransaction() },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading
                ) {
                    Text("Save")
                }
            }
        }
    }
}
```

- [ ] **Step 5: Run test to verify it passes**

Run: `./gradlew test`
Expected: PASS

- [ ] **Step 6: Commit**

```bash
git add app/src/main/java/com/example/personalaccounting/ui/screens/addedit/
git commit -m "feat: add AddEditTransactionScreen and AddEditTransactionViewModel"
```

---

## Task 9: Category Management Screen

**Covers:** [S4, S6]

**Files:**
- Create: `app/src/main/java/com/example/personalaccounting/ui/screens/category/CategoryManagementScreen.kt`
- Create: `app/src/main/java/com/example/personalaccounting/ui/screens/category/CategoryManagementViewModel.kt`

**Interfaces:**
- Consumes: CategoryRepository
- Produces: CategoryManagementScreen composable, CategoryManagementViewModel

- [ ] **Step 1: Write the failing test**

```kotlin
// app/src/test/java/com/example/personalaccounting/ui/screens/category/CategoryManagementViewModelTest.kt
package com.example.personalaccounting.ui.screens.category

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.personalaccounting.data.db.CategoryDao
import com.example.personalaccounting.data.db.DatabaseHelper
import com.example.personalaccounting.data.repository.CategoryRepository
import com.example.personalaccounting.domain.model.Category
import com.example.personalaccounting.domain.model.TransactionType
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.test.assertEquals

@RunWith(AndroidJUnit4::class)
class CategoryManagementViewModelTest {
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var viewModel: CategoryManagementViewModel
    private lateinit var categoryRepository: CategoryRepository
    
    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        dbHelper = DatabaseHelper(context)
        val categoryDao = CategoryDao(dbHelper)
        
        categoryRepository = CategoryRepository(categoryDao)
        viewModel = CategoryManagementViewModel(categoryRepository)
    }
    
    @After
    fun tearDown() {
        dbHelper.close()
    }
    
    @Test
    fun testAddCategory() {
        viewModel.updateName("Food")
        viewModel.updateType(TransactionType.EXPENSE)
        
        viewModel.addCategory()
        
        assertEquals(1, viewModel.categories.value.size)
        assertEquals("Food", viewModel.categories.value[0].name)
    }
    
    @Test
    fun testValidation() {
        // Empty name should fail
        viewModel.updateName("")
        viewModel.updateType(TransactionType.EXPENSE)
        
        viewModel.addCategory()
        
        assertEquals("Category name is required", viewModel.errorMessage.value)
    }
}
```

- [ ] **Step 2: Run test to verify it fails**

Run: `./gradlew test`
Expected: FAIL with "CategoryManagementViewModel not found"

- [ ] **Step 3: Create CategoryManagementViewModel**

```kotlin
// app/src/main/java/com/example/personalaccounting/ui/screens/category/CategoryManagementViewModel.kt
package com.example.personalaccounting.ui.screens.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.personalaccounting.data.repository.CategoryRepository
import com.example.personalaccounting.domain.model.Category
import com.example.personalaccounting.domain.model.TransactionType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CategoryManagementViewModel(
    private val categoryRepository: CategoryRepository
) : ViewModel() {
    
    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories.asStateFlow()
    
    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name.asStateFlow()
    
    private val _type = MutableStateFlow(TransactionType.EXPENSE)
    val type: StateFlow<TransactionType> = _type.asStateFlow()
    
    private val _icon = MutableStateFlow("")
    val icon: StateFlow<String> = _icon.asStateFlow()
    
    private val _color = MutableStateFlow("")
    val color: StateFlow<String> = _color.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()
    
    private val _isSuccess = MutableStateFlow(false)
    val isSuccess: StateFlow<Boolean> = _isSuccess.asStateFlow()
    
    private var editingCategoryId: Long? = null
    
    init {
        loadCategories()
    }
    
    fun loadCategories() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _categories.value = categoryRepository.getAll()
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun updateName(name: String) {
        _name.value = name
        _errorMessage.value = null
    }
    
    fun updateType(type: TransactionType) {
        _type.value = type
    }
    
    fun updateIcon(icon: String) {
        _icon.value = icon
    }
    
    fun updateColor(color: String) {
        _color.value = color
    }
    
    fun addCategory() {
        if (_name.value.isBlank()) {
            _errorMessage.value = "Category name is required"
            return
        }
        
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val category = Category(
                    name = _name.value,
                    icon = _icon.value,
                    color = _color.value,
                    type = _type.value,
                    isDefault = false
                )
                
                categoryRepository.insert(category)
                _isSuccess.value = true
                loadCategories()
                clearForm()
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun updateCategory() {
        if (_name.value.isBlank()) {
            _errorMessage.value = "Category name is required"
            return
        }
        
        if (editingCategoryId == null) {
            _errorMessage.value = "No category selected for editing"
            return
        }
        
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val category = Category(
                    id = editingCategoryId!!,
                    name = _name.value,
                    icon = _icon.value,
                    color = _color.value,
                    type = _type.value,
                    isDefault = false
                )
                
                categoryRepository.update(category)
                _isSuccess.value = true
                loadCategories()
                clearForm()
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun deleteCategory(categoryId: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                categoryRepository.delete(categoryId)
                loadCategories()
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun editCategory(category: Category) {
        editingCategoryId = category.id
        _name.value = category.name
        _type.value = category.type
        _icon.value = category.icon
        _color.value = category.color
    }
    
    fun clearForm() {
        editingCategoryId = null
        _name.value = ""
        _type.value = TransactionType.EXPENSE
        _icon.value = ""
        _color.value = ""
        _errorMessage.value = null
    }
    
    fun resetSuccess() {
        _isSuccess.value = false
    }
}
```

- [ ] **Step 4: Create CategoryManagementScreen**

```kotlin
// app/src/main/java/com/example/personalaccounting/ui/screens/category/CategoryManagementScreen.kt
package com.example.personalaccounting.ui.screens.category

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.personalaccounting.domain.model.Category
import com.example.personalaccounting.domain.model.TransactionType

@Composable
fun CategoryManagementScreen(
    onNavigateBack: () -> Unit,
    viewModel: CategoryManagementViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val categories by viewModel.categories.collectAsState()
    val name by viewModel.name.collectAsState()
    val type by viewModel.type.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val isSuccess by viewModel.isSuccess.collectAsState()
    
    var showDeleteDialog by remember { mutableStateOf<Category?>(null) }
    
    LaunchedEffect(isSuccess) {
        if (isSuccess) {
            viewModel.resetSuccess()
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Category Management") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = androidx.compose.material.icons.Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { padding ->
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {
                // Add Category Form
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = 4.dp
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Add New Category",
                            style = MaterialTheme.typography.h6
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        OutlinedTextField(
                            value = name,
                            onValueChange = { viewModel.updateName(it) },
                            label = { Text("Category Name") },
                            modifier = Modifier.fillMaxWidth(),
                            isError = errorMessage != null
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // Type Selection
                        Text(
                            text = "Type",
                            style = MaterialTheme.typography.subtitle1
                        )
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            FilterChip(
                                selected = type == TransactionType.EXPENSE,
                                onClick = { viewModel.updateType(TransactionType.EXPENSE) },
                                text = { Text("Expense") },
                                modifier = Modifier.weight(1f)
                            )
                            
                            FilterChip(
                                selected = type == TransactionType.INCOME,
                                onClick = { viewModel.updateType(TransactionType.INCOME) },
                                text = { Text("Income") },
                                modifier = Modifier.weight(1f)
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // Error Message
                        errorMessage?.let { error ->
                            Text(
                                text = error,
                                color = MaterialTheme.colors.error,
                                style = MaterialTheme.typography.body2,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }
                        
                        // Add Button
                        Button(
                            onClick = { viewModel.addCategory() },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = !isLoading
                        ) {
                            Text("Add Category")
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Categories List
                Text(
                    text = "Existing Categories",
                    style = MaterialTheme.typography.h6
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(categories) { category ->
                        CategoryItem(
                            category = category,
                            onEdit = { viewModel.editCategory(category) },
                            onDelete = { showDeleteDialog = category }
                        )
                        Divider()
                    }
                }
            }
        }
    }
    
    // Delete Confirmation Dialog
    showDeleteDialog?.let { category ->
        AlertDialog(
            onDismissRequest = { showDeleteDialog = null },
            title = { Text("Delete Category") },
            text = { Text("Are you sure you want to delete '${category.name}'?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteCategory(category.id)
                        showDeleteDialog = null
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun CategoryItem(
    category: Category,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = category.name,
                style = MaterialTheme.typography.subtitle1
            )
            
            Text(
                text = category.type.name,
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
            )
        }
        
        Row {
            TextButton(onClick = onEdit) {
                Text("Edit")
            }
            
            TextButton(onClick = onDelete) {
                Text("Delete")
            }
        }
    }
}
```

- [ ] **Step 5: Run test to verify it passes**

Run: `./gradlew test`
Expected: PASS

- [ ] **Step 6: Commit**

```bash
git add app/src/main/java/com/example/personalaccounting/ui/screens/category/
git commit -m "feat: add CategoryManagementScreen and CategoryManagementViewModel"
```

---

## Task 10: Statistics Screen

**Covers:** [S4, S6]

**Files:**
- Create: `app/src/main/java/com/example/personalaccounting/ui/screens/statistics/StatisticsScreen.kt`
- Create: `app/src/main/java/com/example/personalaccounting/ui/screens/statistics/StatisticsViewModel.kt`

**Interfaces:**
- Consumes: StatisticsService, CategoryRepository
- Produces: StatisticsScreen composable, StatisticsViewModel

- [ ] **Step 1: Write the failing test**

```kotlin
// app/src/test/java/com/example/personalaccounting/ui/screens/statistics/StatisticsViewModelTest.kt
package com.example.personalaccounting.ui.screens.statistics

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.personalaccounting.data.db.CategoryDao
import com.example.personalaccounting.data.db.DatabaseHelper
import com.example.personalaccounting.data.db.TransactionDao
import com.example.personalaccounting.data.repository.CategoryRepository
import com.example.personalaccounting.data.repository.TransactionRepository
import com.example.personalaccounting.domain.model.Category
import com.example.personalaccounting.domain.model.Transaction
import com.example.personalaccounting.domain.model.TransactionType
import com.example.personalaccounting.domain.service.StatisticsService
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate
import kotlin.test.assertEquals

@RunWith(AndroidJUnit4::class)
class StatisticsViewModelTest {
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var viewModel: StatisticsViewModel
    private lateinit var statisticsService: StatisticsService
    private lateinit var categoryRepository: CategoryRepository
    private var testCategoryId: Long = 0
    
    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        dbHelper = DatabaseHelper(context)
        val transactionDao = TransactionDao(dbHelper)
        val categoryDao = CategoryDao(dbHelper)
        
        val transactionRepository = TransactionRepository(transactionDao)
        categoryRepository = CategoryRepository(categoryDao)
        statisticsService = StatisticsService(transactionRepository, categoryRepository)
        
        viewModel = StatisticsViewModel(statisticsService, categoryRepository)
        
        // Create a test category
        val category = Category(
            name = "Food",
            type = TransactionType.EXPENSE,
            isDefault = true
        )
        testCategoryId = categoryRepository.insert(category)
        
        // Add a test transaction
        val transaction = Transaction(
            amount = 100.0,
            type = TransactionType.EXPENSE,
            categoryId = testCategoryId,
            date = LocalDate.now(),
            note = "Lunch"
        )
        transactionDao.insert(transaction)
    }
    
    @After
    fun tearDown() {
        dbHelper.close()
    }
    
    @Test
    fun testLoadCategoryExpenses() {
        viewModel.loadCategoryExpenses()
        
        assertEquals(1, viewModel.categoryExpenses.value.size)
        assertEquals(100.0, viewModel.categoryExpenses.value[0].totalAmount)
    }
}
```

- [ ] **Step 2: Run test to verify it fails**

Run: `./gradlew test`
Expected: FAIL with "StatisticsViewModel not found"

- [ ] **Step 3: Create StatisticsViewModel**

```kotlin
// app/src/main/java/com/example/personalaccounting/ui/screens/statistics/StatisticsViewModel.kt
package com.example.personalaccounting.ui.screens.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.personalaccounting.data.repository.CategoryRepository
import com.example.personalaccounting.domain.model.CategoryExpense
import com.example.personalaccounting.domain.service.MonthlySummary
import com.example.personalaccounting.domain.service.StatisticsService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

class StatisticsViewModel(
    private val statisticsService: StatisticsService,
    private val categoryRepository: CategoryRepository
) : ViewModel() {
    
    private val _categoryExpenses = MutableStateFlow<List<CategoryExpense>>(emptyList())
    val categoryExpenses: StateFlow<List<CategoryExpense>> = _categoryExpenses.asStateFlow()
    
    private val _monthlySummary = MutableStateFlow<MonthlySummary?>(null)
    val monthlySummary: StateFlow<MonthlySummary?> = _monthlySummary.asStateFlow()
    
    private val _selectedYear = MutableStateFlow(LocalDate.now().year)
    val selectedYear: StateFlow<Int> = _selectedYear.asStateFlow()
    
    private val _selectedMonth = MutableStateFlow(LocalDate.now().monthValue)
    val selectedMonth: StateFlow<Int> = _selectedMonth.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    init {
        loadCategoryExpenses()
        loadMonthlySummary()
    }
    
    fun loadCategoryExpenses() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _categoryExpenses.value = statisticsService.getCategoryExpenses(
                    _selectedYear.value,
                    _selectedMonth.value
                )
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun loadMonthlySummary() {
        viewModelScope.launch {
            _monthlySummary.value = statisticsService.getMonthlySummary(
                _selectedYear.value,
                _selectedMonth.value
            )
        }
    }
    
    fun updateMonth(year: Int, month: Int) {
        _selectedYear.value = year
        _selectedMonth.value = month
        loadCategoryExpenses()
        loadMonthlySummary()
    }
    
    fun previousMonth() {
        if (_selectedMonth.value == 1) {
            _selectedYear.value -= 1
            _selectedMonth.value = 12
        } else {
            _selectedMonth.value -= 1
        }
        loadCategoryExpenses()
        loadMonthlySummary()
    }
    
    fun nextMonth() {
        if (_selectedMonth.value == 12) {
            _selectedYear.value += 1
            _selectedMonth.value = 1
        } else {
            _selectedMonth.value += 1
        }
        loadCategoryExpenses()
        loadMonthlySummary()
    }
}
```

- [ ] **Step 4: Create StatisticsScreen**

```kotlin
// app/src/main/java/com/example/personalaccounting/ui/screens/statistics/StatisticsScreen.kt
package com.example.personalaccounting.ui.screens.statistics

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.personalaccounting.domain.model.CategoryExpense
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun StatisticsScreen(
    onNavigateBack: () -> Unit,
    viewModel: StatisticsViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val categoryExpenses by viewModel.categoryExpenses.collectAsState()
    val monthlySummary by viewModel.monthlySummary.collectAsState()
    val selectedYear by viewModel.selectedYear.collectAsState()
    val selectedMonth by viewModel.selectedMonth.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Statistics") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = androidx.compose.material.icons.Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { padding ->
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {
                // Month Navigation
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { viewModel.previousMonth() }) {
                        Icon(
                            imageVector = androidx.compose.material.icons.Icons.Default.ArrowBack,
                            contentDescription = "Previous Month"
                        )
                    }
                    
                    Text(
                        text = "${LocalDate.of(selectedYear, selectedMonth, 1).month.getDisplayName(TextStyle.FULL, Locale.getDefault())} $selectedYear",
                        style = MaterialTheme.typography.h6,
                        fontWeight = FontWeight.Bold
                    )
                    
                    IconButton(onClick = { viewModel.nextMonth() }) {
                        Icon(
                            imageVector = androidx.compose.material.icons.Icons.Default.ArrowForward,
                            contentDescription = "Next Month"
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Monthly Summary Card
                monthlySummary?.let { summary ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = 4.dp
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "Monthly Summary",
                                style = MaterialTheme.typography.h6,
                                fontWeight = FontWeight.Bold
                            )
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(
                                        text = "Income",
                                        style = MaterialTheme.typography.body2,
                                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                                    )
                                    Text(
                                        text = "$%.2f".format(summary.totalIncome),
                                        style = MaterialTheme.typography.h6,
                                        color = Color(0xFF4CAF50)
                                    )
                                }
                                
                                Column {
                                    Text(
                                        text = "Expense",
                                        style = MaterialTheme.typography.body2,
                                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                                    )
                                    Text(
                                        text = "$%.2f".format(summary.totalExpense),
                                        style = MaterialTheme.typography.h6,
                                        color = Color(0xFFF44336)
                                    )
                                }
                                
                                Column {
                                    Text(
                                        text = "Balance",
                                        style = MaterialTheme.typography.body2,
                                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                                    )
                                    Text(
                                        text = "$%.2f".format(summary.balance),
                                        style = MaterialTheme.typography.h6,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Category Expenses
                Text(
                    text = "Expenses by Category",
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                if (categoryExpenses.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No expenses for this month",
                            style = MaterialTheme.typography.body1,
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(categoryExpenses) { categoryExpense ->
                            CategoryExpenseItem(categoryExpense = categoryExpense)
                            Divider()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryExpenseItem(categoryExpense: CategoryExpense) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = categoryExpense.categoryName,
                style = MaterialTheme.typography.subtitle1,
                fontWeight = FontWeight.Medium
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = "${categoryExpense.transactionCount} transactions",
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
            )
        }
        
        Column(
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = "$%.2f".format(categoryExpense.totalAmount),
                style = MaterialTheme.typography.subtitle1,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = "%.1f%%".format(categoryExpense.percentage),
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}
```

- [ ] **Step 5: Run test to verify it passes**

Run: `./gradlew test`
Expected: PASS

- [ ] **Step 6: Commit**

```bash
git add app/src/main/java/com/example/personalaccounting/ui/screens/statistics/
git commit -m "feat: add StatisticsScreen and StatisticsViewModel"
```

---

## Task 11: Integration Testing

**Covers:** [S5, S6]

**Files:**
- Create: `app/src/androidTest/java/com/example/personalaccounting/IntegrationTest.kt`

**Interfaces:**
- Consumes: All previous tasks
- Produces: Integration tests

- [ ] **Step 1: Write the failing test**

```kotlin
// app/src/androidTest/java/com/example/personalaccounting/IntegrationTest.kt
package com.example.personalaccounting

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.personalaccounting.data.db.CategoryDao
import com.example.personalaccounting.data.db.DatabaseHelper
import com.example.personalaccounting.data.db.TransactionDao
import com.example.personalaccounting.data.repository.CategoryRepository
import com.example.personalaccounting.data.repository.TransactionRepository
import com.example.personalaccounting.domain.model.Category
import com.example.personalaccounting.domain.model.Transaction
import com.example.personalaccounting.domain.model.TransactionType
import com.example.personalaccounting.domain.service.StatisticsService
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@RunWith(AndroidJUnit4::class)
class IntegrationTest {
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var transactionRepository: TransactionRepository
    private lateinit var categoryRepository: CategoryRepository
    private lateinit var statisticsService: StatisticsService
    
    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        dbHelper = DatabaseHelper(context)
        val transactionDao = TransactionDao(dbHelper)
        val categoryDao = CategoryDao(dbHelper)
        
        transactionRepository = TransactionRepository(transactionDao)
        categoryRepository = CategoryRepository(categoryDao)
        statisticsService = StatisticsService(transactionRepository, categoryRepository)
    }
    
    @After
    fun tearDown() {
        dbHelper.close()
    }
    
    @Test
    fun testFullWorkflow() {
        // Create categories
        val foodCategory = Category(
            name = "Food",
            type = TransactionType.EXPENSE,
            isDefault = true
        )
        val salaryCategory = Category(
            name = "Salary",
            type = TransactionType.INCOME,
            isDefault = true
        )
        
        val foodCategoryId = categoryRepository.insert(foodCategory)
        val salaryCategoryId = categoryRepository.insert(salaryCategory)
        
        // Add transactions
        val foodTransaction = Transaction(
            amount = 100.0,
            type = TransactionType.EXPENSE,
            categoryId = foodCategoryId,
            date = LocalDate.now(),
            note = "Lunch"
        )
        val salaryTransaction = Transaction(
            amount = 5000.0,
            type = TransactionType.INCOME,
            categoryId = salaryCategoryId,
            date = LocalDate.now(),
            note = "Monthly salary"
        )
        
        val foodTransactionId = transactionRepository.add(foodTransaction)
        val salaryTransactionId = transactionRepository.add(salaryTransaction)
        
        // Verify transactions exist
        val retrievedFood = transactionRepository.getById(foodTransactionId)
        val retrievedSalary = transactionRepository.getById(salaryTransactionId)
        
        assertTrue(retrievedFood != null)
        assertTrue(retrievedSalary != null)
        assertEquals(100.0, retrievedFood?.amount)
        assertEquals(5000.0, retrievedSalary?.amount)
        
        // Verify statistics
        val categoryExpenses = statisticsService.getCategoryExpenses(
            LocalDate.now().year,
            LocalDate.now().monthValue
        )
        
        assertEquals(1, categoryExpenses.size)
        assertEquals(100.0, categoryExpenses[0].totalAmount)
        
        // Verify monthly summary
        val monthlySummary = statisticsService.getMonthlySummary(
            LocalDate.now().year,
            LocalDate.now().monthValue
        )
        
        assertEquals(5000.0, monthlySummary.totalIncome)
        assertEquals(100.0, monthlySummary.totalExpense)
        assertEquals(4900.0, monthlySummary.balance)
    }
}
```

- [ ] **Step 2: Run test to verify it fails**

Run: `./gradlew connectedAndroidTest`
Expected: FAIL with test failure

- [ ] **Step 3: Fix any integration issues**

Review and fix any issues found during integration testing.

- [ ] **Step 4: Run test to verify it passes**

Run: `./gradlew connectedAndroidTest`
Expected: PASS

- [ ] **Step 5: Commit**

```bash
git add app/src/androidTest/
git commit -m "test: add integration tests"
```

---

## Task 12: Final Testing and Polish

**Covers:** [S5, S6]

**Files:**
- Modify: Various files as needed

**Interfaces:**
- Consumes: All previous tasks
- Produces: Final polished app

- [ ] **Step 1: Run all unit tests**

Run: `./gradlew test`
Expected: All tests pass

- [ ] **Step 2: Run all instrumentation tests**

Run: `./gradlew connectedAndroidTest`
Expected: All tests pass

- [ ] **Step 3: Build release APK**

Run: `./gradlew assembleRelease`
Expected: APK generated successfully

- [ ] **Step 4: Final commit**

```bash
git add -A
git commit -m "feat: complete personal accounting app implementation"
```