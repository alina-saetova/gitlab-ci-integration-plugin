package com.saetova.gitlabciintegration.toolwindow.ui

import com.saetova.gitlabciintegration.data.model.PipelineInfo
import com.saetova.gitlabciintegration.data.model.mapToTableData
import javax.swing.table.AbstractTableModel

class PipelinesTableModel : AbstractTableModel() {

    private val columns = arrayOf("Status", "Pipeline ID", "Branch", "Commit SHA", "Updated Time", "Info")

    var pipelinesInfo: List<PipelineInfo> = listOf()

    private var tableData = pipelinesInfo.mapToTableData()

    override fun getRowCount(): Int = tableData.size

    override fun getColumnCount(): Int = columns.size

    override fun getColumnName(column: Int): String = columns.getOrNull(column) ?: "-"

    override fun getValueAt(rowIndex: Int, columnIndex: Int): Any {
        return tableData.getOrNull(rowIndex)?.getOrNull(columnIndex) ?: "-"
    }

    fun setData(data: List<PipelineInfo>) {
        pipelinesInfo = data
        tableData = data.mapToTableData()
        fireTableDataChanged()
    }

    fun clearData() {
        tableData = emptyList()
        fireTableDataChanged()
    }
}