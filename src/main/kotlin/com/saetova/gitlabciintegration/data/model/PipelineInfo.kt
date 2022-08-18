package com.saetova.gitlabciintegration.data.model

import java.text.SimpleDateFormat
import org.gitlab4j.api.models.Pipeline

data class PipelineInfo(
    val id: String,
    val status: PipelineStatus,
    val ref: String,
    val sha: String,
    val timeString: String,
    val url: String
)

fun Pipeline.mapToPresentation(): PipelineInfo {
    return PipelineInfo(
        id.toString(), status.mapToPresentation(), ref, sha, SimpleDateFormat("HH:mm:ss dd-MM-yyyy").format(updatedAt), webUrl
    )
}

fun List<PipelineInfo>.mapToTableData(): List<List<String>> {
    return map { info ->
        listOf(
            info.status.name,
            info.id,
            info.ref,
            info.sha,
            info.timeString,
            "Double click for details"
        )
    }
}