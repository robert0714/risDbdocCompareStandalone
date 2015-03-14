package com.iisigroup.toolkits.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.robert.study.service.PseudoDataService;
import org.robert.study.tradition.dao.GenericDao;
import org.robert.study.utils.POIUtils;

public class PseudoDataServiceImpl implements PseudoDataService {
    private final String SHEET_NAME = "template";
    private GenericDao dao;

    @Override
    public void readConfigData(final File file) {

    }

    @Override
    public void readXLS(final String tableName, final File importFile) throws Exception {
        InputStream inp = null;
        try {
        	Map<String, Integer> metaMap = dao.getColumnMetaInfos(tableName);// java.sql.Types
        	
            inp = new FileInputStream(importFile);
            Workbook wb = WorkbookFactory.create(inp);
            org.apache.poi.ss.usermodel.Sheet sheet = wb.getSheet(SHEET_NAME);
            
            if (sheet == null) {
            	sheet = wb.getSheetAt(0);
            	if(sheet==null)
                throw new Exception("sheet名稱為" + SHEET_NAME + "沒有抓取到");
            }
            Map<String, Integer> headData = getHeadColumnPosition(sheet);
            List<String[]> initData = POIUtils.processFromSheet(sheet);
            List<String> columnNameList = dao.getColumnNameList(tableName);
            String[] line = columnNameList.toArray(new String[] {});
            Map<String, Integer> realHeadData = new HashMap<String, Integer>();
            for (String columnName : line) {
                Integer position = null;
                for (String tmp : headData.keySet()) {
                    if (columnName.equalsIgnoreCase(tmp)) {
                        position = headData.get(tmp);
                        break;
                    }
                }
                if (position != null) {
                    realHeadData.put(columnName, position);
                }
            }
//            for (int i=1;i<initData.size();++i) {
//            	String[] raw =initData.get(i);
//                List<Object> valueList = new ArrayList<Object>();
//                for (String colName : columnNameList) {
//                	Integer index = realHeadData.get(colName);
//                	if(index!=null){
//                		String tmpValue = raw[index];                    
//                        Integer colTypeCode = metaMap.get(colName);
//                        if(colTypeCode!=null){
//                        	 Object value = covertValueTypeFromStringType(tmpValue, colTypeCode);
//                             valueList.add(value);
//                        }                       
//                	}                    
//                }
//                Object[] valueDatas = valueList.toArray(new Object[] {});
//                dao.insertData(tableName, columnNameList.toArray(new String[] {}), valueDatas);
//            }
           String[] keyColums = dao.getPimaryKeys(tableName);
            List<Object[]> dayaList = new ArrayList<Object[]>();
            outer:
            for (int i=1;i<initData.size();++i) {
            	String[] raw =initData.get(i);
                List<Object> valueList = new ArrayList<Object>();
                inner :for (String colName : columnNameList) {
                	
                	Integer index = realHeadData.get(colName);
                	if(index!=null){
                		String tmpValue = raw[index];                    
                        Integer colTypeCode = metaMap.get(colName);
                        if(colTypeCode!=null){
                        	 Object value = covertValueTypeFromStringType(tmpValue, colTypeCode);
                        	 String tmp=null;
                        	 if(keyColums.length>0 && ArrayUtils.contains(keyColums, colName) && value instanceof String){
                        		tmp =(String)value;
                        		if(tmp.trim().length()>0){
                        			 valueList.add(value); 
                        		}else{
                        			continue outer;
                        		}
                        	 }else{
                        		 valueList.add(value); 
                        	 }
                            
                        }                       
                	}                    
                }
                Object[] valueDatas = valueList.toArray(new Object[] {});
                boolean notQulified =false;
                for(Object unit: valueDatas){
                	if(unit instanceof String){
                		String tmp = (String)unit;
                		if(StringUtils.isNotBlank(tmp)){
                			
                		}
                	}
                }
                if(!notQulified){
                	 dayaList.add(valueDatas);
                }               
            }
            dao.batchInsertData(tableName, columnNameList.toArray(new String[] {}), dayaList);

        } finally {
            if (inp != null) {
                try {
                    inp.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Object covertValueTypeFromStringType(String value, Integer colTypeCode) {
        Object result = value;
        switch (colTypeCode) {
        case java.sql.Types.INTEGER:
            result = Integer.valueOf(Double.valueOf(value).intValue());
            break;
        case java.sql.Types.BOOLEAN:
            result = Boolean.valueOf(value);
            break;
        default:
            break;
        }
        if(result==null){
        	result="";
        }
        return result;
    }

    @Override
    public Workbook generateTemplateXLS(final String tableName) throws SQLException {
        Workbook wb = new HSSFWorkbook();
        org.apache.poi.ss.usermodel.Sheet sheet = wb.createSheet(SHEET_NAME);
        CellStyle style01 = POIUtils.buildCellStyle(sheet, 13, CellStyle.ALIGN_CENTER, "標楷體", false);

        CellStyle style02 = POIUtils.buildCellStyle(sheet, 10, CellStyle.ALIGN_LEFT, true, true,
                (int) IndexedColors.WHITE.getIndex());

        List<String> columnNameList = dao.getColumnNameList(tableName);

        String[] line = columnNameList.toArray(new String[] {});

        for (int j = 0; j < line.length; ++j) {
            String value = line[j];
            POIUtils.setSheetCellPosValue(sheet, j, 0, value, style02, 30);
            sheet.autoSizeColumn(j);
        }

        sheet.createFreezePane(0, 1, 0, 1);
        return wb;
    }

    @Override
    public void setDao(GenericDao dao) {
        this.dao = dao;
    }

    public DataSource setupDataSource() {
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName("com.informix.jdbc.IfxDriver");
        ds.setUsername("srismapp");
        ds.setPassword("ris31123");
        ds.setUrl("jdbc:informix-sqli://192.168.9.94:4526/teun0020:informixserver=aix2;DB_LOCALE=zh_tw.utf8;CLIENT_LOCALE=zh_tw.utf8;GL_USEGLU=1");

        return ds;
    }

    @Override
    public void printDataSourceStats(DataSource ds) {
        BasicDataSource bds = (BasicDataSource) ds;
        System.out.println("NumActive: " + bds.getNumActive());
        System.out.println("NumIdle: " + bds.getNumIdle());
    }

    public void shutdownDataSource(DataSource ds) throws SQLException {
        BasicDataSource bds = (BasicDataSource) ds;
        bds.close();
    }

    private Map<String, Integer> getHeadColumnPosition(final Sheet sheet) {
        Map<String, Integer> result = new HashMap<String, Integer>();
        Row header = sheet.getRow(0);
        int headerNumberOfCells = header.getPhysicalNumberOfCells();// 得到column數目
        for (int i = 0; i < headerNumberOfCells; ++i) {
            Cell cell = header.getCell(i);
            String headName = cell.getStringCellValue().trim();
            result.put(headName, Integer.valueOf(i));
        }
        return result;
    }

}
