package org.robert.study.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.iisi.rl.table.DataColumnInfo;

public class Test {
final static  String template ="<bean  class=\"tw.gov.moi.rl.component.dto.AdminOfficePeriodDto\">\r"
+"	<property name=\"siteId\" value=\"%s\" />\r"
+"	<property name=\"adminOfficeCode\" value=\"%s\" />\r"
+"	<property name=\"startYyyMMdd\" value=\"%s\" />\r"
+"	<property name=\"endYyyMMdd\" value=\"%s\" />\r"
+"	<property name=\"officeChtName\" value=\"%s\" />\r"
+"	<property name=\"cityChtName\" value=\"%s\" />\r"
+"	<property name=\"officeEngName1\" value=\"%s\" />\r"
+"	<property name=\"cityEngName1\" value=\"%s\" />\r"
+"	<property name=\"officeEngName2\" value=\"%s\" />\r"
+"	<property name=\"cityEngName2\" value=\"%s\" />\r"			
+"</bean>\r";
    /**
     * @param args
     */
    public static void main(String[] args) {
	final List<String> lines = new ArrayList<String>();
	File srcFile =new File("/home/weblogic/Desktop/RSCD0106.xls");
	File file = new File("/home/weblogic/Desktop/RSCD0106.xml");
	InputStream inp=null;
	try {
		inp = new FileInputStream(srcFile);
		// InputStream inp = new FileInputStream("workbook.xlsx");
		Workbook wb = WorkbookFactory.create(inp);
		org.apache.poi.ss.usermodel.Sheet sheet = wb.getSheet("工作表5");
		;
		List<String[]> initData = POIUtils.processFromSheet(sheet);
		for (int i = 1; i < initData.size(); ++i) {
			String[] line = initData.get(i);
			lines.add(String.format(template, line));
		}
FileUtils.writeLines(file, lines);
	} catch (FileNotFoundException e) {
		e.printStackTrace(); 
	} catch (IOException e) {
		e.printStackTrace();
	}finally{
	    if(inp!=null){
		try {
		    inp.close();
		} catch (IOException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
	    }
	}
    }

}
