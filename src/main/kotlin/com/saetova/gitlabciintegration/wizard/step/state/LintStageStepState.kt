package com.saetova.gitlabciintegration.wizard.step.state

import com.saetova.gitlabciintegration.data.model.ExpireInJob
import com.saetova.gitlabciintegration.data.model.LintChoice
import com.saetova.gitlabciintegration.data.model.OnlyJob
import com.saetova.gitlabciintegration.data.model.WhenJob
import com.saetova.gitlabciintegration.wizard.base.WizardStepState

data class LintStageStepState(
    var lint: LintChoice,
    var runningWhen: WhenJob,
    var runningOnly: OnlyJob,
    var isCachingEnable: Boolean,
    var cachingPath: String? = null,
    var cachingWhen: WhenJob? = null,
    var cachingExpireIn: ExpireInJob? = null
) : WizardStepState