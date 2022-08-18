package com.saetova.gitlabciintegration.wizard.step

import com.intellij.openapi.project.Project
import com.saetova.gitlabciintegration.wizard.CreatingWizardModel
import com.saetova.gitlabciintegration.wizard.base.BaseWizardStep
import com.saetova.gitlabciintegration.wizard.base.WizardStepState
import com.saetova.gitlabciintegration.wizard.step.state.LintStageStepState
import com.saetova.gitlabciintegration.wizard.step.ui.LintStageStepUIBuilder

class LintStageStep(
    project: Project,
    model: CreatingWizardModel,
    stepStateListener: (WizardStepState) -> Unit
) : BaseWizardStep<CreatingWizardModel, LintStageStepUIBuilder, LintStageStepState>(model, project, stepStateListener, STEP_TITLE) {

    companion object {
        const val STEP_TITLE = "Configure Lint stage"
    }

    override fun getViewBuilder(): LintStageStepUIBuilder = LintStageStepUIBuilder(project)
}