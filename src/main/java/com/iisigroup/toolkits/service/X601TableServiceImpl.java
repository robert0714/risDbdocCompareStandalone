package com.iisigroup.toolkits.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;

import com.iisi.rl.table.DataColumnInfo;
import com.iisi.rl.table.DataTable;
/******
 * 職權更正戶籍資料變更
 * 
 * ******/
public class X601TableServiceImpl {
    public List<DataTable> covertToRCDFRRDFFormRldfByCommonmethod(List<DataTable> srcList) {
        List<DataTable> dableList = new ArrayList<DataTable>();
        for (DataTable aDataTable : srcList) {
            covertToRCDFRRDFFormRldfByCommonmethod(aDataTable, dableList);
        }
        return dableList;
    }

    private void covertToRCDFRRDFFormRldfByCommonmethod(DataTable aDataTable, List<DataTable> dableList) {
        List<DataColumnInfo> newList = new ArrayList<DataColumnInfo>();
        newList.add(new DataColumnInfo(new String[] { "PK", "TRANSACTION_ID", "交易序號", "CHAR(22)", "", "N", "" }));
        newList.add(new DataColumnInfo(new String[] { "PK", "UPDATE_SITE_ID", "職權更正地作業點代碼", "CHAR(8)", "", "N", "" }));
        newList.add(new DataColumnInfo(new String[] { "PK", "UPDATE_AREA_CODE", "職權更正地行政區域代碼", "CHAR(8)", "", "N", "" }));
        newList.add(new DataColumnInfo(new String[] { "PK", "UPDATE_ADMIN_OFFICE_CODE", "職權更正地戶所代碼", "CHAR(8)", "", "N", "" }));

        newList.add(new DataColumnInfo(new String[] { "", "APPLY_DATE", "申請日期", "CHAR(07)", "", "N", "" }));
        newList.add(new DataColumnInfo(new String[] { "", "APPLY_TIME", "申請時間", "CHAR(06)", "", "N", "" }));
        newList.add(new DataColumnInfo(new String[] { "", "UPDATE_REASON", "更正原因", "LVARCHAR(500)", "", "N", "" }));
        newList.add(new DataColumnInfo(new String[] { "", "BUSINESS_TYPE", "職權更正類別", "CHAR(01)", "", "N", "A：新增\rM：修改\rD：刪除" }));
         newList.add(new DataColumnInfo(new String[] { "", "STATUS", "流程狀態", "CHAR(1)", "", "N","流程狀態\rA：已申請\rP：核准\rR：核退" }));
        
        newList.add(new DataColumnInfo(new String[] { "", "APPROVE_DATE", "核准日期", "CHAR(07)", "", "N", "執行核准時才填上" }));
        newList.add(new DataColumnInfo(new String[] { "", "APPROVE_TIME", "核准時間", "CHAR(06)", "", "N", "執行核准時才填上" }));

        newList.add(new DataColumnInfo(new String[] { "", "APPROVER_ID", "核准主管使用者代號", "VARCHAR(12)", "", "N", "" }));
        newList.add(new DataColumnInfo(new String[] { "", "APPROVER_NAME", "核准主管姓名", "VARCHAR(32)", "", "N", "" }));
        newList.add(new DataColumnInfo(new String[] { "", "REGISTRAR_ID", "更正戶籍員使用者代號", "VARCHAR(12)", "", "N", "" }));
        newList.add(new DataColumnInfo(new String[] { "", "REGISTRAR_NAME", "更正戶籍員姓名", "VARCHAR(32)", "", "N", "" }));
        


        DataTable rldfs1xxxxMDataTable = (DataTable)SerializationUtils.clone(aDataTable);
        rldfs1xxxxMDataTable.setChineseName(rldfs1xxxxMDataTable.getChineseName().replace("資料檔", "職權更正資料檔").replace("紀錄檔", "職權更正紀錄檔"));

        Map tableNameMapping = getTableNameMapping();
        String newFileName = updateTableName(rldfs1xxxxMDataTable.getFileName(), tableNameMapping);
        String newTableName = updateTableName(rldfs1xxxxMDataTable.getTableName(), tableNameMapping);

        rldfs1xxxxMDataTable.setFileName(newFileName);
        rldfs1xxxxMDataTable.setTableName(newTableName.replace("資料檔", "職權更正資料檔").replace("紀錄檔", "職權更正紀錄檔"));

        rldfs1xxxxMDataTable.setRemark(rldfs1xxxxMDataTable.getTableName() + rldfs1xxxxMDataTable.getChineseName());
        List srcaList = rldfs1xxxxMDataTable.getDataColumnInfos();
        List formerList = processAddPrefix(srcaList, "OLD_", "更改前的");

        List latterList = processAddPrefix(srcaList, "NEW_", "更改後的");

        newList.addAll(formerList);
        newList.addAll(latterList);

        rldfs1xxxxMDataTable.getDataColumnInfos().clear();
        rldfs1xxxxMDataTable.getDataColumnInfos().addAll(newList);

        dableList.add(rldfs1xxxxMDataTable);
    }

    private List<DataColumnInfo> processAddPrefix(List<DataColumnInfo> srcaList, String prefixColumnName,
            String prefixChtName) {
        List<DataColumnInfo> newList = new ArrayList<DataColumnInfo>();
        for (DataColumnInfo unit : srcaList) {
            DataColumnInfo newUnit = (DataColumnInfo) SerializationUtils.clone(unit);

            String[] value = newUnit.getValueArray();
            value[1] = (prefixColumnName + value[1]);
            value[2] = (prefixChtName + value[2]);
            value[0] = "";

            newList.add(newUnit);
        }

        return newList;
    }
    private String updateChineseTableName(final String originalTableNameTitle,final Map<String,String> tableNameMapping,final    Map<String, String> tableChtNameMapping){
    	final 	Set<String> tableNameMappingkeySet = tableNameMapping.keySet();
    	String result  =StringUtils.EMPTY;
    	boolean isUpdate =false;
    	for(String originalTableNameKey : tableNameMappingkeySet){
    		if(originalTableNameTitle.contains( originalTableNameKey)){
    			String laterTableName = tableNameMapping.get(originalTableNameKey);
    			
    			result = tableChtNameMapping.get(laterTableName);
    			isUpdate =true;
    			break;
    		};
    	}
    	if(!isUpdate){
    		result = originalTableNameTitle.replace("RLDFS0", "RLDFS2");
    	}
    	return result;
    }
    private String getNewTableName(final String originalTableNameTitle,final Map<String,String> tableNameMapping){
    	final 	Set<String> tableNameMappingkeySet = tableNameMapping.keySet();
    	String result  =StringUtils.EMPTY;
    	boolean isUpdate =false;
    	for(String originalTableNameKey : tableNameMappingkeySet){
    		if(originalTableNameTitle.contains( originalTableNameKey)){
    			result =tableNameMapping.get(originalTableNameKey);    			
    			break;
    		};
    	}
    	
    	return result;
    }
    private String updateTableName(final String originalTableNameTitle,final Map<String,String> tableNameMapping){
    	final 	Set<String> tableNameMappingkeySet = tableNameMapping.keySet();
    	String result  =StringUtils.EMPTY;
    	boolean isUpdate =false;
    	for(String originalTableNameKey : tableNameMappingkeySet){
    		if(originalTableNameTitle.contains( originalTableNameKey)){
    			String laterTableName = tableNameMapping.get(originalTableNameKey);
    			result = originalTableNameTitle.replace(originalTableNameKey, laterTableName);
    			isUpdate =true;
    			break;
    		};
    	}
    	if(!isUpdate){
    		result = originalTableNameTitle.replace("RLDFS0", "RLDFS2");
    	}
    	return result;
    }
    private Map<String,String> getTableNameMapping(){
    	 final Map<String,String> result = new HashMap<String, String>();
    	 result.put("RLDF001M","RLDFS201");
    	 result.put("RLDF001H","RLDFS202");
    	 result.put("RLDF002M","RLDFS203");
    	 result.put("RLDF002H","RLDFS204");
    	 result.put("RLDF004M","RLDFS205");
    	 result.put("RLDF004H","RLDFS206");
    	 result.put("RLDF005M","RLDFS207");
    	 result.put("RLDF005H","RLDFS208");
    	 result.put("RLDF006M","RLDFS209");
    	 result.put("RLDF006H","RLDFS210");
    	 result.put("RLDF007M","RLDFS211");
    	 result.put("RLDF007H","RLDFS212");
    	 result.put("RLDF008M","RLDFS213");
    	 result.put("RLDF008H","RLDFS214");
    	 result.put("RLDF047M","RLDFS215");
    	 result.put("RLDF047H","RLDFS216");
    	 result.put("RLDFM03M","RLDFS217");
    	 result.put("RLDFM03H","RLDFS218");
    	 result.put("RLDFM09M","RLDFS219");
    	 result.put("RLDFM09H","RLDFS220");
    	 result.put("RLDF021M","RLDFS221");
    	 result.put("RLDF022M","RLDFS222");
    	 result.put("RLDF018M","RLDFS223");
    	 result.put("RLDF019M","RLDFS224");
    	 result.put("RLDF020M","RLDFS225");
    	 return result ; 
    }
//    private Map<String,String> getTableChineseNameMapping(){
//   	 final Map<String,String> result = new HashMap<String, String>();
//   	result.put("RLDFS201","全戶基本資料職權更正紀錄檔");
//   	result.put("RLDFS202","除戶全戶基本資料職權更正紀錄檔");
//   	result.put("RLDFS203","全戶動態記事職權更正紀錄檔");
//   	result.put("RLDFS204","除戶全戶動態記事職權更正紀錄檔");
//   	result.put("RLDFS205","個人基本資料職權更正紀錄檔");
//   	result.put("RLDFS206","除戶個人基本資料職權更正紀錄檔");
//   	result.put("RLDFS207","個人記事職權更正紀錄檔");
//   	result.put("RLDFS208","除戶個人記事職權更正紀錄檔");
//   	result.put("RLDFS209","個人遷徙紀錄職權更正紀錄檔");
//   	result.put("RLDFS210","除戶個人遷徙紀錄職權更正紀錄檔");
//   	result.put("RLDFS211","姓名更改紀錄職權更正紀錄檔");
//   	result.put("RLDFS212","除戶姓名更改紀錄職權更正紀錄檔");
//   	result.put("RLDFS213","特殊人口資料職權更正紀錄檔");
//   	result.put("RLDFS214","除戶特殊人口資料職權更正紀錄檔");
//   	result.put("RLDFS215","姓名羅馬拼音紀錄職權更正紀錄檔");
//   	result.put("RLDFS216","除戶姓名羅馬拼音紀錄職權更正紀錄檔");
//   	result.put("RLDFS217","統號更改紀錄職權更正紀錄檔");
//   	result.put("RLDFS218","除戶統號更改紀錄職權更正紀錄檔");
//   	result.put("RLDFS219","出生日期更改紀錄職權更正紀錄檔");
//   	result.put("RLDFS220","除戶出生日期更改紀錄職權更正紀錄檔");
//   	result.put("RLDFS221","戶號配賦職權更正紀錄檔");
//   	result.put("RLDFS222","統號配賦職權更正紀錄檔");
//   	result.put("RLDFS223","歷史門牌初編資料職權更正紀錄檔");
//   	result.put("RLDFS224","村里鄰及街路門牌變更資料職權更正紀錄檔");
//   	result.put("RLDFS225","里鄰門牌資料職權更正紀錄檔");
//
//   	 return result ; 
//   }
}