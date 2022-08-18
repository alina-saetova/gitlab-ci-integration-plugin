package com.saetova.gitlabciintegration.data.repository

import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.saetova.gitlabciintegration.data.model.PipelineDetailsInfo
import com.saetova.gitlabciintegration.data.model.mapToPresentation
import com.saetova.gitlabciintegration.data.model.mapToPresentationDetails
import com.saetova.gitlabciintegration.listener.LoadBranchesListener
import com.saetova.gitlabciintegration.listener.LoadPipelinesListener
import com.saetova.gitlabciintegration.listener.LoadTagsListener
import com.saetova.gitlabciintegration.service.NotificationService
import com.saetova.gitlabciintegration.settings.persistence.GitlabInfoComponent
import com.saetova.gitlabciintegration.utils.runBackgroundableTask
import org.gitlab4j.api.GitLabApi

class GitlabRepository(val project: Project) {

    companion object {
        private const val PIPELINE_DETAILS_BACKGROUND_TASK_NAME = "Load Pipeline Details..."
        private const val PIPELINES_BACKGROUND_TASK_NAME = "Load Pipelines..."
        private const val CREATE_PIPELINE_BACKGROUND_TASK_NAME = "Create New Pipeline..."
        private const val DELETE_PIPELINE_BACKGROUND_TASK_NAME = "Delete Pipeline..."
        private const val RETRY_PIPELINE_BACKGROUND_TASK_NAME = "Retry Pipeline..."
        private const val CANCEL_PIPELINE_BACKGROUND_TASK_NAME = "Cancel Pipeline..."
        private const val LOAD_BRANCHES_BACKGROUND_TASK_NAME = "Load branches..."
        private const val LOAD_TAGS_BACKGROUND_TASK_NAME = "Load tags..."
    }

    private val gitlabInfo = service<GitlabInfoComponent>()
    private val notificationService = NotificationService.getInstance(project)

    private val gitlabHostUrl by lazy { gitlabInfo.hostUrl }
    private val gitlabToken by lazy { gitlabInfo.token }
    private val projectId by lazy { gitlabInfo.projectId }

    private val gitLabApi = GitLabApi(gitlabHostUrl, gitlabToken)

    fun getPipelineDetails(
        pipelineId: Int,
        onSuccessAction: (PipelineDetailsInfo?) -> Unit
    ) {
        if (!isGitlabInfoExists()) return

        runBackgroundableTask(
            project,
            PIPELINE_DETAILS_BACKGROUND_TASK_NAME,
            task = {
                gitLabApi.pipelineApi.getPipeline(projectId, pipelineId)
            }, onSuccessAction = { result ->
                onSuccessAction(result?.mapToPresentationDetails())
            }, onErrorAction = {
                notificationService.showBackgroundTaskErrorNotification(it)
            })
    }

    fun loadPipelines() {
        if (!isGitlabInfoExists()) return

        runBackgroundableTask(
            project,
            PIPELINES_BACKGROUND_TASK_NAME,
            task = {
                gitLabApi.pipelineApi.getPipelines(projectId)
            }, onSuccessAction = { result ->
                result?.map { it.mapToPresentation() }?.let {
                    project.messageBus.syncPublisher(LoadPipelinesListener.LOAD).load(it)
                }
            }, onErrorAction = {
                notificationService.showLoadPipelinesByBackgroundErrorNotification()
            })
    }

    fun createNewPipeline(
        ref: String,
        onSuccessAction: () -> Unit
    ) {
        if (!isGitlabInfoExists()) return

        runBackgroundableTask(
            project,
            CREATE_PIPELINE_BACKGROUND_TASK_NAME,
            task = {
                gitLabApi.pipelineApi.createPipeline(projectId, ref)
            }, onSuccessAction = {
                onSuccessAction()
            }, onErrorAction = {
                notificationService.showBackgroundTaskErrorNotification(it)
            })
    }

    fun deletePipeline(
        pipelineId: Int,
        onSuccessAction: () -> Unit
    ) {
        if (!isGitlabInfoExists()) return

        runBackgroundableTask(
            project,
            DELETE_PIPELINE_BACKGROUND_TASK_NAME,
            task = {
                gitLabApi.pipelineApi.deletePipeline(projectId, pipelineId)
            }, onSuccessAction = {
                onSuccessAction()
            }, onErrorAction = {
                notificationService.showBackgroundTaskErrorNotification(it)
            })
    }

    fun retryPipeline(
        pipelineId: Int,
        onSuccessAction: () -> Unit
    ) {
        if (!isGitlabInfoExists()) return

        runBackgroundableTask(
            project,
            RETRY_PIPELINE_BACKGROUND_TASK_NAME,
            task = {
                gitLabApi.pipelineApi.retryPipelineJob(projectId, pipelineId)
            }, onSuccessAction = {
                onSuccessAction()
            }, onErrorAction = {
                notificationService.showBackgroundTaskErrorNotification(it)
            })
    }

    fun cancelPipeline(
        pipelineId: Int,
        onSuccessAction: () -> Unit
    ) {
        if (!isGitlabInfoExists()) return

        runBackgroundableTask(
            project,
            CANCEL_PIPELINE_BACKGROUND_TASK_NAME,
            task = {
                gitLabApi.pipelineApi.cancelPipelineJobs(projectId, pipelineId)
            }, onSuccessAction = {
                onSuccessAction()
            }, onErrorAction = {
                notificationService.showBackgroundTaskErrorNotification(it)
            })
    }

    fun loadBranches() {
        if (!isGitlabInfoExists()) return

        runBackgroundableTask(
            project,
            LOAD_BRANCHES_BACKGROUND_TASK_NAME,
            task = {
                gitLabApi.repositoryApi.getBranches(projectId)
            }, onSuccessAction = { branches ->
                branches?.map { it.name }?.let {
                    project.messageBus.syncPublisher(LoadBranchesListener.LOAD).load(it)
                }
            }, onErrorAction = {
                notificationService.showBackgroundTaskErrorNotification(it)
            })
    }

    fun loadTags() {
        if (!isGitlabInfoExists()) return

        runBackgroundableTask(
            project,
            LOAD_TAGS_BACKGROUND_TASK_NAME,
            task = {
                gitLabApi.tagsApi.getTags(projectId)
            }, onSuccessAction = { tags ->
                tags?.map { it.name }?.let {
                    project.messageBus.syncPublisher(LoadTagsListener.LOAD).load(it)
                }
            }, onErrorAction = {
                notificationService.showBackgroundTaskErrorNotification(it)
            })
    }

    private fun isGitlabInfoExists(): Boolean {
        val isGitlabInfoExists = gitlabHostUrl.isNotEmpty() && gitlabToken.isNotEmpty() && projectId.isNotEmpty()
        if (!isGitlabInfoExists) {
            notificationService.showGitlabInfoIsMissingNotification()
        }
        return isGitlabInfoExists
    }
}