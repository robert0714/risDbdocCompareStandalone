package com.iisi.rl.table.script;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.robert.study.discrepancy.model.DiscrepancyTableInterface;


public class ScriptTableInfo implements Serializable, DiscrepancyTableInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 29129179654272739L;
    private String tableName;
    private final static String FIST_STATEMENT = "isql $1 <<! ";
    private final static String SCRIPT_STATEMENT_ANY_WAY = "in $2 extent size $3 next size $4";
    private final static String LOCK_MODE_ROW = "lock mode row;";
    private final static String DROP_STATEMENT = "DROP TABLE %1$1s;";
    private final static String CREATE_STATEMENT = "CREATE TABLE %1$1s( ";
    private final static String CREATE_UNIQUE_INDEX = "create unique index %1$1s_p_key on %1$1s( %2$2s);";
    private final static String CREATE_INDEX = "create index %3$3s on %1$1s(%2$2s);";
    private List<ScriptColumnInfo> dataColumnInfos;

    private String statement;

    public void setDataColumnInfos(List<ScriptColumnInfo> dataColumnInfos) {
        this.dataColumnInfos = dataColumnInfos;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public static String getScriptStatementAnyWay() {
        return SCRIPT_STATEMENT_ANY_WAY;
    }

    public static String getLockModeRow() {
        return LOCK_MODE_ROW;
    }

    public static String getDropStatement() {
        return DROP_STATEMENT;
    }

    public static String getCreateStatement() {
        return CREATE_STATEMENT;
    }

    public static String getCreateIndex() {
        return CREATE_UNIQUE_INDEX;
    }

    public String getStatement() {
        StringBuffer sbf = new StringBuffer();
        sbf.append(FIST_STATEMENT).append('\r').append('\n');
        sbf.append('\r').append('\n');
        sbf.append(String.format(DROP_STATEMENT, tableName)).append('\r').append('\n');
        sbf.append(String.format(CREATE_STATEMENT, tableName)).append('\r').append('\n');
        StringBuffer sbfPrimarKey = new StringBuffer();
        List<String> indexList = new ArrayList<String>();
        for (int i = 0; i < dataColumnInfos.size(); i++) {
            ScriptColumnInfo column = dataColumnInfos.get(i);
            if (column.getPrimaryKey()) {
                sbfPrimarKey.append(column.getColumnName()).append(',');
            }
            if (i == (dataColumnInfos.size() - 1)) {
                sbf.append(column.getStatement()).append('\r').append('\n');
            } else {
                sbf.append(column.getStatement()).append(',').append('\r').append('\n');
            }
            if (column.isIndexKey()) {
                indexList.add(column.getColumnName());
            }
        }
        sbf.append(')').append('\r').append('\n');
        sbf.append(SCRIPT_STATEMENT_ANY_WAY).append('\r').append('\n');
        sbf.append(LOCK_MODE_ROW).append('\r').append('\n');
        String primaryKeyStatement = sbfPrimarKey.toString();

        if (StringUtils.isNotBlank(primaryKeyStatement) && StringUtils.isNotEmpty(primaryKeyStatement)) {
            int position = primaryKeyStatement.lastIndexOf(",");
            primaryKeyStatement = primaryKeyStatement.substring(0, position);
            sbf.append(String.format(CREATE_UNIQUE_INDEX, tableName, primaryKeyStatement)).append('\r').append('\n');
        }
        for (int i = 0; i < indexList.size(); ++i) {
            String columnName = indexList.get(i);
            String order = (i == 0 ? "" : String.valueOf(i + 1));
            String keyName = tableName + "_S" + order + "_key";
            sbf.append(String.format(CREATE_INDEX, tableName, columnName, keyName)).append('\r').append('\n');
        }

        sbf.append('\r').append('\n').append('!');
        statement = sbf.toString();
        return statement;
    }

    public List<ScriptColumnInfo> getDataColumnInfos() {
        if (dataColumnInfos == null) {
            dataColumnInfos = new ArrayList<ScriptColumnInfo>();
        }
        return dataColumnInfos;
    }

}
