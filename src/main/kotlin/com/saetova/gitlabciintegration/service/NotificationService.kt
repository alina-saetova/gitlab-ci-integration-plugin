package com.saetova.gitlabciintegration.service

import com.intellij.notification.Notification
import com.intellij.notification.NotificationGroup
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.saetova.gitlabciintegration.action.OpenPipelineInBrowserAction
import com.saetova.gitlabciintegration.action.OpenPipelineInDialogAction
import com.saetova.gitlabciintegration.action.OpenSettingsAction
import com.saetova.gitlabciintegration.action.RefreshLoadPipelinesAction
import com.saetova.gitlabciintegration.data.model.PipelineInfo
import com.saetova.gitlabciintegration.data.model.PipelineStatus
import com.saetova.gitlabciintegration.data.model.isStartedState

@Service
class NotificationService(
    private val project: Project
) {

    private val openedNotifications = mutableListOf<Notification>()

    companion object {
        fun getInstance(project: Project): NotificationService = project.service()

        private const val MAX_NOTIFICATION_COUNT = 3
    }

    private val notificationGroup = NotificationGroup.balloonGroup("Notification Group")

    fun showNotification(
        title: String,
        content: String,
        type: NotificationType,
        vararg action: AnAction
    ) {
        val notification = notificationGroup.createNotification(
            title,
            content,
            type
        )
        action.forEach { notification.addAction(it)  }
        if (openedNotifications.size == MAX_NOTIFICATION_COUNT) {
            openedNotifications[0].hideBalloon()
            openedNotifications.removeAt(0)
        }
        openedNotifications.add(notification)
        Notifications.Bus.notify(notification)
    }

    fun showGitlabInfoIsMissingNotification() {
        showNotification(
            "Gitlab Info is missing",
            "Open settings and fill in the required information",
            NotificationType.WARNING,
            OpenSettingsAction()
        )
    }

    fun showBackgroundTaskErrorNotification(throwable: Throwable) {
        showNotification(
            "Network error",
            throwable.message.toString(),
            NotificationType.ERROR
        )
    }

    fun showLoadPipelinesByBackgroundErrorNotification() {
        showNotification(
            "Unable to load update pipelines info",
            "Try to refresh loading",
            NotificationType.ERROR,
            RefreshLoadPipelinesAction()
        )
    }

    fun showUpdatedPipelineInfoNotification(pipelines: MutableList<Pair<PipelineInfo?, PipelineInfo>>) {
        val pipelinesToShow = pipelines.take(MAX_NOTIFICATION_COUNT)
        pipelinesToShow.forEach { (oldPipeline, newPipeline) ->
            if (oldPipeline == null && newPipeline.status.isStartedState()) {
                showNewPipelineNotification(newPipeline)
            } else {
                when (newPipeline.status) {
                    PipelineStatus.FAILED -> showPipelineFailedNotification(newPipeline)
                    PipelineStatus.CANCELED -> showPipelineCanceledNotification(newPipeline)
                    else -> showPipelineUpdatedStatusNotification(newPipeline)
                }
            }
        }
    }

    private fun showNewPipelineNotification(pipeline: PipelineInfo) {
        showPipelineNotification(
            "New pipeline",
            "Id: ${pipeline.id}, status: ${pipeline.status.name}, ref: ${pipeline.ref}",
            NotificationType.INFORMATION,
            pipeline
        )
    }

    private fun showPipelineFailedNotification(pipeline: PipelineInfo) {
        showPipelineNotification(
            "Pipeline failed",
            "Id: ${pipeline.id}, ref: ${pipeline.ref}",
            NotificationType.ERROR,
            pipeline
        )
    }

    private fun showPipelineCanceledNotification(pipeline: PipelineInfo) {
        showPipelineNotification(
            "Pipeline canceled",
            "Id: ${pipeline.id}, ref: ${pipeline.ref}",
            NotificationType.WARNING,
            pipeline
        )
    }

    private fun showPipelineUpdatedStatusNotification(pipeline: PipelineInfo) {
        showPipelineNotification(
            "New status of pipeline",
            "Id: ${pipeline.id}, status: ${pipeline.status.name}, ref: ${pipeline.ref}",
            NotificationType.INFORMATION,
            pipeline
        )
    }

    private fun showPipelineNotification(
        title: String,
        content: String,
        type: NotificationType,
        pipeline: PipelineInfo
    ) {
        showNotification(
            title,
            content,
            type,
            OpenPipelineInDialogAction(pipeline.id),
            OpenPipelineInBrowserAction(pipeline.url)
        )
    }
}