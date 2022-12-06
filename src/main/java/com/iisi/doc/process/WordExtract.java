package com.iisi.doc.process;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils; 
import org.apache.poi.extractor.ExtractorFactory;
import org.apache.poi.extractor.POITextExtractor;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.Word6Extractor;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.hwpf.usermodel.Table;
import org.apache.poi.hwpf.usermodel.TableCell;
import org.apache.poi.hwpf.usermodel.TableIterator;
import org.apache.poi.hwpf.usermodel.TableRow; 
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow; 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import com.iisi.rl.table.DataColumnInfo;
import com.iisi.rl.table.DataTable;

public class WordExtract {
    private final Logger log = LoggerFactory.getLogger(WordExtract.class);
    private final String CHANGE_LINE_TAG = "#BR#";
    private final Pattern ONLY_WORD = Pattern
            .compile("[\u4e00-\u9fa5|\uFE30-\uFFA0|\\w|\\(|\\)|_|\\[|\\]|\\{|\\}|\\.]+");

    public List<DataTable> covertFromFolderPath(final String folderPath) throws IOException {
        return covertFromFolder(new File(folderPath));
    }

    public List<File> getQualifiedFile(final File folder) throws IOException {
        List<File> result = new ArrayList<File>();
        for (File aFile : folder.listFiles()) {
            String simpleFileName = aFile.getName();
            if (aFile.isFile() && !simpleFileName.contains("~")) {
                result.add(aFile);
            }
        }
        return result;
    }

    public List<DataTable> covertFromFolder(final File folder) throws IOException {
        List<DataTable> result = new ArrayList<DataTable>();
        for (File aFile : folder.listFiles()) {
            String simpleFileName = aFile.getName();
            if (aFile.isFile() && !simpleFileName.contains("~")) {
                String fileName = aFile.getAbsolutePath();
                DataTable aDataTable = convertTOUnit(fileName);
                result.add(aDataTable);
            }
        }

        return result;
    }
    private String extractTitleHWPFDocument(InputStream fis, final File srcFile) {
    	final String fileName = srcFile.getAbsoluteFile().getName();
        final  List<String> information=new ArrayList<String>();
        try {
            fis = new FileInputStream(srcFile);
            POIFSFileSystem fs = new POIFSFileSystem(fis);
            HWPFDocument doc = new HWPFDocument(fs);

            Range range = doc.getRange();

            TableIterator it = new TableIterator(range);

            while (it.hasNext()) {
                Table tb = (Table) it.next();
                outer: for (int i = 0; i < tb.numRows(); i++) {
                    TableRow tr = tb.getRow(i);
                    // log.debug("------------------------------------");
                    if (tr.numCells() == 1) {
                	information.add(tr.getCell(0).getParagraph(0).text());
                    } 
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
            log.debug("file: " + fileName);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        
        return CollectionUtils.isNotEmpty(information)?information.get(0):fileName;
    }
    private DataTable processHWPFDocument(InputStream fis, final File srcFile) {
    	final String fileName = srcFile.getAbsoluteFile().getName();
        final DataTable result = new DataTable(fileName);
        try {
            fis = new FileInputStream(srcFile);
            POIFSFileSystem fs = new POIFSFileSystem(fis);
            HWPFDocument doc = new HWPFDocument(fs);

            Range range = doc.getRange();

            TableIterator it = new TableIterator(range);

            while (it.hasNext()) {
                Table tb = (Table) it.next();
                outer: for (int i = 0; i < tb.numRows(); i++) {
                    TableRow tr = tb.getRow(i);
                    // log.debug("------------------------------------");
                    if (tr.numCells() == 1) {
                        result.getInfomation().add(tr.getCell(0).getParagraph(0).text());
                    } else if (tr.numCells() == 7) {
                        DataColumnInfo aAttribute = new DataColumnInfo();
                        for (int j = 0; j < tr.numCells(); j++) {
                            TableCell td = tr.getCell(j);
                            for (int k = 0; k < td.numParagraphs(); k++) {
                                Paragraph para = td.getParagraph(k);
                                String s = para.text().trim();
                                aAttribute.getValueArray()[j] = s;
                                if (s.contains("欄位名稱")) {
                                    continue outer;
                                }
                            }
                        }
                        result.getDataColumnInfos().add(aAttribute);
                    }
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
            log.debug("file: " + fileName);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
    private DataTable processHWPFDocument(InputStream fis, final String fileName) {
        final DataTable result = new DataTable(new File(fileName).getAbsoluteFile().getName());
        try {
            fis = new FileInputStream(fileName);
            POIFSFileSystem fs = new POIFSFileSystem(fis);
            HWPFDocument doc = new HWPFDocument(fs);

            Range range = doc.getRange();

            TableIterator it = new TableIterator(range);

            while (it.hasNext()) {
                Table tb = (Table) it.next();
                outer: for (int i = 0; i < tb.numRows(); i++) {
                    TableRow tr = tb.getRow(i);
                    // log.debug("------------------------------------");
                    if (tr.numCells() == 1) {
                        result.getInfomation().add(tr.getCell(0).getParagraph(0).text());
                    } else if (tr.numCells() == 7) {
                        DataColumnInfo aAttribute = new DataColumnInfo();
                        for (int j = 0; j < tr.numCells(); j++) {
                            TableCell td = tr.getCell(j);
                            StringBuffer sbf = new StringBuffer();
                            for (int k = 0; k < td.numParagraphs(); k++) {
                                Paragraph para = td.getParagraph(k);
                                String s = para.text().trim();
                                sbf.append(s);
                                
                            }
                            String contentTmp = sbf.toString();
                            aAttribute.getValueArray()[j] = contentTmp;
                            if (contentTmp.contains("欄位名稱")) {
                                continue outer;
                            }
                        }
                        result.getDataColumnInfos().add(aAttribute);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            log.debug("file: " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
            log.debug("file: " + fileName);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
    private String extractTitleXWPFDocument(InputStream fis, final File file) {
        final String fileName = file.getAbsoluteFile().getName();
        String result =null;
        try {
            fis = new FileInputStream(file);

            XWPFDocument doc = new XWPFDocument(fis);

            XWPFTable table = doc.getTables().get(0);
            List<XWPFTableRow> tableRows = table.getRows();
            for (int i = 0; i < tableRows.size(); i++) {
                XWPFTableRow tr = tableRows.get(i);
                List<XWPFTableCell> tableCells = tr.getTableCells();
                if (tableCells.size() == 1) {
                    XWPFTableCell tableCell = tableCells.get(0);
                    String title = getCellValue_DOCX(tableCell);

                    result =title;
                    System.out.println(0 + " : " + title);
                    
                }
                
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.debug("file: " + fileName);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
    private DataTable processXWPFDocument(InputStream fis, final File file) {
        final DataTable result = new DataTable(file.getAbsoluteFile().getName());
        final Pattern TTILE_PATN = Pattern.compile("(\\w+)(.+)");
        final String fileName = file.getAbsoluteFile().getName();
        try {
            fis = new FileInputStream(file);

            XWPFDocument doc = new XWPFDocument(fis);

            // Iterator<XWPFTable> j = doc.getTablesIterator();
            // while (j.hasNext()) {
            // log.debug(j.next().getText());
            // }
            XWPFTable table = doc.getTables().get(0);
            List<XWPFTableRow> tableRows = table.getRows();
            for (int row = 0; row < tableRows.size(); row++) {
                XWPFTableRow tableRow = tableRows.get(row);
                List<XWPFTableCell> tableCells = tableRow.getTableCells();
                for (int column = 0; column < tableCells.size(); column++) {
                    XWPFTableCell tableCell = tableCells.get(column);
                }
            }

            for (int i = 0; i < tableRows.size(); i++) {
                XWPFTableRow tr = tableRows.get(i);
                List<XWPFTableCell> tableCells = tr.getTableCells();
                // log.debug("------------------------------------");
                if (tableCells.size() == 1) {
                    XWPFTableCell tableCell = tableCells.get(0);
                    String title = getCellValue_DOCX(tableCell);

                    result.setRemark(title);
                    System.out.println(0 + " : " + title);
                    System.out.println(0 + " : " + title.split(CHANGE_LINE_TAG, -1));
                    Matcher matcher = TTILE_PATN.matcher(title.split(CHANGE_LINE_TAG, -1)[0]);
                    if (matcher.find()) {
                        result.setTableName(matcher.group(1));
                        result.setChineseName(matcher.group(2));
                    }
                }
                if (tableCells.size() == 7 && i != 1) {
                    log.debug("----------------------");
                    for (int j = 0; j < tableCells.size(); j++) {
                        XWPFTableCell td = tableCells.get(j);
                        log.debug(j + " : " + getCellValue_DOCX(td));
                    }
                    DataColumnInfo aAttribute = new DataColumnInfo();
                    for (int j = 0; j < 7; j++) {
                        aAttribute.getValueArray()[j] = getCellValue_DOCX(tr.getCell(j));
                        ;
                    }
                    result.getDataColumnInfos().add(aAttribute);
                    log.debug("----------------------");
                }
            }
            return result;

        } catch (Exception e) {
            e.printStackTrace();
            log.debug("file: " + fileName);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
    private DataTable processXWPFDocument(InputStream fis, final String fileName) {
        final DataTable result = new DataTable(new File(fileName).getAbsoluteFile().getName());
        final Pattern TTILE_PATN = Pattern.compile("(\\w+)(.+)");
        try {
            fis = new FileInputStream(fileName);

            XWPFDocument doc = new XWPFDocument(fis);
            
            XWPFTable table = doc.getTables().get(0);
            List<XWPFTableRow> tableRows = table.getRows();
            for (int row = 0; row < tableRows.size(); row++) {
                XWPFTableRow tableRow = tableRows.get(row);
                List<XWPFTableCell> tableCells = tableRow.getTableCells();
                for (int column = 0; column < tableCells.size(); column++) {
                    XWPFTableCell tableCell = tableCells.get(column);
                }
            }

            for (int i = 0; i < tableRows.size(); i++) {
                XWPFTableRow tr = tableRows.get(i);
                List<XWPFTableCell> tableCells = tr.getTableCells();
                // log.debug("------------------------------------");
                if (tableCells.size() == 1) {
                    XWPFTableCell tableCell = tableCells.get(0);
                    String title = getCellValue_DOCX(tableCell);

                    result.setRemark(title);
                    System.out.println(0 + " : " + title);
                    System.out.println(0 + " : " + title.split(CHANGE_LINE_TAG, -1));
                    Matcher matcher = TTILE_PATN.matcher(title.split(CHANGE_LINE_TAG, -1)[0]);
                    if (matcher.find()) {
                        result.setTableName(matcher.group(1));
                        result.setChineseName(matcher.group(2));
                    }
                }
                if (tableCells.size() == 7 && i != 1) {
                    log.debug("----------------------");
                    for (int j = 0; j < tableCells.size(); j++) {
                        XWPFTableCell td = tableCells.get(j);
                        log.debug(j + " : " + getCellValue_DOCX(td));
                    }
                    DataColumnInfo aAttribute = new DataColumnInfo();
                    for (int j = 0; j < 7; j++) {
                        aAttribute.getValueArray()[j] = getCellValue_DOCX(tr.getCell(j));
                        ;
                    }
                    result.getDataColumnInfos().add(aAttribute);
                    log.debug("----------------------");
                }
            }
            return result;

        } catch (Exception e) {
            e.printStackTrace();
            log.debug("file: " + fileName);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    String getCellValue_DOCX(XWPFTableCell td) {
        return getParaDOCX(td.getParagraphs());
    }

    private String getParaDOCX(List<XWPFParagraph> list) {
        StringBuilder ssb = new StringBuilder();
        for (int k = 0; k < list.size(); k++) {
            XWPFParagraph para = list.get(k);
            Matcher matcher = ONLY_WORD.matcher(para.getText());
            while (matcher.find()) {
                ssb.append(matcher.group());
            }
            if (k != list.size() - 1) {
                ssb.append(CHANGE_LINE_TAG);
            }
        }
        return ssb.toString();
    }
    public String convertTOTitle(final File file) {
	String result = null;
        InputStream in = null;
        final String fileName = file.getName(); 
        try {
            in = new FileInputStream(file);

            POITextExtractor textExtractor = ExtractorFactory.createExtractor(in);
            if (textExtractor instanceof WordExtractor) // doc, word
                                                               // 97-2003
            {
                log.debug("doc, word 97-2003");
                // HWPFDocument hwpfDocument = new HWPFDocument(is);
                //extractTitleHWPFDocument
                result = extractTitleHWPFDocument(in, file);

            } else if (textExtractor instanceof XWPFWordExtractor) // docx, word
                                                                   // 2007
            {
                log.debug("doc, word 2007");
                // XWPFWordExtractor extractor = (XWPFWordExtractor)
                // textExtractor;
                //extractTitleXWPFDocument
                result = extractTitleXWPFDocument(in, file);

            }

        }  catch (Exception e) {
            e.printStackTrace();
            log.debug("exception: " + fileName);
        }

        return result;
    }
    public DataTable convertTOUnit(final File file) {
        DataTable result = null;
        InputStream in = null;
        final String fileName = file.getName(); 
        try {
            in = new FileInputStream(file);

            POITextExtractor textExtractor = ExtractorFactory.createExtractor(in);
            if (textExtractor instanceof Word6Extractor) // doc, word 95
            {
                log.debug("doc, word 95");

                // HWPFOldDocument doc = new HWPFOldDocument(is);

            } else if (textExtractor instanceof WordExtractor) // doc, word
                                                               // 97-2003
            {
                log.debug("doc, word 97-2003");
                // HWPFDocument hwpfDocument = new HWPFDocument(is);
                result = processHWPFDocument(in, file);

            } else if (textExtractor instanceof XWPFWordExtractor) // docx, word
                                                                   // 2007
            {
                log.debug("doc, word 2007");
                // XWPFWordExtractor extractor = (XWPFWordExtractor)
                // textExtractor;
                result = processXWPFDocument(in, file);

            }

        }  catch (Exception e) {
            e.printStackTrace();
            log.debug("exception: " + fileName);
        }

        return result;
    }
    public DataTable convertTOUnit(final String fileName) {
        DataTable result = null;
        InputStream in = null;
        try {
            in = new FileInputStream(fileName);

            POITextExtractor textExtractor = ExtractorFactory.createExtractor(in);
            if (textExtractor instanceof Word6Extractor) // doc, word 95
            {
                log.debug("doc, word 95");

                // HWPFOldDocument doc = new HWPFOldDocument(is);

            } else if (textExtractor instanceof WordExtractor) // doc, word
                                                               // 97-2003
            {
                log.debug("doc, word 97-2003");
                // HWPFDocument hwpfDocument = new HWPFDocument(is);
                result = processHWPFDocument(in, fileName);

            } else if (textExtractor instanceof XWPFWordExtractor) // docx, word
                                                                   // 2007
            {
                log.debug("doc, word 2007");
                // XWPFWordExtractor extractor = (XWPFWordExtractor)
                // textExtractor;
                result = processXWPFDocument(in, fileName);

            }

        } catch (Exception e) {
            e.printStackTrace();
            log.debug("exception: " + fileName);
        }

        return result;
    }
}
