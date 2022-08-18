package com.saetova.gitlabciintegration.settings.persistence

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil

@State(
    name = "GitlabInfoComponent",
    storages = [Storage("gitlab-info.xml")]
)
class GitlabInfoComponent(
    var hostUrl: String = "",
    var token: String = "",
    var projectId: String = "",
    var isNeedBackgroundUpdate: Boolean = true
) : PersistentStateComponent<GitlabInfoComponent> {

    override fun getState(): GitlabInfoComponent {
        return this
    }

    override fun loadState(state: GitlabInfoComponent) {
        XmlSerializerUtil.copyBean(state, this)
    }
}