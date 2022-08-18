package com.saetova.gitlabciintegration.listener

import com.intellij.util.messages.Topic
import java.util.EventListener

interface LoadBranchesListener : EventListener {

    companion object {
        val LOAD = Topic.create(
            "Load branches",
            LoadBranchesListener::class.java
        )
    }

    fun load(branches: List<String>)
}