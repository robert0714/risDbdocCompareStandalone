package com.iisi.rl.table.discrepancy;

import java.io.Serializable;

import org.robert.study.discrepancy.model.DiscrepancyColumnInterface;

public class DiscrepancyColumn implements Serializable, DiscrepancyColumnInterface {
    /**
     * 
     */
    private static final long serialVersionUID = -7888526388356498147L;
    private String columnName;
    private String format;
    private boolean nullable;
    private Boolean primaryKey;
    private String statement;

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    public Boolean isPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(Boolean primaryKey) {
        this.primaryKey = primaryKey;
    }

    public String getStatement() {
        return statement;
    }

    public void setStatement(String statement) {
        this.statement = statement;
    }
}
