package com.saetova.gitlabciintegration.data.model

enum class GitlabVariable(val value: String) {
    PER_PIPELINE("\${CI_PIPELINE_ID}"),
    PER_BRANCH("\${CI_COMMIT_BRANCH}"),
    PER_TAG("\${CI_COMMIT_TAG}"),
    PER_PROJECT("\${CI_PROJECT_ID}")
}