package com.saetova.gitlabciintegration.data.model

import java.text.SimpleDateFormat
import org.gitlab4j.api.models.Pipeline

data class PipelineDetailsInfo(
    val id: String,
    val status: PipelineStatus,
    val ref: String,
    val sha: String,
    val created: String,
    val updated: String,
    val triggererName: String,
    val triggererAvatarUrl: String,
    val url: String
)

fun Pipeline.mapToPresentationDetails(): PipelineDetailsInfo {
    return PipelineDetailsInfo(
        id.toString(),
        status.mapToPresentation(),
        ref,
        sha,
        SimpleDateFormat("HH:mm:ss dd-MM-yyyy").format(createdAt),
        SimpleDateFormat("HH:mm:ss dd-MM-yyyy").format(updatedAt),
        user.name,
        user.avatarUrl,
        webUrl
    )
}
