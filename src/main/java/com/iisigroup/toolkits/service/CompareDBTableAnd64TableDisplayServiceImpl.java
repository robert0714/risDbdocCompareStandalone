package com.iisigroup.toolkits.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.jxls.exception.ParsePropertyException;
import net.sf.jxls.transformer.XLSTransformer;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.robert.study.service.CompareDBTableAnd64TableDisplayService;
import org.robert.study.utils.POIUtils;

import com.iisi.report.model.ColumnAttributeDsicrepancyRow;
import com.iisi.report.model.ColumnDsicrepancyRow;
import com.iisi.report.model.ReportBody;
import com.iisi.report.model.ReportTable;
import com.iisi.rl.table.discrepancy.DiscrepancyReport;
import com.iisi.rl.table.discrepancy.DiscrepancyTable;

public class CompareDBTableAnd64TableDisplayServiceImpl implements CompareDBTableAnd64TableDisplayService {
    protected static Logger log = LoggerFactory.getLogger(CompareDBTableAnd64TableDisplayServiceImpl.class);

    

    public void display(DiscrepancyReport report) {
        log.info("report.getDiscrepencyLackingTableName().size(): " + report.getDiscrepencyLackingTableName().size());
        log.info("report.getDiscrepencyTableNums().size(): " + report.getDiscrepencyTableNums().size());
        log.info("report.getDiscrepencyTables().size(): " + report.getDiscrepencyTables().size());
    }
    public  Map<String, String[]> sumaryComparison(final Map<String, String>... sumarys) {
		final Map<String, String[]> result = new HashMap<String, String[]>();
		int columnSize = sumarys.length;
		for (int i = 0; i < columnSize; ++i) {
			final Map<String, String> sumarMap = sumarys[i];
			Set<String> tableSet = sumarMap.keySet();
			for (String tableName : tableSet) {
				String reason = sumarMap.get(tableName);
				String[] reasons = result.get(tableName);
				if (reasons == null) {
					reasons = new String[columnSize];
				}
				reasons[i] = reason;
				result.put(tableName, reasons);
			}
		}
		return result;
	}

    public Map<String,String>  summaryData(DiscrepancyReport report){
    	final List<DiscrepancyTable> discrepancyTableList = report.getDiscrepencyTables();
        final Map<String,String> summarySQLDB =new HashMap<String, String>();
        for(DiscrepancyTable table :discrepancyTableList){
        	String tableName = table.getTableName();
        	Map<String, String[]> sameColumnFormatDiscrepancyData = table.getSameColumnFormatDiscrepancy();
        	Map<String, List<String>> lackingColumnMap = table.getLackingColumnList();
        	final   	Set<String> lackingColumnMapKeySet = lackingColumnMap.keySet();
        	List<String> errorReason = new ArrayList<String>();
        	List<String> columnError = new ArrayList<String>();
        	for(String lackingColumn:lackingColumnMapKeySet){
        		 List<String> detail = lackingColumnMap.get(lackingColumn);
        		 if(CollectionUtils.isNotEmpty(detail)){
        			 columnError.add(StringUtils.join(detail,":"));
        		 }
        	}
        	if(columnError.size()>0){
        		errorReason.add("缺少欄位: ("+ StringUtils.join(columnError,":")+")");
        	}
        	summarySQLDB.put(tableName, StringUtils.join(errorReason ,":"));
        }
        Map<String, List<String>> discrepencyLackingTableName = report.getDiscrepencyLackingTableName();
        for(String key: discrepencyLackingTableName.keySet()){
        	List<String> list = discrepencyLackingTableName.get(key);
        	for(String tableName : list){
        		String reason = summarySQLDB.get(tableName);
        		if(StringUtils.isNotBlank(reason)){
        			summarySQLDB.put(tableName, reason+":"+key+"存在性差異");
        		}else{
        			summarySQLDB.put(tableName, key+"存在性差異");
        		}
        		
        	}
        }
        return summarySQLDB;
    }

    @Override
    public void exportReport3(final DiscrepancyReport report, final String fileName) throws ParsePropertyException,
            InvalidFormatException, IOException {
        Map<String, Object> beans = new HashMap<String, Object>();
        ReportBody reportBody = convertDiscrepancyReport(report);
        beans.put("title", "This is test function");

        beans.put("discrepancyTableBody", reportBody);
        XLSTransformer transformer = new XLSTransformer();

        Workbook aHSSFWorkbook = transformer.transformXLS(
                CompareDBTableAnd64TableDisplayServiceImpl.class.getResource("template.xls").openStream(), beans);
        POIUtils.writeWorkbookOut(new File(fileName), aHSSFWorkbook);
        log.info("final condition..........");
    }

	public void exportReport4(final DiscrepancyReport report,
			final String fileName) throws ParsePropertyException,
			InvalidFormatException, IOException {
		Map<String, Object> beans = new HashMap<String, Object>();
		ReportBody reportBody = convertDiscrepancyReport(report);
		beans.put("title", "This is test function");

		beans.put("discrepancyTableBody", reportBody);
		XLSTransformer transformer = new XLSTransformer();

		Workbook aHSSFWorkbook = transformer.transformXLS(
				CompareDBTableAnd64TableDisplayServiceImpl.class.getResource(
						"template.xls").openStream(), beans);
		POIUtils.writeWorkbookOut(new File(fileName), aHSSFWorkbook);
		log.info("final condition..........");
	}

    private static ReportBody getMockReportBody() {
        ReportBody body = new ReportBody();
        List<ReportTable> reports = new ArrayList<ReportTable>();
        for (int i = 0; i < 10; ++i) {
            ReportTable aReportTable = new ReportTable();
            String tableName = "Table0" + i;
            aReportTable.setTableName(tableName);
            List<ColumnDsicrepancyRow> columnDsicrepancyRowList = aReportTable.getColumnDsicrepancyRowList();
            List<ColumnAttributeDsicrepancyRow> columnAttributeDsicrepancyRowList = aReportTable
                    .getColumnAttributeDsicrepancyRowList();
            for (int j = 0; j < 3; ++j) {
                ColumnDsicrepancyRow row = new ColumnDsicrepancyRow();
                row.setFommer(tableName + " former " + j);
                row.setLatter(tableName + " latter " + j);
                columnDsicrepancyRowList.add(row);
            }
            for (int j = 0; j < 3; ++j) {
                ColumnAttributeDsicrepancyRow row = new ColumnAttributeDsicrepancyRow();
                row.setAttribute1(tableName + " setAttribute1 " + j);
                row.setAttribute2(tableName + " setAttribute2 " + j);
                columnAttributeDsicrepancyRowList.add(row);
            }
            reports.add(aReportTable);
        }
        body.setTables(reports);
        return body;
    }

    private static ReportBody convertDiscrepancyReport(DiscrepancyReport aDiscrepancyReport) {
        ReportBody result = new ReportBody();
        List<ReportTable> tables = result.getTables();
        List<DiscrepancyTable> discrepancyTableList = aDiscrepancyReport.getDiscrepencyTables();
        for (DiscrepancyTable aDiscrepancyTable : discrepancyTableList) {
            ReportTable aReportTable = new ReportTable();
            aReportTable.setTableName(aDiscrepancyTable.getTableName());
            List<ColumnDsicrepancyRow> columnDsicrepancyRowList = aReportTable.getColumnDsicrepancyRowList();
            List<String> sQLScripLackingTableNameList = aDiscrepancyTable.getLackingColumnList().get(
                    "sQLScripLackingColumnNameList");
            List<String> wordDocLackingTableNameList = aDiscrepancyTable.getLackingColumnList().get(
                    "wordDocLackingColumnNameList");
            for (String tmp : sQLScripLackingTableNameList) {
                ColumnDsicrepancyRow aColumnDsicrepancyRow = new ColumnDsicrepancyRow();
                aColumnDsicrepancyRow.setFommer(tmp);
                columnDsicrepancyRowList.add(aColumnDsicrepancyRow);
            }
            for (String tmp : wordDocLackingTableNameList) {
                ColumnDsicrepancyRow aColumnDsicrepancyRow = new ColumnDsicrepancyRow();
                aColumnDsicrepancyRow.setLatter(tmp);
                columnDsicrepancyRowList.add(aColumnDsicrepancyRow);
            }

            List<ColumnAttributeDsicrepancyRow> columnAttributeDsicrepancyRowList = aReportTable
                    .getColumnAttributeDsicrepancyRowList();
            Map<String, String[]> sameColumnFormatDiscrepancy = aDiscrepancyTable.getSameColumnFormatDiscrepancy();
            for (String key : sameColumnFormatDiscrepancy.keySet()) {
                String[] value = sameColumnFormatDiscrepancy.get(key);
                ColumnAttributeDsicrepancyRow aColumnAttributeDsicrepancyRow = new ColumnAttributeDsicrepancyRow();
                aColumnAttributeDsicrepancyRow.setAttribute1(value[0]);
                aColumnAttributeDsicrepancyRow.setAttribute2(value[1]);
                aColumnAttributeDsicrepancyRow.setCoumnName(key);
                columnAttributeDsicrepancyRowList.add(aColumnAttributeDsicrepancyRow);
            }
            tables.add(aReportTable);
        }

        log.info("DiscrepencyTables Tables: " + aDiscrepancyReport.getDiscrepencyTables().size());
        log.info("same Table name: " + aDiscrepancyReport.getSameTableName().size());
        Map<String, List<String>> aDiscrepencyLackingTableName = aDiscrepancyReport.getDiscrepencyLackingTableName();
        List<String> sQLScripLackingTableNameList = aDiscrepencyLackingTableName.get("sQLScripLackingTableNameList");
        List<String> wordDocLackingTableNameList = aDiscrepencyLackingTableName.get("wordDocLackingTableNameList");
        log.info("sQLScripLackingTableNameList: " + sQLScripLackingTableNameList.size());
        log.info("wordDocLackingTableNameList: " + wordDocLackingTableNameList.size());

        result.setSummarySameTableNameNumber(aDiscrepancyReport.getSameTableName().size());
        result.setUnHandleAList(sQLScripLackingTableNameList);
        result.setUnHandleBList(wordDocLackingTableNameList);
        result.setTables(tables);
        return result;

    }
}
