package com.saetova.gitlabciintegration.wizard.step.state

import com.saetova.gitlabciintegration.data.model.StageTemplate
import com.saetova.gitlabciintegration.wizard.base.WizardStepState

data class ChooseStageTemplateState(
    val stageTemplate: StageTemplate
) : WizardStepState