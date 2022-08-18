package com.saetova.gitlabciintegration.utils

import com.intellij.openapi.module.Module
import com.intellij.openapi.module.ModuleManager
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.intellij.psi.search.FilenameIndex
import java.util.logging.Logger

const val BUILD_GRADLE_FILE_NAME = "build.gradle"
const val GRADLE_KEYWORD_APPLY = "apply"
const val GRADLE_KEYWORD_PLUGIN = "plugin"
const val PLUGIN_ANDROID_APP = "com.android.application"

fun Project.getRootModule(): Module {
    return ModuleManager.getInstance(this).modules.toList().first { it.name == this.name }
}

fun Module.isAppModule(): Boolean {
    return findPsiFileByName(BUILD_GRADLE_FILE_NAME)?.let { buildGradlePsiFile ->
        Logger.getAnonymousLogger().warning(buildGradlePsiFile.toString())
        Logger.getAnonymousLogger().warning("AAAAAAAAAAAA")
        return buildGradlePsiFile.children
            .filter { it.text.contains(GRADLE_KEYWORD_APPLY) && it.text.contains(GRADLE_KEYWORD_PLUGIN) }
            .any { it.text.contains(PLUGIN_ANDROID_APP) }
    } ?: false
}

fun Module.findPsiFileByName(name: String): PsiFile? {
    return FilenameIndex.getFilesByName(project, name, moduleContentScope).firstOrNull()
}