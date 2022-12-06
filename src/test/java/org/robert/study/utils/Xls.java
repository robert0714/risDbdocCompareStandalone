/*
 * Copyright (c) 2010-2020 IISI.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of IISI.
 */
package org.robert.study.utils;

import com.iisi.rl.table.DataColumnInfo;
import com.iisi.rl.table.DataTable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage; 
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 */
public class Xls {

    /**
     * @param args
     * @throws InvalidFormatException 
     * @throws IOException 
     */
    public static void main(String[] args) throws InvalidFormatException, IOException {
//        File readFile =new File("/home/weblogic/Documents/桃園升格測試資料.xlsx");
        final InputStream is = Xls.class.getResourceAsStream("Xls.xlsx");
        OPCPackage pkg = OPCPackage.open(is);
        XSSFWorkbook wb = new XSSFWorkbook(pkg);
        final XSSFSheet sheet = wb.getSheetAt(0);
        final int lastRowNum = sheet.getLastRowNum();
        for(int i = 0 ; i< lastRowNum ;++i){
            XSSFRow row = sheet.getRow(i);
            short lastCellNum = row.getLastCellNum();
            for(int j = 0 ;row.getCell(j)!=null && j < lastCellNum ;++j){
                final  XSSFCell cell = row.getCell(j);
                String returnStr =null;
                final  CellType cellStyle = cell.getCellType();
                switch (cellStyle) {
                    case  NUMERIC:
                        double data = cell.getNumericCellValue();
                        int intValue = (int)data;
                        returnStr = String.valueOf(intValue);
                        break;
                    case  STRING:
                        returnStr =  cell.getStringCellValue();
                        break;
                    default:
                        break;
                } 
//                System.out.println(String.format("y:%s, x:%s ,value:%s", i,j,returnStr!=null ?returnStr:""));
                 
                switch(j){
                    case 0:
                        System.out.println("<bean  class=\"tw.gov.moi.rl.component.dto.AdminOfficePeriodDto\">");
                        System.out.println(String.format("<property name=\"siteId\" value=\"%s\" />",
                                returnStr != null ? returnStr : ""));
                        break;
                    case 1:
                        System.out.println(String.format("<property name=\"adminOfficeCode\" value=\"%s\" />",
                                returnStr != null ? returnStr : ""));
                        break;
                    case 2:
                        System.out.println(String.format("<property name=\"startYyyMMdd\" value=\"%s\" />",
                                returnStr != null ? returnStr : ""));
                        break;
                    case 3:
                        System.out.println(String.format("<property name=\"endYyyMMdd\" value=\"%s\" />",
                                returnStr != null ? returnStr : ""));
                        break;
                    case 4:
                        System.out.println(String.format("<property name=\"officeChtName\" value=\"%s\" />",
                                returnStr != null ? returnStr : ""));
                        break;
                    case 5:
                        System.out.println(String.format("<property name=\"cityChtName\" value=\"%s\" />",
                                returnStr != null ? returnStr : ""));
                        break;
                    case 6:
                        System.out.println(String.format("<property name=\"officeEngName1\" value=\"%s\" />",
                                returnStr != null ? returnStr : ""));
                        break;
                    case 7:
                        System.out.println(String.format("<property name=\"cityEngName1\" value=\"%s\" />",
                                returnStr != null ? returnStr : ""));
                        break;
                    case 8:
                        System.out.println(String.format("<property name=\"officeEngName2\" value=\"%s\" />",
                                returnStr != null ? returnStr : ""));
                        break;
                    case 9:
                        System.out.println(String.format("<property name=\"cityEngName2\" value=\"%s\" />",
                                returnStr != null ? returnStr : ""));
                        System.out.println("</bean>");
                        break;
                    case 10:
                        break;
                    case 11:
                        break;
                }
            }
           
        }
        pkg.close();
    }



    
}
