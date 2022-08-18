package com.saetova.gitlabciintegration.wizard.step

import com.intellij.openapi.project.Project
import com.intellij.ui.wizard.WizardStep
import com.saetova.gitlabciintegration.data.model.StageTemplate
import com.saetova.gitlabciintegration.wizard.CreatingWizardModel
import com.saetova.gitlabciintegration.wizard.base.BaseWizardStep
import com.saetova.gitlabciintegration.wizard.base.WizardStepState
import com.saetova.gitlabciintegration.wizard.step.state.ChooseStageTemplateState
import com.saetova.gitlabciintegration.wizard.step.ui.ChooseStageTemplateStepUIBuilder

class ChooseStageTemplateStep(
    project: Project,
    model: CreatingWizardModel,
    stepStateListener: (WizardStepState) -> Unit,
    private val onStageTemplateChose: (StageTemplate) -> Unit
) : BaseWizardStep<CreatingWizardModel, ChooseStageTemplateStepUIBuilder, ChooseStageTemplateState>(model, project, stepStateListener, STEP_TITLE) {

    companion object {
        const val STEP_TITLE = "Choose stage template"
    }

    override fun getViewBuilder(): ChooseStageTemplateStepUIBuilder = ChooseStageTemplateStepUIBuilder(project)

    override fun onNext(model: CreatingWizardModel): WizardStep<*> {
        val state = uiBuilder.getStepState() as ChooseStageTemplateState
        onStageTemplateChose.invoke(state.stageTemplate)
        return super.onNext(model)
    }
}