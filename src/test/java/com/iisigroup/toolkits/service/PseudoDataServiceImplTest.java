package com.iisigroup.toolkits.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.poi.ss.usermodel.Workbook;
import org.robert.study.tradition.dao.GenericDao;

import com.iisigroup.tradition.dao.jdbc.GenericDaoImpl;

public class PseudoDataServiceImplTest {
    static PseudoDataServiceImpl pseudoDataServiceImpl = new PseudoDataServiceImpl();
    static DataSource ds = pseudoDataServiceImpl.setupDataSource();;

    /**
     * @param args
     * @throws SQLException
     */
    public static void main(String[] args) throws SQLException {
        testMethod03();
        pseudoDataServiceImpl.printDataSourceStats(ds);
        pseudoDataServiceImpl.shutdownDataSource(ds);
    }

    public static void testMethod02() {
        GenericDao genericDao = new GenericDaoImpl();
        genericDao.setDataSource(ds);
        pseudoDataServiceImpl.setDao(genericDao);
        try {
            pseudoDataServiceImpl.readXLS("RLDF005M", new File("/home/weblogic/Desktop/RLDF005M_template.xls"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void testMethod03() throws SQLException {
        GenericDao genericDao = new GenericDaoImpl();
        genericDao.setDataSource(ds);
        pseudoDataServiceImpl.setDao(genericDao);
        Workbook wb = pseudoDataServiceImpl.generateTemplateXLS("RLDF005M");
        File export = new File("/home/weblogic/Desktop/RLDF005M_template.xls");
        FileOutputStream fileOut = null;
        try {
            fileOut = new FileOutputStream(export);
            wb.write(fileOut);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileOut != null) {
                try {
                    fileOut.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            pseudoDataServiceImpl.shutdownDataSource(ds);
        }
    }
}
