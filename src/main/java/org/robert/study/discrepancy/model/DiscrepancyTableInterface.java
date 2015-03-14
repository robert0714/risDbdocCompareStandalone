package org.robert.study.discrepancy.model;

import java.util.List;

public interface DiscrepancyTableInterface {
    public String getTableName();

    public <T extends DiscrepancyColumnInterface> List<T> getDataColumnInfos();
}
