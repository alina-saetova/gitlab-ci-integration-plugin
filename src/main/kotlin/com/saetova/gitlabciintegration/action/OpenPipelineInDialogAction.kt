package com.saetova.gitlabciintegration.action

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.saetova.gitlabciintegration.dialog.PipelineDetailsDialog

class OpenPipelineInDialogAction(): AnAction("Open dialog details") {

    private var pipelineId = ""

    constructor(pipelineId: String): this() {
        this.pipelineId = pipelineId
    }

    override fun actionPerformed(e: AnActionEvent) {
        e.project?.let {
            PipelineDetailsDialog(it, pipelineId).show()
        }
    }
}