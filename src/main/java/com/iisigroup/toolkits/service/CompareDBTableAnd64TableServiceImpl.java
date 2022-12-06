package com.iisigroup.toolkits.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.robert.study.discrepancy.model.DiscrepancyColumnInterface;
import org.robert.study.discrepancy.model.DiscrepancyTableInterface;
import org.robert.study.service.CompareDBTableAnd64TableService;

import com.iisi.rl.table.DataColumnInfo;
import com.iisi.rl.table.discrepancy.DiscrepancyReport;
import com.iisi.rl.table.discrepancy.DiscrepancyTable;

public class CompareDBTableAnd64TableServiceImpl implements CompareDBTableAnd64TableService {
    protected static Logger log = LoggerFactory.getLogger(CompareDBTableAnd64TableServiceImpl.class);

    @Override
    public <T extends DiscrepancyTableInterface> Map<String, T> getCommonDataMap(final List<T> scriptList) {
        ConcurrentMap<String, T> result = new ConcurrentHashMap<String, T>();
        for (T table : scriptList) {
            try {
                result.put(table.getTableName(), (T) table);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public <T extends DiscrepancyTableInterface, Z extends DiscrepancyTableInterface> DiscrepancyReport comparationStandardForScript64Table(
            final Map<String, T> sQLScripData, final Map<String, Z> wordDocData) {
        DiscrepancyReport report = new DiscrepancyReport();

        // table discrepancy
        Collection<String>[] initData = compareNames(sQLScripData.keySet(), wordDocData.keySet());
        Set<String> sameTables = (Set<String>) initData[0];
        List<String> sQLScripLackingTableNameList = (List<String>) initData[1];
        List<String> wordDocLackingTableNameList = (List<String>) initData[2];

        Map<String, List<String>> aDiscrepencyLackingTableName = report.getDiscrepencyLackingTableName();
        aDiscrepencyLackingTableName.put("sQLScripLackingTableNameList", sQLScripLackingTableNameList);
        aDiscrepencyLackingTableName.put("wordDocLackingTableNameList", wordDocLackingTableNameList);
        log.info("same Tables: " + sameTables.size());
        log.info("sQLScripLackingTableNameList: " + sQLScripLackingTableNameList.size());
        // displayData(sQLScripLackingTableNameList);
        log.info("wordDocLackingTableNameList: " + wordDocLackingTableNameList.size());
        // displayData(wordDocLackingTableNameList);

        // table column discrepancy
        report.getSameTableName().addAll(sameTables);
        List<String> sameTableName = report.getSameTableName();
        Collections.sort(sameTableName);
        report.setSameTableName(sameTableName);
        List<DiscrepancyTable> discrepencyTables = columnDiscrepancy(sameTableName, sQLScripData, wordDocData);

        report.setDiscrepencyTables(discrepencyTables);
        return report;
    }

    private <T extends DiscrepancyTableInterface, Z extends DiscrepancyTableInterface> List<DiscrepancyTable> columnDiscrepancy(
            final List<String> commonTables, final Map<String, T> sQLScripData, final Map<String, Z> wordDocData) {
        List<DiscrepancyTable> result = new ArrayList<DiscrepancyTable>();
        for (String tableName : commonTables) {
            DiscrepancyTableInterface scriptTableInfo = sQLScripData.get(tableName);
            DiscrepancyTableInterface dataTable = wordDocData.get(tableName);

            //比較相同名稱的table中的column名稱 分別挑出一樣名稱的column，以及不一樣名稱的columns
            DiscrepancyTable aDiscrepancyTable = comparaUnitColumns(scriptTableInfo, dataTable);
            
            //比較相同名稱的table中的column名稱,相同column的Attributes
            aDiscrepancyTable = compareUnitColumnAttributes(aDiscrepancyTable, scriptTableInfo, dataTable);

            // 只有columns有名字上的差異或是column格式上的 差異才能夠加入
            aDiscrepancyTable.setTableName(tableName);

            if (aDiscrepancyTable.isDiscrepancy()) {
                result.add(aDiscrepancyTable);
            }

        }
        return result;
    }

    private <T extends DiscrepancyColumnInterface> Map<String, T> getColumnsMap(
            DiscrepancyTableInterface aDiscrepancyTableInterface) {
        List<T> aList = aDiscrepancyTableInterface.getDataColumnInfos();
        Map<String, T> result = new HashMap<String, T>();

        for (T column : aList) {

            result.put(column.getColumnName(), column);
        }
        return result;
    }

    /*****
     * 比較相同名稱的table中的column名稱,相同column的Attributes
     * ***/
    private <T extends DiscrepancyColumnInterface> DiscrepancyTable compareUnitColumnAttributes(
            final DiscrepancyTable aDiscrepancyTable, final DiscrepancyTableInterface scriptTableInfo,
            final DiscrepancyTableInterface dataTable) {

        Set<String> sameColumns = aDiscrepancyTable.getSameColumns();
        Map<String, T> scriptTableMap = getColumnsMap(scriptTableInfo);
        Map<String, T> dataTableMap = getColumnsMap(dataTable);

        Map<String, String[]> sameColumnFormatDiscrepancy = aDiscrepancyTable.getSameColumnFormatDiscrepancy();

        for (String columnName : sameColumns) {
            String scriptColumnFormat = scriptTableMap.get(columnName).getFormat();
            String dataColumnInfoFormat = dataTableMap.get(columnName).getFormat();
            if (!scriptColumnFormat.equalsIgnoreCase(dataColumnInfoFormat)) {
                // TO　DO...........In future ,we need to modify them dynamically
                // by the source data
                sameColumnFormatDiscrepancy.put(columnName, new String[] { scriptColumnFormat, dataColumnInfoFormat });
            }
        }

        return aDiscrepancyTable;
    }

    /*****
     * 比較相同名稱的table中的column名稱 分別挑出一樣名稱的column，以及不一樣名稱的columns
     * ***/
    private DiscrepancyTable comparaUnitColumns(final DiscrepancyTableInterface scriptTableInfo,
            final DiscrepancyTableInterface dataTable) {
        DiscrepancyTable result = new DiscrepancyTable();
        List<? extends DiscrepancyColumnInterface> scriptTableInfoColumnList = scriptTableInfo.getDataColumnInfos();
        List<DataColumnInfo> dataTableColumnInfos = dataTable.getDataColumnInfos();

        // Column Name Comparative
        Set<String> sQLScripDataColumn = extractDiscrepancyColumnInterface(scriptTableInfoColumnList);
        Set<String> wordDocDataColumn = extractDiscrepancyColumnInterface(dataTableColumnInfos);

        Collection<String>[] initData = compareNames(sQLScripDataColumn, wordDocDataColumn);
        Set<String> sameColumns = (Set<String>) initData[0];
        List<String> sQLScripLackingColumnNameList = (List<String>) initData[1];
        List<String> wordDocLackingColumnNameList = (List<String>) initData[2];
        result.getLackingColumnList().put("sQLScripLackingColumnNameList", sQLScripLackingColumnNameList);
        result.getLackingColumnList().put("wordDocLackingColumnNameList", wordDocLackingColumnNameList);
        result.setSameColumns(sameColumns);
        if (sQLScripLackingColumnNameList.size() > 0 || wordDocLackingColumnNameList.size() > 0) {
            result.setDiscrepancy(true);
        }
        return result;
    }

    private Set<String> extractDiscrepancyColumnInterface(final List<? extends DiscrepancyColumnInterface> srcList) {
        Set<String> result = new HashSet<String>();
        for (DiscrepancyColumnInterface scriptColumn : srcList) {
            String columnName = scriptColumn.getColumnName();
            if (columnName != null) {
                result.add(columnName.trim());
            }
        }
        return result;
    }

    private Collection<String>[] compareNames(final Set<String> aSet, Set<String> bSet) {
        Set<String> sameData = new HashSet<String>();
        // table discrepancy
        List<String> bLackingList = new ArrayList<String>();
        List<String> aLackingList = new ArrayList<String>();
        for (String aName : aSet) {
            String name = aName.trim().replace(String.valueOf(new char[] { (char) 160 }), "");
            if (!bSet.contains(name)) {
                bLackingList.add(name);
            } else {
                sameData.add(name);
            }
        }
        for (String bName : bSet) {
            String name = bName.trim().replace(String.valueOf(new char[] { (char) 160 }), "");
            ;
            if (!aSet.contains(name)) {
                aLackingList.add(name);

            } else {
                sameData.add(name);
            }
        }
        sameData.removeAll(bLackingList);
        sameData.removeAll(aLackingList);
        Collections.sort(aLackingList);
        Collections.sort(bLackingList);
        return new Collection[] { sameData, aLackingList, bLackingList };
    }

}
