package com.saetova.gitlabciintegration.settings.configurable

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.service
import com.intellij.openapi.options.Configurable
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.components.JBTextField
import com.intellij.ui.layout.Row
import com.intellij.ui.layout.panel
import com.saetova.gitlabciintegration.listener.ConfigChangedListener
import com.saetova.gitlabciintegration.settings.persistence.GitlabInfoComponent
import com.saetova.gitlabciintegration.utils.dummyTextBinding
import java.awt.Dimension
import javax.swing.JComponent

class GitlabInfoConfigurable : Configurable {

    companion object {
        private const val DISPLAY_NAME = "Gitlab CI Integration"
        private const val HOST_URL_TEXT_FIELD_LABEL = "Host Url"
        private const val TOKEN_TEXT_FIELD_LABEL = "Access Token"
        private const val PROJECT_ID_TEXT_FIELD_LABEL = "Project ID"
        private const val IS_NEED_BACKGROUND_UPDATE_LABEL = "Background update of pipelines"
        private const val TEXT_FIELD_WIDTH = 300
        private const val TEXT_FIELD_HEIGHT = 50
    }

    private val gitlabInfo = service<GitlabInfoComponent>()

    private lateinit var hostUrlTextField: JBTextField

    private lateinit var tokenTextField: JBTextField

    private lateinit var projectIdTextField: JBTextField

    private lateinit var isNeedBackgroundUpdateCheckbox: JBCheckBox

    override fun createComponent(): JComponent {
        return panel {
            row(HOST_URL_TEXT_FIELD_LABEL) {
                hostUrlTextField = getTextField().apply { invoke() }
            }
            row(TOKEN_TEXT_FIELD_LABEL) {
                tokenTextField = getTextField().apply { invoke() }
            }
            row(PROJECT_ID_TEXT_FIELD_LABEL) {
                projectIdTextField = getTextField().apply { invoke() }
            }
            row {
                isNeedBackgroundUpdateCheckbox = checkBox(
                    text = IS_NEED_BACKGROUND_UPDATE_LABEL
                ).component
                isNeedBackgroundUpdateCheckbox.invoke()
            }
        }
    }

    override fun isModified(): Boolean = true

    override fun apply() {
        gitlabInfo.hostUrl = hostUrlTextField.text
        gitlabInfo.token = tokenTextField.text
        gitlabInfo.projectId = projectIdTextField.text
        gitlabInfo.isNeedBackgroundUpdate = isNeedBackgroundUpdateCheckbox.isSelected
        ApplicationManager.getApplication().messageBus.syncPublisher(ConfigChangedListener.CONFIG_CHANGED).configChanged()
    }

    override fun reset() {
        hostUrlTextField.text = gitlabInfo.hostUrl
        tokenTextField.text = gitlabInfo.token
        projectIdTextField.text = gitlabInfo.projectId
        isNeedBackgroundUpdateCheckbox.isSelected = gitlabInfo.isNeedBackgroundUpdate
    }

    override fun getDisplayName(): String = DISPLAY_NAME

    private fun Row.getTextField(): JBTextField {
        return textField(dummyTextBinding)
            .component.also {
                it.maximumSize = Dimension(TEXT_FIELD_WIDTH, TEXT_FIELD_HEIGHT)
            }
    }
}
