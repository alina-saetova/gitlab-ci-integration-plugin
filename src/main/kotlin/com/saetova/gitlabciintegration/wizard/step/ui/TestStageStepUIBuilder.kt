package com.saetova.gitlabciintegration.wizard.step.ui

import com.intellij.openapi.project.Project
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.layout.LayoutBuilder
import com.intellij.ui.layout.Row
import com.intellij.ui.layout.panel
import com.saetova.gitlabciintegration.data.model.ExpireInJob
import com.saetova.gitlabciintegration.data.model.OnlyJob
import com.saetova.gitlabciintegration.data.model.WhenJob
import com.saetova.gitlabciintegration.wizard.base.WizardStepState
import com.saetova.gitlabciintegration.wizard.base.WizardStepUIBuilder
import com.saetova.gitlabciintegration.wizard.step.state.TestStageStepState
import com.saetova.gitlabciintegration.wizard.step.ui.common.getExpireInComboBox
import com.saetova.gitlabciintegration.wizard.step.ui.common.getOnlyComboBox
import com.saetova.gitlabciintegration.wizard.step.ui.common.getWhenComboBox
import javax.swing.DefaultComboBoxModel
import javax.swing.JComponent
import javax.swing.JRadioButton

class TestStageStepUIBuilder(
    val project: Project
) : WizardStepUIBuilder {

    companion object {
        private const val TITLED_ROW_RUNNING_TEXT = "Running"
        private const val TITLED_ROW_CACHING_TEXT = "Configure artifact"
        private const val COMBO_BOX_WHEN_RUNNING_LABEL_TEXT = "Run when"
        private const val COMBO_BOX_WHEN_CACHING_LABEL_TEXT = "Create when"
        private const val COMBO_BOX_EXPIRE_IN_CACHING_LABEL_TEXT = "Artifact expire in"
        private const val COMBO_BOX_ONLY_LABEL_TEXT = "Only on"
        private const val RADIO_BUTTON_ENABLE_TEXT = "Enable"
        private const val RADIO_BUTTON_DISABLE_TEXT = "Disable"
        private const val CHECKBOX_ENABLE_REPORT_TEXT = "Enable report in merge request"
    }

    private var isArtifactEnabled: Boolean = false
    private var isReportEnabled: Boolean = false

    private var whenRunningModel = DefaultComboBoxModel(WhenJob.values())
    private var onlyModel = DefaultComboBoxModel(OnlyJob.values())
    private var whenArtifactModel = DefaultComboBoxModel(WhenJob.values())
    private var expireInModel = DefaultComboBoxModel(ExpireInJob.values())

    override fun getStepState(): WizardStepState {
        return TestStageStepState(
            whenRunning = whenRunningModel.selectedItem as? WhenJob ?: WhenJob.ALWAYS,
            onlyRunning = onlyModel.selectedItem as? OnlyJob ?: OnlyJob.BRANCHES,
            isArtifactEnabled = this.isArtifactEnabled,
            whenArtifact = whenArtifactModel.selectedItem as? WhenJob,
            expireInArtifact = expireInModel.selectedItem as? ExpireInJob,
            isReportEnabled = this.isReportEnabled
        )
    }

    override fun build(): JComponent {
        return panel {
            getRunningOptionsSection()
            getCachingSection()
        }
    }

    private fun LayoutBuilder.getRunningOptionsSection() {
        titledRow(TITLED_ROW_RUNNING_TEXT) {
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

    private fun LayoutBuilder.getCachingSection() {
        titledRow(TITLED_ROW_CACHING_TEXT) {
            buttonGroup {
                val whenComboBox = getWhenComboBox(whenArtifactModel)
                val expireInComboBox = getExpireInComboBox(expireInModel)
                val enableRadioButton = JRadioButton(RADIO_BUTTON_ENABLE_TEXT, false).apply {
                    addChangeListener {
                        isArtifactEnabled = isSelected
                        whenComboBox.isEnabled = isSelected
                        expireInComboBox.isEnabled = isSelected
                    }
                }
                val enableReportCheckbox = getEnableReportCheckbox()
                row {
                    JRadioButton(RADIO_BUTTON_DISABLE_TEXT, true).invoke()
                }
                row {
                    enableRadioButton.invoke()
                    row(label = COMBO_BOX_WHEN_CACHING_LABEL_TEXT) {
                        whenComboBox.invoke()
                    }
                    row(label = COMBO_BOX_EXPIRE_IN_CACHING_LABEL_TEXT) {
                        expireInComboBox.invoke()
                    }
                    row {
                        enableReportCheckbox.invoke()
                    }
                }
            }
        }
    }

    private fun Row.getEnableReportCheckbox(): JBCheckBox {
        return JBCheckBox(
            CHECKBOX_ENABLE_REPORT_TEXT,
            false
        ).apply {
            addActionListener {
                isReportEnabled = isSelected
            }
        }
    }
}