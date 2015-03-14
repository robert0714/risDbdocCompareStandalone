package com.iisi.rl.table.script;

import java.io.Serializable;

import org.robert.study.discrepancy.model.DiscrepancyColumnInterface;


public class ScriptColumnInfo implements Serializable, DiscrepancyColumnInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 9137574183107553178L;
    private String columnName;
    private String format;
    private boolean nullable;
    private Boolean primaryKey;
    private boolean indexKey;
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

    public Boolean getPrimaryKey() {
    	if(primaryKey ==null){
    		primaryKey=Boolean.FALSE;
    	}
        return primaryKey;
    }

    public void setPrimaryKey(Boolean primaryKey) {
        this.primaryKey = primaryKey;
    }

    public boolean isIndexKey() {
        return indexKey;
    }

    public void setIndexKey(boolean indexKey) {
        this.indexKey = indexKey;
    }

    public String getStatement() {
        StringBuffer sbf = new StringBuffer();
        sbf.append(this.columnName).append(" \t").append(this.format).append(" \t").append(nullable ? " " : "NOT NULL");
        statement = sbf.toString();
        return statement;
    }

}
