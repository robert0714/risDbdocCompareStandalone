package com.iisi.report.model;

import java.util.ArrayList;
import java.util.List;

public class ReportBody {
    public List<ReportTable> tables = new ArrayList<ReportTable>();

    public List<ReportTable> getTables() {
        return tables;
    }

    public void setTables(List<ReportTable> tables) {
        this.tables = tables;
    }

    public int summarySameTableNameNumber;
    public List<String> unHandleAList = new ArrayList<String>();
    public List<String> unHandleBList = new ArrayList<String>();

    public int getSummarySameTableNameNumber() {
        return summarySameTableNameNumber;
    }

    public void setSummarySameTableNameNumber(int summarySameTableNameNumber) {
        this.summarySameTableNameNumber = summarySameTableNameNumber;
    }

    public List<String> getUnHandleAList() {
        return unHandleAList;
    }

    public void setUnHandleAList(List<String> unHandleAList) {
        this.unHandleAList = unHandleAList;
    }

    public List<String> getUnHandleBList() {
        return unHandleBList;
    }

    public void setUnHandleBList(List<String> unHandleBList) {
        this.unHandleBList = unHandleBList;
    }

}
