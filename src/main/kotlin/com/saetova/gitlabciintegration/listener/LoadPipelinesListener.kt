package com.saetova.gitlabciintegration.listener

import com.intellij.util.messages.Topic
import com.saetova.gitlabciintegration.data.model.PipelineInfo
import java.util.EventListener

interface LoadPipelinesListener : EventListener {

    companion object {
        val LOAD = Topic.create(
            "Load pipelines",
            LoadPipelinesListener::class.java
        )
    }

    fun load(pipelines: List<PipelineInfo>)
}
