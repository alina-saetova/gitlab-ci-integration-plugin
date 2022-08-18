package com.saetova.gitlabciintegration.action

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.options.ShowSettingsUtil
import com.saetova.gitlabciintegration.settings.configurable.GitlabInfoConfigurable

class OpenSettingsAction: AnAction("Open Settings") {

    override fun actionPerformed(e: AnActionEvent) {
        ShowSettingsUtil.getInstance()
            .showSettingsDialog(e.project, GitlabInfoConfigurable::class.java)
    }
}