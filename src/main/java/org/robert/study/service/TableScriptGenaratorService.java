package org.robert.study.service;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Workbook;

import com.iisi.rl.table.DataTable;
import com.iisi.rl.table.jdbc.schema.JDBCTablseSchema;
import com.iisi.rl.table.script.ScriptTableInfo;

public interface TableScriptGenaratorService {

    public Workbook generateTemplate();

    public DataTable readTemplateDataTable(final File exportFile);

    public void validation(final DataTable aDataTable) throws Exception;

    public ScriptTableInfo covertFrom64TableDoc(final DataTable dataTable);

    public String covertTxtFrom64TableDoc(final DataTable dataTable);

    public Map<String, String> generateInitialScript(final List<DataTable> aList);

    public List<DataTable> generate64TableDocFromTemplate(final String symbolCode, final String chineseName,
            final DataTable eachRegRLDFSDataTable, final DataTable eachRegXLDFSDataTable,
            final DataTable regCommonDataTable, final DataTable externalRegDataTable,
            final DataTable xldfsCommonDataTable);
    /***
     * @author "Robert Lee" 
     * 辦理他所-XXX申請書歷史資料檔(W系列)
     * ***/
    public DataTable RLDFWXXXFromRLDFSXXX(final DataTable srcDataTable);
    public Map<String, String> generateFinalScript(final List<DataTable> aList);

    /***
     * 產生除戶資料
     * 
     * ***/
    public List<DataTable> generateHistoryRegData(final List<DataTable> aList);
    
    public List<DataTable> convertDataTableListfromJDBCTablseSchemaList(final List<JDBCTablseSchema> jdbcTableList);
    
    public List<ScriptTableInfo> covertScriptFromJDBCTablseSchemaList(final List<JDBCTablseSchema> jdbcTableList);

}
