package com.saetova.gitlabciintegration.dialog

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.layout.panel
import com.saetova.gitlabciintegration.data.repository.GitlabRepository
import com.saetova.gitlabciintegration.listener.LoadBranchesListener
import com.saetova.gitlabciintegration.listener.LoadTagsListener
import com.saetova.gitlabciintegration.listener.TriggerRefreshPipelinesListener
import javax.swing.DefaultComboBoxModel
import javax.swing.JComponent

class CreatePipelineDialog(
    val project: Project
) : DialogWrapper(project, true) {

    companion object {
        private const val DIALOG_TITLE = "Create New Pipeline"
        private const val REF_TEXT_FIELD_TITLE = "Choose branch or tag: "
        private const val DIALOG_PREFERRED_WIDTH = 350
    }

    private val gitlabRepository = GitlabRepository(project)

    private var refModel = DefaultComboBoxModel(emptyArray<String>())

    init {
        title = DIALOG_TITLE
        init()
        subscribeToMessageBus()
    }

    override fun doOKAction() {
        val ref = refModel.selectedItem.toString()
        super.doOKAction()
        gitlabRepository.createNewPipeline(ref) {
            project.messageBus.syncPublisher(TriggerRefreshPipelinesListener.REFRESH).refresh()
        }
    }

    override fun createCenterPanel(): JComponent {
        gitlabRepository.loadBranches()
        gitlabRepository.loadTags()

        return panel {
            row(REF_TEXT_FIELD_TITLE) {
                comboBox(
                    model = refModel,
                    getter = { "" }, setter = {}
                )
            }
        }.withPreferredWidth(DIALOG_PREFERRED_WIDTH)
    }

    private fun subscribeToMessageBus() {
        project.messageBus.connect().subscribe(LoadBranchesListener.LOAD, object : LoadBranchesListener {
            override fun load(branches: List<String>) {
                branches.forEach { refModel.addElement(it) }
            }
        })
        project.messageBus.connect().subscribe(LoadTagsListener.LOAD, object : LoadTagsListener {
            override fun load(tags: List<String>) {
                tags.forEach { refModel.addElement(it) }
            }
        })
    }
}