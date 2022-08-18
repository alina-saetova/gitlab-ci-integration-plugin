package com.saetova.gitlabciintegration.action

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.saetova.gitlabciintegration.service.BackgroundTaskService

class RefreshLoadPipelinesAction: AnAction("Refresh Pipelines") {

    override fun actionPerformed(e: AnActionEvent) {
        e.project?.let { BackgroundTaskService.getInstance(it).restartBackgroundTask() }
    }
}