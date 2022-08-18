package com.saetova.gitlabciintegration.wizard.step.state

import com.saetova.gitlabciintegration.data.model.Image
import com.saetova.gitlabciintegration.wizard.base.WizardStepState

data class ChooseImageStepState(
    val image: Image? = null,
    val buildToolsVersion: String? = null,
    val customImage: String? = null
) : WizardStepState