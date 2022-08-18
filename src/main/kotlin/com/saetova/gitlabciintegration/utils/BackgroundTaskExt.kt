package com.saetova.gitlabciintegration.utils

import com.intellij.openapi.application.EdtReplacementThread
import com.intellij.openapi.progress.PerformInBackgroundOption
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
import com.intellij.openapi.progress.impl.BackgroundableProcessIndicator
import com.intellij.openapi.project.Project

inline fun <reified T> runBackgroundableTask(
    project: Project,
    taskName: String,
    crossinline task: () -> T,
    crossinline onSuccessAction: (T?) -> Unit,
    crossinline onErrorAction: (Throwable) -> Unit
) {
    var result: T? = null
    val backgroundableTask = object : Task.Backgroundable(
        project,
        taskName,
        false,
        PerformInBackgroundOption.ALWAYS_BACKGROUND
    ) {
        override fun run(indicator: ProgressIndicator) {
            result = task()
        }

        override fun onSuccess() {
            super.onSuccess()
            project.logError("Successful task: $result")
            onSuccessAction(result)
        }

        override fun whereToRunCallbacks(): EdtReplacementThread {
            return EdtReplacementThread.EDT
        }

        override fun onThrowable(error: Throwable) {
            super.onThrowable(error)
            project.logError("Failed task: $error")
            onErrorAction(error)
        }
    }
    val indicator = BackgroundableProcessIndicator(backgroundableTask)
    ProgressManager.getInstance().runProcessWithProgressAsynchronously(backgroundableTask, indicator)
}