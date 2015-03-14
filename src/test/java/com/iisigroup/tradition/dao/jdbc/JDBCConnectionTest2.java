package com.iisigroup.tradition.dao.jdbc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.sql.DataSource;

import net.sf.jxls.exception.ParsePropertyException;
import net.sf.jxls.transformer.XLSTransformer;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.io.FileUtils;
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

public class JDBCConnectionTest2 {
	 private final static String DROP_STATEMENT = ".*DROP.*TABLE.*";
	/**
	 * @param args
	 */
    public static void main(String[] args) {
//	Map<String,List<String>>  data=   deserialization(new File("/home/weblogic/Desktop/Xldfserialization.dat"));
//	for(String key : data.keySet()){
//	    System.out.println(key);
//	     List<String> list = data.get(key);
//	     
//	}
//	try {
//	    testMethod001();
//
//	} catch (Exception e) {
//	    e.printStackTrace();
//	}
	JDBCConnectionTest2 mm =new JDBCConnectionTest2();
	mm.showSITdiscrepenciesAndhibewrrtnaties();
	
	
	//XLDFW0DH
//	List<String> lines = mm.getDiscrepency("XLDFW0DH");
//	for(String columnName : lines){
//	    System.out.println(columnName);
//	}
    }
    public static <T> T deserialization(File inputFile) {
        T result = null;
        ObjectInputStream ois = null;

        try {
            ois = new ObjectInputStream(new FileInputStream(inputFile));
            // int i = ois.readInt();
            // String today = (String) ois.readObject();
            result = (T) ois.readObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return result;
    }
    public static void testMethod001() throws SQLException, ParsePropertyException, InvalidFormatException, IOException {
	BasicDataSource ds = new BasicDataSource();
	ds.setDriverClassName(defaulDriverClassName);
	ds.setUsername(defaulUsername);
	ds.setPassword(defaulPassword);
	ds.setUrl(defaulRLUrl);

	GenericDaoImpl genericDao = new GenericDaoImpl();
	genericDao.setDataSource(ds);

	List<String> columnList = genericDao.getColumnNameList("XLDFM10M");
	for (String columnName : columnList) {
	    System.out.println(columnName);
	}
    }
	private static final String defaulDriverClassName = "com.informix.jdbc.IfxDriver";
    private static final String defaulUsername = "srismapp";
    private static final String defaulPassword = "ris31123";
    private static final String defaulRLUrl = "jdbc:informix-sqli://192.168.10.18:4526/teun0020:informixserver=aix2;DB_LOCALE=zh_tw.utf8;CLIENT_LOCALE=zh_tw.utf8;GL_USEGLU=1";
    private static final String defaulRRUrl = "jdbc:informix-sqli://192.168.10.18:4526/teun0000:informixserver=aix2;DB_LOCALE=zh_tw.utf8;CLIENT_LOCALE=zh_tw.utf8;GL_USEGLU=1";
    private static final String defaulRCUrl = "jdbc:informix-sqli://192.168.10.18:4526/chun0000:informixserver=aix2;DB_LOCALE=zh_tw.utf8;CLIENT_LOCALE=zh_tw.utf8;GL_USEGLU=1";

	private static  Pattern sqlPattern = Pattern.compile(".*.sql");
	
    
    public void showSITdiscrepencies(){
	try {
	    List<String> tables = FileUtils.readLines(new File("/home/weblogic/Desktop/xldfFiles"));
	    for(String tableName :tables){
		if(!tableName.matches("XLDF[S|W|R].*")){		    
		    List<String> discrepencies = getDiscrepency(tableName);
			if(CollectionUtils.isNotEmpty(discrepencies)){
			    System.out.println("................");
			    System.out.println(tableName);
			    for(String columnName: discrepencies){
				 System.out.println(columnName);
			    }
			} 
		}
		
		
	    }
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }
    public List<String> getDiscrepency(final String tableName) {
	List<String> result = new ArrayList<String>();
	BasicDataSource ds = new BasicDataSource();
	ds.setDriverClassName(defaulDriverClassName);
	ds.setUsername(defaulUsername);
	ds.setPassword(defaulPassword);
	ds.setUrl(defaulRLUrl);

	GenericDaoImpl genericDao = new GenericDaoImpl();
	genericDao.setDataSource(ds);
	try {
	    List<String> xldfColumnList = genericDao.getColumnNameList(tableName.trim());
	    List<String> rldfColumnList = genericDao.getColumnNameList(tableName.replace("XLDF", "RLDF").trim());
	    Collection aaa = CollectionUtils.subtract(xldfColumnList, rldfColumnList);
	    Collection bb = CollectionUtils.subtract(rldfColumnList, xldfColumnList);
	    final Set<String> discrepencies  = new HashSet<String>();
//	    discrepencies.addAll(aaa);
	    discrepencies.addAll(bb);
	    discrepencies.remove("transaction_id");
	    discrepencies.remove("sequence_id");
	    discrepencies.remove("serial_no");
	    discrepencies.remove("state");
	    discrepencies.remove("action");
	    discrepencies.remove("lock_mode");
	    discrepencies.remove("select_mode");
	    
	    result.addAll(discrepencies);
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	return result;
    }
    public List<String> getDiscrepencyFromColumns(final String tableName) {
	List<String> result = new ArrayList<String>();
	BasicDataSource ds = new BasicDataSource();
	ds.setDriverClassName(defaulDriverClassName);
	ds.setUsername(defaulUsername);
	ds.setPassword(defaulPassword);
	ds.setUrl(defaulRLUrl);

	GenericDaoImpl genericDao = new GenericDaoImpl();
	genericDao.setDataSource(ds);
	try {
	    List<String> xldfColumnList = genericDao.getColumnNameList(tableName.trim());
	   
	    
	    result.addAll(xldfColumnList);
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	return result;
    }
    
    public void showSITdiscrepenciesAndhibewrrtnaties(){
	Map<String, List<String>> data = deserialization(new File("/home/weblogic/Desktop/Xldfserialization.dat"));
	for (String key : data.keySet()) {
	    System.out.println(key);
	    String tableName = StringUtils.remove(key, "Type");
	    List<String> list = data.get(key);
	    
	    final List<String> columnList = getDiscrepencyFromColumns(tableName);
	    
	    for(String attributeName :list){
		String columnName = retrieveColumName(attributeName) .toLowerCase();
		if(!columnList.contains(columnName)){
		    System.out.println("attributeName:  "+attributeName);
		}
	    }
	}
    }
    public Set<String> getAbnormalSet(){
	 Set<String> result = new HashSet<String>();
	 
	 return result;
    }
    
    protected String retrieveColumName(final String attributeName) {
	String[] attributeArray = StringUtils.splitByCharacterTypeCamelCase(attributeName);
	return StringUtils.join(attributeArray, "_");
    }
	

	
}
