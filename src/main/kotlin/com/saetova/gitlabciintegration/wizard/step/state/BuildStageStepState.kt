package com.saetova.gitlabciintegration.wizard.step.state

import com.saetova.gitlabciintegration.data.model.ExpireInJob
import com.saetova.gitlabciintegration.data.model.OnlyJob
import com.saetova.gitlabciintegration.data.model.WhenJob
import com.saetova.gitlabciintegration.wizard.base.WizardStepState

data class BuildStageStepState(
    val whenRunning: WhenJob,
    val onlyRunning: OnlyJob,
    val isArtifactEnabled: Boolean = false,
    val whenArtifact: WhenJob? = null,
    val expireInArtifact: ExpireInJob? = null
) : WizardStepState