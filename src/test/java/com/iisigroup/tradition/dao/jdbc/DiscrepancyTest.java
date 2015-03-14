package com.iisigroup.tradition.dao.jdbc;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;

public class DiscrepancyTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int columnDisplaySize = 100000;
		String columnTypeName ="testColumn";
//		String result =StringUtils.upperCase(columnTypeName +"("+String.format("%1$,02d", columnDisplaySize)+")");
		String result =StringUtils.upperCase(columnTypeName +"("+String.format("%02d", columnDisplaySize)+")");
		System.out.println("result: "+result);
	}
	public List<String> getColumnNameList(String table, DataSource ds)
			throws SQLException {
		List<String> result = new ArrayList<String>();
		Connection connection = null;
		try {
			connection = ds.getConnection();
			Statement stmt = connection.createStatement(
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = stmt.executeQuery("SELECT * FROM " + table);
			ResultSetMetaData md = rs.getMetaData();

			int col = md.getColumnCount();

			for (int i = 1; i <= col; i++) {
				String tableName = md.getTableName(i);
				System.out.print("tableName: " + tableName);
				String colName = md.getColumnName(i);
				System.out.print(", columnName: " + colName);
				String columnTypeName = md.getColumnTypeName(i);
				System.out.print(", columnTypeName: " + columnTypeName);

				int columnDisplaySize = md.getColumnDisplaySize(i);
				System.out.print(", columnDisplaySize: " + columnDisplaySize);
				int nullable = md.isNullable(i);
				System.out.print(", nullable: " + nullable);
				boolean autoIncrement = md.isAutoIncrement(i);
				System.out.println(", autoIncrement: " + autoIncrement);

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

	public List<String> getAllTable(DataSource ds) throws SQLException {
		List<String> result = new ArrayList<String>();
		Connection connection = null;
		try {
			connection = ds.getConnection();

			DatabaseMetaData dbmd = connection.getMetaData();

			String[] types = { "TABLE" };
			ResultSet resultSet = dbmd.getTables(null, null, "%", types);

			while (resultSet.next()) {
				String tableName = resultSet.getString(3);

				String tableCatalog = resultSet.getString(1);
				String tableSchema = resultSet.getString(2);
				System.out.println("tableName: "+tableName);
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
	private static List<String>  processAsSentence(final String text){
		String testString =StringUtils.replace(text, ";", ".");
		BreakIterator sentIterator = BreakIterator.getSentenceInstance(Locale.US);
		sentIterator.setText(testString);
		int start = sentIterator.first();
		int end = -1;
		List<String> sentences = new ArrayList<String>();
		while ((end = sentIterator.next()) != BreakIterator.DONE) {
			String sentence = testString.substring(start, end);
			start = end;
			sentences.add(sentence);
		}
		return sentences;
	}

}
