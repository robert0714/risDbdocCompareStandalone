package org.robert.study.utils;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlRootElement;
 
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "rldf088tType", propOrder = {
    "applyTransactionId",
    "applySequenceId",
    "personId",
    "siteId",
    "hhNowState",
    "registerYyymmdd",
    "hhResidentNo",
    "wwPersonName",
    "wwForeignAddress",
    "hhStreetDoorplate",
    "hhNationalityCode",
    "wwNeighbor",
    "workId",
    "mrgDevPlace",
    "wsId",
    "wsIpaddress",
    "hhCityCountyCode",
    "hhTownCode",
    "hhPassportId",
    "hhForeignAddress",
    "hhPersonId",
    "wwCityCountyCode",
    "executeYyymmdd",
    "wwNationality",
    "hhNeighbor",
    "hhForeignName",
    "wwResidentNo",
    "hhPersonName",
    "registerTime",
    "wwBirthDate",
    "hhVillage",
    "hhNeighborChar",
    "cancelMark",
    "acceptSiteId",
    "wsIpaddressL2",
    "applyCode",
    "wwPassportId",
    "wwStreetDoorplate",
    "wwForeignName",
    "wwVillage",
    "wwNeighborChar",
    "hhNationality",
    "hhBirthDate",
    "hhAreaCode",
    "wwPersonId",
    "wwTownCode",
    "wwOtherId",
    "wwNationalityCode",
    "hhOtherId",
    "wwNowState",
    "mrgDevPlaceCode",
    "wwAreaCode"})
@XmlRootElement(name = "Rldf088tType", namespace = "http://tw.gov.moi/domain")
public class Rldf088tType implements java.io.Serializable { 

    private static final long serialVersionUID = 1L;

    @XmlElement(name = "ApplyTransactionId",required = true)
    private String applyTransactionId = "";

    @XmlElement(name = "ApplySequenceId",required = true)
    private Integer applySequenceId = 0;

    @XmlElement(name = "PersonId",required = true)
    private String personId = "";

    @XmlElement(name = "SiteId",required = true)
    private String siteId = "";

    @XmlElement(name = "HhNowState",required = true)
    private String hhNowState = "";

    @XmlElement(name = "RegisterYyymmdd",required = true)
    private String registerYyymmdd = "";

    @XmlElement(name = "HhResidentNo",required = true)
    private String hhResidentNo = "";

    @XmlElement(name = "WwPersonName",required = true)
    private String wwPersonName = "";

    @XmlElement(name = "WwForeignAddress",required = true)
    private String wwForeignAddress = "";

    @XmlElement(name = "HhStreetDoorplate",required = true)
    private String hhStreetDoorplate = "";

    @XmlElement(name = "HhNationalityCode",required = true)
    private String hhNationalityCode = "";

    @XmlElement(name = "WwNeighbor",required = true)
    private String wwNeighbor = "";

    @XmlElement(name = "WorkId",required = true)
    private String workId = "";

    @XmlElement(name = "MrgDevPlace",required = true)
    private String mrgDevPlace = "";

    @XmlElement(name = "WsId",required = true)
    private String wsId = "";

    @XmlElement(name = "WsIpaddress",required = true)
    private String wsIpaddress = "";

    @XmlElement(name = "HhCityCountyCode",required = true)
    private String hhCityCountyCode = "";

    @XmlElement(name = "HhTownCode",required = true)
    private String hhTownCode = "";

    @XmlElement(name = "HhPassportId",required = true)
    private String hhPassportId = "";

    @XmlElement(name = "HhForeignAddress",required = true)
    private String hhForeignAddress = "";

    @XmlElement(name = "HhPersonId",required = true)
    private String hhPersonId = "";

    @XmlElement(name = "WwCityCountyCode",required = true)
    private String wwCityCountyCode = "";

    @XmlElement(name = "ExecuteYyymmdd",required = true)
    private String executeYyymmdd = "";

    @XmlElement(name = "WwNationality",required = true)
    private String wwNationality = "";

    @XmlElement(name = "HhNeighbor",required = true)
    private String hhNeighbor = "";

    @XmlElement(name = "HhForeignName",required = true)
    private String hhForeignName = "";

    @XmlElement(name = "WwResidentNo",required = true)
    private String wwResidentNo = "";

    @XmlElement(name = "HhPersonName",required = true)
    private String hhPersonName = "";

    @XmlElement(name = "RegisterTime",required = true)
    private String registerTime = "";

    @XmlElement(name = "WwBirthDate",required = true)
    private String wwBirthDate = "";

    @XmlElement(name = "HhVillage",required = true)
    private String hhVillage = "";

    @XmlElement(name = "HhNeighborChar",required = true)
    private String hhNeighborChar = "";

    @XmlElement(name = "CancelMark",required = true)
    private String cancelMark = "";

    @XmlElement(name = "AcceptSiteId",required = true)
    private String acceptSiteId = "";

    @XmlElement(name = "WsIpaddressL2",required = true)
    private String wsIpaddressL2 = "";

    @XmlElement(name = "ApplyCode",required = true)
    private String applyCode = "";

    @XmlElement(name = "WwPassportId",required = true)
    private String wwPassportId = "";

    @XmlElement(name = "WwStreetDoorplate",required = true)
    private String wwStreetDoorplate = "";

    @XmlElement(name = "WwForeignName",required = true)
    private String wwForeignName = "";

    @XmlElement(name = "WwVillage",required = true)
    private String wwVillage = "";

    @XmlElement(name = "WwNeighborChar",required = true)
    private String wwNeighborChar = "";

    @XmlElement(name = "HhNationality",required = true)
    private String hhNationality = "";

    @XmlElement(name = "HhBirthDate",required = true)
    private String hhBirthDate = "";

    @XmlElement(name = "HhAreaCode",required = true)
    private String hhAreaCode = "";

    @XmlElement(name = "WwPersonId",required = true)
    private String wwPersonId = "";

    @XmlElement(name = "WwTownCode",required = true)
    private String wwTownCode = "";

    @XmlElement(name = "WwOtherId",required = true)
    private String wwOtherId = "";

    @XmlElement(name = "WwNationalityCode",required = true)
    private String wwNationalityCode = "";

    @XmlElement(name = "HhOtherId",required = true)
    private String hhOtherId = "";

    @XmlElement(name = "WwNowState",required = true)
    private String wwNowState = "";

    @XmlElement(name = "MrgDevPlaceCode",required = true)
    private String mrgDevPlaceCode = "";

    @XmlElement(name = "WwAreaCode",required = true)
    private String wwAreaCode = "";

    public Rldf088tType() {
    }

    public void setApplyTransactionId(String applyTransactionId) {
        this.applyTransactionId= applyTransactionId;
    }

    public String getApplyTransactionId() {
        return this.applyTransactionId;
    }

    public void setApplySequenceId(Integer applySequenceId) {
        this.applySequenceId= applySequenceId;
    }

    public Integer getApplySequenceId() {
        return this.applySequenceId;
    }

    public void setPersonId(String personId) {
        this.personId= personId;
    }

    public String getPersonId() {
        return this.personId;
    }

    public void setSiteId(String siteId) {
        this.siteId= siteId;
    }

    public String getSiteId() {
        return this.siteId;
    }


    public void setHhNowState(String hhNowState) {
        this.hhNowState= hhNowState;
    }

    public String getHhNowState() {
        return this.hhNowState;
    }

    public void setRegisterYyymmdd(String registerYyymmdd) {
        this.registerYyymmdd= registerYyymmdd;
    }

    public String getRegisterYyymmdd() {
        return this.registerYyymmdd;
    }

    public void setHhResidentNo(String hhResidentNo) {
        this.hhResidentNo= hhResidentNo;
    }

    public String getHhResidentNo() {
        return this.hhResidentNo;
    }

    public void setWwPersonName(String wwPersonName) {
        this.wwPersonName= wwPersonName;
    }

    public String getWwPersonName() {
        return this.wwPersonName;
    }

    public void setWwForeignAddress(String wwForeignAddress) {
        this.wwForeignAddress= wwForeignAddress;
    }

    public String getWwForeignAddress() {
        return this.wwForeignAddress;
    }

    public void setHhStreetDoorplate(String hhStreetDoorplate) {
        this.hhStreetDoorplate= hhStreetDoorplate;
    }

    public String getHhStreetDoorplate() {
        return this.hhStreetDoorplate;
    }

    public void setHhNationalityCode(String hhNationalityCode) {
        this.hhNationalityCode= hhNationalityCode;
    }

    public String getHhNationalityCode() {
        return this.hhNationalityCode;
    }

    public void setWwNeighbor(String wwNeighbor) {
        this.wwNeighbor= wwNeighbor;
    }

    public String getWwNeighbor() {
        return this.wwNeighbor;
    }

    public void setWorkId(String workId) {
        this.workId= workId;
    }

    public String getWorkId() {
        return this.workId;
    }

    public void setMrgDevPlace(String mrgDevPlace) {
        this.mrgDevPlace= mrgDevPlace;
    }

    public String getMrgDevPlace() {
        return this.mrgDevPlace;
    }

    public void setWsId(String wsId) {
        this.wsId= wsId;
    }

    public String getWsId() {
        return this.wsId;
    }

    public void setWsIpaddress(String wsIpaddress) {
        this.wsIpaddress= wsIpaddress;
    }

    public String getWsIpaddress() {
        return this.wsIpaddress;
    }

    public void setHhCityCountyCode(String hhCityCountyCode) {
        this.hhCityCountyCode= hhCityCountyCode;
    }

    public String getHhCityCountyCode() {
        return this.hhCityCountyCode;
    }

    public void setHhTownCode(String hhTownCode) {
        this.hhTownCode= hhTownCode;
    }

    public String getHhTownCode() {
        return this.hhTownCode;
    }

    public void setHhPassportId(String hhPassportId) {
        this.hhPassportId= hhPassportId;
    }

    public String getHhPassportId() {
        return this.hhPassportId;
    }

    public void setHhForeignAddress(String hhForeignAddress) {
        this.hhForeignAddress= hhForeignAddress;
    }

    public String getHhForeignAddress() {
        return this.hhForeignAddress;
    }

    public void setHhPersonId(String hhPersonId) {
        this.hhPersonId= hhPersonId;
    }

    public String getHhPersonId() {
        return this.hhPersonId;
    }

    public void setWwCityCountyCode(String wwCityCountyCode) {
        this.wwCityCountyCode= wwCityCountyCode;
    }

    public String getWwCityCountyCode() {
        return this.wwCityCountyCode;
    }

    public void setExecuteYyymmdd(String executeYyymmdd) {
        this.executeYyymmdd= executeYyymmdd;
    }

    public String getExecuteYyymmdd() {
        return this.executeYyymmdd;
    }

    public void setWwNationality(String wwNationality) {
        this.wwNationality= wwNationality;
    }

    public String getWwNationality() {
        return this.wwNationality;
    }

    public void setHhNeighbor(String hhNeighbor) {
        this.hhNeighbor= hhNeighbor;
    }

    public String getHhNeighbor() {
        return this.hhNeighbor;
    }

    public void setHhForeignName(String hhForeignName) {
        this.hhForeignName= hhForeignName;
    }

    public String getHhForeignName() {
        return this.hhForeignName;
    }

    public void setWwResidentNo(String wwResidentNo) {
        this.wwResidentNo= wwResidentNo;
    }

    public String getWwResidentNo() {
        return this.wwResidentNo;
    }

    public void setHhPersonName(String hhPersonName) {
        this.hhPersonName= hhPersonName;
    }

    public String getHhPersonName() {
        return this.hhPersonName;
    }

    public void setRegisterTime(String registerTime) {
        this.registerTime= registerTime;
    }

    public String getRegisterTime() {
        return this.registerTime;
    }

    public void setWwBirthDate(String wwBirthDate) {
        this.wwBirthDate= wwBirthDate;
    }

    public String getWwBirthDate() {
        return this.wwBirthDate;
    }

    public void setHhVillage(String hhVillage) {
        this.hhVillage= hhVillage;
    }

    public String getHhVillage() {
        return this.hhVillage;
    }

    public void setHhNeighborChar(String hhNeighborChar) {
        this.hhNeighborChar= hhNeighborChar;
    }

    public String getHhNeighborChar() {
        return this.hhNeighborChar;
    }

    public void setCancelMark(String cancelMark) {
        this.cancelMark= cancelMark;
    }

    public String getCancelMark() {
        return this.cancelMark;
    }

    public void setAcceptSiteId(String acceptSiteId) {
        this.acceptSiteId= acceptSiteId;
    }

    public String getAcceptSiteId() {
        return this.acceptSiteId;
    }

    public void setWsIpaddressL2(String wsIpaddressL2) {
        this.wsIpaddressL2= wsIpaddressL2;
    }

    public String getWsIpaddressL2() {
        return this.wsIpaddressL2;
    }

    public void setApplyCode(String applyCode) {
        this.applyCode= applyCode;
    }

    public String getApplyCode() {
        return this.applyCode;
    }

    public void setWwPassportId(String wwPassportId) {
        this.wwPassportId= wwPassportId;
    }

    public String getWwPassportId() {
        return this.wwPassportId;
    }

    public void setWwStreetDoorplate(String wwStreetDoorplate) {
        this.wwStreetDoorplate= wwStreetDoorplate;
    }

    public String getWwStreetDoorplate() {
        return this.wwStreetDoorplate;
    }

    public void setWwForeignName(String wwForeignName) {
        this.wwForeignName= wwForeignName;
    }

    public String getWwForeignName() {
        return this.wwForeignName;
    }

    public void setWwVillage(String wwVillage) {
        this.wwVillage= wwVillage;
    }

    public String getWwVillage() {
        return this.wwVillage;
    }

    public void setWwNeighborChar(String wwNeighborChar) {
        this.wwNeighborChar= wwNeighborChar;
    }

    public String getWwNeighborChar() {
        return this.wwNeighborChar;
    }

    public void setHhNationality(String hhNationality) {
        this.hhNationality= hhNationality;
    }

    public String getHhNationality() {
        return this.hhNationality;
    }

    public void setHhBirthDate(String hhBirthDate) {
        this.hhBirthDate= hhBirthDate;
    }

    public String getHhBirthDate() {
        return this.hhBirthDate;
    }

    public void setHhAreaCode(String hhAreaCode) {
        this.hhAreaCode= hhAreaCode;
    }

    public String getHhAreaCode() {
        return this.hhAreaCode;
    }

    public void setWwPersonId(String wwPersonId) {
        this.wwPersonId= wwPersonId;
    }

    public String getWwPersonId() {
        return this.wwPersonId;
    }

    public void setWwTownCode(String wwTownCode) {
        this.wwTownCode= wwTownCode;
    }

    public String getWwTownCode() {
        return this.wwTownCode;
    }

    public void setWwOtherId(String wwOtherId) {
        this.wwOtherId= wwOtherId;
    }

    public String getWwOtherId() {
        return this.wwOtherId;
    }

    public void setWwNationalityCode(String wwNationalityCode) {
        this.wwNationalityCode= wwNationalityCode;
    }

    public String getWwNationalityCode() {
        return this.wwNationalityCode;
    }

    public void setHhOtherId(String hhOtherId) {
        this.hhOtherId= hhOtherId;
    }

    public String getHhOtherId() {
        return this.hhOtherId;
    }

    public void setWwNowState(String wwNowState) {
        this.wwNowState= wwNowState;
    }

    public String getWwNowState() {
        return this.wwNowState;
    }

    public void setMrgDevPlaceCode(String mrgDevPlaceCode) {
        this.mrgDevPlaceCode= mrgDevPlaceCode;
    }

    public String getMrgDevPlaceCode() {
        return this.mrgDevPlaceCode;
    }

    public void setWwAreaCode(String wwAreaCode) {
        this.wwAreaCode= wwAreaCode;
    }

    public String getWwAreaCode() {
        return this.wwAreaCode;
    }


    @Override
    public int hashCode(){
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.applyTransactionId == null) ? 0 : this.applyTransactionId.hashCode());
        result = prime * result + ((this.applySequenceId == null) ? 0 : this.applySequenceId.hashCode());
        result = prime * result + ((this.personId == null) ? 0 : this.personId.hashCode());
        result = prime * result + ((this.siteId == null) ? 0 : this.siteId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj){
           return true;
        }
        if (obj== null){
            return false;
        }
        if (getClass() != obj.getClass()){
            return false;
        }
        Rldf088tType other = (Rldf088tType) obj;
        if(this.applyTransactionId == null){
            if (other.applyTransactionId!=null){
                return false;
            }
        }else if(!this.applyTransactionId.equals(other.applyTransactionId)){
            return false;
        }
        if(this.applySequenceId == null){
            if (other.applySequenceId!=null){
                return false;
            }
        }else if(!this.applySequenceId.equals(other.applySequenceId)){
            return false;
        }
        if(this.personId == null){
            if (other.personId!=null){
                return false;
            }
        }else if(!this.personId.equals(other.personId)){
            return false;
        }
        if(this.siteId == null){
            if (other.siteId!=null){
                return false;
            }
        }else if(!this.siteId.equals(other.siteId)){
            return false;
        }
        return true;
    }
}
