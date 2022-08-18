package com.saetova.gitlabciintegration.wizard.step.ui

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.ComboBox
import com.intellij.ui.components.JBTextField
import com.intellij.ui.layout.*
import com.saetova.gitlabciintegration.data.model.Image
import com.saetova.gitlabciintegration.utils.logInfo
import com.saetova.gitlabciintegration.utils.dummyNullableTextBinding
import com.saetova.gitlabciintegration.utils.dummyTextBinding
import com.saetova.gitlabciintegration.wizard.base.WizardStepState
import com.saetova.gitlabciintegration.wizard.base.WizardStepUIBuilder
import com.saetova.gitlabciintegration.wizard.step.state.ChooseImageStepState
import javax.swing.DefaultComboBoxModel
import javax.swing.JComponent
import javax.swing.JRadioButton
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener

class ChooseImageStepUIBuilder(
    val project: Project
) : WizardStepUIBuilder {

    companion object {
        const val WINDOW_WIDTH = 400
        const val WINDOW_HEIGHT = 400
        const val VERSION_LABEL_TEXT = "Build tools version"
        const val NAME_LABEL_TEXT = "Name"
        const val SHARED_RUNNER_LABEL_TEXT = "Public shared runner"
    }

    private var imageModel = DefaultComboBoxModel(Image.getList())
    private var versionOpenJDKModel = DefaultComboBoxModel(Image.OPENJDK.imageInfo.buildToolsVersions)
    private var versionSharedModel = DefaultComboBoxModel(Image.OPENJDK.imageInfo.buildToolsVersions)

    private var selectedImage: Image? = null
    private var selectedVersion: String? = null
    private var customImage: String? = null

    lateinit var customTextField: JBTextField

    override fun getStepState(): WizardStepState {
        return ChooseImageStepState(
            image = selectedImage,
            buildToolsVersion = selectedVersion,
            customImage = customImage
        )
    }

    override fun build(): JComponent {
        return panel {
            buttonGroup {
                getOpenJDKSelection()
                getSharedRunnersSelection()
                getCustomRunnerSelection()
            }
        }.withPreferredSize(WINDOW_WIDTH, WINDOW_HEIGHT)
    }

    private fun LayoutBuilder.getOpenJDKSelection() {
        row {
            val versionComboBox = getVersionComboBox(versionOpenJDKModel)

            JRadioButton(Image.OPENJDK.name)
                .apply {
                    addChangeListener {
                        versionComboBox.isEnabled = isSelected
                        selectedImage = Image.OPENJDK
                        selectedVersion = versionOpenJDKModel.selectedItem as? String
                        customImage = null
                        project.logInfo("Selected image name=$selectedImage, selected build tools version=$selectedVersion")
                    }
                    isSelected = true
                    invoke()
                }

            row {
                label(VERSION_LABEL_TEXT)
                versionComboBox.invoke()
            }
        }
    }

    private fun LayoutBuilder.getSharedRunnersSelection() {
        row {
            val imageNameComboBox = comboBox(
                model = imageModel,
                getter = { Image.OPENJDK }, setter = {}
            ).component.also {
                it.isEnabled = false
                it.addActionListener { _ ->
                    selectedImage = it.selectedItem as Image
                    versionSharedModel.removeAllElements()
                    selectedImage?.imageInfo?.buildToolsVersions?.forEach { item -> versionSharedModel.addElement(item) }
                }
            }

            val versionComboBox = getVersionComboBox(versionSharedModel)

            JRadioButton(SHARED_RUNNER_LABEL_TEXT)
                .apply {
                    addChangeListener {
                        imageNameComboBox.isEnabled = isSelected
                        versionComboBox.isEnabled = isSelected

                        selectedImage = imageModel.selectedItem as? Image ?: Image.OPENJDK
                        selectedVersion = versionSharedModel.selectedItem as? String
                        customImage = null
                        project.logInfo("Selected image name=$selectedImage, selected build tools version=$selectedVersion")
                    }
                    invoke()
                }

            row {
                label(NAME_LABEL_TEXT)
                imageNameComboBox.invoke()
                label(VERSION_LABEL_TEXT)
                versionComboBox.invoke()
            }
        }
    }

    private fun LayoutBuilder.getCustomRunnerSelection() {
        row {
            customTextField = textField(dummyTextBinding)
                .component.apply {
                    isEnabled = false
                    document.addDocumentListener(textListener)
                }

            JRadioButton(Image.CUSTOM.name)
                .apply {
                    addChangeListener {
                        customTextField.isEnabled = isSelected
                        selectedImage = Image.CUSTOM
                        customImage = customTextField.text
                        project.logInfo("Selected image name=$selectedImage, selected build tools version=$selectedVersion")
                    }
                    invoke()
                }

            row {
                label(NAME_LABEL_TEXT)
                customTextField.invoke()
            }
        }
    }

    private fun Row.getVersionComboBox(model: DefaultComboBoxModel<String>): ComboBox<String> {
        return comboBox(
            model = model,
            modelBinding = dummyNullableTextBinding
        ).component.also {
            it.isEnabled = false
            it.addActionListener { _ ->
                selectedVersion = it.selectedItem as? String
            }
        }
    }

    private val textListener = object : DocumentListener {
        override fun insertUpdate(e: DocumentEvent?) {
            customImage = customTextField.text
        }

        override fun removeUpdate(e: DocumentEvent?) {
            customImage = customTextField.text
        }

        override fun changedUpdate(e: DocumentEvent?) {
            customImage = customTextField.text
        }
    }
}
