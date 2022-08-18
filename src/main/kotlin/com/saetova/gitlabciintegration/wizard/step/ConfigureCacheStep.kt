package com.saetova.gitlabciintegration.wizard.step

import com.intellij.openapi.project.Project
import com.saetova.gitlabciintegration.wizard.CreatingWizardModel
import com.saetova.gitlabciintegration.wizard.base.BaseWizardStep
import com.saetova.gitlabciintegration.wizard.base.WizardStepState
import com.saetova.gitlabciintegration.wizard.step.state.ConfigureCacheStepState
import com.saetova.gitlabciintegration.wizard.step.ui.ConfigureCacheStepUIBuilder

class ConfigureCacheStep(
    project: Project,
    model: CreatingWizardModel,
    stepStateListener: (WizardStepState) -> Unit
) : BaseWizardStep<CreatingWizardModel, ConfigureCacheStepUIBuilder, ConfigureCacheStepState>(model, project, stepStateListener, STEP_TITLE) {

    companion object {
        const val STEP_TITLE = "Configure cache"
    }

    override fun getViewBuilder(): ConfigureCacheStepUIBuilder = ConfigureCacheStepUIBuilder(project)
}