package com.saetova.gitlabciintegration.toolwindow.ui

import com.intellij.icons.AllIcons
import com.intellij.ui.table.JBTable
import java.awt.Component
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.JLabel
import javax.swing.JTable
import javax.swing.SwingConstants
import javax.swing.table.TableCellRenderer
import javax.swing.table.TableModel

class PipelinesTable(
    tableModel: TableModel,
    onDetailsClicked: (String) -> Unit
) : JBTable(tableModel) {

    init {
        setCellSelectionEnabled(true)
        addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent?) {
                if (e.isDoubleClick() && selectedColumn.isLastColumn()) {
                    val pipelineId = tableModel.getValueAt(selectedRow, 1).toString()
                    onDetailsClicked(pipelineId)
                }
            }
        })
    }

    override fun getCellRenderer(row: Int, column: Int): TableCellRenderer {
        val default = super.getCellRenderer(row, column)
        return if (column.isLastColumn()) {
            DetailsCellRenderer
        } else {
            default
        }
    }

    private object DetailsCellRenderer : TableCellRenderer {
        override fun getTableCellRendererComponent(
            table: JTable?,
            value: Any?,
            isSelected: Boolean,
            hasFocus: Boolean,
            row: Int,
            column: Int
        ): Component {
            return JLabel(value.toString(), AllIcons.General.ShowInfos, SwingConstants.LEFT)
        }
    }

    private fun Int.isLastColumn(): Boolean = this == columnCount - 1

    private fun MouseEvent?.isDoubleClick(): Boolean = this?.clickCount == 2
}