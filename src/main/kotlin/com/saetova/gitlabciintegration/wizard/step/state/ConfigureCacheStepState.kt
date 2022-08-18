package com.saetova.gitlabciintegration.wizard.step.state

import com.saetova.gitlabciintegration.data.model.CacheKeyChoice
import com.saetova.gitlabciintegration.data.model.GitlabVariable
import com.saetova.gitlabciintegration.wizard.base.WizardStepState

data class ConfigureCacheStepState(
    var isCachingDisabled: Boolean,
    var isGradleFilesCached: Boolean,
    var isCustomFilesCached: Boolean,
    val customFilePath: String? = null,
    var cacheKeyChoice: CacheKeyChoice,
    var selectedCacheVariable: GitlabVariable? = GitlabVariable.PER_BRANCH,
    var customKey: String? = null
) : WizardStepState