package com.saetova.gitlabciintegration.wizard.step

import com.intellij.openapi.project.Project
import com.saetova.gitlabciintegration.wizard.CreatingWizardModel
import com.saetova.gitlabciintegration.wizard.base.BaseWizardStep
import com.saetova.gitlabciintegration.wizard.base.WizardStepState
import com.saetova.gitlabciintegration.wizard.step.state.TestStageStepState
import com.saetova.gitlabciintegration.wizard.step.ui.TestStageStepUIBuilder

class TestStageStep(
    project: Project,
    model: CreatingWizardModel,
    stepStateListener: (WizardStepState) -> Unit
) : BaseWizardStep<CreatingWizardModel, TestStageStepUIBuilder, TestStageStepState>(model, project, stepStateListener, STEP_TITLE) {

    companion object {
        const val STEP_TITLE = "Configure Test stage"
    }

    override fun getViewBuilder(): TestStageStepUIBuilder = TestStageStepUIBuilder(project)
}