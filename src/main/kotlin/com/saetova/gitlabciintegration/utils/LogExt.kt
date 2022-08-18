package com.saetova.gitlabciintegration.utils

import com.intellij.openapi.project.Project
import com.saetova.gitlabciintegration.service.LoggerService

fun Project.logError(message: String) {
    LoggerService.getInstance(this).error(message)
}

fun Project.logInfo(message: String) {
    LoggerService.getInstance(this).info(message)
}