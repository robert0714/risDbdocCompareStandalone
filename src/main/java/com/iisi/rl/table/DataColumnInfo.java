package com.iisi.rl.table;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;
import org.robert.study.discrepancy.model.DiscrepancyColumnInterface;

public final class DataColumnInfo implements Serializable, DiscrepancyColumnInterface {
    /**
     * 
     */
    private static final long serialVersionUID = -3804119513827198326L;
    private Boolean key;
    private String columnName;
    private String chineseName;
    private String format;
    private String defaultValue;
    private boolean nullable;
    private String description;
    private String[] valueArray = new String[7];

    public DataColumnInfo(String[] valueArray) {
        super();
        this.valueArray = valueArray;
    }

    public DataColumnInfo() {
        super();
    }
    public Boolean getPrimaryKey(){
    	if (StringUtils.isNotEmpty(valueArray[0])
                && ( valueArray[0].trim().contains("PK"))) {
            key = true;
        } else {
            key = false;
        }
        return key;
    }
    public Boolean isKey() {
        // System.out.println("valueArray[0]: " + valueArray[0].trim());
        if (StringUtils.isNotEmpty(valueArray[0])
                && ( valueArray[0].trim().contains("PK"))) {
            key = true;
        } else {
            key = false;
        }
        return key;
    }

    public String getColumnName() {
        if (valueArray[1] != null) {
            columnName = valueArray[1].trim();
            // System.out.println("length: " + columnName.length());
        } else {
            columnName = "";
        }
        return columnName;
    }

    public String getChineseName() {
        if (valueArray[2] != null) {
            chineseName = valueArray[2].trim();
            // System.out.println("length: " + chineseName.length());
        } else {
            chineseName = "";
        }
        return chineseName;
    }

    public String getFormat() {
        if (valueArray[3] != null) {
            format = valueArray[3].trim();
            // System.out.println("length: " + format.length());
        } else {
            format = "";
        }
        return format;
    }

    public String getDefaultValue() {
        if (valueArray[4] != null) {
            defaultValue = valueArray[4].trim();
            // System.out.println("length: " + defaultValue.length());
        } else {
            defaultValue = "";
        }
        return defaultValue;
    }

    public boolean isNullable() {
        if (valueArray[5] != null && "N".equalsIgnoreCase(valueArray[5])) {
            nullable = false;
        } else {
            nullable = true;
        }
        return nullable;
    }

    public String[] getValueArray() {
        return valueArray;
    }

    public String getDescription() {
        if (valueArray[6] != null) {
            description = valueArray[6].trim();
            // System.out.println("length: " + description.length());
        } else {
            description = "";
        }
        return description;
    }
}
