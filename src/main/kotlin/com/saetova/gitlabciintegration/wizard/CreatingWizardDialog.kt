package com.saetova.gitlabciintegration.wizard

import com.intellij.openapi.project.Project
import com.intellij.ui.wizard.WizardDialog
import com.saetova.gitlabciintegration.service.GeneratorService
import com.saetova.gitlabciintegration.utils.logInfo
import com.saetova.gitlabciintegration.wizard.base.WizardStepState

class CreatingWizardDialog(
    val project: Project
) : WizardDialog<CreatingWizardModel>(project, true, CreatingWizardModel(project) { state -> stepStates.add(state) } ) {

    companion object {
        var stepStates: MutableList<WizardStepState> = mutableListOf()
    }

    override fun doCancelAction() {
        super.doCancelAction()
        project.logInfo("Cancel action performed")
    }

    override fun onWizardGoalAchieved() {
        super.onWizardGoalAchieved()
        project.logInfo(stepStates.toString())
        GeneratorService.getInstance(project).generateCIfile(stepStates)
    }
}