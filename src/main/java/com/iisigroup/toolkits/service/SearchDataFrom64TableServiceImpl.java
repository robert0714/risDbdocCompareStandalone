package com.iisigroup.toolkits.service;


import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.iisi.rl.table.DataColumnInfo;
import com.iisi.rl.table.DataTable;

public class SearchDataFrom64TableServiceImpl {
	
	public String  getResultForSearch(final String chineseName,final String columnName, final List<DataTable> srcList) {
		
		boolean findchinese =StringUtils.isNotBlank(chineseName);
		final String searchCht = StringUtils.trim(chineseName);
		
		boolean findColumnName =StringUtils.isNotBlank(columnName);
		final String searchColumnName = StringUtils.trim(columnName);
		
		List<SearResult> resultList = new ArrayList<SearchDataFrom64TableServiceImpl.SearResult>();
		for(final DataTable table : srcList){
			final List<DataColumnInfo> dataColumnInfos = table.getDataColumnInfos();
			boolean needAddResult=false;
			final List<DataColumnInfo> searchColumnInfos=new ArrayList<DataColumnInfo>();
			for(DataColumnInfo column : dataColumnInfos){
				if(findchinese && StringUtils.contains(column.getChineseName(), searchCht)){
					needAddResult=true;
					searchColumnInfos.add(column);
				}
				if(findColumnName && StringUtils.contains(column.getColumnName(), searchColumnName)){
					needAddResult=true;
					searchColumnInfos.add(column);
				}
				
			}
			if(needAddResult){
				SearResult unit =new SearResult();
//				unit.setDataTable(table);
				unit.setSearchColumnInfos(searchColumnInfos);
				unit.setTableName(table.getTableName());
				resultList.add(unit);
			}
		}
		
		return convertString(resultList);
	}

	protected String convertString(final List<SearResult> resultList ){
		StringBuilder sbf =new StringBuilder();
		for(SearResult unit :resultList){
			sbf.append(String.format("--------------------------------------------------------\rTable Name: %s\r", unit.getTableName()));
			for(DataColumnInfo column :unit.getSearchColumnInfos() ){
				
				String line = String.format("%s, %s, %s \r",
						StringUtils.trim(column.getColumnName()), 
						StringUtils.trim(column.getChineseName()), 
						StringUtils.trim(column.getFormat()));
				sbf.append(line);
			}
		}
		
		return sbf.toString();
	}
	
	class SearResult {
		private String tableName;
		private List<DataColumnInfo> searchColumnInfos;
//		private DataTable dataTable;
		public String getTableName() {
			return tableName;
		}
		public void setTableName(String tableName) {
			this.tableName = tableName;
		}
		public List<DataColumnInfo> getSearchColumnInfos() {
			return searchColumnInfos;
		}
		public void setSearchColumnInfos(List<DataColumnInfo> searchColumnInfos) {
			this.searchColumnInfos = searchColumnInfos;
		}
//		public DataTable getDataTable() {
//			return dataTable;
//		}
//		public void setDataTable(DataTable dataTable) {
//			this.dataTable = dataTable;
//		}
		
	}
}
