package com.newvisiondz.sayaradz.views


import android.app.Instrumentation
import android.support.test.InstrumentationRegistry.getInstrumentation
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.rule.ActivityTestRule
import com.newvisiondz.sayaradz.R
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test


class BrandDisplayTest {

    @get:Rule
    val activityRule = ActivityTestRule(LoginActivity::class.java)
    private var mActivity: LoginActivity? = null
    private var monitor: Instrumentation.ActivityMonitor =
        getInstrumentation().addMonitor(BrandDisplay::class.java.name, null, false)


    @org.junit.Before
    fun setUp() {
        mActivity = activityRule.activity
    }

    @Test
    fun testLunch() {
        assertNotNull(mActivity!!.findViewById(R.id.googleSignIn))

        onView(withId(R.id.googleSignIn)).perform(click())
        val brandDisplay: BrandDisplay = getInstrumentation().waitForMonitorWithTimeout(monitor, 10000) as BrandDisplay
        assertNotNull(brandDisplay)
        brandDisplay.finish()
    }

    @org.junit.After
    fun tearDown() {
        mActivity = null
    }
}