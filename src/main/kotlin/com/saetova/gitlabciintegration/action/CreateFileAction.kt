package com.saetova.gitlabciintegration.action

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.saetova.gitlabciintegration.utils.logInfo
import com.saetova.gitlabciintegration.wizard.CreatingWizardDialog

class CreateFileAction: AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        e.project?.logInfo("Create file action permormed")

        e.project?.let {
            CreatingWizardDialog(it).show()
        }
    }
}