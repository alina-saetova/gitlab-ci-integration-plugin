package com.saetova.gitlabciintegration.listener

import com.intellij.util.messages.Topic
import java.util.EventListener

interface LoadTagsListener : EventListener {

    companion object {
        val LOAD = Topic.create(
            "Load tags",
            LoadTagsListener::class.java
        )
    }

    fun load(tags: List<String>)
}