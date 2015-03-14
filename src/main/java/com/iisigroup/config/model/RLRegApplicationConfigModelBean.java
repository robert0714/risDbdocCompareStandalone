package com.iisigroup.config.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class RLRegApplicationConfigModelBean    implements Serializable  {

    /**
     * 
     */
    private static final long serialVersionUID = -5915014920932696074L;
    /**
     * example:無戶籍者結離婚登記
     * */
    private String chineseName;
    /**
     * example:0W100
     * */
    private String symbolCode;
    private boolean tableTypeR;
    private boolean tableTypeS;
    private boolean tableTypeT;
    private boolean tableTypeW;
    /**
     * 0Z4(現無戶籍者結婚登記申請書)/ref file location <br/>
     * 0Z5(現無戶籍者離婚登記申請書)/ref file location
     * **/
    private Map<String, String> tableTypeRefLocationMap;

    public String getChineseName() {
        return chineseName;
    }

    public void setChineseName(String chineseName) {
        this.chineseName = chineseName;
    }

    public String getSymbolCode() {
        return symbolCode;
    }

    public void setSymbolCode(String symbolCode) {
        this.symbolCode = symbolCode;
    }

    public boolean isTableTypeR() {
        return tableTypeR;
    }

    public void setTableTypeR(boolean tableTypeR) {
        this.tableTypeR = tableTypeR;
    }

    public boolean isTableTypeS() {
        return tableTypeS;
    }

    public void setTableTypeS(boolean tableTypeS) {
        this.tableTypeS = tableTypeS;
    }

    public boolean isTableTypeT() {
        return tableTypeT;
    }

    public void setTableTypeT(boolean tableTypeT) {
        this.tableTypeT = tableTypeT;
    }

    public boolean isTableTypeW() {
        return tableTypeW;
    }

    public void setTableTypeW(boolean tableTypeW) {
        this.tableTypeW = tableTypeW;
    }

    public Map<String, String> getTableTypeRefLocationMap() {
        if (tableTypeRefLocationMap == null) {
            tableTypeRefLocationMap = new HashMap<String, String>();
        }
        return tableTypeRefLocationMap;
    }

    public void setTableTypeRefLocationMap(Map<String, String> tableTypeRefLocationMap) {
        this.tableTypeRefLocationMap = tableTypeRefLocationMap;
    }

}
