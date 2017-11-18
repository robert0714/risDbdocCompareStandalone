package org.robert.study.tradition.dao;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.iisi.rl.table.jdbc.schema.JDBCTableMiningResult;
import com.iisi.rl.table.jdbc.schema.JDBCTablseSchema;
import com.iisi.rl.table.script.ScriptTableInfo;

public interface GenericDao {
    public void setDataSource(DataSource dataSource);

    public Map<String, Integer> getColumnMetaInfos(final String table)throws SQLException;
    public String[] getPimaryKeys(final String table) throws SQLException;
    public void insertData(final String table, String[] columnNames, Object[] valueDatas)throws SQLException;
    public void insertData(final String table, String[] columnNames, List<Object[]> valueData) throws SQLException;
    public List<String> getColumnNameList(String table) throws SQLException;
    public void batchInsertData(final String table, String[] columnNames, List<Object[]> valueData) throws SQLException;
    public List<JDBCTablseSchema> getJDBCTablseSchemasBySingleConnection2(List<String> tablelist, DataSource ds)
			throws SQLException ;
    public JDBCTableMiningResult getJDBCTablseSchemasBySingleConnection(List<ScriptTableInfo> tablelist, DataSource ds)
			throws SQLException;
	public   List<String>  getAllTableName(DataSource ds) throws SQLException;
}
