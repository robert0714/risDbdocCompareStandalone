package org.robert.study.service;

import java.util.List;
import java.util.Map;

import org.robert.study.discrepancy.model.DiscrepancyTableInterface;

import com.iisi.rl.table.discrepancy.DiscrepancyReport;

public interface CompareDBTableAnd64TableService {

    public <T extends DiscrepancyTableInterface, Z extends DiscrepancyTableInterface> DiscrepancyReport comparationStandardForScript64Table(
            final Map<String, T> sQLScripData, final Map<String, Z> wordDocData);

    public <T extends DiscrepancyTableInterface> Map<String, T> getCommonDataMap(final List<T> scriptList);

}
