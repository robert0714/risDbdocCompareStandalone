package com.iisi.rl.table;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.robert.study.discrepancy.model.DiscrepancyTableInterface;

public final class DataTable implements Serializable, DiscrepancyTableInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1942658932054432588L;
    private String fileName;
    private String description;
    private String chineseName;
    private String tableName;
    private String remark;
    private List<DataColumnInfo> dataColumnInfos;
    private List<String> infomation;

    public DataTable(String fileName) {
        super();
        this.fileName = fileName;
    }

    public String getRemark() {
    	if(remark==null){
    		remark = tableName+chineseName;
    	}
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setTableName(final String tableName) {
        if (tableName != null) {
            this.tableName = tableName.toUpperCase();
        }
    }

    public String getTableName() {
        if (tableName == null && fileName != null && fileName.contains(".doc") && fileName.contains("6-4-")) {
            tableName = fileName.replace(".doc", "").replace("6-4-", "");
            tableName = validateTableName(tableName);
        }
        return tableName;
    }

    // Logic
    private String validateTableName(final String srcTablerName) {
        String result = null;
        int firstChar = (int) srcTablerName.toCharArray()[0];

        if (getNumerCharacterUnicodeSymbolCode().contains(Integer.valueOf(firstChar))) {
            result = srcTablerName;
        } else {
            result = srcTablerName.substring(1);
        }
        return result;
    }

    // Logic
    private final Set<Integer> getNumerCharacterUnicodeSymbolCode() {
        Set<Integer> result = new HashSet<Integer>();
        for (int i = 97; i < 123; i++) {
            result.add(Integer.valueOf(i));
        }
        for (int i = 65; i < 90; i++) {
            result.add(Integer.valueOf(i));
        }
        return result;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDescription() {
        return description;
    }

    public String getChineseName() {
        if (chineseName == null && getInfomation().size() > 0) {
            chineseName = getInfomation().get(0).replace(getTableName(), "").trim();
        }
        return chineseName;
    }

    public void setChineseName(String chineseName) {
        this.chineseName = chineseName;
    }

    public List<DataColumnInfo> getDataColumnInfos() {
        if (dataColumnInfos == null) {
            dataColumnInfos = new ArrayList<DataColumnInfo>();
        }
        return dataColumnInfos;
    }

    public List<String> getInfomation() {
        if (infomation == null) {
            infomation = new ArrayList<String>();
        }
        return infomation;
    }
}
