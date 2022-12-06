package com.iisigroup.tradition.dao.jdbc;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.robert.study.discrepancy.model.DiscrepancyColumnInterface;
import org.robert.study.tradition.dao.GenericDao;

import com.iisi.rl.table.jdbc.schema.JDBCColumnSachema;
import com.iisi.rl.table.jdbc.schema.JDBCTableMiningResult;
import com.iisi.rl.table.jdbc.schema.JDBCTablseSchema;
import com.iisi.rl.table.script.ScriptColumnInfo;
import com.iisi.rl.table.script.ScriptTableInfo;

public class GenericDaoImpl implements GenericDao {
	private Log log = LogFactory.getLog(GenericDaoImpl.class);
	private DataSource dataSource;

	@Resource(name = "dataSource")
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public List<String> getAllTableName(DataSource ds) throws SQLException {
		List<String> result = new ArrayList<String>();
		Connection connection = null;
		try {
			connection = ds.getConnection();

			final DatabaseMetaData dbmd = connection.getMetaData();

			String[] types = { "TABLE" };
			ResultSet resultSet = dbmd.getTables(null, null, "%", types);

			while (resultSet.next()) {
				String tableName = resultSet.getString(3);
				String tableCatalog = resultSet.getString(1);
				String tableSchema = resultSet.getString(2);
				result.add(tableName);
			}
			resultSet.close();
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	public String[] getPimaryKeys(final String table) throws SQLException {
		List<String> result = new ArrayList<String>();
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			DatabaseMetaData metaData = connection.getMetaData();
			ResultSet rs = metaData.getPrimaryKeys(null, null, table);

			while (rs.next()) {
				String columnName = rs.getString("COLUMN_NAME");
//				System.out.println("getPrimaryKeys(): columnName=" + columnName);
				result.add(columnName);
			}

			rs.close();
			stmt.clearBatch();
			stmt.close();
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return result.toArray(new String[] {});
	}

	public Map<String, Set<String>> getIndexInfo(final String table) throws SQLException {
		Map<String, Set<String>> result = new HashMap<String, Set<String>>();
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			result =getIndexInfoBySingleConnection(table, connection);

			
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	public Map<String, Set<String>> getIndexInfoBySingleConnection(final String table, final Connection connection) throws SQLException {
		final Map<String, Set<String>> result = new HashMap<String, Set<String>>();
		final Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		final DatabaseMetaData meta = connection.getMetaData();

		final ResultSet resultSet = meta.getIndexInfo(null, null, table, true, true);

		while (resultSet.next()) {
			final String indexName = resultSet.getString("INDEX_NAME");
			final String columnName = resultSet.getString("COLUMN_NAME");
			final int ordinalPosistion = NumberUtils.toInt(resultSet.getString("ORDINAL_POSITION"));
			// System.out.println("indexName: " + indexName + " ,  columnName: "
			// + columnName);
			
			if(ordinalPosistion>0){
				//為0者不是當初送出去的table schema
				Set<String> compositeColumns = result.get(indexName);
				if (compositeColumns == null) {
					compositeColumns = new HashSet<String>();
				}
				compositeColumns.add(columnName);
				result.put(indexName, compositeColumns);
			}
		}

		resultSet.close();
		stmt.clearBatch();
		stmt.close();

		return result;
	}

	public Map<String, Integer> getColumnMetaInfos(final String table) throws SQLException {
		Map<String, Integer> result = new HashMap<String, Integer>();
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = stmt.executeQuery("SELECT * FROM " + table);
			ResultSetMetaData md = rs.getMetaData();
			int col = md.getColumnCount();

			for (int i = 1; i <= col; i++) {
				String colName = md.getColumnName(i);
				// String colType = md.getColumnTypeName(i);
				int colTypeCode = md.getColumnType(i);
				result.put(colName, Integer.valueOf(colTypeCode));
			}
			rs.close();
			stmt.clearBatch();
			stmt.close();
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	public void batchInsertData(final String table, String[] columnNames, List<Object[]> valueData) throws SQLException {
		Connection connection = null;
		Object[] valueDatas = null;

		try {
			connection = dataSource.getConnection();
			connection.setAutoCommit(false);
			StringBuffer sbf01 = new StringBuffer();
			StringBuffer sbf02 = new StringBuffer();
			for (String columnName : columnNames) {
				sbf01.append(columnName).append(',');
				sbf02.append("?,");
			}
			if (columnNames.length > 1) {
				sbf01.delete(sbf01.lastIndexOf(","), sbf01.length());
				sbf02.delete(sbf02.lastIndexOf(","), sbf02.length());
			}

			PreparedStatement pStmt = connection.prepareStatement("INSERT INTO " + table + " (" + sbf01.toString() + ") VALUES ( " + sbf02.toString() + " )");

			for (int j = 0; j < valueData.size(); ++j) {
				valueDatas = valueData.get(j);
				if (columnNames.length != valueDatas.length) {
					StringBuffer sbf = new StringBuffer();
					for (int i = 0; i < valueDatas.length; i++) {
						Object data = valueDatas[i];
						sbf.append(data != null ? data.toString() : "").append(',');
					}
					throw new SQLException("指定輸入欄位數目不一致..." + sbf.toString());
				}

				for (int i = 0; i < valueDatas.length; i++) {
					Object data = valueDatas[i];
					pStmt.setObject(i + 1, data);
				}
				pStmt.addBatch();

				int aa = j % 1700;
				if ((aa == 0)) {
					pStmt.executeBatch();
					pStmt.clearBatch();
				}

			}
			pStmt.executeBatch();
			pStmt.clearBatch();

			pStmt.close();
			connection.commit();
		} catch (SQLException e) {
			log.info("insert failed!!! data as below");
			for (int i = 0; i < valueDatas.length; i++) {
				Object data = valueDatas[i];
				log.info(data);
			}
			if (connection != null) {
				try {
					connection.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
			throw new SQLException(e);
		} finally {
			if (connection != null) {
				try {
					connection.close();

				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void insertData(final String table, String[] columnNames, List<Object[]> valueData) throws SQLException {
		Connection connection = null;
		Object[] valueDatas = null;

		try {
			connection = dataSource.getConnection();
			connection.setAutoCommit(false);
			StringBuffer sbf01 = new StringBuffer();
			StringBuffer sbf02 = new StringBuffer();
			for (String columnName : columnNames) {
				sbf01.append(columnName).append(',');
				sbf02.append("?,");
			}
			if (columnNames.length > 1) {
				sbf01.delete(sbf01.lastIndexOf(","), sbf01.length());
				sbf02.delete(sbf02.lastIndexOf(","), sbf02.length());
			}

			PreparedStatement pStmt = connection.prepareStatement("INSERT INTO " + table + " (" + sbf01.toString() + ") VALUES ( " + sbf02.toString() + " )");

			for (int j = 0; j < valueData.size(); ++j) {
				valueDatas = valueData.get(j);
				if (columnNames.length != valueDatas.length) {
					StringBuffer sbf = new StringBuffer();
					for (int i = 0; i < valueDatas.length; i++) {
						Object data = valueDatas[i];
						sbf.append(data != null ? data.toString() : "").append(',');
					}
					throw new SQLException("指定輸入欄位數目不一致..." + sbf.toString());
				}
				for (int i = 0; i < valueDatas.length; i++) {
					Object data = valueDatas[i];
					pStmt.setObject(i + 1, data);
				}
				pStmt.executeUpdate();
			}

			pStmt.clearBatch();
			pStmt.close();
			connection.commit();
		} catch (SQLException e) {
			log.info("insert failed!!! data as below");
			for (int i = 0; i < valueDatas.length; i++) {
				Object data = valueDatas[i];
				log.info(data);
			}
			if (connection != null) {
				try {
					connection.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
			throw new SQLException(e);
		} finally {
			if (connection != null) {
				try {
					connection.close();

				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void insertData(final String table, String[] columnNames, Object[] valueDatas) throws SQLException {
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			connection.setAutoCommit(false);
			StringBuffer sbf01 = new StringBuffer();
			StringBuffer sbf02 = new StringBuffer();
			for (String columnName : columnNames) {
				sbf01.append(columnName).append(',');
				sbf02.append("?,");
			}
			if (columnNames.length > 1) {
				sbf01.delete(sbf01.lastIndexOf(","), sbf01.length());
				sbf02.delete(sbf02.lastIndexOf(","), sbf02.length());
			}

			PreparedStatement pStmt = connection.prepareStatement("INSERT INTO " + table + " (" + sbf01.toString() + ") VALUES ( " + sbf02.toString() + " )");

			for (int i = 0; i < valueDatas.length; i++) {
				Object valueData = valueDatas[i];
				pStmt.setObject(i + 1, valueData);
			}

			pStmt.executeUpdate();
			pStmt.clearBatch();
			pStmt.close();
			connection.commit();
		} catch (SQLException e) {
			log.info("insert failed!!! data as below");
			for (Object valueData : valueDatas) {
				log.info(valueData);
			}
			if (connection != null) {
				try {
					connection.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
			throw new SQLException(e);
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public List<String> getColumnNameList(String table) throws SQLException {
		List<String> result = new ArrayList<String>();
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = stmt.executeQuery("SELECT * FROM " + table);
			ResultSetMetaData md = rs.getMetaData();
			int col = md.getColumnCount();

			for (int i = 1; i <= col; i++) {
				String colName = md.getColumnName(i);
				result.add(colName);
			}
			rs.close();
			stmt.clearBatch();
			stmt.close();
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	public List<JDBCTablseSchema> getJDBCTablseSchemasBySingleConnection2(List<String> tablelist, DataSource ds) throws SQLException {
		Connection connection = null;
		List<JDBCTablseSchema> result = new ArrayList<JDBCTablseSchema>();
		List<String> exceptionList = new ArrayList<String>();
		try {
			connection = ds.getConnection();
			for (String tableName : tablelist) {
				try {
					JDBCTablseSchema unit = getJDBCTablseSchemaBySingleConnection(tableName, connection);
					result.add(unit);
				} catch (java.sql.SQLException e) {
					exceptionList.add(e.getMessage());
				}
			}

		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		System.out.println("exceptionList: " + exceptionList.size());
		return result;
	}

	public JDBCTableMiningResult getJDBCTablseSchemasBySingleConnection(List<ScriptTableInfo> tablelist, DataSource ds) throws SQLException {
		JDBCTableMiningResult aJDBCTableNiningResult = new JDBCTableMiningResult();
		Connection connection = null;
		List<JDBCTablseSchema> result = new ArrayList<JDBCTablseSchema>();
		List<String> exceptionList = new ArrayList<String>();
		try {
			connection = ds.getConnection();
			for (ScriptTableInfo data : tablelist) {
				String tableName = data.getTableName();
				try {
					JDBCTablseSchema unit = getJDBCTablseSchemaBySingleConnection(tableName, connection);
					result.add(unit);
				} catch (java.sql.SQLException e) {
					exceptionList.add(e.getMessage());
				}
			}

		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		aJDBCTableNiningResult.setResult(result);
		aJDBCTableNiningResult.setInitInputData(tablelist);
		aJDBCTableNiningResult.setExceptionList(exceptionList);
		return aJDBCTableNiningResult;
	}

	private JDBCTablseSchema getJDBCTablseSchemaBySingleConnection(String table, Connection connection) throws SQLException {
		final Map<String, Set<String>> indexInfo = getIndexInfoBySingleConnection(table, connection);

		final JDBCTablseSchema result = new JDBCTablseSchema();

		final List<JDBCColumnSachema> columnList = result.getDataColumnInfos();

		final Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		final ResultSet rs = stmt.executeQuery("SELECT * FROM " + table);
		final ResultSetMetaData md = rs.getMetaData();

		int col = md.getColumnCount();

		for (int i = 1; i <= col; i++) {
			String tableName = md.getTableName(i);
			result.setTableName(tableName.toUpperCase());
			// System.out.print("tableName: " + tableName);
			String colName = md.getColumnName(i);
			// System.out.print(", columnName: " + colName);
			String columnTypeName = md.getColumnTypeName(i);
			// System.out.print(", columnTypeName: " + columnTypeName);

			int columnDisplaySize = md.getColumnDisplaySize(i);
			// System.out.print(", columnDisplaySize: " + columnDisplaySize);
			int nullable = md.isNullable(i);
			// System.out.print(", nullable: " + nullable);
			boolean autoIncrement = md.isAutoIncrement(i);
			// System.out.println(", autoIncrement: " + autoIncrement);
			JDBCColumnSachema unit = new JDBCColumnSachema(tableName, colName.toUpperCase(), columnTypeName.toUpperCase(), columnDisplaySize, nullable,
					autoIncrement);

			for (String key : indexInfo.keySet()) {
				if (StringUtils.containsIgnoreCase(key, "_p_key")) {
					final Set<String> keycolumns = indexInfo.get(key);
					for (String keycolumn : keycolumns) {
						if (StringUtils.equalsIgnoreCase(unit.getColumnName(), keycolumn)) {
							unit.setPrimaryKey(true);
						}
					}
				}
			}
			columnList.add(unit);
		}
		rs.close();
		stmt.clearBatch();
		stmt.close();

		result.setIndexInfo(indexInfo);
		return result;
	}
}
