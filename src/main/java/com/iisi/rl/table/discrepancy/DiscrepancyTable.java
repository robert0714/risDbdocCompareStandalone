package com.iisi.rl.table.discrepancy;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.robert.study.discrepancy.model.DiscrepancyColumnInterface;
import org.robert.study.discrepancy.model.DiscrepancyTableInterface;

public class DiscrepancyTable implements Serializable, DiscrepancyTableInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 4532707969494741420L;
    public String tableName;
    public List<String> Information;
    public Map<String, Integer> discrepancyColumnNums = new HashMap<String, Integer>();
    public List<DiscrepancyColumn> dataColumnInfos;
    public Map<String, List<DiscrepancyColumn>> discrepancyColumns = new HashMap<String, List<DiscrepancyColumn>>();
    public Map<String, List<String>> lackingColumnList = new HashMap<String, List<String>>();
    public Set<String> sameColumns = new HashSet<String>();
    
    /**
     * 相同名稱的column,但format不同
     * **/
    public Map<String, String[]> sameColumnFormatDiscrepancy = new HashMap<String, String[]>();
    public boolean discrepancy;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<String> getInformation() {
        return Information;
    }

    public void setInformation(List<String> information) {
        Information = information;
    }

    public Map<String, List<DiscrepancyColumn>> getDiscrepancyColumns() {
        return discrepancyColumns;
    }

    public void setDiscrepancyColumns(Map<String, List<DiscrepancyColumn>> discrepancyColumns) {
        this.discrepancyColumns = discrepancyColumns;
    }

    public Map<String, Integer> getDiscrepancyColumnNums() {
        return discrepancyColumnNums;
    }

    public void setDiscrepancyColumnNums(Map<String, Integer> discrepancyColumnNums) {
        this.discrepancyColumnNums = discrepancyColumnNums;
    }

    public List<? extends DiscrepancyColumnInterface> getDataColumnInfos() {
        return dataColumnInfos;
    }

    public Map<String, List<String>> getLackingColumnList() {
        return lackingColumnList;
    }

    public void setLackingColumnList(Map<String, List<String>> lackingColumnList) {
        this.lackingColumnList = lackingColumnList;
    }

    public Set<String> getSameColumns() {
        return sameColumns;
    }

    public void setSameColumns(Set<String> sameColumns) {
        this.sameColumns = sameColumns;
    }

    public Map<String, String[]> getSameColumnFormatDiscrepancy() {
        return sameColumnFormatDiscrepancy;
    }

    public void setSameColumnFormatDiscrepancy(Map<String, String[]> sameColumnFormatDiscrepancy) {
        this.sameColumnFormatDiscrepancy = sameColumnFormatDiscrepancy;
    }

    public boolean isDiscrepancy() {
        return discrepancy;
    }

    public void setDiscrepancy(boolean discrepancy) {
        this.discrepancy = discrepancy;
    }

}
