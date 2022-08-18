package com.saetova.gitlabciintegration.wizard.step

import com.intellij.openapi.project.Project
import com.saetova.gitlabciintegration.wizard.CreatingWizardModel
import com.saetova.gitlabciintegration.wizard.base.BaseWizardStep
import com.saetova.gitlabciintegration.wizard.base.WizardStepState
import com.saetova.gitlabciintegration.wizard.step.state.ChooseImageStepState
import com.saetova.gitlabciintegration.wizard.step.ui.ChooseImageStepUIBuilder

class ChooseImageStep(
    project: Project,
    model: CreatingWizardModel,
    stepStateListener: (WizardStepState) -> Unit
) : BaseWizardStep<CreatingWizardModel, ChooseImageStepUIBuilder, ChooseImageStepState>(model, project, stepStateListener, STEP_TITLE) {

    companion object {
        const val STEP_TITLE = "Choose Image for running pipeline"
    }

    override fun getViewBuilder(): ChooseImageStepUIBuilder = ChooseImageStepUIBuilder(project)
}