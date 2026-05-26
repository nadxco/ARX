package com.example

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.annotation.Config
import androidx.test.core.app.ActivityScenario
import com.example.MainActivity

@RunWith(AndroidJUnit4::class)
@Config(sdk = [33])
class CrashHuntTest {
    @Test
    fun testMainActivityLaunchesWithoutCrash() {
        ActivityScenario.launch(MainActivity::class.java).use { scenario ->
            scenario.onActivity { activity ->
                // If we reach here, no crash on startup
                println("Activity launched successfully!")
            }
        }
    }
}
