package com.iisi.sd.main.gui;

import java.util.List;

import org.robert.study.discrepancy.model.DiscrepancyTableInterface;


public interface DataSourceInterface<T extends DiscrepancyTableInterface> {
    public List<T> getDataList();
}
