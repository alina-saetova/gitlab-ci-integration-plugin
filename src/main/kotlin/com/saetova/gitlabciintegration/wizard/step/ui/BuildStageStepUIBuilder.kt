package com.saetova.gitlabciintegration.wizard.step.ui

import com.intellij.openapi.project.Project
import com.intellij.ui.layout.LayoutBuilder
import com.intellij.ui.layout.panel
import com.saetova.gitlabciintegration.data.model.ExpireInJob
import com.saetova.gitlabciintegration.data.model.OnlyJob
import com.saetova.gitlabciintegration.data.model.WhenJob
import com.saetova.gitlabciintegration.wizard.base.WizardStepState
import com.saetova.gitlabciintegration.wizard.base.WizardStepUIBuilder
import com.saetova.gitlabciintegration.wizard.step.state.BuildStageStepState
import com.saetova.gitlabciintegration.wizard.step.ui.common.getExpireInComboBox
import com.saetova.gitlabciintegration.wizard.step.ui.common.getOnlyComboBox
import com.saetova.gitlabciintegration.wizard.step.ui.common.getWhenComboBox
import javax.swing.DefaultComboBoxModel
import javax.swing.JComponent
import javax.swing.JRadioButton

class BuildStageStepUIBuilder(
    val project: Project
) : WizardStepUIBuilder {

    companion object {
        private const val TITLED_ROW_ARTIFACT_SECTION = "Configure artifact"
        private const val TITLED_ROW_RUNNING_SECTION = "Running"
        private const val RADIO_BUTTON_ARTIFACT_DISABLED_TITLE = "Disable"
        private const val RADIO_BUTTON_ARTIFACT_ENABLED_TITLE = "Enable"
        private const val COMBO_BOX_EXPIRE_IN_LABEL_TEXT = "Artifact expire in"
        private const val COMBO_BOX_ONLY_LABEL_TEXT = "Only on"
        private const val COMBO_BOX_WHEN_ARTIFACT_LABEL_TEXT = "Creating when"
        private const val COMBO_BOX_WHEN_RUNNING_LABEL_TEXT = "Run when"
    }

    private var whenRunningModel = DefaultComboBoxModel(WhenJob.values())
    private var whenArtifactModel = DefaultComboBoxModel(WhenJob.values())
    private var onlyModel = DefaultComboBoxModel(OnlyJob.values())
    private var expireInModel = DefaultComboBoxModel(ExpireInJob.values())

    private var isArtifactEnabled: Boolean = false

    override fun getStepState(): WizardStepState {
        return BuildStageStepState(
            whenRunning = whenRunningModel.selectedItem as? WhenJob ?: WhenJob.ALWAYS,
            onlyRunning = onlyModel.selectedItem as? OnlyJob ?: OnlyJob.BRANCHES,
            isArtifactEnabled = this.isArtifactEnabled,
            whenArtifact = whenArtifactModel.selectedItem as? WhenJob,
            expireInArtifact = expireInModel.selectedItem as? ExpireInJob
        )
    }

    override fun build(): JComponent {
        return panel {
            row {
                getRunningSection()
                getArtifactSection()
            }
        }
    }

    private fun LayoutBuilder.getRunningSection() {
        titledRow(TITLED_ROW_RUNNING_SECTION) {
            row {
                val whenComboBox = getWhenComboBox(whenRunningModel, true)
                val onlyComboBox = getOnlyComboBox(onlyModel, true)
                row(label = COMBO_BOX_WHEN_RUNNING_LABEL_TEXT) {
                    whenComboBox.invoke()
                }
                row(label = COMBO_BOX_ONLY_LABEL_TEXT) {
                    onlyComboBox.invoke()
                }
            }
        }
    }

    private fun LayoutBuilder.getArtifactSection() {
        titledRow(TITLED_ROW_ARTIFACT_SECTION) {
            buttonGroup {
                row {
                    JRadioButton(RADIO_BUTTON_ARTIFACT_DISABLED_TITLE).apply {
                        isSelected = true
                        invoke()
                    }
                }
                val whenComboBox = getWhenComboBox(whenArtifactModel)
                val expireInComboBox = getExpireInComboBox(expireInModel)
                val enabledRadioButton = JRadioButton(RADIO_BUTTON_ARTIFACT_ENABLED_TITLE).apply {
                    addChangeListener {
                        isArtifactEnabled = this.isSelected
                        whenComboBox.isEnabled = isSelected
                        expireInComboBox.isEnabled = isSelected
                    }
                }
                row {
                    enabledRadioButton.invoke()
                    row(label = COMBO_BOX_WHEN_ARTIFACT_LABEL_TEXT) {
                        whenComboBox.invoke()
                    }
                    row(label = COMBO_BOX_EXPIRE_IN_LABEL_TEXT) {
                        expireInComboBox.invoke()
                    }
                }
            }
        }
    }
}