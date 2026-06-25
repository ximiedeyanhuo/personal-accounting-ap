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
