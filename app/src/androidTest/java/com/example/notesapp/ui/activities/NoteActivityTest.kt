package com.example.notesapp.ui.activities

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import com.example.notesapp.R
import com.example.notesapp.common.getColorInt
import com.example.notesapp.data.Note
import com.example.notesapp.ui.viewmodels.NoteViewModel
import com.example.notesapp.ui.viewstates.NoteViewState
import io.mockk.*
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import org.koin.standalone.StandAloneContext
import org.koin.standalone.StandAloneContext.loadKoinModules

class NoteActivityTest {
    @get:Rule
    val activityTestRule = ActivityTestRule(NoteActivity::class.java, true, false)

    private val model = mockk<NoteViewModel>(relaxed = true)
    private val viewStateLiveData = MutableLiveData<NoteViewState>()

    private val testNote = Note("333", "title", "body")

    @Before
    fun setUp() {
        loadKoinModules(
                listOf(
                        module { this.viewModel(override = true) { model } }
                )
        )
        every { model.getViewState() } returns viewStateLiveData
        every { model.loadNote(any()) } just runs
        every { model.save(any()) } just runs
        every { model.deleteNote() } just runs
        every { model.onCleared() } just runs
        Intent().apply {
            putExtra(NoteActivity::class.java.name + "extra.NOTE_ID", testNote.id)
        }.let {
            activityTestRule.launchActivity(it)
        }

    }

    @After
    fun tearDown() {
        StandAloneContext.stopKoin()
    }

    @Test
    fun should_show_color_picker() {
        onView(withId(R.id.palette)).perform(click())
        onView(withId(R.id.color_picker)).check(matches(isCompletelyDisplayed()))
    }


    @Test
    fun should_hide_color_picker() {
        onView(withId(R.id.palette)).perform(click()).perform(click())

        onView(withId(R.id.color_picker)).check(matches(not(isDisplayed())))
    }

    @Test
    fun should_set_toolbar_color() {
        onView(withId(R.id.palette)).perform(click())
        onView(withTagValue(`is`(Note.Colors.BLUE))).perform(click())

        val colorInt = Note.Colors.BLUE.getColorInt(activityTestRule.activity)

        onView(withId(R.id.toolbar)).check { view, _ ->
            assertTrue("toolbar background color does not match",
                    (view.background as? ColorDrawable)?.color == colorInt)
        }
    }

    @Test
    fun should_call_saveNote() {
        onView(withId(R.id.et_title)).perform(typeText(testNote.title))
        verify(timeout = 1000) { model.save(any()) }
    }

    @Test
    fun should_show_note() {
        activityTestRule.launchActivity(null)
        viewStateLiveData.postValue(NoteViewState(NoteViewState.Data(note = testNote)))

        onView(withId(R.id.et_title)).check(matches(withText(testNote.title)))
        onView(withId(R.id.et_body)).check(matches(withText(testNote.text)))
    }
}