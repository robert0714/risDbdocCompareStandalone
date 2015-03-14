package org.robert.study.service;

import java.io.File;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.poi.ss.usermodel.Workbook;
import org.robert.study.tradition.dao.GenericDao;

public interface PseudoDataService {

    public void shutdownDataSource(DataSource ds) throws SQLException;

    public void printDataSourceStats(DataSource ds);

    public DataSource setupDataSource();

    public void setDao(GenericDao dao);

    public Workbook generateTemplateXLS(final String tableName) throws SQLException;

    public void readXLS(final String tableName, final File importFile) throws Exception;

    public void readConfigData(final File file);

}
