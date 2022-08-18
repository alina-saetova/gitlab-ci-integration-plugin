package com.saetova.gitlabciintegration.wizard

import com.intellij.openapi.project.Project
import com.intellij.ui.wizard.WizardModel
import com.saetova.gitlabciintegration.data.model.StageTemplate
import com.saetova.gitlabciintegration.utils.logError
import com.saetova.gitlabciintegration.wizard.base.WizardStepState
import com.saetova.gitlabciintegration.wizard.step.ChooseImageStep
import com.saetova.gitlabciintegration.wizard.step.ChooseStageTemplateStep
import com.saetova.gitlabciintegration.wizard.step.ConfigureCacheStep
import com.saetova.gitlabciintegration.wizard.step.LintStageStep
import com.saetova.gitlabciintegration.wizard.step.BuildStageStep
import com.saetova.gitlabciintegration.wizard.step.FinishStep
import com.saetova.gitlabciintegration.wizard.step.TestStageStep

class CreatingWizardModel(
    val project: Project,
    stepStateListener: (WizardStepState) -> Unit
) : WizardModel("Create Gitlab CI configuration") {

    private var finishStep = FinishStep()

    private val onStageTemplateChose: (StageTemplate) -> Unit = {
        project.logError(it.name)
        val lintStage = LintStageStep(project, this, stepStateListener)
        val buildStage = BuildStageStep(project, this, stepStateListener)
        addAfter(currentStep, lintStage)
        addAfter(lintStage, buildStage)
        when (it) {
            StageTemplate.BASIC -> {
                // nothing
            }
            StageTemplate.STANDARD -> {
                // also add test stage
                addAfter(buildStage, TestStageStep(project, this, stepStateListener))
            }
            StageTemplate.PLUS -> {
                // add test stage
                addAfter(buildStage, TestStageStep(project, this, stepStateListener))
                // TODO also add other stages
            }
        }
    }

    init {
        add(ChooseImageStep(project, this, stepStateListener))
        add(ConfigureCacheStep(project, this, stepStateListener))
        add(ChooseStageTemplateStep(project, this, stepStateListener, onStageTemplateChose))
        add(finishStep)
    }
}