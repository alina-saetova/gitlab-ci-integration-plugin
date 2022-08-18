package com.saetova.gitlabciintegration.wizard.base

import javax.swing.JComponent

interface WizardStepUIBuilder {

    fun build(): JComponent

    fun getStepState(): WizardStepState
}