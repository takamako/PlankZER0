package vip.example.plank

import android.content.Context
import androidx.benchmark.junit4.BenchmarkRule
import androidx.benchmark.junit4.measureRepeated

import androidx.test.*
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.assertEquals
import org.junit.Rule

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @get:Rule
    val benchmarkRule = BenchmarkRule()

    @Test
//    fun useAppContext() {
        fun benchmarkSomeWork() = benchmarkRule.measureRepeated {


    // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        assertEquals("vip.example.plank", appContext.packageName)
    }
}
