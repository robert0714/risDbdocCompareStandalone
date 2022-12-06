package com.iisigroup.toolkits.service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang3.SerializationUtils;

import com.iisi.rl.table.DataColumnInfo;
import com.iisi.rl.table.DataTable;

public class RRRCTableServiceImpl {
	public List<DataTable>  covertToRCDFRRDFFormRldfByCommonmethod(final List<DataTable> srcList) {
		List<DataTable> dableList  =new ArrayList<DataTable>();		
		for(DataTable aDataTable :srcList){
			covertToRCDFRRDFFormRldfByCommonmethod(aDataTable, dableList);
		}
		return dableList;
	}
	private void covertToRCDFRRDFFormRldfByCommonmethod( final DataTable aDataTable  ,final List<DataTable> dableList  ){
		final String originalTableName = aDataTable.getTableName();
		//跟據originalTableName來判別是否為附繳證件資料檔....因為他會影響命名格式
		
		final String originalFileName = aDataTable.getFileName();
		//跟據originalFileName來判別是否為附繳證件資料檔....因為他會影響命名格式
		
		//TODO ...
		Pattern regexFileName = Pattern.compile("6-4-RLDFS[\\w1-9][\\w1-9][\\w1-9]B.doc");
		Pattern regexTableName = Pattern.compile("RLDFS[\\w1-9][\\w1-9][\\w1-9]B");
		
		boolean isCertificate =false;
		if(regexFileName.matcher(originalFileName).matches()  && regexTableName.matcher(originalTableName).matches() ){
			isCertificate =true;
		}
		
		List<DataColumnInfo> newList = new ArrayList<DataColumnInfo>();
		newList.add(new DataColumnInfo(new String[] { "", "FROM_SITE_ID", "通報起始地", "CHAR(8)", "", "N", "" }));
		newList.add(new DataColumnInfo(new String[] { "", "NOTICE_DATE", "通報日期", "CHAR(7)", "", "N", "" }));
		newList.add(new DataColumnInfo(new String[] { "", "NOTICE_TIME", "通報時間", "CHAR(6)", "", "N", "" }));
		newList.add(new DataColumnInfo(new String[] { "", "OLD_APPLY_SEQ", "對應到舊資料之申請書序號(APPLY_SEQ) ", "CHAR(20)", "", "N", "" }));
		
		
		
		
		DataTable rcdfxxxxMDataTable = (DataTable) SerializationUtils.clone(aDataTable);		
		rcdfxxxxMDataTable.setChineseName(rcdfxxxxMDataTable.getChineseName());
		if(isCertificate){
			rcdfxxxxMDataTable.setFileName(rcdfxxxxMDataTable.getFileName().replace("RLDFS", "RCDF"));
			rcdfxxxxMDataTable.setTableName(rcdfxxxxMDataTable.getTableName().replace("RLDFS", "RCDF"));
		}else{
			rcdfxxxxMDataTable.setFileName(rcdfxxxxMDataTable.getFileName().replace("RLDFS", "RCDF").replace(".doc", "A.doc"));
			rcdfxxxxMDataTable.setTableName(rcdfxxxxMDataTable.getTableName().replace("RLDFS", "RCDF")+"A");
		}
		
		
		rcdfxxxxMDataTable.setRemark(rcdfxxxxMDataTable.getTableName()+rcdfxxxxMDataTable.getChineseName());
		rcdfxxxxMDataTable.getDataColumnInfos().addAll(newList);
		
		DataTable rrdfxxxxMDataTable = (DataTable) SerializationUtils.clone(aDataTable);		
		rrdfxxxxMDataTable.setChineseName(rrdfxxxxMDataTable.getChineseName());
		
		if(isCertificate){
			rrdfxxxxMDataTable.setFileName(rrdfxxxxMDataTable.getFileName().replace("RLDFS", "RRDF"));
			rrdfxxxxMDataTable.setTableName(rrdfxxxxMDataTable.getTableName().replace("RLDFS", "RRDF"));
		}else{
			rrdfxxxxMDataTable.setFileName(rrdfxxxxMDataTable.getFileName().replace("RLDFS", "RRDF").replace(".doc", "A.doc"));
			rrdfxxxxMDataTable.setTableName(rrdfxxxxMDataTable.getTableName().replace("RLDFS", "RRDF")+"A");
		}
		
		
		rrdfxxxxMDataTable.setRemark(rrdfxxxxMDataTable.getTableName()+rrdfxxxxMDataTable.getChineseName());
		rrdfxxxxMDataTable.getDataColumnInfos().addAll(newList);
		
		dableList.add(rcdfxxxxMDataTable);
		dableList.add(rrdfxxxxMDataTable);		
	}
	
	
	public List<DataTable>  covertToRCDFRRDFFormRldfByCommonmethodX6000(final List<DataTable> srcList) {
		List<DataTable> dableList  =new ArrayList<DataTable>();		
		for(DataTable aDataTable :srcList){
			covertToRCDFRRDFFormRldfByCommonmethodX6000(aDataTable, dableList);
		}
		return dableList;
	}
	private void covertToRCDFRRDFFormRldfByCommonmethodX6000( final DataTable aDataTable  ,final List<DataTable> dableList  ){
		List<DataColumnInfo> newList = new ArrayList<DataColumnInfo>();
		newList.add(new DataColumnInfo(new String[] { "", "FROM_SITE_ID", "通報起始地", "CHAR(8)", "", "N", "" }));
		newList.add(new DataColumnInfo(new String[] { "", "NOTICE_DATE", "通報日期", "CHAR(7)", "", "N", "" }));
		newList.add(new DataColumnInfo(new String[] { "", "NOTICE_TIME", "通報時間", "CHAR(6)", "", "N", "" }));
		
		DataTable rcdfxxxxMDataTable = (DataTable) SerializationUtils.clone(aDataTable);		
		rcdfxxxxMDataTable.setChineseName(rcdfxxxxMDataTable.getChineseName());
		rcdfxxxxMDataTable.setFileName(rcdfxxxxMDataTable.getFileName().replace("RLDFS", "RCDF").replace(".doc", "A.doc"));
		rcdfxxxxMDataTable.setTableName(rcdfxxxxMDataTable.getTableName().replace("RLDFS", "RCDF")+"A");
		rcdfxxxxMDataTable.setRemark(rcdfxxxxMDataTable.getTableName()+rcdfxxxxMDataTable.getChineseName());
		rcdfxxxxMDataTable.getDataColumnInfos().addAll(newList);
		
		DataTable rrdfxxxxMDataTable = (DataTable) SerializationUtils.clone(aDataTable);		
		rrdfxxxxMDataTable.setChineseName(rrdfxxxxMDataTable.getChineseName());
		rrdfxxxxMDataTable.setFileName(rrdfxxxxMDataTable.getFileName().replace("RLDFS", "RRDF").replace(".doc", "A.doc"));
		rrdfxxxxMDataTable.setTableName(rrdfxxxxMDataTable.getTableName().replace("RLDFS", "RRDF")+"A");
		rrdfxxxxMDataTable.setRemark(rrdfxxxxMDataTable.getTableName()+rrdfxxxxMDataTable.getChineseName());
		rrdfxxxxMDataTable.getDataColumnInfos().addAll(newList);
		
		dableList.add(rcdfxxxxMDataTable);
		dableList.add(rrdfxxxxMDataTable);		
	}
}
