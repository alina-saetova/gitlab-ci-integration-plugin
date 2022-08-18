package com.saetova.gitlabciintegration.wizard.step.ui.common

import com.intellij.openapi.ui.ComboBox
import com.intellij.ui.layout.Row
import com.saetova.gitlabciintegration.data.model.ExpireInJob
import com.saetova.gitlabciintegration.data.model.OnlyJob
import com.saetova.gitlabciintegration.data.model.WhenJob
import javax.swing.DefaultComboBoxModel

fun Row.getWhenComboBox(model: DefaultComboBoxModel<WhenJob>, isEnabled: Boolean = false, action: ((WhenJob?) -> Unit)? = null): ComboBox<WhenJob> {
    return comboBox(
        model = model,
        getter = { WhenJob.ON_SUCCESS }, setter = {}
    ).component.also {
        it.isEnabled = isEnabled
        it.addActionListener { _ ->
            action?.let { it1 -> it1(it.selectedItem as WhenJob?) }
        }
    }
}

fun Row.getOnlyComboBox(model: DefaultComboBoxModel<OnlyJob>, isEnabled: Boolean = false, action: ((OnlyJob?) -> Unit)? = null): ComboBox<OnlyJob> {
    return comboBox(
        model = model,
        getter = { OnlyJob.MERGE_REQUESTS }, setter = {}
    ).component.also {
        it.isEnabled = isEnabled
        it.addActionListener { _ ->
            action?.let { it1 -> it1(it.selectedItem as OnlyJob?) }
        }
    }
}

fun Row.getExpireInComboBox(model: DefaultComboBoxModel<ExpireInJob>, isEnabled: Boolean = false, action: ((ExpireInJob?) -> Unit)? = null): ComboBox<ExpireInJob> {
    return comboBox(
        model = model,
        getter = { ExpireInJob.DAY }, setter = {}
    ).component.also {
        it.isEnabled = isEnabled
        it.addActionListener { _ ->
            action?.let { it1 -> it1(it.selectedItem as ExpireInJob?) }
        }
    }
}
