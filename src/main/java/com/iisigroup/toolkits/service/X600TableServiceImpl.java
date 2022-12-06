package com.iisigroup.toolkits.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.SerializationUtils;

import com.iisi.rl.table.DataColumnInfo;
import com.iisi.rl.table.DataTable;

public class X600TableServiceImpl {
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

        newList.add(new DataColumnInfo(new String[] { "", "APPLY_DATE", "申請日期", "CHAR(7)", "", "N", "" }));
        newList.add(new DataColumnInfo(new String[] { "", "APPLY_TIME", "申請時間", "CHAR(6)", "", "N", "" }));
        newList.add(new DataColumnInfo(new String[] { "", "UPDATE_REASON", "更正原因", "LVARCHAR(500)", "", "N", "" }));
        newList.add(new DataColumnInfo(new String[] { "", "BUSINESS_TYPE", "職權更正類別", "CHAR(1)", "", "N", "A：新增\rM：修改\rD：刪除" }));
        newList.add(new DataColumnInfo(new String[] { "", "STATUS", "流程狀態", "CHAR(1)", "", "N","流程狀態\rA：已申請\rP：核准\rR：核退" }));
        
        newList.add(new DataColumnInfo(new String[] { "", "APPROVE_DATE", "核准日期", "CHAR(7)", "", "N", "執行核准時才填上" }));
        newList.add(new DataColumnInfo(new String[] { "", "APPROVE_TIME", "核准時間", "CHAR(6)", "", "N", "執行核准時才填上" }));

        newList.add(new DataColumnInfo(new String[] { "", "APPROVER_ID", "核准主管使用者代號", "VARCHAR(12)", "", "N", "" }));
        newList.add(new DataColumnInfo(new String[] { "", "APPROVER_NAME", "核准主管姓名", "VARCHAR(32)", "", "N", "" }));
        newList.add(new DataColumnInfo(new String[] { "", "REGISTRAR_ID", "更正戶籍員使用者代號", "VARCHAR(12)", "", "N", "" }));
        newList.add(new DataColumnInfo(new String[] { "", "REGISTRAR_NAME", "更正戶籍員姓名", "VARCHAR(32)", "", "N", "" }));

        DataTable rldfs1xxxxMDataTable = (DataTable) SerializationUtils.clone(aDataTable);
        rldfs1xxxxMDataTable.setChineseName(rldfs1xxxxMDataTable.getChineseName().replace("申請書資料檔", "申請書職權更正紀錄檔"));
        rldfs1xxxxMDataTable.setFileName(rldfs1xxxxMDataTable.getFileName().replace("RLDFS0", "RLDFS1"));
        rldfs1xxxxMDataTable.setTableName(rldfs1xxxxMDataTable.getTableName().replace("RLDFS0", "RLDFS1")
                .replace("申請書資料檔", "申請書職權更正紀錄檔"));

        rldfs1xxxxMDataTable.setRemark(rldfs1xxxxMDataTable.getTableName() + rldfs1xxxxMDataTable.getChineseName());
        List<DataColumnInfo> srcaList = rldfs1xxxxMDataTable.getDataColumnInfos();
        List<DataColumnInfo> formerList = processAddPrefix(srcaList, "OLD_", "更改前的");

        List<DataColumnInfo> latterList = processAddPrefix(srcaList, "NEW_", "更改後的");

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
}