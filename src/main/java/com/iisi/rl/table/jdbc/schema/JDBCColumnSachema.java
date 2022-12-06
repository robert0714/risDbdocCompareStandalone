package com.iisi.rl.table.jdbc.schema;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;
import org.robert.study.discrepancy.model.DiscrepancyColumnInterface;

public class JDBCColumnSachema implements Serializable ,DiscrepancyColumnInterface{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5950902205523541595L;
	public JDBCColumnSachema() {
		super();
	}
	public JDBCColumnSachema(String tableName, String columnName,
			String columnTypeName, int columnDisplaySize, int nullable,
			boolean autoIncrement) {
		super();
		this.tableName = tableName;
		this.columnName = columnName;
		this.columnTypeName = columnTypeName;
		this.columnDisplaySize = columnDisplaySize;
		this.nullable = nullable;
		this.autoIncrement = autoIncrement;
	}
	private String tableName ;
	private String columnName;
	private String columnTypeName;
	private int columnDisplaySize;
	private int nullable;
	private Boolean primaryKey;
	private boolean autoIncrement ;
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public String getColumnTypeName() {
		return columnTypeName;
	}
	public void setColumnTypeName(String columnTypeName) {
		this.columnTypeName = columnTypeName;
	}
	public int getColumnDisplaySize() {
		return columnDisplaySize;
	}
	public void setColumnDisplaySize(int columnDisplaySize) {
		this.columnDisplaySize = columnDisplaySize;
	}
	public int getNullable() {
		return nullable;
	}
	public void setNullable(int nullable) {
		this.nullable = nullable;
	}
	public boolean isAutoIncrement() {
		return autoIncrement;
	}
	public void setAutoIncrement(boolean autoIncrement) {
		this.autoIncrement = autoIncrement;
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
	public String getFormat() {
		if(StringUtils.equalsIgnoreCase("INT", columnTypeName)){
			return "INTEGER";
		}else{
//			String result =StringUtils.upperCase(columnTypeName +"("+String.format("%1$,02d", columnDisplaySize)+")");
			String result =StringUtils.upperCase(columnTypeName +"("+String.format("%02d", columnDisplaySize)+")");
			return result;
		}
	}
	

}
