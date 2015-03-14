package com.iisi.rl.table.jdbc.schema;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.robert.study.discrepancy.model.DiscrepancyColumnInterface;
import org.robert.study.discrepancy.model.DiscrepancyTableIncludeIndexInfoInterface;

public class JDBCTablseSchema implements Serializable ,DiscrepancyTableIncludeIndexInfoInterface{

	/**
	 * 
	 */
	private static final long serialVersionUID = -39301045112649343L;
	
	private String tableName ;
	private List <JDBCColumnSachema> dataColumnInfos ;
	private Map<String, Set<String>> indexInfo ;
	
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public List<JDBCColumnSachema> getDataColumnInfos() {
		if(dataColumnInfos ==null){
			dataColumnInfos =new ArrayList<JDBCColumnSachema>();
		}
		return dataColumnInfos;
	}
	public void setDataColumnInfos(List<JDBCColumnSachema> dataColumnInfos) {
		this.dataColumnInfos = dataColumnInfos;
	}
	public Map<String, Set<String>> getIndexInfo() {
		if(indexInfo ==null){
			indexInfo =new HashMap<String, Set<String>>();
		}
		return indexInfo;
	}
	public void setIndexInfo(Map<String, Set<String>> indexInfo) {
		this.indexInfo = indexInfo;
	}
	
}
