package com.saetova.gitlabciintegration.settings.configurable

import com.intellij.openapi.options.Configurable
import com.intellij.openapi.options.ConfigurableProvider

class GitlabInfoConfigurableProvider : ConfigurableProvider() {

    override fun createConfigurable(): Configurable = GitlabInfoConfigurable()
}
