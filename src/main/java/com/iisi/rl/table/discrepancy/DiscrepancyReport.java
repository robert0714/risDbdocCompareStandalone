package com.iisi.rl.table.discrepancy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DiscrepancyReport implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 6744666622685519520L;
    private List<DiscrepancyTable> discrepencyTables = new ArrayList<DiscrepancyTable>();
    public Map<String, Integer> discrepencyTableNums = new HashMap<String, Integer>();
    public Map<String, List<String>> discrepencyLackingTableName = new HashMap<String, List<String>>();
    public List<String> sameTableName = new ArrayList<String>();

    public List<DiscrepancyTable> getDiscrepencyTables() {
        return discrepencyTables;
    }

    public void setDiscrepencyTables(List<DiscrepancyTable> discrepencyTables) {
        this.discrepencyTables = discrepencyTables;
    }

    public Map<String, Integer> getDiscrepencyTableNums() {
        return discrepencyTableNums;
    }

    public void setDiscrepencyTableNums(Map<String, Integer> discrepencyTableNums) {
        this.discrepencyTableNums = discrepencyTableNums;
    }

    public Map<String, List<String>> getDiscrepencyLackingTableName() {
        return discrepencyLackingTableName;
    }

    public void setDiscrepencyLackingTableName(Map<String, List<String>> discrepencyLackingTableName) {
        this.discrepencyLackingTableName = discrepencyLackingTableName;
    }

    public List<String> getSameTableName() {
        return sameTableName;
    }

    public void setSameTableName(List<String> sameTableName) {
        this.sameTableName = sameTableName;
    }

}
