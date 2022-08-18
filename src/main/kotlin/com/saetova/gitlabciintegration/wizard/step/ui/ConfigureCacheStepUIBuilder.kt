package com.saetova.gitlabciintegration.wizard.step.ui

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.ComboBox
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.components.JBTextField
import com.intellij.ui.layout.LayoutBuilder
import com.intellij.ui.layout.Row
import com.intellij.ui.layout.panel
import com.saetova.gitlabciintegration.data.model.CacheKeyChoice
import com.saetova.gitlabciintegration.data.model.GitlabVariable
import com.saetova.gitlabciintegration.utils.dummyTextBinding
import com.saetova.gitlabciintegration.wizard.base.WizardStepState
import com.saetova.gitlabciintegration.wizard.base.WizardStepUIBuilder
import com.saetova.gitlabciintegration.wizard.step.state.ConfigureCacheStepState
import java.awt.Dimension
import javax.swing.DefaultComboBoxModel
import javax.swing.JComponent
import javax.swing.JRadioButton

class ConfigureCacheStepUIBuilder(
    val project: Project
) : WizardStepUIBuilder {

    companion object {
        private const val CHECKBOX_DISABLE_CACHING_TEXT = "Do not caching files"
        private const val CHECKBOX_GRADLE_TEXT = "Gradle files"
        private const val CHECKBOX_CUSTOM_TEXT = "Custom files"
        private const val ROW_FILES_TEXT = "File paths"
        private const val ROW_CACHE_TEXT = "Cache key"
        private const val TEXTFIELD_CUSTOM_KEY_TEXT = "Path to files (comma-separated)"
        private const val TEXTFIELD_WIDTH = 200
        private const val TEXTFIELD_HEIGHT = 50
    }

    private var isCachingDisabled: Boolean = false
    private var isGradleFilesCached: Boolean = false
    private var isCustomFilesCached: Boolean = false
    private var cacheKeyChoice: CacheKeyChoice = CacheKeyChoice.DEFAULT

    private var variablesModel = DefaultComboBoxModel(GitlabVariable.values())
    private lateinit var customTextField: JBTextField
    private lateinit var customKeyTextField : JBTextField

    override fun getStepState(): WizardStepState {
        return ConfigureCacheStepState(
            isCachingDisabled,
            isGradleFilesCached,
            isCustomFilesCached,
            customTextField.text,
            cacheKeyChoice,
            variablesModel.selectedItem as? GitlabVariable,
            customKeyTextField.text
        )
    }

    override fun build(): JComponent {
        return panel {
            row {
                checkBox(
                    text = CHECKBOX_DISABLE_CACHING_TEXT,
                    isSelected = false
                ).component.also {
                    it.addActionListener { _ ->
                        isCachingDisabled = it.isSelected
                    }
                }
            }
            getCacheFilePathsSection()
            getCacheKeySection()
        }
    }

    private fun LayoutBuilder.getCacheFilePathsSection() {
        titledRow(ROW_FILES_TEXT) {
            val gradleCheckBox = getGradleCheckBox()
            val customFilesCheckBox = getCustomFilesCheckBox()
            customTextField = getCustomFileTextField()

            row {
                gradleCheckBox.invoke()
            }
            row {
                customFilesCheckBox.invoke()
                row(label = TEXTFIELD_CUSTOM_KEY_TEXT) {
                    customTextField.invoke()
                }
            }
        }
    }

    private fun LayoutBuilder.getCacheKeySection() {
        titledRow(ROW_CACHE_TEXT) {
            buttonGroup {
                customKeyTextField = getCustomFileTextField()
                val variablesComboBox = getVariablesComboBox()
                val variableRadioButton = JRadioButton(CacheKeyChoice.VARIABLES.title).apply {
                    addChangeListener {
                        variablesComboBox.isEnabled = isSelected
                        cacheKeyChoice = CacheKeyChoice.VARIABLES
                    }
                }
                row {
                    JRadioButton(CacheKeyChoice.DEFAULT.title).apply {
                        isSelected = true
                        addChangeListener {
                            cacheKeyChoice = CacheKeyChoice.DEFAULT
                        }
                        invoke()
                    }
                }
                row {
                    variableRadioButton.invoke()
                    row {
                        variablesComboBox.invoke()
                    }
                }
                row {
                    JRadioButton(CacheKeyChoice.CUSTOM.title).apply {
                        addChangeListener {
                            customKeyTextField.isEnabled = isSelected
                            cacheKeyChoice = CacheKeyChoice.CUSTOM
                        }
                    }.invoke()
                    row {
                        customKeyTextField.invoke()
                    }
                }
            }
        }
    }

    private fun Row.getGradleCheckBox(): JBCheckBox {
        return checkBox(
            text = CHECKBOX_GRADLE_TEXT,
            isSelected = false
        ).component.also {
            it.addActionListener { _ ->
                isGradleFilesCached = it.isSelected
            }
        }
    }

    private fun Row.getCustomFilesCheckBox(): JBCheckBox {
        return checkBox(
            text = CHECKBOX_CUSTOM_TEXT,
            isSelected = false
        ).component.also {
            it.addActionListener { _ ->
                isCustomFilesCached = it.isSelected
                customTextField.isEnabled = it.isSelected
            }
        }
    }

    private fun Row.getCustomFileTextField(): JBTextField {
        return textField(dummyTextBinding)
            .component.also {
                it.isEnabled = false
                it.maximumSize = Dimension(TEXTFIELD_WIDTH, TEXTFIELD_HEIGHT)
            }
    }

    private fun Row.getVariablesComboBox(): ComboBox<GitlabVariable> {
        return comboBox(
            model = variablesModel,
            getter = { GitlabVariable.PER_BRANCH }, setter = {}
        ).component.also {
            it.isEnabled = false
        }
    }
}