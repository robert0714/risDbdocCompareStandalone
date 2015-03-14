package com.iisi.doc.process;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.lang.SerializationUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.robert.study.utils.POIUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.xwpf.usermodel.XWPFFooter;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText;

import com.iisi.rl.table.DataColumnInfo;
import com.iisi.rl.table.DataTable;
import com.iisi.rl.table.script.ScriptTableInfo;
import com.iisigroup.toolkits.service.ExcelManipulationServiceImpl;

public class DocWriter {
	private static String CHANGE_LINE_TAG = "#BR#";

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws Exception {
		ExcelManipulationServiceImpl main = new ExcelManipulationServiceImpl();
		DataTable templateDataTable = main.readTemplateDataTable(new File("z://aaaaa.xls"));
		DataTable rldfXM = (DataTable) SerializationUtils.clone(templateDataTable);
		rldfXM.setTableName("rldfXM");
		rldfXM.setChineseName("未成年子女權利與義務行駛負擔登記記事欄位化");
		rldfXM.setFileName("RLDFX080M.doc");
		ScriptTableInfo aScriptTableInfo = main.covertFrom64TableDoc(rldfXM);
		System.out.println(aScriptTableInfo.getStatement());
		System.out.println(main.covertTxtFrom64TableDoc(rldfXM));

		DocWriter aDocWriter = new DocWriter();
		XWPFDocument doc = aDocWriter.createNewWord(rldfXM);
		POIUtils.writePOIXMLDocumentPartOut(new File("z://RLDFX080M.doc"), doc);

	}

	public XWPFDocument createNewWord(DataTable ddtable) throws IOException {
		XWPFDocument document = null;

		document = new XWPFDocument(getClass().getResource("templateDoc.docx").openStream());

		List<XWPFParagraph> paragraphs = document.getParagraphs();
		setParagraphText(ddtable.getChineseName(), paragraphs.get(0));
		setParagraphText("表1" + ddtable.getChineseName(), paragraphs.get(1));
		List<XWPFTable> tables = document.getTables();
		XWPFTable table = tables.get(0);
		List<XWPFTableRow> tableRows = table.getRows();
		for (int row = 0; row < tableRows.size(); row++) {
			XWPFTableRow tableRow = tableRows.get(row);
			List<XWPFTableCell> tableCells = tableRow.getTableCells();
			if (tableCells.size() == 1) {
				XWPFTableCell tableCell = tableCells.get(0);
				setCellValue(ddtable.getRemark(), tableCell);
			}
		}
		for (int ii = 0; ii < ddtable.getDataColumnInfos().size(); ii++) {
			DataColumnInfo column = ddtable.getDataColumnInfos().get(ii);
			XWPFTableRow row = table.createRow();
			for (int j = 0; j < 6; j++) {
				row.createCell();
			}
			this.setCellValue(column.isKey() ? "PK" : "", row.getCell(0));
			this.setCellValue(column.getColumnName(), row.getCell(1));
			this.setCellValue(column.getChineseName(), row.getCell(2));
			this.setCellValue(column.getFormat(), row.getCell(3));
			this.setCellValue(column.getDefaultValue(), row.getCell(4));
			this.setCellValue(column.isNullable() ? "" : "N", row.getCell(5));
			this.setCellValue(column.getDescription(), row.getCell(6));
		}

		if (document != null) {
			String footerName = new StringBuilder("6-4-").append(ddtable.getTableName()).toString();
			wrappedProcess(document, footerName);
		}
		return document;
	}

	/***
	 * Processing the left bottom corner of template page,replacing the text
	 * "6-4-RLDF001M" to the new word that you want to change
	 * ***/
	private void wrappedProcess(XWPFDocument document, String replacingword) {
		List<XWPFFooter> footerList = document.getFooterList();
		StringBuffer footerInspect = new StringBuffer();
		for (XWPFFooter footer : footerList) {
			List<XWPFTable> tables = footer.getTables();
			if (!CollectionUtils.isEmpty(tables)) {
				XWPFTable table = tables.get(0);
				for (CTRow row : table.getCTTbl().getTrList()) {
					for (CTTc cell : row.getTcList()) {
						// System.out.println("ctp size: " +
						// cell.getPList().size());
						for (CTP ctp : cell.getPList()) {
							XWPFParagraph p = new XWPFParagraph(ctp, table.getBody());
							List<XWPFRun> array = p.getRuns();
							XWPFRun run = array.get(0);
							// System.out.println("run.toString(): " +
							// run.toString());
							CTR ctrun = run.getCTR();
							XmlCursor c = ctrun.newCursor();
							c.selectPath("./*");
							StringBuffer text = new StringBuffer();
							while (c.toNextSelection()) {
								XmlObject o = c.getObject();
								if (o instanceof CTText) {
									String tagName = o.getDomNode().getNodeName();
									if (!("w:instrText".equals(tagName))) {
										CTText aCTText = (CTText) o;
										String textContentValue = aCTText.getStringValue();
										if (textContentValue != null && textContentValue.contains("6-4-RLDF001M")) {
											if (StringUtils.isNotBlank(replacingword)) {
												aCTText.setStringValue(replacingword);
											} else {
												aCTText.setStringValue("6-4-M");
											}

										}
										// System.out.println("**********: " +
										// ((CTText) o).getStringValue());
										text.append(((CTText) o).getStringValue());
									}
								}
							}
							c.dispose();
							// System.out.println("_________:" +
							// text.toString());
							footerInspect.append(text);
						}
					}
				}
			}
			System.out.println("footer:" + footerInspect.toString());
		}
	}

	private void setParagraphText(String text, XWPFParagraph para) {
		if (text == null) {
			return;
		}
		if (para.getRuns().size() > 0) {
			for (; para.getRuns().size() > 0;) {
				para.removeRun(0);
			}
		}
		String[] args = text.split(CHANGE_LINE_TAG, -1);
		for (int ii = 0; ii < args.length; ii++) {
			System.out.println("ParagraphText: " + ii + "..." + args[ii]);
			para.createRun().setText(args[ii]);
			if (ii != args.length - 1) {
				System.out.println("<<<");
				para.createRun().addBreak();
			}
		}
	}

	private void setCellValue(String value, XWPFTableCell cell) {
		if (value == null || cell == null) {
			return;
		}
		for (; cell.getParagraphs().size() > 0;) {
			cell.removeParagraph(0);
		}
		XWPFParagraph para = null;
		String[] args = value.split(CHANGE_LINE_TAG, -1);
		for (int ii = 0; ii < args.length; ii++) {
			para = cell.addParagraph();
			setDefaultParagraphConfig(para);
			para.createRun().setText(args[ii]);
		}
	}

	private void setDefaultParagraphConfig(XWPFParagraph para) {
		para.setIndentationFirstLine(0);// 設定尺規為0
		para.setIndentationHanging(0);
		para.setIndentationLeft(0);
		para.setIndentationRight(0);
		para.setAlignment(ParagraphAlignment.LEFT);
		if (para.getRuns().size() > 0) {
			for (int ii = 0; ii < para.getRuns().size(); ii++) {
				para.removeRun(ii);
				ii--;
			}
		}
	}

}
