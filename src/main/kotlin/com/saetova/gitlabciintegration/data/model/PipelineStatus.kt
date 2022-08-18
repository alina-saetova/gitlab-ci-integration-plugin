package com.saetova.gitlabciintegration.data.model

import org.gitlab4j.api.models.PipelineStatus as ApiPipelineStatus

enum class PipelineStatus(val imageName: String) {
    RUNNING("status_running.png"),
    PENDING("status_pending.png"),
    SUCCESS("status_success.png"),
    FAILED("status_failed.png"),
    CANCELED("status_canceled.png"),
    SKIPPED("status_skipped.png"),
    MANUAL("status_manual.png")
}

fun ApiPipelineStatus.mapToPresentation(): PipelineStatus {
    return when(this) {
        ApiPipelineStatus.CANCELED -> PipelineStatus.CANCELED
        ApiPipelineStatus.FAILED -> PipelineStatus.FAILED
        ApiPipelineStatus.MANUAL -> PipelineStatus.MANUAL
        ApiPipelineStatus.PENDING -> PipelineStatus.PENDING
        ApiPipelineStatus.RUNNING -> PipelineStatus.RUNNING
        ApiPipelineStatus.SKIPPED -> PipelineStatus.SKIPPED
        ApiPipelineStatus.SUCCESS -> PipelineStatus.SUCCESS
    }
}

fun PipelineStatus.isStartedState(): Boolean {
    return this == PipelineStatus.RUNNING || this == PipelineStatus.PENDING
}