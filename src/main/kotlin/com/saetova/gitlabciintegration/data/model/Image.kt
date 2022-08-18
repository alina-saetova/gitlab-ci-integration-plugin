package com.saetova.gitlabciintegration.data.model

import com.intellij.util.containers.toArray

enum class Image(val imageInfo: ImageInfo = ImageInfo()) {
    OPENJDK(ImageInfo("openjdk:8-jdk", arrayOf("28.0.2", "28.0.3", "29.0.2", "29.0.3", "30.0.2"))),
    CUSTOM,
    JANGREWE(ImageInfo("jangrewe/gitlab-ci-android", arrayOf("30.0.2"))),
    SEANGHAY(ImageInfo("seanghay/android-ci", arrayOf("29.0.2"))),
    JAVIERSANTOS(ImageInfo("javiersantos/android-ci:latest", arrayOf("28.0.2", "28.0.3"))),
    MINGC(ImageInfo("mingc/android-build-box:latest", arrayOf("28.0.3", "29.0.2", "29.0.3", "30.0.0"))),
    INOVEX(ImageInfo("inovex/gitlab-ci-android", arrayOf("28.0.3")));

    companion object {
        fun getList(): Array<Image> {
            val mutableList = values().toMutableList()
            mutableList.removeAt(0)
            mutableList.removeAt(0)
            return mutableList.toArray(emptyArray())
        }
    }
}

data class ImageInfo(
    val imageName: String? = "",
    val buildToolsVersions: Array<String> = arrayOf()
)
