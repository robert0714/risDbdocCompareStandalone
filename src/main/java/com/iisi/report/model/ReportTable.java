package com.iisi.report.model;

import java.util.ArrayList;
import java.util.List;

public class ReportTable {
    private String tableName;
    private List<ColumnDsicrepancyRow> columnDsicrepancyRowList;
    private List<ColumnAttributeDsicrepancyRow> columnAttributeDsicrepancyRowList;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<ColumnDsicrepancyRow> getColumnDsicrepancyRowList() {
        if (columnDsicrepancyRowList == null) {
            columnDsicrepancyRowList = new ArrayList<ColumnDsicrepancyRow>();
        }
        return columnDsicrepancyRowList;
    }

    public void setColumnDsicrepancyRowList(List<ColumnDsicrepancyRow> columnDsicrepancyRowList) {
        this.columnDsicrepancyRowList = columnDsicrepancyRowList;
    }

    public List<ColumnAttributeDsicrepancyRow> getColumnAttributeDsicrepancyRowList() {
        if (columnAttributeDsicrepancyRowList == null) {
            columnAttributeDsicrepancyRowList = new ArrayList<ColumnAttributeDsicrepancyRow>();
        }
        return columnAttributeDsicrepancyRowList;
    }

    public void setColumnAttributeDsicrepancyRowList(
            List<ColumnAttributeDsicrepancyRow> columnAttributeDsicrepancyRowList) {

        this.columnAttributeDsicrepancyRowList = columnAttributeDsicrepancyRowList;
    }

}
