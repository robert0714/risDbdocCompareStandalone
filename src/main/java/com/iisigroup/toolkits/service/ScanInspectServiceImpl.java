package com.iisigroup.toolkits.service;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.robert.study.service.ScanInspectService;
import org.robert.study.utils.Utils;

import com.iisi.rl.table.script.ScriptColumnInfo;
import com.iisi.rl.table.script.ScriptTableInfo;

/**
 * @author robert.lee
 * */
public class ScanInspectServiceImpl implements ScanInspectService {
    protected static Logger log = LoggerFactory.getLogger(ScanInspectServiceImpl.class);

    /**
     * @param args
     */
    public static void main(String[] args) {
        List<String> data = Utils.inputReadFile(new File("D:/userDatas/robert/Desktop/create script/RLDE052M.sql"));
        ScanInspectService main = new ScanInspectServiceImpl();
        ScriptTableInfo table = main.extractTable(data);
        log.info(table.getStatement());
    }

    private final static String SCRIPT_STATEMENT_ANY_WAY = ".*IN.*EXTENT.*SIZE.*NEXT.*SIZE.*";
    private final static String DROP_STATEMENT = ".*DROP.*TABLE.*";
    private final static String CREATE_STATEMENT = ".*CREATE.*TABLE.*%1$1s.*";
    private final static String CREATE_UNIQUE_INDEX = ".*CREATE.*UNIQUE.*INDEX.*%1$1S_P_KEY.*ON.*%1$1S.*(.*).*;";
    private final static String CREATE_UNIQUE_INDEX_2 = ".*CREATE.*UNIQUE.*INDEX.*%1$1S_P_KEY.*ON.*";   
    private final static String CREATE_INDEX_3 = ".*;";
    private final static String CREATE_COMMON_INDEX = ".*CREATE.*INDEX.*%1$1S_S.*_KEY.*ON.*";
    private final static String CREATE_COMMON_INDEX_v2 = ".*CREATE.*INDEX.*ON.*";

    private static String[] extractPrimaryKeys(final String originalString) {
        int start = originalString.indexOf('(');
        int end = originalString.indexOf(')');
        String initdata = originalString.substring(start + 1, end);
        String[] result = StringUtils.split(initdata  ,',');        
        return result;
    }

    private static String[] extractIndexKey(final String originalString) {
        int start = originalString.indexOf('(');
        int end = originalString.indexOf(')');
        final String initdata = originalString.substring(start + 1, end).trim();
        final String[] result =  StringUtils.split(initdata  ,',');   

        return result;
    }
    
    @Override
    public List<File> getQualifiedFile(final File folder) throws IOException {
        List<File> result = new ArrayList<File>();
        for (File aFile : folder.listFiles()) {
            String simpleFileName = aFile.getName();
            if (aFile.isFile() && !simpleFileName.contains("~") && aFile.getName().contains(".sql")) {
                result.add(aFile);
            }
        }
        return result;
    }

    @Override
    public ScriptTableInfo convertTOUnit(final File aFile) throws RuntimeException {
        List<String> data = Utils.inputReadFile(aFile);
        ScriptTableInfo table = null;
        String message = "exception: " + aFile.getName();
        try {
            table = extractTable(data);
        } catch (Exception e) {
            log.info(message);
            e.printStackTrace();
        } finally {
            try {
                if (table != null){
                	 table.getTableName();
                }
                   
            } catch (Exception e) {
                e.printStackTrace();
                log.info(message);
            }
        }
//        if (table == null) {
//            log.info(message);
//            throw new RuntimeException(message);
//        }
        return table;
    }

    @Override
    public List<ScriptTableInfo> covertFromFolder(final File direction) {
        List<ScriptTableInfo> correctList = new ArrayList<ScriptTableInfo>();
        List<ScriptTableInfo> javaBeanList = new ArrayList<ScriptTableInfo>();
        for (File aFile : direction.listFiles()) {
            if (aFile.isFile() && aFile.getName().contains(".sql")) {
                List<String> data = Utils.inputReadFile(aFile);
                ScriptTableInfo table = null;
                try {
                    table = extractTable(data);
                } catch (Exception e) {
                    log.info("exception: " + aFile.getName());
                    e.printStackTrace();
                }
                if (table == null) {
                    log.info("exception: " + aFile.getName());
                } else {
                    javaBeanList.add(table);
                }

            }
        }
        log.info("script no: " + javaBeanList.size());

        for (ScriptTableInfo aScriptTableInfo : javaBeanList) {
            // log.info("name: " + aScriptTableInfo.getTableName());
            try {
                // createSQL(aScriptTableInfo, new File("z:/tmpScript/" +
                // aScriptTableInfo.getTableName()+ ".sql"));
                aScriptTableInfo.getTableName();
                correctList.add(aScriptTableInfo);
            } catch (Exception e) {
                e.printStackTrace();
                log.info("exception: " + aScriptTableInfo.getTableName());
            }
        }
        log.info("formmer: " + javaBeanList.size());
        log.info("latter: " + correctList.size());
        return correctList;
    }

    public ScriptTableInfo extractTable(final List<String> srcList) {
        String tableName = getTableName(srcList);
        if (tableName == null) {
            return null;
        }
        ScriptTableInfo result = new ScriptTableInfo();
        result.setTableName(tableName);
        List<ScriptColumnInfo> columns=null;
		try {
			columns = getColumns(srcList, tableName);
		} catch (Exception e) {
			System.out.println("table: "+tableName);
			e.printStackTrace();
		}
		final Map<String,String[]> indexMap =getIndexInfo(srcList);
		
		for(String key : indexMap.keySet()){
			if(StringUtils.containsIgnoreCase(key, "_p_key")){
				 final String[] keycolumns = indexMap.get(key);
				for (ScriptColumnInfo column : columns){
					for(String keycolumn :keycolumns){
						if(StringUtils.equalsIgnoreCase(column.getColumnName(), keycolumn)){
							column.setPrimaryKey(true);
						}
					}
					
				}
			}
		}
		
        result.setDataColumnInfos(columns);
        return result;
    }

    private static Map<String,String[]> getIndexInfo(final List<String> srcList) {
    	 Map<String,String[]> result = new HashMap<String, String[]>();
        String initData =StringUtils.join(srcList, "");
        List<String> sentances = processAsSentence(initData);
        for (int i = 0; i < sentances.size(); ++i) {
            String tmp = sentances.get(i).toUpperCase();            
            if (Pattern.compile(CREATE_COMMON_INDEX_v2).matcher(tmp).matches()) {
            	String[] indexColumns = extractIndexKey(tmp);
            	String[] sentancesArray = StringUtils.split(sentances.get(i)," ");
            	if(sentancesArray.length >3 && StringUtils.equalsIgnoreCase(sentancesArray[2], "index")){
            		result.put(sentancesArray[3], indexColumns);
            	}else if(sentancesArray.length >3 && StringUtils.equalsIgnoreCase(sentancesArray[1], "index")){
            		result.put(sentancesArray[2], indexColumns);
            	}            	
            }
        }
        return result;
    }
    
    private static List<String>  processAsSentence(final String text){
		String[] testString =StringUtils.split(text, ";");
		List<String > sentences   = Arrays.asList(testString);
		return sentences;
	}
    private static String getTableName(final List<String> srcList) {
        String result = null;
        String initData = null;
        for (int i = 0; i < srcList.size(); ++i) {
            String tmp = srcList.get(i).toUpperCase();            
            
            if (Pattern.compile(DROP_STATEMENT).matcher(tmp).matches()) {
                initData = tmp;
                break;
            }
        }
        if (initData != null) {
            result = initData.replace("DROP", "").replace("TABLE", "").replace(";", "").trim();
        }
        return result;
    }

    private static List<ScriptColumnInfo> getColumns(final List<String> srcList, String tableName) {

        String judge01 = String.format(CREATE_STATEMENT, tableName.toUpperCase());
        String judge02 = SCRIPT_STATEMENT_ANY_WAY;
        String judge03 = String.format(CREATE_UNIQUE_INDEX, tableName.toUpperCase());
        String judge04 = String.format(CREATE_UNIQUE_INDEX_2, tableName.toUpperCase());
        String judge05 = CREATE_INDEX_3;
        String judge06 = String.format(CREATE_COMMON_INDEX, tableName.toUpperCase());
        int startCode = 0;
        int endCode = 0;
        String[] primaryKeyArray = null;
        Set<String> indexSet = new HashSet<String>();

        for (int i = 0; i < srcList.size(); ++i) {
            String tmp = srcList.get(i).trim().toUpperCase();
            if (Pattern.compile(judge01).matcher(tmp).matches()) {
                // log.info(tmp);
                startCode = i + 1;
            }
            if (Pattern.compile(judge02).matcher(tmp).matches()) {
                // log.info(tmp);
                endCode = i - 2;
                ;
            }
            if (Pattern.compile(judge03).matcher(tmp).matches()) {
                try {
					primaryKeyArray = extractPrimaryKeys(tmp);
				} catch (java.lang.StringIndexOutOfBoundsException e) {
					 String tmp2 = srcList.get(i+1).trim().toUpperCase();
					 String tmp3 = tmp+tmp2;
					 primaryKeyArray = extractPrimaryKeys(tmp3);
				}
            }
           
            if (Pattern.compile(judge06).matcher(tmp).matches()) {            	 
                try {
                	
					indexSet.addAll(Arrays.asList(extractIndexKey(tmp)));
				} catch (java.lang.StringIndexOutOfBoundsException e) {
					 String tmp2 = srcList.get(i+1).trim().toUpperCase();
					 String tmp3 = tmp+tmp2;
					 List<String> list = Arrays.asList(extractIndexKey(tmp3));
					 indexSet.addAll(list);
				}
            }
        }

        if (primaryKeyArray == null) {
            int startCalCode = 0;
            int endCalCode = 0;
            for (int i = 0; i < srcList.size(); ++i) {
                String tmp = srcList.get(i).trim().toUpperCase();
                if (Pattern.compile(judge04).matcher(tmp).matches()) {
                    startCalCode = i;
                }
                if (Pattern.compile(judge05).matcher(tmp).matches() && startCalCode != 0) {
                    endCalCode = i;
                    break;
                }
            }
            StringBuffer sbf = new StringBuffer();
            for (int i = startCalCode; i < (endCalCode + 1); ++i) {
                String tmp = srcList.get(i);
                sbf.append(tmp);
            }
            if (startCalCode != 0 && endCalCode != 0) {
                primaryKeyArray = extractPrimaryKeys(sbf.toString());
            }

        }
        final List<ScriptColumnInfo> columnList = new ArrayList<ScriptColumnInfo>();
        if (startCode != 0 && endCode != 0) {
            for (int i = startCode; i <= endCode; ++i) {
                final String line = srcList.get(i);
                if (StringUtils.isEmpty(line.trim()) || StringUtils.isBlank(line.trim())
                        || StringUtils.isWhitespace(line.trim())) {
                    continue;
                }

                StringTokenizer str = new StringTokenizer(line, "\t");
                List<String> statment = new ArrayList<String>();
                while (str.hasMoreElements()) {
                    StringTokenizer innerStr = new StringTokenizer(str.nextElement().toString().trim(), " ");
                    while (innerStr.hasMoreElements()) {
                        Object tt = innerStr.nextElement();
                        if (tt instanceof String) {
                            statment.add(tt.toString().trim());
                        }

                    }
                }
                if (statment.size() == 1) {
                    str = new StringTokenizer(line, " ");
                    statment = new ArrayList<String>();
                    while (str.hasMoreElements()) {
                        statment.add(str.nextElement().toString().trim());
                    }
                }
                ScriptColumnInfo column = new ScriptColumnInfo();
                if (statment.size() >= 3) {
                    String columnName = statment.get(0);
                    column.setColumnName(columnName.toUpperCase());
                    column.setFormat(statment.get(1).toUpperCase());

                    if (primaryKeyArray != null && Arrays.asList(primaryKeyArray).contains(columnName)) {
                        column.setPrimaryKey(true);
                    }
                    if (indexSet.contains(columnName)) {
                        column.setIndexKey(true);
                    }
                } else if (statment.size() == 2) {
                    String columnName = statment.get(0);
                    column.setColumnName(columnName.toUpperCase());
                    column.setFormat(statment.get(1).toUpperCase().replace("NOT NULL", "").trim());

                    if (primaryKeyArray != null && Arrays.asList(primaryKeyArray).contains(columnName)) {
                        column.setPrimaryKey(true);
                    }
                    if (indexSet.contains(columnName)) {
                        column.setIndexKey(true);
                    }
                }

                if (column.getColumnName() == null) {
                    log.info("column.getColumnName() == null tableName: " + tableName);
                    log.info("line: " + line);
                } else {
                    String nullableInfo = line.toUpperCase().replace(column.getColumnName(), "")
                            .replace(column.getFormat(), "").replace(",", "");
                    if (nullableInfo.contains("NULL") && nullableInfo.contains("NOT")) {
                        column.setNullable(false);
                    } else {
                        column.setNullable(true);
                    }
                    columnList.add(column);
                }

            }
        }
        return columnList;
    }

    @Override
    public void createSQL(final ScriptTableInfo table, final File destiFile) throws IOException {
        String sqlScript = table.getStatement();
        Utils.outputFile(destiFile, sqlScript);
    }

    private static Map<Integer, String> getIndexMap(final List<String> srcList) {
        Map<Integer, String> indexMap = new HashMap<Integer, String>();
        int i = 0;
        for (String tmp : srcList) {
            indexMap.put(Integer.valueOf(i), tmp);
            ++i;
        }
        return indexMap;
    }
}
