/*
 * Copyright (c) 2010-2020 IISI.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of IISI.
 */
package tw.gov.moi.rl.component.dto;

import java.io.Serializable;

/**
 * 戶籍登記作業物件
 * 
 * @author DAXIONG
 */
public class HouseholdOperation implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 作業代碼 */
    private String operationCode;

    /** 作業類別 */
    private String operationCategory;

    /** 作業名稱 */
    private String operationName;

    /** 作業頁面名稱 */
    private String operationPageName;

    /** 作業階段 */
    private String operationPhase;

    /** 是否可異地辦理 */
    private boolean crossSiteEnabled;

    /** L2 戶籍大簿頁是否顯示 */
    private boolean householdPageEnabled;

    /** 申請人個數 */
    private int applicantNumber;

    /** 當事人是否必填 */
    private boolean currentPersonRequired;

    private boolean firstOperation;

    public boolean isFirstOperation() {
        return firstOperation;
    }

    public void setFirstOperation(boolean firstOperation) {
        this.firstOperation = firstOperation;
    }

    public String getOperationCode() {
        return operationCode;
    }

    public void setOperationCode(String operationCode) {
        this.operationCode = operationCode;
    }

    public String getOperationCategory() {
        return operationCategory;
    }

    public void setOperationCategory(String operationCategory) {
        this.operationCategory = operationCategory;
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public String getOperationPageName() {
        return operationPageName;
    }

    public void setOperationPageName(String operationPageName) {
        this.operationPageName = operationPageName;
    }

    public boolean isCrossSiteEnabled() {
        return crossSiteEnabled;
    }

    public void setCrossSiteEnabled(boolean crossSiteEnabled) {
        this.crossSiteEnabled = crossSiteEnabled;
    }

    public boolean isHouseholdPageEnabled() {
        return householdPageEnabled;
    }

    public void setHouseholdPageEnabled(boolean householdPageEnabled) {
        this.householdPageEnabled = householdPageEnabled;
    }

    public int getApplicantNumber() {
        return applicantNumber;
    }

    public void setApplicantNumber(int applicantNumber) {
        this.applicantNumber = applicantNumber;
    }

    public boolean isCurrentPersonRequired() {
        return currentPersonRequired;
    }

    public void setCurrentPersonRequired(boolean currentPersonRequired) {
        this.currentPersonRequired = currentPersonRequired;
    }

    /**
     * @return the operationPhase
     */
    public String getOperationPhase() {
        return operationPhase;
    }

    /**
     * @param operationPhase
     *            the operationPhase to set
     */
    public void setOperationPhase(String operationPhase) {
        this.operationPhase = operationPhase;
    }
}