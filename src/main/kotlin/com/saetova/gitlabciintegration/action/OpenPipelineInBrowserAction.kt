package com.saetova.gitlabciintegration.action

import com.intellij.ide.BrowserUtil
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

class OpenPipelineInBrowserAction(): AnAction("Open in browser") {

    private var pipelineUrl = ""

    constructor(pipelineUrl: String): this() {
        this.pipelineUrl = pipelineUrl
    }

    override fun actionPerformed(e: AnActionEvent) {
        BrowserUtil.browse(pipelineUrl)
    }
}