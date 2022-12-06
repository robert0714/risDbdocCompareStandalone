package com.iisigroup.toolkits.service;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sf.jxls.exception.ParsePropertyException;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.robert.study.discrepancy.model.DiscrepancyTableInterface;
import org.robert.study.service.CompareDBTableAnd64TableDisplayService;
import org.robert.study.service.CompareDBTableAnd64TableService;

import com.iisi.rl.table.discrepancy.DiscrepancyReport;
import com.iisi.rl.table.jdbc.schema.JDBCTableMiningResult;
import com.iisi.rl.table.jdbc.schema.JDBCTablseSchema;
import com.iisi.rl.table.script.ScriptTableInfo;

public class CompareDBTableAndSqlScriptServiceImpl {	
	private CompareDBTableAnd64TableService baseCompareService  ;
	
	public CompareDBTableAnd64TableService getBaseCompareService() {
		if(baseCompareService ==null){
			baseCompareService = new CompareDBTableAnd64TableServiceV2Impl();
		}
		return baseCompareService;
	}

	public void setBaseCompareService(CompareDBTableAnd64TableService baseCompareService) {
		this.baseCompareService = baseCompareService;
	}

	private String[] extractTable(final String originalString) {
		int start = originalString.indexOf('(');
		int end = originalString.indexOf(')');
		String initdata = originalString.substring(start + 1, end);
		String[] result = StringUtils.split(initdata, ',');
		return result;
	}

	public <T extends DiscrepancyTableInterface, Z extends DiscrepancyTableInterface> DiscrepancyReport generateDiscrepancyReport(final List<ScriptTableInfo> compareList  ,final List<JDBCTablseSchema> dataList)
			throws SQLException, ParsePropertyException, InvalidFormatException, IOException {
		
		CompareDBTableAnd64TableService baseCompareService = getBaseCompareService() ;

		final Map<String, Z> scriptDataSourceMap = (Map<String, Z>) baseCompareService.getCommonDataMap(compareList);
		final Map<String, T> JDBCTablseSchemaDataSourceMap = (Map<String, T>) baseCompareService.getCommonDataMap(dataList);

		System.out.println("scriptDataSourceMap size: " + scriptDataSourceMap.size());
		System.out.println("JDBCTablseSchemaDataSourceMap size: " + JDBCTablseSchemaDataSourceMap.size());

		DiscrepancyReport report = baseCompareService.comparationStandardForScript64Table(scriptDataSourceMap, JDBCTablseSchemaDataSourceMap);

		return report;
	}
	
	public <T extends DiscrepancyTableInterface, Z extends DiscrepancyTableInterface> DiscrepancyReport generateDiscrepancyReport(final JDBCTableMiningResult initResult)
			throws SQLException, ParsePropertyException, InvalidFormatException, IOException {
		
		List<String> eceptionList = initResult.getExceptionList();
		List<String> excludeList = new ArrayList<String>();
		for (String tmp : eceptionList) {
			System.out.println(extractTable(tmp)[0]);
			excludeList.add(extractTable(tmp)[0]);
		}
		System.out.println("excludeList: " + excludeList.size());

		List<ScriptTableInfo> initInputData = initResult.getInitInputData();
		System.out.println("initInputData size: " + initInputData.size());
		List<DiscrepancyTableInterface> compareList = new ArrayList<DiscrepancyTableInterface>();
		for (ScriptTableInfo script : initInputData) {
			String tableName = StringUtils.trim(script.getTableName()).toLowerCase();
			if (!excludeList.contains(tableName)) {
				compareList.add(script);
			}
		}
		System.out.println("compareList size: " + compareList.size());

		List<JDBCTablseSchema> dataList = initResult.getResult();
		System.out.println(initResult.getResult().size());

		CompareDBTableAnd64TableService baseCompareService = getBaseCompareService() ;

		final Map<String, Z> scriptDataSourceMap = (Map<String, Z>) baseCompareService.getCommonDataMap(compareList);
		final Map<String, T> JDBCTablseSchemaDataSourceMap = (Map<String, T>) baseCompareService.getCommonDataMap(dataList);

		System.out.println("scriptDataSourceMap size: " + scriptDataSourceMap.size());
		System.out.println("JDBCTablseSchemaDataSourceMap size: " + JDBCTablseSchemaDataSourceMap.size());

		DiscrepancyReport report = baseCompareService.comparationStandardForScript64Table(scriptDataSourceMap, JDBCTablseSchemaDataSourceMap);

		return report;
	}
}
