package com.saetova.gitlabciintegration.data.model

enum class LintChoice(val title: String, val defaultReportPath: String) {
    ANDROIDLINT("Standard Android Lint", "\$CI_PROJECT_DIR/**/lint/reports/"),
    KTLINT("Ktlint", "build/reports/checkstyle/"),
    DETEKT("Detekt", "build/reports/detekt/")
}