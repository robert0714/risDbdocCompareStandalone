package com.iisigroup.toolkits.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.SerializationUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.robert.study.utils.POIUtils;
import org.robert.study.utils.Utils;

import com.iisi.rl.table.DataColumnInfo;
import com.iisi.rl.table.DataTable;
import com.iisi.rl.table.script.ScriptColumnInfo;
import com.iisi.rl.table.script.ScriptTableInfo;

public class ExcelManipulationServiceImpl {
    private final String SHEET_NAME = "template";

    public static void testMethod01() {
        ExcelManipulationServiceImpl main = new ExcelManipulationServiceImpl();
        Workbook aWorkbook = main.generateTemplate();
        FileOutputStream fileOut = null;
        try {
            fileOut = new FileOutputStream("z://aaaaa.xls");
            aWorkbook.write(fileOut);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileOut != null) {
                try {
                    fileOut.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void outputCSVFile(final List<String[]> srcList, final String outputFileName)   {
        final Iterator<String[]> iterator = srcList.iterator();
        BufferedWriter bw = null;
        
        try {
            bw = new BufferedWriter(new FileWriter(new File(outputFileName)));
            while(iterator.hasNext()){
                final String[] strArray = iterator.next();
                bw.write(StringUtils.join(strArray,","));
                bw.newLine();
                iterator.remove();
            }
            
        } catch (Exception e) {
            // TODO Auto-generated catch block
            // LOGGER.error("", e);
        }finally{
            if(bw!=null){
                try {
                    bw.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    // LOGGER.error("", e);
                }
            }
        }

    }
    public void outputFile(final List<String[]> srcList, final String outputFileName) {
        final Workbook wb = new HSSFWorkbook();
        final org.apache.poi.ss.usermodel.Sheet sheet = wb.createSheet(SHEET_NAME);
        final CellStyle style02 = POIUtils.buildCellStyle(sheet, 10, CellStyle.ALIGN_LEFT, true, true,
                (int) IndexedColors.WHITE.getIndex());

        List<String[]> aList = new ArrayList<String[]>();
        aList.add(new String[] { "檔名", "敘述" });
        aList.addAll(srcList);
        for (int i = 0; i < aList.size(); ++i) {
            String[] line = aList.get(i);

            for (int j = 0; j < line.length; ++j) {
                String value = line[j];
                POIUtils.setSheetCellPosValue(sheet, j, i, value, style02, 30);
                sheet.autoSizeColumn(j);
            }
        }
        sheet.createFreezePane(0, 1, 0, 1);
        FileOutputStream fileOut = null;
        try {
            fileOut = new FileOutputStream(outputFileName);
            wb.write(fileOut);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileOut != null) {
                try {
                    fileOut.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Map<String, String> generateFinalScript(final List<DataTable> aList) {
        Map<String, String> result = new HashMap<String, String>();
        for (DataTable table : aList) {
            ScriptTableInfo script = covertFrom64TableDoc(table);
            result.put(table.getTableName() + ".sql", script.getStatement());
        }
        return result;
    }

    public void generateSummary(final List<DataTable> aList) {
        for (DataTable table : aList) {
            if (table != null) {
                System.out.println("------------");
                System.out.println(table.getFileName());
                System.out.println(table.getInfomation());
                System.out.println("------------");
            }
        }
    }

    public Map<String, String> generateFinalChineseColumn(final List<DataTable> aList) {
        Map<String, String> result = new HashMap<String, String>();
        for (DataTable table : aList) {
            if (table != null) {
                String content = covertToTXTFrom64TableDoc(table);
                result.put(table.getTableName() + ".csv", content);
            }
        }
        return result;
    }

    public String covertToTXTFrom64TableDoc(final DataTable dataTable) {
        final StringBuffer sbf = new StringBuffer();
        String result = StringUtils.EMPTY;
        if (dataTable != null) {
            List<DataColumnInfo> dataColumnInfos = dataTable.getDataColumnInfos();
            if (CollectionUtils.isNotEmpty(dataColumnInfos)) {
                for (DataColumnInfo column : dataColumnInfos) {
                    sbf.append(column.getColumnName()).append(',').append(column.getChineseName()).append("\r");
                }
            }
            result = sbf.toString();
        }
        return result;
    }

    /***
     * @return Map<String,String> (fileName,content)
     * 
     * ***/
    public Map<String, String> generateInitialScript(final List<DataTable> aList) {
        Map<String, String> result = new HashMap<String, String>();
        for (DataTable table : aList) {
            result.put(table.getTableName() + ".txt", covertTxtFrom64TableDoc(table));
        }
        return result;
    }

    private static String get64TableFileName(final String symbolCode, final String prefix, final String suffix) {
        return new StringBuffer().append("6-4-").append(prefix).append(symbolCode).append(suffix).append(".doc")
                .toString();
    }

    private List<DataColumnInfo> extractPrimaryKeyColumns(final DataTable rldf002mDataTable) {
        List<DataColumnInfo> result = new ArrayList<DataColumnInfo>();
        List<DataColumnInfo> dataColumnInfos = rldf002mDataTable.getDataColumnInfos();
        for (DataColumnInfo column : dataColumnInfos) {
            if (column.isKey()) {
                result.add((DataColumnInfo) SerializationUtils.clone(column));
            }
        }
        return result;
    }

    private void recombinationDataColumnInfo(final DataTable rldfXM, final List<DataColumnInfo> keyColumnList) {
        List<DataColumnInfo> extendColumns = new ArrayList<DataColumnInfo>(rldfXM.getDataColumnInfos());
        rldfXM.getDataColumnInfos().clear();
        rldfXM.getDataColumnInfos().addAll(keyColumnList);
        Set<String> keyColumnNameSet = new HashSet<String>();
        for (DataColumnInfo tmp : keyColumnList) {
            keyColumnNameSet.add(tmp.getColumnName());
        }
        for (DataColumnInfo tmp : extendColumns) {
            if (!keyColumnNameSet.contains(tmp.getColumnName())) {
                rldfXM.getDataColumnInfos().add(tmp);
            }
        }

    }

    public List<DataTable> generate64TableDocFromTemplate(final DataTable templateDataTable, final String symbolCode,
            final String chineseName, final DataTable rldf002mDataTable, final DataTable rldf005mDataTable,
            final DataTable rldf002hDataTable, final DataTable rldf005hDataTable) {
        List<DataTable> result = new ArrayList<DataTable>();
        DataTable rldfXM = (DataTable) SerializationUtils.clone(templateDataTable);// rldf002mDataTable
                                                                                   // pk
        recombinationDataColumnInfo(rldfXM, extractPrimaryKeyColumns(rldf002mDataTable));
        DataTable rldfYM = (DataTable) SerializationUtils.clone(templateDataTable);// rldf005mDataTable
                                                                                   // pk
        recombinationDataColumnInfo(rldfYM, extractPrimaryKeyColumns(rldf005mDataTable));

        DataTable rldfXH = (DataTable) SerializationUtils.clone(templateDataTable);// rldf002hDataTable
                                                                                   // pk
        recombinationDataColumnInfo(rldfXH, extractPrimaryKeyColumns(rldf002hDataTable));
        DataTable rldfYH = (DataTable) SerializationUtils.clone(templateDataTable);// rldf005hDataTable
                                                                                   // pk
        recombinationDataColumnInfo(rldfYH, extractPrimaryKeyColumns(rldf005hDataTable));

        DataTable rrdfXM = (DataTable) SerializationUtils.clone(templateDataTable);
        recombinationDataColumnInfo(rrdfXM, extractPrimaryKeyColumns(rldf002mDataTable));
        DataTable rrdfYM = (DataTable) SerializationUtils.clone(templateDataTable);
        recombinationDataColumnInfo(rrdfYM, extractPrimaryKeyColumns(rldf005mDataTable));

        rldfXM.setChineseName(chineseName + "全戶記事欄位化");
        rldfXM.setFileName(get64TableFileName(symbolCode, "RLDFX", "M"));

        rldfXH.setChineseName("除戶" + chineseName + "全戶記事欄位化");
        rldfXH.setFileName(get64TableFileName(symbolCode, "RLDFX", "H"));

        rldfYM.setChineseName(chineseName + "個人記事欄位化");
        rldfYM.setFileName(get64TableFileName(symbolCode, "RLDFY", "M"));

        rldfYH.setChineseName("除戶" + chineseName + "個人記事欄位化");
        rldfYH.setFileName(get64TableFileName(symbolCode, "RLDFY", "H"));

        rrdfXM.setChineseName(chineseName + "全戶記事欄位化");
        rrdfXM.setFileName(get64TableFileName(symbolCode, "RRDFX", "M"));
        rrdfYM.setChineseName(chineseName + "個人記事欄位化");
        rrdfYM.setFileName(get64TableFileName(symbolCode, "RRDFY", "M"));

        DataTable xldfXM = modiferOfXLDF(rldfXM);
        DataTable xldfXH = modiferOfXLDF(rldfXH);
        DataTable xldfYM = modiferOfXLDF(rldfYM);
        DataTable xldfYH = modiferOfXLDF(rldfYH);

        result.add(rldfXM);
        result.add(rldfYM);
        result.add(rldfXH);
        result.add(rldfYH);

        result.add(xldfXM);
        result.add(xldfXH);
        result.add(xldfYM);
        result.add(xldfYH);

        result.add(rrdfXM);
        result.add(rrdfYM);
        return result;
    }

    /***
     * 產生對應的XLDF
     * ***/
    public DataTable modiferOfXLDF(final DataTable srcDataTable) {
        DataTable xldfXM = (DataTable) SerializationUtils.clone(srcDataTable);
        List<DataColumnInfo> dataColumnInfos = xldfXM.getDataColumnInfos();
        for (DataColumnInfo dataColumnInfo : dataColumnInfos) {
            boolean reserved = false;
            if ("PK".equals(dataColumnInfo.getValueArray()[0])) {
                reserved = true;
            }
            dataColumnInfo.getValueArray()[0] = "";
            if (!reserved) {
                dataColumnInfo.getValueArray()[5] = "";//RLDF原本是primary key欄位不允許null
            }
        }
        List<DataColumnInfo> newList = new ArrayList<DataColumnInfo>();
        newList.add(new DataColumnInfo(new String[] { "PK", "TRANSACTION_ID", "交易序號", "CHAR(22)", "", "N", "" }));
        newList.add(new DataColumnInfo(new String[] { "PK", "SEQUENCE_ID", "作業順序編號", "INTEGER", "", "N", "" }));
        newList.add(new DataColumnInfo(new String[] { "PK", "SERIAL_NO", "流水號", "INTEGER", "", "N", "" }));
        newList.add(new DataColumnInfo(new String[] { "", "STATE", "狀態", "CHAR(01)", "", "N",
                "O:Origin\r\nC:Change\r\n" }));
        newList.add(new DataColumnInfo(new String[] { "", "ACTION", "異動別", "CHAR(01)", "", "N",
                "R:Read\r\nU:Update\r\nD:Delete\r\nC:Copy\r\nI:Insert\r\n" }));

        newList.add(new DataColumnInfo(new String[] { "", "LOCK_MODE", "是否鎖定", "VARCHAR(05)", "", "N", "" }));
        newList.add(new DataColumnInfo(new String[] { "", "SITE_ID", "作業點代碼", "CHAR(08)", "", "", "" }));
        //		newList.add(new DataColumnInfo(new String[] { "", "SELECT_MODE", "選擇模式", "INTEGER", "", "", "" }));

        recombinationDataColumnInfo(xldfXM, newList);

        xldfXM.setFileName(xldfXM.getFileName().replaceFirst("RLDF", "XLDF"));
        xldfXM.setChineseName("臨時─" + xldfXM.getChineseName());
        xldfXM.setTableName(xldfXM.getTableName().replaceFirst("RLDF", "XLDF"));
        return xldfXM;
    }

    public String covertTxtFrom64TableDoc(final DataTable dataTable) {
        StringBuffer sbf = new StringBuffer();
        sbf.append(dataTable.getTableName()).append("\r\n");
        for (DataColumnInfo column : dataTable.getDataColumnInfos()) {
            sbf.append(column.getColumnName()).append(" \t").append(column.getFormat()).append("\r\n");
        }
        return sbf.toString();
    }

    public ScriptTableInfo covertFrom64TableDoc(final DataTable dataTable) {
        ScriptTableInfo result = new ScriptTableInfo();
        List<ScriptColumnInfo> dataColumnInfos = result.getDataColumnInfos();
        for (DataColumnInfo column : dataTable.getDataColumnInfos()) {
            ScriptColumnInfo scriptColumnInfo = new ScriptColumnInfo();
            scriptColumnInfo.setColumnName(column.getColumnName());
            scriptColumnInfo.setFormat(column.getFormat());
            scriptColumnInfo.setNullable(column.isNullable());
            scriptColumnInfo.setPrimaryKey(column.isKey());
            dataColumnInfos.add(scriptColumnInfo);
        }
        result.setTableName(dataTable.getTableName());
        return result;
    }

    /****
     * 針對已完成的DataTable進行驗證 驗證範圍 <br/>
     * 1)具有pkey<br/>
     * 2)table名稱必須由英文構成<br/>
     * 3)不能有重複的columns<br/>
     * 4)column中的format必須存在<br/>
     * *****/
    public void validation(final DataTable aDataTable) throws Exception {
        List<DataColumnInfo> columnList = aDataTable.getDataColumnInfos();
        Set<String> checkNameSet = new HashSet<String>();
        List<String> errors = new ArrayList<String>();
        boolean keyExisted = false;
        String tableName = aDataTable.getTableName();
        if (!Utils.isEnglishCharacters(tableName)) {
            errors.add("將要產生的" + tableName + "欄位名稱： " + tableName + "這樣是不合法的欄位名稱(只能由半形英文大小寫構成) ");
        }
        for (DataColumnInfo dataColumnInfo : columnList) {
            if (dataColumnInfo.isKey()) {
                keyExisted = true;
            }
            String columnName = dataColumnInfo.getColumnName().toUpperCase();
            String format = dataColumnInfo.getFormat().trim();
            if (checkNameSet.contains(columnName)) {
                errors.add("將要產生的" + tableName + "會有重複的" + columnName);
            } else {
                checkNameSet.add(columnName);
            }
            if (StringUtils.isEmpty(format) || StringUtils.isEmpty(columnName)) {
                errors.add("將要產生的" + tableName + "欄位名稱： " + columnName + "; 格式： " + format + ";「欄位名稱」 ,「格式」必須同時存在。");
            }
            if (!Utils.isEnglishCharacters(columnName)) {
                errors.add("將要產生的" + tableName + "欄位名稱： ," + columnName + "這樣是不合法的欄位名稱(只能由半形英文大小寫、_,構成,) ");
            }
        }
        if (!keyExisted) {
            errors.add("將要產生的" + tableName + "(" + aDataTable.getFileName() + "," + aDataTable.getChineseName() + ")"
                    + "必須要有primary key欄位..");
        }
        if (CollectionUtils.isNotEmpty(errors)) {
            StringBuffer sbf = new StringBuffer();
            for (String message : errors) {
                sbf.append(message).append("\n\r");
            }
            throw new Exception(sbf.toString());
        }
    }

    public DataTable readTemplateDataTable(final File exportFile) throws Exception {
        DataTable result = new DataTable("template");
        List<DataColumnInfo> columnList = result.getDataColumnInfos();
        InputStream inp;
        try {
            inp = new FileInputStream(exportFile);
            // InputStream inp = new FileInputStream("workbook.xlsx");
            Workbook wb = WorkbookFactory.create(inp);
            org.apache.poi.ss.usermodel.Sheet sheet = wb.getSheet(SHEET_NAME);
            Map<String, Integer> headData = getHeadColumnPosition(sheet);
            List<String[]> initData = POIUtils.processFromSheet(sheet);
            for (int i = 1; i < initData.size(); ++i) {
                String[] line = initData.get(i);
                DataColumnInfo column = new DataColumnInfo();
                column.getValueArray()[0] = line[headData.get(COLUMN_HEADNAME_KEY)];
                column.getValueArray()[1] = line[headData.get(COLUMN_HEADNAME_ENG_NAME)];
                column.getValueArray()[2] = line[headData.get(COLUMN_HEADNAME_CHT_NAME)];
                column.getValueArray()[3] = line[headData.get(COLUMN_HEADNAME_FORMAT)];
                column.getValueArray()[4] = line[headData.get(COLUMN_HEADNAME_DEFAULT_VALUE)];
                column.getValueArray()[5] = line[headData.get(COLUMN_HEADNAME_IS_NULLABLE)];
                column.getValueArray()[6] = line[headData.get(COLUMN_HEADNAME_DESCRIPTION)];
                columnList.add(column);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public Workbook generateTemplate() {
        Workbook wb = new HSSFWorkbook();
        org.apache.poi.ss.usermodel.Sheet sheet = wb.createSheet(SHEET_NAME);
        CellStyle style01 = POIUtils.buildCellStyle(sheet, 13, CellStyle.ALIGN_CENTER, "標楷體", false);

        CellStyle style02 = POIUtils.buildCellStyle(sheet, 10, CellStyle.ALIGN_LEFT, true, true,
                (int) IndexedColors.WHITE.getIndex());

        List<String[]> aList = getRLDFPrimaryKey();

        for (int i = 0; i < aList.size(); ++i) {
            String[] line = aList.get(i);

            for (int j = 0; j < line.length; ++j) {
                String value = line[j];
                POIUtils.setSheetCellPosValue(sheet, j, i, value, style02, 30);
                sheet.autoSizeColumn(j);

                // sheet.addMergedRegion(new CellRangeAddress(26, // first row
                // (0-based)
                // 26, // last row (0-based)
                // 0, // first column (0-based)
                // 4 // last column (0-based)
                // ));
            }
        }

        sheet.createFreezePane(0, 1, 0, 1);
        return wb;
    }

    private List<String[]> getRLDFPrimaryKey() {
        List<String[]> result = new ArrayList<String[]>();
        result.add(new String[] { COLUMN_HEADNAME_KEY, COLUMN_HEADNAME_ENG_NAME, COLUMN_HEADNAME_CHT_NAME,
                COLUMN_HEADNAME_FORMAT, COLUMN_HEADNAME_DEFAULT_VALUE, COLUMN_HEADNAME_IS_NULLABLE,
                COLUMN_HEADNAME_DESCRIPTION });
        result.add(new String[] { "", "APPLY_TRANSACTION_ID", "交易序號", "CHAR(22)", "", "N", "" });
        result.add(new String[] { "", "APPLY_SEQUENCE_ID", "作業順序編號", "INTEGER", "", "N", "" });
        result.add(new String[] { "", "REGISTER_CODE", "記事代碼", "CHAR(10)", "", "N", "" });
        result.add(new String[] { "", "REGISTER_ADMIN_OFFICE_CODE", "登記戶所代碼", "CHAR(08)", "", "N", "" });
        result.add(new String[] { "", "REGISTRAR_NAME", "戶籍員姓名", "VARCHAR(32)", "", "N", "" });
        return result;
    }

    private final String COLUMN_HEADNAME_KEY = "Key";
    private final String COLUMN_HEADNAME_ENG_NAME = "欄位名稱";
    private final String COLUMN_HEADNAME_CHT_NAME = "中文名稱";
    private final String COLUMN_HEADNAME_FORMAT = "格式";
    private final String COLUMN_HEADNAME_DEFAULT_VALUE = "預設值";
    private final String COLUMN_HEADNAME_IS_NULLABLE = "允許Null";
    private final String COLUMN_HEADNAME_DESCRIPTION = "說明";

    private Map<String, Integer> getHeadColumnPosition(final Sheet sheet) {
        Map<String, Integer> result = new HashMap<String, Integer>();
        Row header = sheet.getRow(0);
        int headerNumberOfCells = header.getPhysicalNumberOfCells();// 得到column數目
        for (int i = 0; i < headerNumberOfCells; ++i) {
            Cell cell = header.getCell(i);
            String headName = cell.getStringCellValue().trim();
            if (COLUMN_HEADNAME_KEY.equalsIgnoreCase(headName)) {
                result.put(COLUMN_HEADNAME_KEY, Integer.valueOf(i));
            } else if (COLUMN_HEADNAME_ENG_NAME.equalsIgnoreCase(headName)) {
                result.put(COLUMN_HEADNAME_ENG_NAME, Integer.valueOf(i));
            } else if (COLUMN_HEADNAME_CHT_NAME.equalsIgnoreCase(headName)) {
                result.put(COLUMN_HEADNAME_CHT_NAME, Integer.valueOf(i));
            } else if (COLUMN_HEADNAME_FORMAT.equalsIgnoreCase(headName)) {
                result.put(COLUMN_HEADNAME_FORMAT, Integer.valueOf(i));
            } else if (COLUMN_HEADNAME_DEFAULT_VALUE.equalsIgnoreCase(headName)) {
                result.put(COLUMN_HEADNAME_DEFAULT_VALUE, Integer.valueOf(i));
            } else if (COLUMN_HEADNAME_IS_NULLABLE.equalsIgnoreCase(headName)) {
                result.put(COLUMN_HEADNAME_IS_NULLABLE, Integer.valueOf(i));
            } else if (COLUMN_HEADNAME_DESCRIPTION.equalsIgnoreCase(headName)) {
                result.put(COLUMN_HEADNAME_DESCRIPTION, Integer.valueOf(i));
            }
        }
        return result;
    }

}
