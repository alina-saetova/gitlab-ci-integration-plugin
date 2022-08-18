package com.saetova.gitlabciintegration.data.model

enum class WhenJob(val text: String) {
    ON_SUCCESS("On success"),
    ON_FAILURE("On failure"),
    ALWAYS("Always")
}

enum class OnlyJob(val text: String) {
    MERGE_REQUESTS("On merge requests"),
    BRANCHES("On branches"),
    TAGS("On tags"),
}

enum class ExpireInJob(val text: String) {
    HOUR("1 hour"), DAY("1 day"), WEEK("1 week")
}