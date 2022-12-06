package com.iisigroup.toolkits.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.robert.study.service.TableScriptGenaratorService;
import org.robert.study.utils.POIUtils;
import org.robert.study.utils.Utils;

import com.iisi.doc.process.DocWriter;
import com.iisi.rl.table.DataColumnInfo;
import com.iisi.rl.table.DataTable;
import com.iisi.rl.table.jdbc.schema.JDBCColumnSachema;
import com.iisi.rl.table.jdbc.schema.JDBCTablseSchema;
import com.iisi.rl.table.script.ScriptColumnInfo;
import com.iisi.rl.table.script.ScriptTableInfo;

public class TableScriptGenaratorServiceImpl implements TableScriptGenaratorService {
    private final String COLUMN_HEADNAME_KEY = "Key";
    private final String COLUMN_HEADNAME_ENG_NAME = "欄位名稱";
    private final String COLUMN_HEADNAME_CHT_NAME = "中文名稱";
    private final String COLUMN_HEADNAME_FORMAT = "格式";
    private final String COLUMN_HEADNAME_DEFAULT_VALUE = "預設值";
    private final String COLUMN_HEADNAME_IS_NULLABLE = "允許Null";
    private final String COLUMN_HEADNAME_DESCRIPTION = "說明";
    private final String SHEET_NAME = "template";

    public static void main(String[] args) throws IOException {
	testMethod01();
    }

    public static void testMethod01() throws IOException {
	TableScriptGenaratorServiceImpl main = new TableScriptGenaratorServiceImpl();
	// 各項登記申請書獨有欄位
	DataTable eachRegRLDFSDataTable = main.readTemplateDataTable(new File("/home/weblogic/Desktop/rldfs.xls"));

	// 各項XLDFS系列獨有欄位
	DataTable eachRegXLDFSDataTable = main.readTemplateDataTable(new File("/home/weblogic/Desktop/xldfs.xls"));

	// 申請書共通欄項
	DataTable regCommonDataTable = main.readTemplateDataTable(new File("/home/weblogic/Desktop/regCommonDataTable.xls"));

	// 外來申請書共通欄項
	DataTable externalRegDataTable = main.readTemplateDataTable(new File("/home/weblogic/Desktop/externalRegDataTable.xls"));

	// XLDF共通欄項
	DataTable xldfsCommonDataTable = main.readTemplateDataTable(new File("/home/weblogic/Desktop/xldfsCommonDataTable.xls"));

	// 產生6-4 table doc
	List<DataTable> docList = main.generate64TableDocFromTemplate("080", "未成年子女權利與義務行駛負擔登記", eachRegRLDFSDataTable, eachRegXLDFSDataTable,
		regCommonDataTable, externalRegDataTable, xldfsCommonDataTable);
	Map<String, String> scriptInitDataMap = main.generateInitialScript(docList);
	Map<String, String> scriptFinalDataMap = main.generateFinalScript(docList);

	DocWriter aDocWriter = new DocWriter();

	// 選定匯出資料夾
	final File dir = new File("/home/weblogic/tmp");

	for (DataTable aDataTable : docList) {
	    XWPFDocument doc = aDocWriter.createNewWord(aDataTable);
	    POIUtils.writePOIXMLDocumentPartOut(new File(dir.getAbsolutePath() + "//" + aDataTable.getFileName()), doc);
	}
	for (String fileName : scriptInitDataMap.keySet()) {
	    String content = scriptInitDataMap.get(fileName);
	    File aFile = new File(dir.getAbsolutePath() + "//" + fileName);
	    Utils.outputFile(aFile, content);
	}
	for (String fileName : scriptFinalDataMap.keySet()) {
	    String content = scriptFinalDataMap.get(fileName);
	    File aFile = new File(dir.getAbsolutePath() + "//" + fileName);
	    if (content != null) {
		Utils.outputFile(aFile, content);
	    } else {
		System.out.println("file name: " + fileName);
	    }

	}
    }

    private String patternString = "6-4-[R|X]LDF[M|0]\\d{2}[M|T]\\.doc";

    /***
     * 產生除戶資料
     * 
     * ***/
    public List<DataTable> generateHistoryRegData(final List<DataTable> aList) {
	List<DataTable> result = new ArrayList<DataTable>();
	for (DataTable table : aList) {
	    String originalChineseName = table.getChineseName();
	    if (originalChineseName == null) {
		System.out.println(table.getFileName());
	    }
	    if (!originalChineseName.contains("除戶") && Pattern.compile(patternString).matcher(table.getFileName()).matches()) {

		DataTable newTable = (DataTable) SerializationUtils.clone(table);
		String fileName = null;
		String tableName = null;
		if (table.getTableName().lastIndexOf("M") > 0) {
		    tableName = table.getTableName().substring(0, table.getTableName().lastIndexOf("M")) + "H";
		} else {
		    tableName = table.getTableName() + "H";
		}
		if (table.getFileName().contains("M.doc")) {
		    fileName = table.getFileName().replace("M.doc", "H.doc");
		} else if (table.getFileName().contains("T.doc")) {
		    fileName = table.getFileName().replace("T.doc", "TH.doc");
		}

		String chineseName = "除戶-" + table.getChineseName();

		boolean isXldf = false;
		if (table.getFileName().contains("XLDF")) {
		    isXldf = true;
		    chineseName = table.getChineseName().replace("臨時", "臨時-除戶");
		} else if (table.getFileName().contains("RLDF")) {
		    chineseName = "除戶-" + table.getChineseName();
		}

		newTable.setChineseName(chineseName);
		newTable.setFileName(fileName);
		newTable.setTableName(tableName);

		List<DataColumnInfo> newdataColumnInfoList = new ArrayList<DataColumnInfo>();
		List<DataColumnInfo> dataColumnInfoList = newTable.getDataColumnInfos();

		List<DataColumnInfo> pkList = filterPKColumnList(dataColumnInfoList);

		List<DataColumnInfo> nonpkList = filterNonPKColumnList(dataColumnInfoList);

		newdataColumnInfoList.addAll(pkList);
		modifyHistoryTable(newdataColumnInfoList, isXldf);
		newdataColumnInfoList.addAll(nonpkList);

		dataColumnInfoList.clear();
		newTable.getDataColumnInfos().addAll(newdataColumnInfoList);
		DataTable xldfDataTable = modiferOfXLDF(newTable);
		result.add(newTable);
		result.add(xldfDataTable);
	    }
	}

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
		dataColumnInfo.getValueArray()[5] = "";// RLDF原本是primary key欄位不允許null
	    }
	}
	List<DataColumnInfo> newList = new ArrayList<DataColumnInfo>();
	newList.add(new DataColumnInfo(new String[] { "PK", "TRANSACTION_ID", "交易序號", "CHAR(22)", "", "N", "" }));
	newList.add(new DataColumnInfo(new String[] { "PK", "SEQUENCE_ID", "作業順序編號", "INTEGER", "", "N", "" }));
	newList.add(new DataColumnInfo(new String[] { "PK", "SERIAL_NO", "流水號", "INTEGER", "", "N", "" }));
	newList.add(new DataColumnInfo(new String[] { "", "STATE", "狀態", "CHAR(01)", "", "N", "O:Origin\r\nC:Change\r\n" }));
	newList.add(new DataColumnInfo(new String[] { "", "ACTION", "異動別", "CHAR(01)", "", "N", "R:Read\r\nU:Update\r\nD:Delete\r\nC:Copy\r\nI:Insert\r\n" }));

	newList.add(new DataColumnInfo(new String[] { "", "LOCK_MODE", "是否鎖定", "VARCHAR(05)", "", "N", "" }));
	newList.add(new DataColumnInfo(new String[] { "", "SITE_ID", "作業點代碼", "CHAR(08)", "", "", "" }));
	// newList.add(new DataColumnInfo(new String[] { "", "SELECT_MODE", "選擇模式", "INTEGER", "", "", "" }));

	recombinationDataColumnInfo(xldfXM, newList);

	xldfXM.setFileName(xldfXM.getFileName().replaceFirst("RLDF", "XLDF"));
	xldfXM.setChineseName("臨時─" + xldfXM.getChineseName());
	xldfXM.setTableName(xldfXM.getTableName().replaceFirst("RLDF", "XLDF"));
	xldfXM.setRemark(xldfXM.getTableName() + " " + xldfXM.getChineseName());
	return xldfXM;
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

    private List<DataColumnInfo> filterPKColumnList(List<DataColumnInfo> dataColumnInfoList) {
	List<DataColumnInfo> result = new ArrayList<DataColumnInfo>();
	for (DataColumnInfo column : dataColumnInfoList) {
	    if (column.isKey()) {
		result.add(column);
	    }
	}

	return result;
    }

    private List<DataColumnInfo> filterNonPKColumnList(List<DataColumnInfo> dataColumnInfoList) {
	List<DataColumnInfo> result = new ArrayList<DataColumnInfo>();
	for (DataColumnInfo column : dataColumnInfoList) {
	    if (!column.isKey()) {
		result.add(column);
	    }
	}

	return result;
    }

    private void modifyHistoryTable(List<DataColumnInfo> newdataColumnInfoList, boolean isXldf) {
	DataColumnInfo removeYyyymmddColumn = new DataColumnInfo();
	DataColumnInfo removeTimeColumn = new DataColumnInfo();
	removeYyyymmddColumn.getValueArray()[0] = isXldf ? "" : "PK";
	removeYyyymmddColumn.getValueArray()[1] = "REMOVE_YYYMMDD";
	removeYyyymmddColumn.getValueArray()[2] = "除戶日期";
	removeYyyymmddColumn.getValueArray()[3] = "CHAR(07)";
	removeYyyymmddColumn.getValueArray()[4] = "";
	removeYyyymmddColumn.getValueArray()[5] = isXldf ? "" : "N";
	removeYyyymmddColumn.getValueArray()[6] = "";
	removeTimeColumn.getValueArray()[0] = isXldf ? "" : "PK";
	removeTimeColumn.getValueArray()[1] = "REMOVE_TIME";
	removeTimeColumn.getValueArray()[2] = "除戶時間";
	removeTimeColumn.getValueArray()[3] = "CHAR(06)";
	removeTimeColumn.getValueArray()[4] = "";
	removeTimeColumn.getValueArray()[5] = isXldf ? "" : "N";
	removeTimeColumn.getValueArray()[6] = "";
	newdataColumnInfoList.add(removeYyyymmddColumn);
	newdataColumnInfoList.add(removeTimeColumn);
    }

    @Override
    public Map<String, String> generateFinalScript(final List<DataTable> aList) {
	Map<String, String> result = new HashMap<String, String>();
	for (DataTable table : aList) {
	    ScriptTableInfo script = covertFrom64TableDoc(table);
	    result.put(table.getTableName() + ".sql", script.getStatement());
	}
	return result;
    }

    /****
     * @param symbolCode
     *            登記代碼
     * @param chineseName
     *            登記名稱(ex:出生登記、死亡登記、結婚登記、未成年子女權利與義務行使負擔登記)
     * @param eachRegRLDFSDataTable
     *            各項登記申請書獨有欄位
     * @param eachRegXLDFSDataTable
     *            各項XLDFS系列獨有欄位
     * @param regCommonDataTable
     *            申請書共通欄項
     * @param externalRegDataTable
     *            外來申請書共通欄項
     * @param xldfsCommonDataTable
     *            XLDF共通欄項
     * ***/
    @Override
    public List<DataTable> generate64TableDocFromTemplate(final String symbolCode, final String chineseName, final DataTable eachRegRLDFSDataTable,
	    final DataTable eachRegXLDFSDataTable, final DataTable regCommonDataTable, final DataTable externalRegDataTable,
	    final DataTable xldfsCommonDataTable) {
	List<DataTable> result = new ArrayList<DataTable>();
	// 產出一般申請書RLDFSXXX
	DataTable rldfsXXX = generateRLDFCommonXXX(symbolCode, chineseName + "申請書資料檔", "RLDFS", regCommonDataTable, eachRegRLDFSDataTable);
	result.add(rldfsXXX);

	// 產出一般申請書歷史資料檔RLDFTXXX
	DataTable rldftXXX = generateRLDFCommonXXX(symbolCode, chineseName + "申請書歷史資料檔", "RLDFT", regCommonDataTable, eachRegRLDFSDataTable);
	result.add(rldftXXX);

	// 產出一般辦理他所-申請書歷史資料檔RLDFTXXX
	DataTable rldfwXXX = generateRLDFCommonXXX(symbolCode, "辦理他所-" + chineseName + "申請書歷史資料檔", "RLDFW", regCommonDataTable, eachRegRLDFSDataTable);
	result.add(rldfwXXX);

	// 產出一般外來申請書資料檔RLDFTXXX
	DataTable rldfrXXX = generateRLDFRXXX(symbolCode, chineseName, regCommonDataTable, eachRegRLDFSDataTable, externalRegDataTable);
	result.add(rldfrXXX);

	// 產出一般申請書資料檔XLDFSXXX
	DataTable xldfsXXX = generateXLDFSXXX(symbolCode, chineseName, xldfsCommonDataTable, eachRegRLDFSDataTable, regCommonDataTable, eachRegXLDFSDataTable);
	result.add(xldfsXXX);
	
	//產出一般申請書附繳證件資料檔XLDFSXXXB
	DataTable xldfsXXXB = generateXLDFSXXXB(symbolCode, chineseName, xldfsCommonDataTable);
	result.add(xldfsXXXB);
	
	//產出一般申請書附繳證件資料檔XLDFSXXXB
	DataTable rldfsXXXB = generateRLDFSXXXB(symbolCode, chineseName, regCommonDataTable);
	result.add(rldfsXXXB);
	
	// 產出一般臨時申請書資料檔XLDFTXXX
	DataTable xldftXXX = generateXLDFTXXX(symbolCode, chineseName, xldfsCommonDataTable, eachRegRLDFSDataTable, regCommonDataTable);
	result.add(xldftXXX);

	// 產出 臨時-辦理他所申請書資料檔
	DataTable xldfwXXX = generateXLDFWXXX(symbolCode, chineseName, xldfsCommonDataTable, eachRegRLDFSDataTable, regCommonDataTable);
	result.add(xldfwXXX);

	// 產出 臨時-外來申請書資料檔
	DataTable xldfrXXX = generateXLDFRXXX(symbolCode, chineseName, xldfsCommonDataTable, eachRegRLDFSDataTable, externalRegDataTable, regCommonDataTable);
	result.add(xldfrXXX);

	return result;
    }

    /***
     * @param symbolCode
     *            登記代碼
     * @param chineseName
     *            登記名稱(ex:出生登記、死亡登記、結婚登記、未成年子女權利與義務行使負擔登記)
     * @param xldfsCommonDataTable
     *            XLDF共通欄項
     * @param regCommonDataTable
     *            申請書共通欄項
     * @param externalRegDataTable
     *            外來申請書共通欄項
     * @param eachRegRLDFSDataTable
     *            各項登記申請書獨有欄位
     * ***/
    private DataTable generateXLDFRXXX(final String symbolCode, final String chineseName, final DataTable xldfsCommonDataTable,
	    final DataTable eachRegRLDFSDataTable, final DataTable externalRegDataTable, final DataTable regCommonDataTable) {
	DataTable xldfrXXX = (DataTable) SerializationUtils.clone(xldfsCommonDataTable);
	DataTable xldfRegRLDFSData = (DataTable) SerializationUtils.clone(eachRegRLDFSDataTable);
	DataTable xldfExternalRegDataTable = (DataTable) SerializationUtils.clone(externalRegDataTable);
	DataTable xldfRegCommonData = (DataTable) SerializationUtils.clone(regCommonDataTable);

	xldfrXXX.getDataColumnInfos().addAll(forXLDFProcess(xldfRegCommonData));
	xldfrXXX.getDataColumnInfos().addAll(forXLDFProcess(xldfExternalRegDataTable));
	xldfrXXX.getDataColumnInfos().addAll(forXLDFProcess(xldfRegRLDFSData));

	xldfrXXX.setChineseName("臨時-" + chineseName + "外來申請書資料檔");
	xldfrXXX.setFileName(get64TableFileName(symbolCode, "XLDFR", ""));
	return xldfrXXX;
    }

    /***
     * @param symbolCode
     *            登記代碼
     * @param chineseName
     *            登記名稱(ex:出生登記、死亡登記、結婚登記、未成年子女權利與義務行使負擔登記)
     * @param xldfsCommonDataTable
     *            XLDF共通欄項
     * @param regCommonDataTable
     *            申請書共通欄項
     * @param eachRegRLDFSDataTable
     *            各項登記申請書獨有欄位
     * ***/
    private DataTable generateXLDFWXXX(final String symbolCode, final String chineseName, final DataTable xldfsCommonDataTable,
	    final DataTable eachRegRLDFSDataTable, final DataTable regCommonDataTable) {
	DataTable xldfwXXX = (DataTable) SerializationUtils.clone(xldfsCommonDataTable);
	DataTable xldfRegRLDFSData = (DataTable) SerializationUtils.clone(eachRegRLDFSDataTable);
	DataTable xldfRegCommonData = (DataTable) SerializationUtils.clone(regCommonDataTable);

	xldfwXXX.getDataColumnInfos().addAll(forXLDFProcess(xldfRegCommonData));
	xldfwXXX.getDataColumnInfos().addAll(forXLDFProcess(xldfRegRLDFSData));

	xldfwXXX.setChineseName("臨時-辦理他所" + chineseName + "申請書資料檔");
	xldfwXXX.setFileName(get64TableFileName(symbolCode, "XLDFW", ""));
	return xldfwXXX;
    }

    /***
     * @param symbolCode
     *            登記代碼
     * @param chineseName
     *            登記名稱(ex:出生登記、死亡登記、結婚登記、未成年子女權利與義務行使負擔登記)
     * @param xldfsCommonDataTable
     *            XLDF共通欄項
     * @param regCommonDataTable
     *            申請書共通欄項
     * @param eachRegRLDFSDataTable
     *            各項登記申請書獨有欄位
     * @param eachRegXLDFSDataTable
     *            各項XLDFS系列獨有欄位
     * ***/
	private DataTable generateXLDFSXXX(final String symbolCode, final String chineseName, final DataTable xldfsCommonDataTable,
			final DataTable eachRegRLDFSDataTable, final DataTable regCommonDataTable, final DataTable eachRegXLDFSDataTable) {
		DataTable xldfsXXX = (DataTable) SerializationUtils.clone(xldfsCommonDataTable);
		DataTable xldfRegRLDFSData = (DataTable) SerializationUtils.clone(eachRegRLDFSDataTable);
		DataTable xldfRegCommonData = (DataTable) SerializationUtils.clone(regCommonDataTable);
		DataTable xldfRegXLDFSData = (DataTable) SerializationUtils.clone(eachRegXLDFSDataTable);

		xldfsXXX.getDataColumnInfos().addAll(forXLDFProcess(xldfRegCommonData));
		xldfsXXX.getDataColumnInfos().addAll(forXLDFProcess(xldfRegRLDFSData));
		xldfsXXX.getDataColumnInfos().addAll(forXLDFProcess(xldfRegXLDFSData));
		DataColumnInfo forCognosColumn = new DataColumnInfo();
		forCognosColumn.getValueArray()[0] = "";
		forCognosColumn.getValueArray()[1] = "IS_CERTIFICATE";
		forCognosColumn.getValueArray()[2] = "是否有附繳證件";
		forCognosColumn.getValueArray()[3] = "CHAR(01)";
		forCognosColumn.getValueArray()[4] = "";
		forCognosColumn.getValueArray()[5] = "";
		forCognosColumn.getValueArray()[6] = "如果有附繳證件填入 Y 沒有填入N";
		xldfsXXX.getDataColumnInfos().add(forCognosColumn);
		xldfsXXX.setChineseName("臨時" + chineseName + "申請書資料檔");
		xldfsXXX.setFileName(get64TableFileName(symbolCode, "XLDFS", ""));
		return xldfsXXX;
	}
	private List<DataColumnInfo> getcertifaicateColumnListForXLDF(){
		List<DataColumnInfo> result = new ArrayList<DataColumnInfo>();
		DataColumnInfo applyTxIdColumn = new DataColumnInfo();
		applyTxIdColumn.getValueArray()[0] = "";
		applyTxIdColumn.getValueArray()[1] = "APPLY_TRANSACTION_ID";
		applyTxIdColumn.getValueArray()[2] = "交易序號";
		applyTxIdColumn.getValueArray()[3] = "CHAR(22)";
		applyTxIdColumn.getValueArray()[4] = "";
		applyTxIdColumn.getValueArray()[5] = "";//N
		applyTxIdColumn.getValueArray()[6] = "";
		
		DataColumnInfo applySeqIdColumn = new DataColumnInfo();
		applySeqIdColumn.getValueArray()[0] = "";
		applySeqIdColumn.getValueArray()[1] = "APPLY_SEQUENCE_ID";
		applySeqIdColumn.getValueArray()[2] = "作業順序編號";
		applySeqIdColumn.getValueArray()[3] = "INTEGER";
		applySeqIdColumn.getValueArray()[4] = "";
		applySeqIdColumn.getValueArray()[5] = "";//N
		applySeqIdColumn.getValueArray()[6] = "";
		
		DataColumnInfo siteIdColumn = new DataColumnInfo();
		siteIdColumn.getValueArray()[0] = "";
		siteIdColumn.getValueArray()[1] = "SITE_ID";
		siteIdColumn.getValueArray()[2] = "資料儲存地作業點代碼";
		siteIdColumn.getValueArray()[3] = "CHAR(08)";
		siteIdColumn.getValueArray()[4] = "";
		siteIdColumn.getValueArray()[5] = "";//N
		siteIdColumn.getValueArray()[6] = "";
		
		DataColumnInfo applySeqColumn = new DataColumnInfo();
		applySeqColumn.getValueArray()[0] = "";
		applySeqColumn.getValueArray()[1] = "APPLY_SEQ";
		applySeqColumn.getValueArray()[2] = "申請書流水號";
		applySeqColumn.getValueArray()[3] = "INTEGER";
		applySeqColumn.getValueArray()[4] = "";
		applySeqColumn.getValueArray()[5] = "";//N
		applySeqColumn.getValueArray()[6] = "";
		
		DataColumnInfo certCodeColumn = new DataColumnInfo();
		certCodeColumn.getValueArray()[0] = "";
		certCodeColumn.getValueArray()[1] = "CERTIFICATE_CODE";
		certCodeColumn.getValueArray()[2] = "附繳證件代碼";
		certCodeColumn.getValueArray()[3] = "VARCHAR(03)";
		certCodeColumn.getValueArray()[4] = "";
		certCodeColumn.getValueArray()[5] = "";//N
		certCodeColumn.getValueArray()[6] = "代碼號碼使用過後,不可重複使用,所以長度使用3碼";
		
		DataColumnInfo certChtColumn = new DataColumnInfo();
		certChtColumn.getValueArray()[0] = "";
		certChtColumn.getValueArray()[1] = "CERTIFICATE_CHT";
		certChtColumn.getValueArray()[2] = "附繳證件名稱內容";
		certChtColumn.getValueArray()[3] = "VARCHAR(255)";
		certChtColumn.getValueArray()[4] = "";
		certChtColumn.getValueArray()[5] = "";
		certChtColumn.getValueArray()[6] = "如果附繳證件代碼填入其他,則CERTIFICATE_CHT要填入其他內容";
		
		
		result.add(applyTxIdColumn);
		result.add(applySeqIdColumn);
		result.add(siteIdColumn);
		result.add(applySeqColumn);
		result.add(certCodeColumn);
		result.add(certChtColumn);
		return result;
	}
	private List<DataColumnInfo> getcertifaicateColumnListForRLDF(){
		List<DataColumnInfo> result = new ArrayList<DataColumnInfo>();
		DataColumnInfo applyTxIdColumn = new DataColumnInfo();
		applyTxIdColumn.getValueArray()[0] = "PK";
		applyTxIdColumn.getValueArray()[1] = "APPLY_TRANSACTION_ID";
		applyTxIdColumn.getValueArray()[2] = "交易序號";
		applyTxIdColumn.getValueArray()[3] = "CHAR(22)";
		applyTxIdColumn.getValueArray()[4] = "";
		applyTxIdColumn.getValueArray()[5] = "N";//N
		applyTxIdColumn.getValueArray()[6] = "";
		
		DataColumnInfo applySeqIdColumn = new DataColumnInfo();
		applySeqIdColumn.getValueArray()[0] = "PK";
		applySeqIdColumn.getValueArray()[1] = "APPLY_SEQUENCE_ID";
		applySeqIdColumn.getValueArray()[2] = "作業順序編號";
		applySeqIdColumn.getValueArray()[3] = "INTEGER";
		applySeqIdColumn.getValueArray()[4] = "";
		applySeqIdColumn.getValueArray()[5] = "N";//N
		applySeqIdColumn.getValueArray()[6] = "";
		
		DataColumnInfo siteIdColumn = new DataColumnInfo();
		siteIdColumn.getValueArray()[0] = "PK";
		siteIdColumn.getValueArray()[1] = "SITE_ID";
		siteIdColumn.getValueArray()[2] = "資料儲存地作業點代碼";
		siteIdColumn.getValueArray()[3] = "CHAR(08)";
		siteIdColumn.getValueArray()[4] = "";
		siteIdColumn.getValueArray()[5] = "N";//N
		siteIdColumn.getValueArray()[6] = "";
		
		DataColumnInfo applySeqColumn = new DataColumnInfo();
		applySeqColumn.getValueArray()[0] = "PK";
		applySeqColumn.getValueArray()[1] = "APPLY_SEQ";
		applySeqColumn.getValueArray()[2] = "申請書流水號";
		applySeqColumn.getValueArray()[3] = "INTEGER";
		applySeqColumn.getValueArray()[4] = "";
		applySeqColumn.getValueArray()[5] = "N";//N
		applySeqColumn.getValueArray()[6] = "";
		
		DataColumnInfo certCodeColumn = new DataColumnInfo();
		certCodeColumn.getValueArray()[0] = "PK";
		certCodeColumn.getValueArray()[1] = "CERTIFICATE_CODE";
		certCodeColumn.getValueArray()[2] = "附繳證件代碼";
		certCodeColumn.getValueArray()[3] = "VARCHAR(03)";
		certCodeColumn.getValueArray()[4] = "";
		certCodeColumn.getValueArray()[5] = "N";//N
		certCodeColumn.getValueArray()[6] = "代碼號碼使用過後,不可重複使用,所以長度使用3碼";
		
		DataColumnInfo certChtColumn = new DataColumnInfo();
		certChtColumn.getValueArray()[0] = "";
		certChtColumn.getValueArray()[1] = "CERTIFICATE_CHT";
		certChtColumn.getValueArray()[2] = "附繳證件名稱內容";
		certChtColumn.getValueArray()[3] = "VARCHAR(255)";
		certChtColumn.getValueArray()[4] = "";
		certChtColumn.getValueArray()[5] = "N";
		certChtColumn.getValueArray()[6] = "如果附繳證件代碼填入其他,則CERTIFICATE_CHT要填入其他內容";
		
		
		result.add(applyTxIdColumn);
		result.add(applySeqIdColumn);
		result.add(siteIdColumn);
		result.add(applySeqColumn);
		result.add(certCodeColumn);
		result.add(certChtColumn);
		return result;
	}
	private DataTable generateRLDFSXXXB(final String symbolCode, final String chineseName, final DataTable rldfsCommonDataTable ) {
		DataTable rldfsXXXB = (DataTable) SerializationUtils.clone(rldfsCommonDataTable);
		rldfsXXXB.getDataColumnInfos().clear();
		//TODO
		List<DataColumnInfo> certifaicateColumnList =getcertifaicateColumnListForRLDF();
		rldfsXXXB.getDataColumnInfos().addAll(certifaicateColumnList);
		
		rldfsXXXB.setChineseName( chineseName + "申請書附繳證件資料檔");
		rldfsXXXB.setFileName(get64TableFileName(symbolCode, "RLDFS", "B"));
		return rldfsXXXB;
	}
	private DataTable generateXLDFSXXXB(final String symbolCode, final String chineseName, final DataTable xldfsCommonDataTable ) {
		DataTable xldfsXXXB = (DataTable) SerializationUtils.clone(xldfsCommonDataTable);
		List<DataColumnInfo> certifaicateColumnList =getcertifaicateColumnListForXLDF();
		xldfsXXXB.getDataColumnInfos().addAll(certifaicateColumnList);
		
		xldfsXXXB.setChineseName("臨時" + chineseName + "申請書附繳證件資料檔");
		xldfsXXXB.setFileName(get64TableFileName(symbolCode, "XLDFS", "B"));
		return xldfsXXXB;
	}
    /***
     * @param symbolCode
     *            登記代碼
     * @param chineseName
     *            登記名稱(ex:出生登記、死亡登記、結婚登記、未成年子女權利與義務行使負擔登記)
     * @param xldfsCommonDataTable
     *            XLDF共通欄項
     * @param regCommonDataTable
     *            申請書共通欄項
     * @param eachRegRLDFSDataTable
     *            各項登記申請書獨有欄位
     * ***/
	private DataTable generateXLDFTXXX(final String symbolCode, final String chineseName, final DataTable xldfsCommonDataTable,
			final DataTable eachRegRLDFSDataTable, final DataTable regCommonDataTable) {
		DataTable xldftXXX = (DataTable) SerializationUtils.clone(xldfsCommonDataTable);
		DataTable xldfRegRLDFSData = (DataTable) SerializationUtils.clone(eachRegRLDFSDataTable);
		DataTable xldfRegCommonData = (DataTable) SerializationUtils.clone(regCommonDataTable);

		xldftXXX.getDataColumnInfos().addAll(forXLDFProcess(xldfRegCommonData));
		xldftXXX.getDataColumnInfos().addAll(forXLDFProcess(xldfRegRLDFSData));

		xldftXXX.setChineseName("臨時" + chineseName + "申請書歷史資料檔");
		xldftXXX.setFileName(get64TableFileName(symbolCode, "XLDFT", ""));
		return xldftXXX;
	}

	private List<DataColumnInfo> forXLDFProcess(DataTable xldfsXXX) {
		List<DataColumnInfo> dataColumnInfos = xldfsXXX.getDataColumnInfos();
		List<DataColumnInfo> result = new ArrayList<DataColumnInfo>();
		for (DataColumnInfo dataColumnInfo : dataColumnInfos) {
		    //不能對RLDF表格原本是pk的欄位強迫在XLDF為是not null,這會造成xldf在作update失敗
//			boolean reserved = false;
//			if ("PK".equals(dataColumnInfo.getValueArray()[0])) {
//				reserved = true;
//			}
			dataColumnInfo.getValueArray()[0] = "";
//			if (!reserved) {
				dataColumnInfo.getValueArray()[5] = "";// RLDF原本是primary
														// key欄位不允許null
//			}

			result.add(dataColumnInfo);
		}
		return result;
	}

    /***
     * @param symbolCode
     *            登記代碼
     * @param chineseName
     *            登記名稱(ex:出生登記、死亡登記、結婚登記、未成年子女權利與義務行使負擔登記)
     * @param regCommonDataTable
     *            申請書共通欄項
     * @param eachRegRLDFSDataTable
     *            各項登記申請書獨有欄位
     * 
     * ***/
    private DataTable generateRLDFCommonXXX(final String symbolCode, final String chineseName, final String filePrefixName, final DataTable regCommonDataTable,
	    final DataTable eachRegRLDFSDataTable) {
	DataTable rldfCommoXXX = (DataTable) SerializationUtils.clone(regCommonDataTable);
	DataTable rldfsXXXForEachData = (DataTable) SerializationUtils.clone(eachRegRLDFSDataTable);
	rldfCommoXXX.getDataColumnInfos().addAll(rldfsXXXForEachData.getDataColumnInfos());

	rldfCommoXXX.setChineseName(chineseName);
	rldfCommoXXX.setFileName(get64TableFileName(symbolCode, filePrefixName, ""));
	return rldfCommoXXX;
    }

    /***
     * @author "Robert Lee" 辦理他所-XXX申請書歷史資料檔(W系列)
     * ***/
    public DataTable RLDFWXXXFromRLDFSXXX(final DataTable srcDataTable) {
	DataTable rldfwXXX = (DataTable) SerializationUtils.clone(srcDataTable);
	rldfwXXX.setTableName(rldfwXXX.getTableName().replace("RLDFS", "RLDFW"));
	rldfwXXX.setFileName(rldfwXXX.getFileName().replace("RLDFS", "RLDFW"));
	rldfwXXX.setChineseName("辦理他所" + rldfwXXX.getChineseName().replace("資料檔", "歷史資料檔"));
	rldfwXXX.setRemark(rldfwXXX.getTableName() + " " + rldfwXXX.getChineseName());
	return rldfwXXX;
    }

    /***
     * @param symbolCode
     *            登記代碼
     * @param chineseName
     *            登記名稱(ex:出生登記、死亡登記、結婚登記、未成年子女權利與義務行使負擔登記)
     * @param regCommonDataTable
     *            申請書共通欄項
     * @param eachRegRLDFSDataTable
     *            各項登記申請書獨有欄位
     * @param externalRegDataTable
     *            外來申請書共通欄項
     * ***/
    private DataTable generateRLDFRXXX(final String symbolCode, final String chineseName, final DataTable regCommonDataTable,
	    final DataTable eachRegRLDFSDataTable, final DataTable externalRegDataTable) {
	DataTable rldfrXXX = (DataTable) SerializationUtils.clone(regCommonDataTable);
	DataTable rldfr1XXXForEachData = (DataTable) SerializationUtils.clone(externalRegDataTable);
	DataTable rldfr2XXXForEachData = (DataTable) SerializationUtils.clone(eachRegRLDFSDataTable);

	rldfrXXX.getDataColumnInfos().addAll(rldfr1XXXForEachData.getDataColumnInfos());
	rldfrXXX.getDataColumnInfos().addAll(rldfr2XXXForEachData.getDataColumnInfos());
	rldfrXXX.setChineseName(chineseName + "外來申請書資料檔");
	rldfrXXX.setFileName(get64TableFileName(symbolCode, "RLDFR", ""));
	return rldfrXXX;
    }

    /***
     * @return Map<String,String> (fileName,content)
     * 
     * ***/
    @Override
    public Map<String, String> generateInitialScript(final List<DataTable> aList) {
	Map<String, String> result = new HashMap<String, String>();
	for (DataTable table : aList) {
	    result.put(table.getTableName() + ".txt", covertTxtFrom64TableDoc(table));
	}
	return result;
    }

    private static String get64TableFileName(final String symbolCode, final String prefix, final String suffix) {
//    	final String StringFormat ="6-4-$S$S$S.doc";
    	String result=new StringBuffer().append("6-4-").append(prefix).append(symbolCode).append(suffix).append(".doc").toString();
//    	String result=String.format(StringFormat, prefix,symbolCode,suffix);
    	return result;
    }

    /***
     * 由table doc產生嘉國需要的TXT
     * **/
    @Override
    public String covertTxtFrom64TableDoc(final DataTable dataTable) {
	StringBuffer sbf = new StringBuffer();
	sbf.append(dataTable.getTableName()).append("\r\n");
	for (DataColumnInfo column : dataTable.getDataColumnInfos()) {
	    sbf.append(column.getColumnName()).append(" \t").append(column.getFormat()).append("\r\n");
	}
	return sbf.toString();
    }

    /***
     * 由table doc產生*.sql（for Informix）
     * **/
    @Override
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
				errors.add("將要產生的" + tableName + "欄位名稱： " + columnName + "這樣是不合法的欄位名稱(只能由半形英文大小寫、_,構成,) ");
			}
		}
		if (!keyExisted) {
			errors.add("將要產生的" + tableName + "(" + aDataTable.getFileName() + "," + aDataTable.getChineseName() + ")" + "必須要有primary key欄位..");
		}
		if (CollectionUtils.isNotEmpty(errors)) {
			StringBuffer sbf = new StringBuffer();
			for (String message : errors) {
				sbf.append(message).append("\n\r");
			}
			throw new Exception(sbf.toString());
		}
	}
	private ScriptTableInfo covertScriptFromJDBCTablseSchema(final JDBCTablseSchema dataTable) {
		ScriptTableInfo result = new ScriptTableInfo();
		final 	Map<String, Set<String>> indexInfo = dataTable.getIndexInfo();
    	final 	Set<String> indexInfoKeySet = indexInfo.keySet();
    	Set<String> candidateKeys = null;
    	Set<String> candidateIndexs = new HashSet<String>();
    	for(String key :indexInfoKeySet){
    		if (StringUtils.containsIgnoreCase(key, "_p_key")){
    			candidateKeys = indexInfo.get(key);
    		}else{
    			candidateIndexs.addAll( indexInfo.get(key));
    		}
    	}
		List<ScriptColumnInfo> dataColumnInfos = result.getDataColumnInfos();
		for (JDBCColumnSachema column : dataTable.getDataColumnInfos()) {
			String columnName = StringUtils.upperCase(column.getColumnName());
			ScriptColumnInfo scriptColumnInfo = new ScriptColumnInfo();
			scriptColumnInfo.setColumnName(column.getColumnName());
			scriptColumnInfo.setFormat(column.getFormat());
			if(candidateKeys!=null& candidateKeys.contains(StringUtils.lowerCase(columnName))){    			
    			scriptColumnInfo.setNullable(false);
    			scriptColumnInfo.setPrimaryKey(true);
    		}else{
    			scriptColumnInfo.setPrimaryKey(false);
    		}
			if (candidateKeys != null && candidateKeys.contains(StringUtils.lowerCase(columnName))
					&& candidateIndexs.contains(StringUtils.lowerCase(columnName))) {
				scriptColumnInfo.setNullable(false);
			} else if (candidateKeys != null
					&& candidateIndexs.contains(StringUtils.lowerCase(columnName))) {
				scriptColumnInfo.setNullable(false);
			}
			
			
			dataColumnInfos.add(scriptColumnInfo);
		}
		String tableName =StringUtils.upperCase(dataTable.getTableName());
		result.setTableName(tableName);
		return result;
	}
	 public List<ScriptTableInfo> covertScriptFromJDBCTablseSchemaList(final List<JDBCTablseSchema> jdbcTableList){
	    	final List<ScriptTableInfo>  result = new ArrayList<ScriptTableInfo>();
	    	for(JDBCTablseSchema jdbcTable:jdbcTableList){
	    		ScriptTableInfo aDataTable = covertScriptFromJDBCTablseSchema(jdbcTable);
	    		result.add(aDataTable);
	    	}
	    	return result;
	    }
    public List<DataTable> convertDataTableListfromJDBCTablseSchemaList(final List<JDBCTablseSchema> jdbcTableList){
    	final List<DataTable>  result = new ArrayList<DataTable>();
    	for(JDBCTablseSchema jdbcTable:jdbcTableList){
    		DataTable aDataTable = convertDataTablefromJDBCTablseSchema(jdbcTable);
    		result.add(aDataTable);
    	}
    	return result;
    }
    private DataTable  convertDataTablefromJDBCTablseSchema(final JDBCTablseSchema jdbcTable){
    	DataTable result = new DataTable("template");
    	List<DataColumnInfo> columnList = result.getDataColumnInfos();
    	final String tableName =StringUtils.upperCase(jdbcTable.getTableName());
    	result.setFileName(String.format("6-4-%s.doc", tableName));
    	result.setTableName(tableName);
    	result.setChineseName("請補上中文名稱");
    	result.setRemark(tableName+"請補上中文名稱");
    	final List<JDBCColumnSachema> jdbcDataColumnInfos = jdbcTable.getDataColumnInfos();
    	final 	Map<String, Set<String>> indexInfo = jdbcTable.getIndexInfo();
    	final 	Set<String> indexInfoKeySet = indexInfo.keySet();
    	Set<String> candidateKeys = null;
    	Set<String> candidateIndexs = new HashSet<String>();
    	for(String key :indexInfoKeySet){
    		if (StringUtils.containsIgnoreCase(key, "_p_key")){
    			candidateKeys = indexInfo.get(key);
    		}else{
    			candidateIndexs.addAll( indexInfo.get(key));
    		}
    	}
    	for(JDBCColumnSachema jdbccColumnSachema :jdbcDataColumnInfos ){
    		String columnName = jdbccColumnSachema.getColumnName();
    		DataColumnInfo column = new DataColumnInfo();
    		if(candidateKeys ==null){
    			System.out.println("tableName: "+tableName+" has no pk");
    		}
    		if(candidateKeys!=null&& candidateKeys.contains(StringUtils.lowerCase(columnName))){
    			column.getValueArray()[0] = "PK";
    			column.getValueArray()[6] = "Unique";
    			column.isKey();
    		}else{
    			column.getValueArray()[0] = StringUtils.EMPTY;
    			column.getValueArray()[6] = StringUtils.EMPTY;
    		}
			if (candidateKeys != null && candidateKeys.contains(StringUtils.lowerCase(columnName))
					&& candidateIndexs.contains(StringUtils.lowerCase(columnName))) {
				column.getValueArray()[0] = "PK\nIndex";
			} else if (candidateKeys != null
					&& candidateIndexs.contains(StringUtils.lowerCase(columnName))) {
				column.getValueArray()[0] = "Index";
				column.getValueArray()[6] = "Unique";
			}
    		
    		column.getValueArray()[1] = StringUtils.upperCase(columnName );
    		column.getValueArray()[2] = "請補上中文名稱";
    		column.getValueArray()[3] = jdbccColumnSachema.getFormat();
    		column.getValueArray()[4] = StringUtils.EMPTY;
    		column.getValueArray()[5] = jdbccColumnSachema.getNullable()!=1?"N":"";
    		

    		columnList.add(column);
    	}
		return result;
	}
    /**
     * 將外部匯入的Excel資料整理需要的格式
     * **/
    @Override
    public DataTable readTemplateDataTable(final File exportFile) {
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
		String columnHeadNameEnglishName = line[headData.get(COLUMN_HEADNAME_ENG_NAME)];
		if (StringUtils.isNotBlank(columnHeadNameEnglishName)) {
		    columnHeadNameEnglishName = columnHeadNameEnglishName.trim();
		} else {
		    continue;
		}

		column.getValueArray()[0] = line[headData.get(COLUMN_HEADNAME_KEY)];
		column.getValueArray()[1] = columnHeadNameEnglishName;
		column.getValueArray()[2] = line[headData.get(COLUMN_HEADNAME_CHT_NAME)];
		column.getValueArray()[3] = line[headData.get(COLUMN_HEADNAME_FORMAT)];
		column.getValueArray()[4] = line[headData.get(COLUMN_HEADNAME_DEFAULT_VALUE)];
		column.getValueArray()[5] = line[headData.get(COLUMN_HEADNAME_IS_NULLABLE)];
		column.getValueArray()[6] = line[headData.get(COLUMN_HEADNAME_DESCRIPTION)];

		columnList.add(column);
	    }

	} catch (FileNotFoundException e) {
	    e.printStackTrace(); 
	} catch (IOException e) {
	    e.printStackTrace();
	}
	return result;
    }

    @Override
    public Workbook generateTemplate() {
	Workbook wb = new HSSFWorkbook();
	org.apache.poi.ss.usermodel.Sheet sheet = wb.createSheet(SHEET_NAME);
	CellStyle style01 = POIUtils.buildCellStyle(sheet, Integer.valueOf(13),   "標楷體", false);

	CellStyle style02 = POIUtils.buildCellStyle(sheet, Integer.valueOf(10),  true, true, (int) IndexedColors.WHITE.getIndex());

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
	result.add(new String[] { COLUMN_HEADNAME_KEY, COLUMN_HEADNAME_ENG_NAME, COLUMN_HEADNAME_CHT_NAME, COLUMN_HEADNAME_FORMAT,
		COLUMN_HEADNAME_DEFAULT_VALUE, COLUMN_HEADNAME_IS_NULLABLE, COLUMN_HEADNAME_DESCRIPTION });
	result.add(new String[] { "PK", "REGISTER_CODE", "記事代碼", "CHAR(10)", "", "N", "" });
	result.add(new String[] { "PK", "REGISTER_ADMIN_OFFICE_CODE", "登記戶所代碼", "CHAR(08)", "", "N", "" });
	result.add(new String[] { "PK", "REGISTRAR_NAME", "戶籍員姓名", "VARCHAR(32)", "", "N", "" });
	result.add(new String[] { "PK", "APPLY_TRANSACTION_ID", "交易序號", "CHAR(22)", "", "N", "" });
	result.add(new String[] { "PK", "APPLY_SEQUENCE_ID", "作業順序編號", "INTEGER", "", "N", "" });
	return result;
    }

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
