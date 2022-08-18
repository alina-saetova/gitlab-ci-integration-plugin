package com.saetova.gitlabciintegration.wizard.step.ui

import com.intellij.openapi.project.Project
import com.intellij.ui.components.JBTextField
import com.intellij.ui.layout.LayoutBuilder
import com.intellij.ui.layout.Row
import com.intellij.ui.layout.panel
import com.saetova.gitlabciintegration.data.model.*
import com.saetova.gitlabciintegration.utils.dummyTextBinding
import com.saetova.gitlabciintegration.wizard.base.WizardStepState
import com.saetova.gitlabciintegration.wizard.base.WizardStepUIBuilder
import com.saetova.gitlabciintegration.wizard.step.state.LintStageStepState
import com.saetova.gitlabciintegration.wizard.step.ui.common.getExpireInComboBox
import com.saetova.gitlabciintegration.wizard.step.ui.common.getOnlyComboBox
import com.saetova.gitlabciintegration.wizard.step.ui.common.getWhenComboBox
import java.awt.Dimension
import javax.swing.DefaultComboBoxModel
import javax.swing.JComponent
import javax.swing.JRadioButton

class LintStageStepUIBuilder(
    val project: Project
) : WizardStepUIBuilder {

    companion object {
        private const val TITLED_ROW_LINT_TEXT = "Choose lint"
        private const val TITLED_ROW_RUNNING_TEXT = "Running"
        private const val TITLED_ROW_CACHING_TEXT = "Configure artifact"
        private const val COMBO_BOX_WHEN_RUNNING_LABEL_TEXT = "Run when"
        private const val COMBO_BOX_WHEN_CACHING_LABEL_TEXT = "Create when"
        private const val COMBO_BOX_EXPIRE_IN_CACHING_LABEL_TEXT = "Artifact expire in"
        private const val COMBO_BOX_ONLY_LABEL_TEXT = "Only on"
        private const val RADIO_BUTTON_ENABLE_TEXT = "Enable"
        private const val RADIO_BUTTON_DISABLE_TEXT = "Disable"
        private const val TEXTFIELD_WIDTH = 200
        private const val TEXTFIELD_HEIGHT = 50
        private const val TEXTFIELD_LABEL_TEXT = "Lint report path"
    }

    private lateinit var cachePathTextField: JBTextField

    private var selectedLint = LintChoice.ANDROIDLINT
    private var isCachingEnabled: Boolean = false
    private var whenRunningModel = DefaultComboBoxModel(WhenJob.values())
    private var whenCachingModel = DefaultComboBoxModel(WhenJob.values())
    private var expireInModel = DefaultComboBoxModel(ExpireInJob.values())
    private var onlyModel = DefaultComboBoxModel(OnlyJob.values())

    override fun getStepState(): WizardStepState {
        return LintStageStepState(
            lint = selectedLint,
            runningWhen = whenRunningModel.selectedItem as? WhenJob ?: WhenJob.ALWAYS,
            runningOnly = onlyModel.selectedItem as? OnlyJob ?: OnlyJob.BRANCHES,
            isCachingEnable = isCachingEnabled,
            cachingPath = cachePathTextField.text,
            cachingWhen = whenCachingModel.selectedItem as? WhenJob,
            cachingExpireIn = expireInModel.selectedItem as? ExpireInJob
        )
    }

    override fun build(): JComponent {
        return panel {
            getChooseLintSection()
            getRunningOptionsSection()
            getCachingSection()
        }
    }

    private fun LayoutBuilder.getChooseLintSection() {
        titledRow(TITLED_ROW_LINT_TEXT) {
            buttonGroup {
                row { getLintRadioButton(LintChoice.ANDROIDLINT, true).invoke() }
                row { getLintRadioButton(LintChoice.KTLINT).invoke() }
                row { getLintRadioButton(LintChoice.DETEKT).invoke() }
            }
        }
    }

    private fun getLintRadioButton(lint: LintChoice, isSelected: Boolean = false): JRadioButton {
        return JRadioButton(lint.title, isSelected).apply {
            addChangeListener {
                selectedLint = lint
            }
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
                cachePathTextField = getCachePathTextField()
                val whenComboBox = getWhenComboBox(whenCachingModel)
                val expireInComboBox = getExpireInComboBox(expireInModel)
                val enableRadioButton = JRadioButton(RADIO_BUTTON_ENABLE_TEXT, false).apply {
                    addChangeListener {
                        isCachingEnabled = isSelected
                        whenComboBox.isEnabled = isSelected
                        expireInComboBox.isEnabled = isSelected
                        cachePathTextField.isEnabled = isSelected
                    }
                }
                row {
                    JRadioButton(RADIO_BUTTON_DISABLE_TEXT, true).invoke()
                }
                row {
                    enableRadioButton.invoke()
                    row(label = TEXTFIELD_LABEL_TEXT) {
                        cachePathTextField.invoke()
                        checkBox(
                            text = "Default path",
                            isSelected = false
                        ).component.also {
                            it.addActionListener { _ ->
                                if (it.isSelected) {
                                    cachePathTextField.text = ""
                                }
                            }
                        }.invoke()
                    }
                    row(label = COMBO_BOX_WHEN_CACHING_LABEL_TEXT) {
                        whenComboBox.invoke()
                    }
                    row(label = COMBO_BOX_EXPIRE_IN_CACHING_LABEL_TEXT) {
                        expireInComboBox.invoke()
                    }
                }
            }
        }
    }

    private fun Row.getCachePathTextField(): JBTextField {
        return textField(dummyTextBinding)
            .component.also {
                it.isEnabled = false
                it.maximumSize = Dimension(TEXTFIELD_WIDTH, TEXTFIELD_HEIGHT)
            }
    }
}