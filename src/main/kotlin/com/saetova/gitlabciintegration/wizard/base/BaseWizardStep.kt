package com.saetova.gitlabciintegration.wizard.base

import com.intellij.openapi.project.Project
import com.intellij.ui.wizard.WizardModel
import com.intellij.ui.wizard.WizardNavigationState
import com.intellij.ui.wizard.WizardStep
import com.saetova.gitlabciintegration.utils.logError
import javax.swing.JComponent

abstract class BaseWizardStep<WM: WizardModel, WSB: WizardStepUIBuilder, WSS: WizardStepState>(
    protected val model: WM,
    protected val project: Project,
    private val stepStateListener: (WizardStepState) -> Unit,
    title: String
) : WizardStep<WM>(title) {

    abstract fun getViewBuilder(): WSB
    protected lateinit var uiBuilder: WSB

    override fun prepare(state: WizardNavigationState?): JComponent {
        uiBuilder = getViewBuilder()
        return uiBuilder.build()
    }

    @Suppress("UNCHECKED_CAST")
    override fun onNext(model: WM): WizardStep<*> {
        val state = uiBuilder.getStepState() as WSS
        project.logError(state.toString())
        stepStateListener.invoke(state)
        return super.onNext(model)
    }
}