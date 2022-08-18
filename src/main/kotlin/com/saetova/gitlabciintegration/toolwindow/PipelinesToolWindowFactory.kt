package com.saetova.gitlabciintegration.toolwindow

import com.intellij.icons.AllIcons
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogPanel
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.layout.panel
import com.saetova.gitlabciintegration.data.model.PipelineInfo
import com.saetova.gitlabciintegration.dialog.CreatePipelineDialog
import com.saetova.gitlabciintegration.dialog.PipelineDetailsDialog
import com.saetova.gitlabciintegration.listener.LoadPipelinesListener
import com.saetova.gitlabciintegration.listener.TriggerRefreshPipelinesListener
import com.saetova.gitlabciintegration.service.BackgroundTaskService
import com.saetova.gitlabciintegration.service.NotificationService
import com.saetova.gitlabciintegration.toolwindow.ui.PipelinesTable
import com.saetova.gitlabciintegration.toolwindow.ui.PipelinesTableModel
import javax.swing.JButton

class PipelinesToolWindowFactory : ToolWindowFactory {

    private val tableModel = PipelinesTableModel()

    private lateinit var project: Project
    private lateinit var backgroundTaskService: BackgroundTaskService
    private lateinit var notificationService: NotificationService

    private var isFirstLoading = true

    private val addButton by lazy { JButton(AllIcons.General.Add).apply { addActionListener { createNewPipeline() } } }
    private val refreshButton by lazy { JButton(AllIcons.Actions.Refresh).apply { addActionListener { refreshPipelines() } } }

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val contentManager = toolWindow.contentManager
        val content1 = contentManager.factory.createContent(createDialogPanel(project), "", false)
        contentManager.addContent(content1)
    }

    private fun createDialogPanel(project: Project): DialogPanel {
        this.project = project
        backgroundTaskService = BackgroundTaskService.getInstance(project)
        notificationService = NotificationService.getInstance(project)
        subscribeToMessageBus()

        return panel {
            row {
                cell {
                    refreshButton.invoke()
                    addButton.invoke()
                }
            }
            row {
                JBScrollPane(
                    PipelinesTable(tableModel) {
                        PipelineDetailsDialog(project, it).show()
                    }
                ).invoke()
            }
        }
    }

    private fun subscribeToMessageBus() {
        project.messageBus.connect().subscribe(LoadPipelinesListener.LOAD, object : LoadPipelinesListener {
            override fun load(pipelines: List<PipelineInfo>) {
                if (isFirstLoading) {
                    isFirstLoading = false
                } else {
                    showUpdateNotification(pipelines)
                }
                tableModel.setData(pipelines)
            }
        })
        project.messageBus.connect().subscribe(TriggerRefreshPipelinesListener.REFRESH, object : TriggerRefreshPipelinesListener {
            override fun refresh() {
                refreshPipelines()
            }
        })
    }

    private fun createNewPipeline() {
        CreatePipelineDialog(project).show()
    }

    private fun refreshPipelines() {
        tableModel.clearData()
        backgroundTaskService.restartBackgroundTask()
    }

    private fun showUpdateNotification(receivedPipelines: List<PipelineInfo>) {
        val oldPipelines = tableModel.pipelinesInfo.take(10)
        val newPipelines = receivedPipelines.take(10)
        val difPipelines = mutableListOf<Pair<PipelineInfo?, PipelineInfo>>()
        newPipelines.forEach { newPipeline ->
            val oldPipeline = oldPipelines.find { it.id == newPipeline.id }
            if (oldPipeline == null) {
                difPipelines.add(null to newPipeline)
            } else {
                if (oldPipeline.status != newPipeline.status) {
                    difPipelines.add(oldPipeline to newPipeline)
                }
            }
        }
        notificationService.showUpdatedPipelineInfoNotification(difPipelines)
    }
}