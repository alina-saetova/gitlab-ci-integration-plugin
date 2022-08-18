package com.saetova.gitlabciintegration.wizard.step

import com.intellij.openapi.project.Project
import com.saetova.gitlabciintegration.wizard.CreatingWizardModel
import com.saetova.gitlabciintegration.wizard.base.BaseWizardStep
import com.saetova.gitlabciintegration.wizard.base.WizardStepState
import com.saetova.gitlabciintegration.wizard.step.state.BuildStageStepState
import com.saetova.gitlabciintegration.wizard.step.ui.BuildStageStepUIBuilder

class BuildStageStep(
    project: Project,
    model: CreatingWizardModel,
    stepStateListener: (WizardStepState) -> Unit
) : BaseWizardStep<CreatingWizardModel, BuildStageStepUIBuilder, BuildStageStepState>(model, project, stepStateListener, STEP_TITLE) {

    companion object {
        const val STEP_TITLE = "Configure Build stage"
    }

    override fun getViewBuilder(): BuildStageStepUIBuilder = BuildStageStepUIBuilder(project)
}