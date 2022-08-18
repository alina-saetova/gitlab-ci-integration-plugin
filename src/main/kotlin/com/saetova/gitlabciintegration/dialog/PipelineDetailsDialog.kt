package com.saetova.gitlabciintegration.dialog

import com.intellij.ide.BrowserUtil
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.layout.panel
import com.saetova.gitlabciintegration.data.model.PipelineDetailsInfo
import com.saetova.gitlabciintegration.data.model.PipelineStatus
import com.saetova.gitlabciintegration.data.repository.GitlabRepository
import com.saetova.gitlabciintegration.listener.TriggerRefreshPipelinesListener
import com.saetova.gitlabciintegration.utils.getImageIcon
import com.saetova.gitlabciintegration.utils.getLinkTextColor
import java.awt.Cursor
import java.awt.Image
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.net.URL
import javax.imageio.ImageIO
import javax.swing.ImageIcon
import javax.swing.JButton
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JTextArea

class PipelineDetailsDialog(
    val project: Project,
    val pipelineId: String
) : DialogWrapper(project, true) {

    companion object {
        private const val PREFERRED_WIDTH = 700
        private const val PREFERRED_HEIGHT = 250
        private const val USER_AVATAR_SIZE = 50
        private const val DIALOG_TITLE = "Pipeline Details"
        private const val PIPELINE_ID_TEXT_AREA_TITLE = "Pipeline ID: "
        private const val STATUS_TEXT_AREA_TITLE = "Status: "
        private const val SHA_COMMIT_TEXT_AREA_TITLE = "SHA commit: "
        private const val REF_TEXT_AREA_TITLE = "Ref: "
        private const val CREATED_TEXT_AREA_TITLE = "Created: "
        private const val UPDATED_TEXT_AREA_TITLE = "Updated: "
        private const val TRIGGERER_TEXT_AREA_TITLE = "Triggerer: "
        private const val URL_TEXT_AREA_TITLE = "Url: "
        private const val DELETE_BUTTON_TITLE = "Delete"
        private const val RETRY_BUTTON_TITLE = "Retry"
        private const val STOP_BUTTON_TITLE = "Stop"
    }

    private val gitlabRepository = GitlabRepository(project)

    private val pipelineIdTextArea by lazy { JTextArea().apply { isEditable = false } }
    private val statusLabel by lazy { JLabel() }
    private val shaCommitTextArea by lazy { JTextArea().apply { isEditable = false } }
    private val refTextArea by lazy { JTextArea().apply { isEditable = false } }
    private val createdTextArea by lazy { JTextArea().apply { isEditable = false } }
    private val updatedTextArea by lazy { JTextArea().apply { isEditable = false } }
    private val triggererTextArea by lazy { JTextArea().apply { isEditable = false } }
    private val triggererLabel by lazy { JLabel() }
    private val urlTextArea by lazy {
        JTextArea().apply {
            foreground = getLinkTextColor()
            cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
            addMouseListener(object : MouseAdapter() {
                override fun mouseClicked(e: MouseEvent?) {
                    BrowserUtil.browse(text)
                }
            })
            isEditable = false
        }
    }
    private val deleteButton by lazy {
        JButton(DELETE_BUTTON_TITLE).apply {
            addActionListener { deletePipeline() }
        }
    }
    private val retryButton by lazy {
        JButton(RETRY_BUTTON_TITLE).apply {
            addActionListener { retryPipeline() }
            isVisible = false
        }
    }
    private val stopButton by lazy {
        JButton(STOP_BUTTON_TITLE).apply {
            addActionListener { stopPipeline() }
            isVisible = false
        }
    }

    init {
        title = DIALOG_TITLE
        init()
        loadPipeline()
    }

    override fun createCenterPanel(): JComponent {
        return panel {
            row(PIPELINE_ID_TEXT_AREA_TITLE) { pipelineIdTextArea.invoke() }
            row(STATUS_TEXT_AREA_TITLE) { statusLabel.invoke() }
            row(SHA_COMMIT_TEXT_AREA_TITLE) { shaCommitTextArea.invoke() }
            row(REF_TEXT_AREA_TITLE) { refTextArea.invoke() }
            row(CREATED_TEXT_AREA_TITLE) { createdTextArea.invoke() }
            row(UPDATED_TEXT_AREA_TITLE) { updatedTextArea.invoke() }
            row(TRIGGERER_TEXT_AREA_TITLE) {
                triggererTextArea.invoke()
                triggererLabel.invoke()
            }
            row(URL_TEXT_AREA_TITLE) { urlTextArea.invoke() }
            row {
                cell {
                    deleteButton.invoke()
                    retryButton.invoke()
                    stopButton.invoke()
                }
            }
        }.withPreferredSize(PREFERRED_WIDTH, PREFERRED_HEIGHT)
    }

    private fun loadPipeline() {
        gitlabRepository.getPipelineDetails(pipelineId.toInt()) {
            handleGetPipelineResponse(it)
        }
    }

    private fun handleGetPipelineResponse(result: PipelineDetailsInfo?) {
        if (result == null) return
        pipelineIdTextArea.text = result.id
        statusLabel.text = result.status.name
        statusLabel.icon = getImageIcon(result.status.imageName)
        shaCommitTextArea.text = result.sha
        refTextArea.text = result.ref
        createdTextArea.text = result.created
        updatedTextArea.text = result.updated
        triggererTextArea.text = result.triggererName
        triggererLabel.icon = ImageIcon(
            ImageIO.read(URL(result.triggererAvatarUrl))
                .getScaledInstance(USER_AVATAR_SIZE, USER_AVATAR_SIZE, Image.SCALE_SMOOTH)
        )
        urlTextArea.text = result.url

        if (result.status == PipelineStatus.CANCELED || result.status == PipelineStatus.FAILED) {
            retryButton.isVisible = true
        }
        if (result.status == PipelineStatus.PENDING || result.status == PipelineStatus.RUNNING) {
            stopButton.isVisible = true
        }
    }

    private fun deletePipeline() {
        gitlabRepository.deletePipeline(pipelineId.toInt()) {
            project.messageBus.syncPublisher(TriggerRefreshPipelinesListener.REFRESH).refresh()
            doOKAction()
        }
    }

    private fun retryPipeline() {
        gitlabRepository.retryPipeline(pipelineId.toInt()) {
            project.messageBus.syncPublisher(TriggerRefreshPipelinesListener.REFRESH).refresh()
            doOKAction()
        }
    }

    private fun stopPipeline() {
        gitlabRepository.cancelPipeline(pipelineId.toInt()) {
            project.messageBus.syncPublisher(TriggerRefreshPipelinesListener.REFRESH).refresh()
            doOKAction()
        }
    }
}