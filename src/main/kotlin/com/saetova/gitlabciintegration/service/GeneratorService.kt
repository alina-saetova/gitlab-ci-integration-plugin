package com.saetova.gitlabciintegration.service

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.saetova.gitlabciintegration.data.model.CacheKeyChoice
import com.saetova.gitlabciintegration.data.model.Image
import com.saetova.gitlabciintegration.data.model.StageTemplate
import com.saetova.gitlabciintegration.wizard.base.WizardStepState
import com.saetova.gitlabciintegration.wizard.step.state.ChooseImageStepState
import com.saetova.gitlabciintegration.wizard.step.state.ConfigureCacheStepState
import com.saetova.gitlabciintegration.wizard.step.state.ChooseStageTemplateState
import com.saetova.gitlabciintegration.wizard.step.state.LintStageStepState
import com.saetova.gitlabciintegration.wizard.step.state.BuildStageStepState
import freemarker.template.Configuration
import freemarker.template.Template
import freemarker.template.TemplateExceptionHandler
import java.io.File
import java.io.FileWriter

@Service
class GeneratorService(
    private val project: Project
) {

    companion object {
        private const val TEMPLATES_PATH = "/Users/alina/IdeaProjects/GitlabCIIntegration/src/main/resources/"
        private const val TEMPLATE_NAME = "template.ftl"
        private const val GENERATED_FILE_NAME = ".gitlab-ci.yml"
        private const val ROOT_KEY_IMAGE = "image"
        private const val CONFIGURATION_ENCODING = "UTF-8"
        private const val ROOT_KEY_CACHE_STEP_CACHE_ENABLED = "cacheEnabled"
        private const val ROOT_KEY_CACHE_STEP_GRADLE_FILES = "gradleFilesCached"
        private const val ROOT_KEY_CACHE_STEP_CUSTOM_FILES_CACHED = "customFilesCached"
        private const val ROOT_KEY_CACHE_STEP_CUSTOM_FILES_PATH = "cacheCustomFilePath"
        private const val ROOT_KEY_CACHE_STEP_KEY_VALUE = "cacheKeyValue"
        private const val ROOT_KEY_LINT_STEP_LINT = "lint"
        private const val ROOT_KEY_RUNNING_WHEN = "runningWhen"
        private const val ROOT_KEY_RUNNING_ONLY = "runningOnly"
        private const val ROOT_KEY_LINT_STEP_CACHING_ENABLED = "cachingEnabled"
        private const val ROOT_KEY_LINT_STEP_CACHING_PATH = "cachingPath"
        private const val ROOT_KEY_LINT_STEP_CACHING_WHEN = "cachingWhen"
        private const val ROOT_KEY_LINT_STEP_CACHING_EXPIRE_IN = "cachingExpireIn"
        private const val ROOT_KEY_BUILD_STEP_ARTIFACT_ENABLED = "artifactEnabled"
        private const val ROOT_KEY_BUILD_STEP_WHEN_ARTIFACT = "whenArtifact"
        private const val ROOT_KEY_BUILD_STEP_ARTIFACT_EXPIRE_IN = "expireInArtifact"
        private const val ROOT_KEY_OPENJDK_COMPILE_SDK = "androidCompileSdk"
        private const val ROOT_KEY_OPENJDK_BUILD_TOOLS = "androidBuildTools"
        private const val ROOT_KEY_OPENJDK_SDK_TOOLS = "androidSdkTools"

        fun getInstance(project: Project): GeneratorService = project.service()
    }

    private val root: MutableMap<String, Any?> = HashMap()
    private lateinit var selectedTemplate: Template
    private lateinit var freeMarkerConfiguration: Configuration

    fun generateCIfile(stepStates: MutableList<WizardStepState>) {
        initConfiguration()
        fillRootForTemplate(stepStates)
        runTemplate()
    }

    private fun initConfiguration() {
        freeMarkerConfiguration = Configuration(Configuration.VERSION_2_3_30)
        freeMarkerConfiguration.apply {
            setDirectoryForTemplateLoading(File(TEMPLATES_PATH))
            defaultEncoding = CONFIGURATION_ENCODING
            templateExceptionHandler = TemplateExceptionHandler.RETHROW_HANDLER
            logTemplateExceptions = false
            wrapUncheckedExceptions = true
            fallbackOnNullLoopVariable = false
        }
    }

    private fun fillRootForTemplate(stepStates: MutableList<WizardStepState>) {
        // TODO sdelat' normalno a ne tak
        handleChooseImageStep(stepStates[0] as ChooseImageStepState)
        handleConfigureCacheStep(stepStates[1] as ConfigureCacheStepState)

        val stageTemplate = (stepStates[2] as ChooseStageTemplateState).stageTemplate
        when (stageTemplate) {
            StageTemplate.BASIC -> {
                handleLintStageStep(stepStates[3] as LintStageStepState)
                handleBuildStageStep(stepStates[4] as BuildStageStepState)
            }
            else -> {
                // TODO to be continued in the next semester
            }
        }
    }

    private fun handleChooseImageStep(state: ChooseImageStepState) {
        val image = when (state.image) {
            Image.CUSTOM -> state.customImage
            Image.OPENJDK, Image.JANGREWE, Image.SEANGHAY, Image.INOVEX -> state.image.imageInfo.imageName
            Image.JAVIERSANTOS, Image.MINGC -> "${state.image.imageInfo.imageName}:${state.buildToolsVersion}"
            else -> null
        }
        root[ROOT_KEY_IMAGE] = image
        if (state.image == Image.OPENJDK) {
            root[ROOT_KEY_OPENJDK_COMPILE_SDK] = state.buildToolsVersion?.substring(0, 2)
            root[ROOT_KEY_OPENJDK_BUILD_TOOLS] = state.buildToolsVersion
            root[ROOT_KEY_OPENJDK_SDK_TOOLS] = "6514223"
        }
    }

    private fun handleConfigureCacheStep(state: ConfigureCacheStepState) {
        root[ROOT_KEY_CACHE_STEP_CACHE_ENABLED] = !state.isCachingDisabled
        if (state.isCachingDisabled) return
        root[ROOT_KEY_CACHE_STEP_GRADLE_FILES] = state.isGradleFilesCached
        root[ROOT_KEY_CACHE_STEP_CUSTOM_FILES_CACHED] = state.isCustomFilesCached
        root[ROOT_KEY_CACHE_STEP_CUSTOM_FILES_PATH] = state.customFilePath
        root[ROOT_KEY_CACHE_STEP_KEY_VALUE] = when (state.cacheKeyChoice) {
            CacheKeyChoice.VARIABLES -> state.selectedCacheVariable?.value
            CacheKeyChoice.CUSTOM -> state.customKey
            else -> null
        }
    }

    private fun handleLintStageStep(state: LintStageStepState) {
        root[ROOT_KEY_LINT_STEP_LINT] = state.lint.name
        root[ROOT_KEY_RUNNING_WHEN] = state.runningWhen.name.toLowerCase()
        root[ROOT_KEY_RUNNING_ONLY] = state.runningOnly.name.toLowerCase()
        root[ROOT_KEY_LINT_STEP_CACHING_ENABLED] = state.isCachingEnable
        if (state.isCachingEnable) {
            root[ROOT_KEY_LINT_STEP_CACHING_PATH] = if (state.cachingPath?.isNotEmpty() == true) {
                state.cachingPath
            } else {
                state.lint.defaultReportPath
            }
            root[ROOT_KEY_LINT_STEP_CACHING_WHEN] = state.cachingWhen?.name?.toLowerCase()
            root[ROOT_KEY_LINT_STEP_CACHING_EXPIRE_IN] = state.runningOnly.name.toLowerCase()
        }

    }

    private fun handleBuildStageStep(state: BuildStageStepState) {
        root[ROOT_KEY_RUNNING_WHEN] = state.whenRunning.name.toLowerCase()
        root[ROOT_KEY_RUNNING_ONLY] = state.onlyRunning.name.toLowerCase()
        root[ROOT_KEY_BUILD_STEP_ARTIFACT_ENABLED] = state.isArtifactEnabled
        if (state.isArtifactEnabled) {
            root[ROOT_KEY_BUILD_STEP_WHEN_ARTIFACT] = state.whenArtifact?.name?.toLowerCase()
            root[ROOT_KEY_BUILD_STEP_ARTIFACT_EXPIRE_IN] = state.expireInArtifact?.name?.toLowerCase()
        }
    }

    private fun runTemplate() {
        selectedTemplate = freeMarkerConfiguration.getTemplate(TEMPLATE_NAME)

        val sourceFile = File(project.basePath, GENERATED_FILE_NAME)
        val writer = FileWriter(sourceFile)

        selectedTemplate.process(root, writer)
    }
}