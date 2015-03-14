package com.iisigroup.tradition.dao.jdbc;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.sql.DataSource;

import net.sf.jxls.exception.ParsePropertyException;
import net.sf.jxls.transformer.XLSTransformer;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.robert.study.discrepancy.model.DiscrepancyTableInterface;
import org.robert.study.service.CompareDBTableAnd64TableDisplayService;
import org.robert.study.service.CompareDBTableAnd64TableService;
import org.robert.study.tradition.dao.GenericDao;
import org.robert.study.utils.POIUtils;
import org.robert.study.utils.Utils;

import com.iisi.doc.process.WordExtract;
import com.iisi.report.model.ColumnDsicrepancyRow;
import com.iisi.report.model.ReportBody;
import com.iisi.rl.table.DataTable;
import com.iisi.rl.table.discrepancy.DiscrepancyColumn;
import com.iisi.rl.table.discrepancy.DiscrepancyReport;
import com.iisi.rl.table.discrepancy.DiscrepancyTable;
import com.iisi.rl.table.jdbc.schema.JDBCColumnSachema;
import com.iisi.rl.table.jdbc.schema.JDBCTableMiningResult;
import com.iisi.rl.table.jdbc.schema.JDBCTablseSchema;
import com.iisi.rl.table.script.ScriptColumnInfo;
import com.iisi.rl.table.script.ScriptTableInfo;
import com.iisigroup.toolkits.service.CompareDBTableAnd64TableDisplayServiceImpl;
import com.iisigroup.toolkits.service.CompareDBTableAnd64TableServiceImpl;
import com.iisigroup.toolkits.service.CompareDBTableAnd64TableServiceV2Impl;
import com.iisigroup.toolkits.service.CompareDBTableAndSqlScriptServiceImpl;
import com.iisigroup.toolkits.service.JdbcProcessServiceImpl;
import com.iisigroup.toolkits.service.ScanInspectServiceImpl;

public class JDBCConnectionTest {
	 private final static String DROP_STATEMENT = ".*DROP.*TABLE.*";
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		try {
	//			 System.out.println(String.format("%1$,02d", 1));
//			testMethod000() ;
//			testMethod001();
			
			
//			testGenerateCSV();
			
//			analysis00();
			
			testSelectAllTables();
			
//			convertTest();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	public static void testMethod000() throws SQLException{
		BasicDataSource ds = new BasicDataSource();
		ds.setDriverClassName(defaulDriverClassName);
		ds.setUsername(defaulUsername);
        ds.setPassword(defaulPassword);
        ds.setUrl(defaulRCUrl);
		
        GenericDaoImpl genericDao = new GenericDaoImpl();
        genericDao.setDataSource(ds);
        Map<String, Set<String>> indexmap = genericDao.getIndexInfo("RCDF005L");
        for(String indexName :indexmap.keySet()){
        	System.out.print("index: "+indexName);
        	String value = StringUtils.join(indexmap.get(indexName),',');
        	System.out.println(",  index value: "+value);
        }
	}
	public static void testMethod001() throws SQLException, ParsePropertyException, InvalidFormatException, IOException{
		BasicDataSource ds = new BasicDataSource();
		ds.setDriverClassName(defaulDriverClassName);
		ds.setUsername(defaulUsername);
        ds.setPassword(defaulPassword);
        ds.setUrl(defaulRCUrl);
		
        GenericDaoImpl genericDao = new GenericDaoImpl();
        genericDao.setDataSource(ds);
        Map<String, Set<String>> indexmap = genericDao.getIndexInfo("RCDF005L");
        for(String indexName :indexmap.keySet()){
        	System.out.print("index: "+indexName);
        	String value = StringUtils.join(indexmap.get(indexName),',');
        	System.out.println(",  index value: "+value);
        }
        JdbcProcessServiceImpl aJdbcProcessServiceImpl =new JdbcProcessServiceImpl();
        JDBCTableMiningResult initResult = aJdbcProcessServiceImpl.retirieveTableInfoFromDBAccordingSQL(new File("/home/weblogic/Desktop/test_RC_script"), ds);
        
        CompareDBTableAndSqlScriptServiceImpl aCompareDBTableAndSqlScriptServiceImpl = new CompareDBTableAndSqlScriptServiceImpl();
		
        DiscrepancyReport aa = aCompareDBTableAndSqlScriptServiceImpl.generateDiscrepancyReport(initResult);
        System.out.println(aa.getSameTableName().size());
        
	}
	private static final String defaulDriverClassName = "com.informix.jdbc.IfxDriver";
    private static final String defaulUsername = "srismapp";
    private static final String defaulPassword = "ris31123";
    private static final String defaulRLUrl = "jdbc:informix-sqli://192.168.9.94:4526/teun0020:informixserver=aix2;DB_LOCALE=zh_tw.utf8;CLIENT_LOCALE=zh_tw.utf8;GL_USEGLU=1";
    private static final String defaulRRUrl = "jdbc:informix-sqli://192.168.9.94:4526/teun0000:informixserver=aix2;DB_LOCALE=zh_tw.utf8;CLIENT_LOCALE=zh_tw.utf8;GL_USEGLU=1";
    private static final String defaulRCUrl = "jdbc:informix-sqli://192.168.9.94:4526/chun0000:informixserver=aix2;DB_LOCALE=zh_tw.utf8;CLIENT_LOCALE=zh_tw.utf8;GL_USEGLU=1";

	private static  Pattern sqlPattern = Pattern.compile(".*.sql");
	
	
	private static void testSelectAllTables() throws SQLException, ParsePropertyException, InvalidFormatException, IOException{
		String rcUrl =defaulRCUrl;
		String rrUrl =defaulRRUrl;
		String rlUrl =defaulRLUrl;
		String rcScript = "/home/weblogic/Desktop/create script_RC";
		String rrScript = "/home/weblogic/Desktop/create script_RR";
		String rlScript = "/home/weblogic/Desktop/create script_RL";
		
		String rcDoc = "/home/weblogic/Desktop/6-4-TableSchema_RC";
		String rrDoc = "/home/weblogic/Desktop/6-4-TableSchema_RR";
		String rlDoc = "/home/weblogic/Desktop/6-4-TableSchema_RL";
		
		
		String rcSerialFile = "/home/weblogic/Desktop/RC_jdbclist";
		String rrSerialFile = "/home/weblogic/Desktop/RR_jdbclist";
		String rlSerialFile = "/home/weblogic/Desktop/RL_jdbclist";
		String rcSerialScriptTableInfoFile = "/home/weblogic/Desktop/RC_ScriptTableInfoList";
		String rrSerialScriptTableInfoFile = "/home/weblogic/Desktop/RR_ScriptTableInfoList";
		String rlSerialScriptTableInfoFile = "/home/weblogic/Desktop/RL_ScriptTableInfoList";
		
		
		CompareDBTableAnd64TableDisplayService displayer = new CompareDBTableAnd64TableDisplayServiceImpl();
		
		DiscrepancyReport rcScriptDBReport = testSelectAllTablesScriptDB(rcUrl, rcScript, rcSerialFile, rcSerialScriptTableInfoFile, "RC");
		DiscrepancyReport rrScriptDBReport = testSelectAllTablesScriptDB(rrUrl, rrScript, rrSerialFile, rrSerialScriptTableInfoFile, "RR");
		DiscrepancyReport rlScriptDBReport = testSelectAllTablesScriptDB(rlUrl, rlScript, rlSerialFile, rlSerialScriptTableInfoFile, "RL");
		
		DiscrepancyReport rcDocDBReport = testSelectAllTablesDocDB(rcUrl, rcDoc, rcSerialFile, rcSerialScriptTableInfoFile, "RC");
		DiscrepancyReport rrDocDBReport = testSelectAllTablesDocDB(rrUrl, rrDoc, rrSerialFile, rrSerialScriptTableInfoFile, "RR");
		DiscrepancyReport rlDocDBReport = testSelectAllTablesDocDB(rlUrl, rlDoc, rlSerialFile, rlSerialScriptTableInfoFile, "RL");
		
		final Map<String, String[]>  displayRCData= displayer.sumaryComparison(displayer.summaryData(rcScriptDBReport),displayer.summaryData(rcDocDBReport));		
		final Map<String, String[]>  displayRRData= displayer.sumaryComparison(displayer.summaryData(rrScriptDBReport),displayer.summaryData(rrDocDBReport));	
		final Map<String, String[]>  displayRLData= displayer.sumaryComparison(displayer.summaryData(rlScriptDBReport),displayer.summaryData(rlDocDBReport));	
		
		generateCSV(displayRCData, "RC");
		generateCSV(displayRRData, "RR");
		generateCSV(displayRLData, "RL");
	}
	private static void testGenerateCSV(){
		File rlDataFile = new File("/home/weblogic/Desktop/RL_SQL_disprenpancyReport201305271841.dat");
		DiscrepancyReport sqlScriptDbReport = Utils.deserialization(rlDataFile);
		
		File rlDocDataFile = new File("/home/weblogic/Desktop/RL_Doc_disprenpancyReport201305271854.dat");
		DiscrepancyReport docDbReport = Utils.deserialization(rlDocDataFile);
		
		CompareDBTableAnd64TableDisplayService displayer = new CompareDBTableAnd64TableDisplayServiceImpl();
		final Map<String, String[]>  displayRLData= displayer.sumaryComparison(displayer.summaryData(sqlScriptDbReport),displayer.summaryData(docDbReport));
		generateCSV(displayRLData, "RL");
	}
	private static void generateCSV(final Map<String, String[]>  displayRCData ,String systemCode){
		final List<String> outputRClist =new ArrayList<String>();
		for(String tableName:displayRCData.keySet()){
			String[] reasons = displayRCData.get(tableName);
			outputRClist.add(tableName+" , "+StringUtils.join(reasons ,","));		
		}
		Utils.outputFile("/home/weblogic/Desktop/"+systemCode+".csv", outputRClist );
	}
	private static void convertTest(){
	    JDBCTableMiningResult data = Utils.deserialization(new File("E:/Users/Robert/Desktop/table_schema_2013_0526/test/RL_script_data"));
	    List<JDBCTablseSchema> list = data.getResult();
	    List<ScriptTableInfo> InitInputData = data.getInitInputData();
	 System.out.println(list.size());
	}
	private static ScriptTableInfo covertFromJDBCTablseSchema(final JDBCTablseSchema sourceData){
	    final  ScriptTableInfo result =new ScriptTableInfo();
	    final String tablename = sourceData.getTableName();
	    final   Map<String, Set<String>> indexInfo = sourceData.getIndexInfo();
	    
	    final    List<JDBCColumnSachema> JDBCColumnSachemas = sourceData.getDataColumnInfos();
	    
	    result.setTableName(tablename);
	    final   List<ScriptColumnInfo> dataColumnInfos = result.getDataColumnInfos();
	    
	    return result;
	}
	private static DiscrepancyReport testSelectAllTablesScriptDB(final String defaulUrl ,final String rXScript , final String rXSerialFile,final String rxSerialScriptTableInfoFile  ,final String systemCode) throws SQLException, ParsePropertyException, InvalidFormatException, IOException{
		BasicDataSource ds = new BasicDataSource();
		ds.setDriverClassName(defaulDriverClassName);
		ds.setUsername(defaulUsername);
        ds.setPassword(defaulPassword);
        ds.setUrl(defaulUrl);
        JdbcProcessServiceImpl aJdbcProcessServiceImpl =new JdbcProcessServiceImpl();
        List<JDBCTablseSchema> jdbclist = aJdbcProcessServiceImpl.retirieveTableInfoFromDB(ds);
        System.out.println(jdbclist.size());
        File folder =new File(rXScript);
        final ScanInspectServiceImpl scanInspect = new ScanInspectServiceImpl();
		List<ScriptTableInfo> ScriptTableInfoList = new ArrayList<ScriptTableInfo>();
		 
		for(File file :folder.listFiles()){
			if(file.isFile() && sqlPattern.matcher(file.getName()).matches()){
				ScriptTableInfo aDataTable = scanInspect.convertTOUnit(file);
				ScriptTableInfoList.add(aDataTable);
				 
			}
		}
		Utils.serialization(jdbclist, new File(rXSerialFile));
		Utils.serialization(ScriptTableInfoList, new File(rxSerialScriptTableInfoFile));
		CompareDBTableAndSqlScriptServiceImpl aCompareDBTableAndSqlScriptServiceImpl = new CompareDBTableAndSqlScriptServiceImpl();
		
		
		DiscrepancyReport report = aCompareDBTableAndSqlScriptServiceImpl.generateDiscrepancyReport(ScriptTableInfoList, jdbclist);
		
		CompareDBTableAnd64TableDisplayService displayer = new CompareDBTableAnd64TableDisplayServiceImpl();
        displayer.display(report);
        
		
		 String outfileName = "/home/weblogic/Desktop/"
	                + "//"+systemCode+"_SQL_disprenpancyReport" + new SimpleDateFormat("yyyyMMddHHmm").format(new Date())
	                + ".xls";
	   displayer.exportReport3(report, outfileName);
	   
	   String reportfileName = "/home/weblogic/Desktop/"
               + "//"+systemCode+"_SQL_disprenpancyReport" + new SimpleDateFormat("yyyyMMddHHmm").format(new Date())
               + ".dat";
	   Utils.serialization(report, new File(reportfileName));
	   return report;
	}
	private  static <T extends DiscrepancyTableInterface, Z extends DiscrepancyTableInterface> DiscrepancyReport testSelectAllTablesDocDB(final String defaulUrl ,final String rXDoc , final String rXSerialFile,final String rxSerialDocTableInfoFile  ,final String systemCode) throws SQLException, ParsePropertyException, InvalidFormatException, IOException{
		BasicDataSource ds = new BasicDataSource();
		ds.setDriverClassName(defaulDriverClassName);
		ds.setUsername(defaulUsername);
        ds.setPassword(defaulPassword);
        ds.setUrl(defaulUrl);
        JdbcProcessServiceImpl aJdbcProcessServiceImpl =new JdbcProcessServiceImpl();
        List<JDBCTablseSchema> jdbclist = aJdbcProcessServiceImpl.retirieveTableInfoFromDB(ds);
        
		List<DataTable> docList = new ArrayList<DataTable>();
		final File rc64TableDocFileFolder =  new File(rXDoc);
		
		final WordExtract aWordExtract = new WordExtract();
		final List<File> alist = aWordExtract.getQualifiedFile(rc64TableDocFileFolder);
		for (File aFile : alist) {
             String fileName = aFile.getAbsolutePath();
             DataTable aDataTable = aWordExtract.convertTOUnit(fileName);
             docList.add(aDataTable);
        }
		
		Utils.serialization(jdbclist, new File(rXSerialFile));
		Utils.serialization(docList, new File(rxSerialDocTableInfoFile));
		CompareDBTableAndSqlScriptServiceImpl aCompareDBTableAndSqlScriptServiceImpl = new CompareDBTableAndSqlScriptServiceImpl();
		CompareDBTableAnd64TableService baseCompareService  = new CompareDBTableAnd64TableServiceV2Impl();
		
		final Map<String, Z> JDBCTablseSchemaDataSourceMap= (Map<String, Z>) baseCompareService.getCommonDataMap(jdbclist);
		final Map<String, T>   docSourceMap  = (Map<String, T>) baseCompareService.getCommonDataMap(docList);
		
		System.out.println("scriptDataSourceMap size: " + docSourceMap.size());
		System.out.println("JDBCTablseSchemaDataSourceMap size: " + JDBCTablseSchemaDataSourceMap.size());

		DiscrepancyReport report = baseCompareService.comparationStandardForScript64Table(docSourceMap, JDBCTablseSchemaDataSourceMap);
		String outfileName = "/home/weblogic/Desktop/"
	                + "//"+systemCode+"_Doc_disprenpancyReport" + new SimpleDateFormat("yyyyMMddHHmm").format(new Date())
	                + ".xls";
		CompareDBTableAnd64TableDisplayService displayer = new CompareDBTableAnd64TableDisplayServiceImpl();
		
	    displayer.exportReport3(report, outfileName);
	   
	   String reportfileName = "/home/weblogic/Desktop/"
               + "//"+systemCode+"_Doc_disprenpancyReport" + new SimpleDateFormat("yyyyMMddHHmm").format(new Date())
               + ".dat";
	   Utils.serialization(report, new File(reportfileName));
	   return report;
	}
	
	public static  <T extends DiscrepancyTableInterface, Z extends DiscrepancyTableInterface> void analysis00() throws IOException, ParsePropertyException, InvalidFormatException{
		String systemCode ="RL";
		File rcDataFile = new File("/home/weblogic/Desktop/RC_SQL_disprenpancyReport201305271359.dat");
		DiscrepancyReport sqlScriptDbReport = Utils.deserialization(rcDataFile);
		
		CompareDBTableAnd64TableDisplayService displayer = new CompareDBTableAnd64TableDisplayServiceImpl();
        displayer.display(sqlScriptDbReport);
        final Map<String, String> dbScriptSumary = displayer.summaryData(sqlScriptDbReport);
        //DB && script
        
        
        String rcSerialFile = "/home/weblogic/Desktop/RC_jdbclist";
		List<JDBCTablseSchema> jdbclist = Utils.deserialization(new File(rcSerialFile));
		List<DataTable> docList = new ArrayList<DataTable>();
		final File rc64TableDocFileFolder =  new File("/home/weblogic/Desktop/6-4-TableSchema_RC");
		
		final WordExtract aWordExtract = new WordExtract();
		final List<File> alist = aWordExtract.getQualifiedFile(rc64TableDocFileFolder);
		for (File aFile : alist) {
             String fileName = aFile.getAbsolutePath();
             DataTable aDataTable = aWordExtract.convertTOUnit(fileName);
             docList.add(aDataTable);
        }
		System.out.println(docList.size());
		
		CompareDBTableAnd64TableService baseCompareService  = new CompareDBTableAnd64TableServiceV2Impl();
		
		final Map<String, Z> JDBCTablseSchemaDataSourceMap= (Map<String, Z>) baseCompareService.getCommonDataMap(jdbclist);
		final Map<String, T>   docSourceMap  = (Map<String, T>) baseCompareService.getCommonDataMap(docList);
		
		System.out.println("scriptDataSourceMap size: " + docSourceMap.size());
		System.out.println("JDBCTablseSchemaDataSourceMap size: " + JDBCTablseSchemaDataSourceMap.size());

		DiscrepancyReport report = baseCompareService.comparationStandardForScript64Table(docSourceMap, JDBCTablseSchemaDataSourceMap);
		
		final Map<String, String> dbDocSumary = displayer.summaryData(report);
		
		final Map<String, String[]>  displayData= displayer.sumaryComparison(dbScriptSumary,dbDocSumary);		
		final List<String> outputlist =new ArrayList<String>();
		for(String tableName:displayData.keySet()){
			String[] reasons = displayData.get(tableName);
			outputlist.add(tableName+" , "+StringUtils.join(reasons ,","));		
		}
		Utils.outputFile("/home/weblogic/Desktop/RC.csv", outputlist );
	}

	
}
