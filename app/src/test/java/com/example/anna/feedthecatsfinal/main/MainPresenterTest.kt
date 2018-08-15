package com.example.anna.feedthecatsfinal.main

import com.example.anna.feedthecatsfinal.CatsFeedingEvent
import org.junit.Test

class MainPresenterTest {

    val classUnderTest = MainPresenter()

    @Test
    fun shouldBePossibleToPassListOfValues() {
        //Preparation
        val testData = emptyList<CatsFeedingEvent>()

        //invocation
        classUnderTest.onEventDataReceived(testData)

        //
    }
}