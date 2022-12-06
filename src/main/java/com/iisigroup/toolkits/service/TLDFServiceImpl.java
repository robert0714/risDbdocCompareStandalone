package com.iisigroup.toolkits.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.SerializationUtils;

import com.iisi.rl.table.DataTable;

public class TLDFServiceImpl {
	private TableScriptGenaratorServiceImpl method = new TableScriptGenaratorServiceImpl();
	public List<DataTable>  covertToTLDFFormRldfByCommonmethod(final List<DataTable> srcList) {
		List<DataTable> dableList  =new ArrayList<DataTable>();		
		for(DataTable aDataTable :srcList){
			covertToTLDFFormRldfByCommonmethod(aDataTable, dableList);
		}
		return dableList;
	}
	private void covertToTLDFFormRldfByCommonmethod( final DataTable aDataTable  ,final List<DataTable> dableList  ){
		
		DataTable tldfxxxxMDataTable = (DataTable) SerializationUtils.clone(aDataTable);		
		tldfxxxxMDataTable.setChineseName("暫時-"+tldfxxxxMDataTable.getChineseName());
		tldfxxxxMDataTable.setFileName(tldfxxxxMDataTable.getFileName().replace("RLDF", "TLDF"));
		tldfxxxxMDataTable.setTableName(tldfxxxxMDataTable.getTableName().replace("RLDF", "TLDF"));
		tldfxxxxMDataTable.setRemark(tldfxxxxMDataTable.getTableName()+tldfxxxxMDataTable.getChineseName());
		
		DataTable xldfxxxxMTDataTable = method.modiferOfXLDF(tldfxxxxMDataTable);
		xldfxxxxMTDataTable.setFileName(xldfxxxxMTDataTable.getFileName().replace("TLDF", "XLDF"));
		xldfxxxxMTDataTable.setFileName(xldfxxxxMTDataTable.getFileName().replace(".doc", "T.doc"));
		xldfxxxxMTDataTable.setTableName(xldfxxxxMTDataTable.getTableName()+"T");
		xldfxxxxMTDataTable.setRemark(xldfxxxxMTDataTable.getTableName()+xldfxxxxMTDataTable.getChineseName());
		
		dableList.add(tldfxxxxMDataTable);
		dableList.add(xldfxxxxMTDataTable);		
	}
}
