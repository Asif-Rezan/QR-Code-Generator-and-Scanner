package com.asifrezan.qrcodegenerator
import android.content.Intent
import android.content.Intent.ACTION_SEND
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.activityScenarioRule
import org.hamcrest.Matchers.allOf
import org.junit.Rule
import org.junit.Test


class TodoFragmentTest
{
    @get:Rule
    val activityScenarioRule = activityScenarioRule<MainActivity>()

    @Test
    fun testCodeGenarateButton_expecteedQR()
    {
        onView(withId(R.id.downloadButton)).perform(click())

        onView(withId(R.id.textViewId)).check(matches(withText("Sample text")))



    }

    @Test
    fun testDownloadButton()
    {
        Intents.init()
        val expected = allOf(hasAction(Intent.ACTION_SEND))
        onView(withId(R.id.downloadButton)).perform(click())
        intended(expected)
        Intents.release()


    }



}