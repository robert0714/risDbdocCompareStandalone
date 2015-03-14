package org.robert.study.discrepancy.model;

import java.util.Map;
import java.util.Set;

public interface DiscrepancyTableIncludeIndexInfoInterface extends DiscrepancyTableInterface{
	
    public Map<String, Set<String>> getIndexInfo();
}
