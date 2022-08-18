package com.saetova.gitlabciintegration.utils

import com.intellij.ui.layout.PropertyBinding

val dummyTextBinding: PropertyBinding<String> = PropertyBinding({ "" }, {})

val dummyNullableTextBinding: PropertyBinding<String?> = PropertyBinding({ "" }, {})