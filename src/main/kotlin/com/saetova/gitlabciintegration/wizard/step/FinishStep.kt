package com.saetova.gitlabciintegration.wizard.step

import com.intellij.ui.layout.panel
import com.intellij.ui.wizard.WizardNavigationState
import com.intellij.ui.wizard.WizardStep
import com.saetova.gitlabciintegration.wizard.CreatingWizardModel
import javax.swing.JComponent

class FinishStep : WizardStep<CreatingWizardModel>() {

    override fun prepare(state: WizardNavigationState?): JComponent {
        return panel {
            row("Everything is ready! " +
                    "\nConfig file .gitlab-ci.yml will be generated in the root of your project") {}
        }
    }
}