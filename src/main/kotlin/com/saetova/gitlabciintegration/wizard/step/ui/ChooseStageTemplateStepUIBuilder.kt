package com.saetova.gitlabciintegration.wizard.step.ui

import com.intellij.openapi.project.Project
import com.intellij.ui.layout.LayoutBuilder
import com.intellij.ui.layout.panel
import com.saetova.gitlabciintegration.data.model.StageTemplate
import com.saetova.gitlabciintegration.utils.logError
import com.saetova.gitlabciintegration.wizard.base.WizardStepState
import com.saetova.gitlabciintegration.wizard.base.WizardStepUIBuilder
import com.saetova.gitlabciintegration.wizard.step.state.ChooseStageTemplateState
import javax.swing.JComponent

class ChooseStageTemplateStepUIBuilder(
    val project: Project
) : WizardStepUIBuilder {

    companion object {
        private const val BASIC_STAGE_DESCRIPTION = "Stages: lint and build"
        private const val STANDARD_STAGE_DESCRIPTION = "Stages: lint, build and test"
        private const val PLUS_STAGE_DESCRIPTION = "Stages: lint, build, test and custom jobs"
    }

    private var selectedStageTemplate: StageTemplate = StageTemplate.BASIC

    override fun getStepState(): WizardStepState {
        return ChooseStageTemplateState(selectedStageTemplate)
    }

    override fun build(): JComponent {
        return panel {
            buttonGroup {
                getSelection(StageTemplate.BASIC, BASIC_STAGE_DESCRIPTION, true)
                getSelection(StageTemplate.STANDARD, STANDARD_STAGE_DESCRIPTION)
                getSelection(StageTemplate.PLUS, PLUS_STAGE_DESCRIPTION)
            }
        }
    }

    private fun LayoutBuilder.getSelection(stageTemplate: StageTemplate, description: String, isSelected: Boolean = false) {
        row {
            radioButton(stageTemplate.title).component.apply {
                this.isSelected = isSelected
                addChangeListener {
                    selectedStageTemplate = stageTemplate
                    project.logError(selectedStageTemplate.name)

                }
            }.invoke()
            row { commentNoWrap(description) }
        }
    }
}
