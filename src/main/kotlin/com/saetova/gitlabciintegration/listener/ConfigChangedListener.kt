package com.saetova.gitlabciintegration.listener

import com.intellij.util.messages.Topic
import java.util.EventListener

interface ConfigChangedListener : EventListener {

    companion object {
        val CONFIG_CHANGED = Topic.create(
            "Gitlab config has been changed",
            ConfigChangedListener::class.java
        )
    }

    fun configChanged()
}
