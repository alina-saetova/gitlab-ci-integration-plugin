package com.saetova.gitlabciintegration.listener

import com.intellij.util.messages.Topic
import java.util.EventListener

interface TriggerRefreshPipelinesListener : EventListener {

    companion object {
        val REFRESH = Topic.create(
            "Refresh pipelines",
            TriggerRefreshPipelinesListener::class.java
        )
    }

    fun refresh()
}
