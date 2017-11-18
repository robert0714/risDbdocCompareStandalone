package com.iisigroup.toolkits.service;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.robert.study.tradition.dao.GenericDao;

import com.iisi.rl.table.jdbc.schema.JDBCTableMiningResult;
import com.iisi.rl.table.jdbc.schema.JDBCTablseSchema;
import com.iisi.rl.table.script.ScriptTableInfo;
import com.iisigroup.tradition.dao.jdbc.GenericDaoImpl;

public class JdbcProcessServiceImpl {
	private   Pattern sqlPattern = Pattern.compile(".*.sql");
	
	

	public JDBCTableMiningResult retirieveTableInfoFromDBAccordingSQL(final File folder, final javax.sql.DataSource ds) throws SQLException {
		List<String> tablelist = new ArrayList<String>();
		final ScanInspectServiceImpl scanInspect = new ScanInspectServiceImpl();
		List<ScriptTableInfo> ScriptTableInfoList = new ArrayList<ScriptTableInfo>();
		for (File file : folder.listFiles()) {
			if (file.isFile() && sqlPattern.matcher(file.getName()).matches()) {
				ScriptTableInfo aDataTable = scanInspect.convertTOUnit(file);
				ScriptTableInfoList.add(aDataTable);
				tablelist.add(aDataTable.getTableName());
			}
		}
		GenericDao genericDao = new GenericDaoImpl();
		genericDao.setDataSource(ds);

		JDBCTableMiningResult initResult = genericDao.getJDBCTablseSchemasBySingleConnection(ScriptTableInfoList, ds);
		return initResult;
	}

    public List<JDBCTablseSchema> retirieveTableInfoFromDB(final javax.sql.DataSource ds) throws SQLException {
	GenericDao genericDao = new GenericDaoImpl();
	genericDao.setDataSource(ds);
	final List<String> tablelist = genericDao.getAllTableName(ds);

	List<JDBCTablseSchema> result = genericDao.getJDBCTablseSchemasBySingleConnection2(tablelist, ds);

	return result;
    }
}
