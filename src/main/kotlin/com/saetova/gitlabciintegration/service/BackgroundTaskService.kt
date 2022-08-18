package com.saetova.gitlabciintegration.service

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.util.concurrency.AppExecutorUtil
import com.intellij.util.messages.MessageBus
import com.saetova.gitlabciintegration.data.repository.GitlabRepository
import com.saetova.gitlabciintegration.listener.ConfigChangedListener
import com.saetova.gitlabciintegration.settings.persistence.GitlabInfoComponent
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

@Service
class BackgroundTaskService(
    private val project: Project
) {

    companion object {
        fun getInstance(project: Project): BackgroundTaskService = project.service()

        private const val INITIAL_DELAY = 0L
        private const val UPDATE_DELAY = 30L
    }

    private var scheduledFuture: ScheduledFuture<*>? = null
    private var messageBus: MessageBus = project.messageBus
    private var backgroundTask: Runnable
    private var gitlabRepository = GitlabRepository(project)
    private val gitlabInfo = service<GitlabInfoComponent>()
    private var isRunning = false

    init {
        messageBus.connect().subscribe(
            ConfigChangedListener.CONFIG_CHANGED,
            object : ConfigChangedListener {
                override fun configChanged() {
                    restartBackgroundTask()
                }
            }
        )

        backgroundTask = Runnable {
            gitlabRepository.loadPipelines()
        }

        startBackgroundTask()
    }

    fun startBackgroundTask() {
        if (isRunning) return
        scheduledFuture = if (gitlabInfo.isNeedBackgroundUpdate) {
            AppExecutorUtil.getAppScheduledExecutorService().scheduleWithFixedDelay(
                backgroundTask,
                INITIAL_DELAY,
                UPDATE_DELAY,
                TimeUnit.SECONDS
            )
        } else {
            AppExecutorUtil.getAppScheduledExecutorService().schedule(
                backgroundTask,
                INITIAL_DELAY,
                TimeUnit.SECONDS
            )
        }
        isRunning = true
    }

    fun stopBackgroundTask() {
        if (!isRunning) return
        scheduledFuture?.cancel(false)
        isRunning = false
    }

    fun restartBackgroundTask() {
        if (isRunning) {
            stopBackgroundTask()
        }
        startBackgroundTask()
        isRunning = true
    }
}