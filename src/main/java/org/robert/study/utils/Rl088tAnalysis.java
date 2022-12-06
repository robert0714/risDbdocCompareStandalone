package org.robert.study.utils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.jxls.transformer.XLSTransformer;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.poi.ss.usermodel.Workbook;
 

public class Rl088tAnalysis {

    /**
     * @param args
     */
    public static void main(String[] args) {
	final Rl088tAnalysis analysis = new Rl088tAnalysis();
	File src = new File("/home/weblogic/Downloads/rldf088t_1030304_u.txt");
	final List<Rldf088tType> enities = new ArrayList<Rldf088tType>();
	try {
	    
	    final	List<String> lines = FileUtils.readLines(src);
	    for(String line : lines){
		final String tmp = StringUtils.replace(line, "\\,", "_");
		final String[] strArray = StringUtils.splitByWholeSeparator(tmp, ",");
//		System.out.println("----------------------");

		final Rldf088tType rldf088t = new Rldf088tType();
		rldf088t.setApplySequenceId(1);
		
		rldf088t.setApplyTransactionId("TXRC"+strArray[3]+strArray[4]+String.format("%010d", RandomUtils.nextInt(1234567890,0)));
		//TXFL201301181016303021
		//strArray[3]
		rldf088t.setSiteId(strArray[0]);
		rldf088t.setWsId(strArray[1]);
		rldf088t.setApplyCode(strArray[2]);
		rldf088t.setRegisterYyymmdd(strArray[3]);
		rldf088t.setRegisterTime(strArray[4]);
		rldf088t.setExecuteYyymmdd(strArray[5]);
		
		rldf088t.setHhPersonId(strArray[6]);
		rldf088t.setHhPersonName(strArray[7]);
		rldf088t.setHhBirthDate(strArray[8]);
		rldf088t.setHhResidentNo(strArray[9]);
		rldf088t.setHhPassportId(strArray[10]);
		rldf088t.setHhOtherId(strArray[11]);
		rldf088t.setHhForeignName(strArray[12]);
		rldf088t.setHhNowState(strArray[14]);
		
		rldf088t.setWwPersonId(strArray[16]);
		rldf088t.setWwPersonName(strArray[17]);
		rldf088t.setWwBirthDate(strArray[18]);
		rldf088t.setWwResidentNo(strArray[19]);
		rldf088t.setWwPassportId(strArray[20]);
		rldf088t.setWwOtherId(strArray[21]);
		rldf088t.setWwForeignName(strArray[22]);
		rldf088t.setWwNowState(strArray[24]);
		rldf088t.setCancelMark(strArray[27]);
		
		for(int i = 0 ;i<strArray.length ;++i){
		    String value  =  strArray[i];
		    if(i==13  ){
			String code = StringUtils.substring(value, 0, 3);
			String name = StringUtils.substring(value, 3, value.length());
			rldf088t.setHhNationalityCode(code);
			rldf088t.setHhNationality(name);
		    }
		    if(i==23  ){
			String code = StringUtils.substring(value, 0, 3);
			String name = StringUtils.substring(value, 3, value.length());
			rldf088t.setWwNationalityCode(code);
			rldf088t.setWwNationality(name);
		    }
		    if(i==26  ){
			String code = StringUtils.substring(value, 0, 3);
			String name = StringUtils.substring(value, 3, value.length());
			rldf088t.setMrgDevPlaceCode(code);
			rldf088t.setMrgDevPlace(name);
		    }
		    if(i==15  ){
			final String addressline = StringUtils.replace(value, "_", ",");
			Address address = analysis.convertFromLine(addressline);
			rldf088t.setHhAreaCode(address.getSiteId());
			rldf088t.setHhCityCountyCode(address.getCityCountycode());
			rldf088t.setHhTownCode(address.getTownCode());
			rldf088t.setHhVillage(address.getVillage());
			rldf088t.setHhNeighbor(address.getNeighbor());
			rldf088t.setHhNeighborChar(address.getNeighborChar());			
			rldf088t.setHhStreetDoorplate(address.getStreetDoorplate());
			rldf088t.setHhForeignAddress(address.getForeignAddress());
		    }
		    if(  i==25){

			final String addressline = StringUtils.replace(value, "_", ",");
			Address address = analysis.convertFromLine(addressline);
			rldf088t.setWwAreaCode(address.getSiteId());
			rldf088t.setWwCityCountyCode(address.getCityCountycode());
			rldf088t.setWwTownCode(address.getTownCode());
			rldf088t.setWwVillage(address.getVillage());
			rldf088t.setWwNeighbor(address.getNeighbor());
			rldf088t.setWwNeighborChar(address.getNeighborChar());			
			rldf088t.setWwStreetDoorplate(address.getStreetDoorplate());
			rldf088t.setWwForeignAddress(address.getForeignAddress());
		    }
		}
		enities.add(rldf088t);
	    }
	    
	} catch (IOException e) {
	    e.printStackTrace();
	}
	System.out.println(enities.size());
//	List<String> sqls = analysis .getSql(enities);
//	for(int i = 0 ; i < sqls.size() ; ++i ){
//	    String sql = sqls.get(i);
//	    if(i % 20 == 0){
//		 System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
//	    }
//	    System.out.println(sql);
//	}
//	analysis .writeToFile(enities);
    }
    private void writeToFile(final List<Rldf088tType> enities ){
	Map<String, Object> beans = new HashMap<String, Object>();
        

        beans.put("enities", enities);
        XLSTransformer transformer = new XLSTransformer();
	
	try {
	    Workbook aHSSFWorkbook = transformer.transformXLS(
	    	Rl088tAnalysis.class.getResource("xldf088t_template.xls").openStream(), beans);
	    POIUtils.writeWorkbookOut(new File("/home/weblogic/Desktop/workbook.xls"), aHSSFWorkbook);
	} catch (Exception e) {
	    e.printStackTrace();
	} 
	
        
//	Workbook wb = new HSSFWorkbook();
//	Sheet sheet1 = wb.createSheet("new sheet");
//	
//	
//	
//	
//	FileOutputStream fileOut = null;
//	try {
//	    fileOut = new FileOutputStream("/home/weblogic/Desktop/workbook.xls");
//	    wb.write(fileOut);
//	} catch (Exception e) {
//	} finally{
//	    if (fileOut != null) {
//		try {
//		    fileOut.close();
//		} catch (IOException e) {
//		    e.printStackTrace();
//		}
//	    }
//	}
	
    }
    private String retireveColumnName(final String attributeName){
	 String columnName =StringUtils.join( StringUtils.splitByCharacterTypeCamelCase(attributeName),"_").toLowerCase();
	 if(columnName.equals("ws_ipaddress_l_2")){
	     columnName ="ws_ipaddress_l2";
	 }
	 return columnName;
    }
    private String convertInserSql(final Rldf088tType rldf088t){
	final 	Field[] declaredFields = Rldf088tType.class.getDeclaredFields();
	final List<String>  columenList = new ArrayList<String>();
	final List<String>  parameterList = new ArrayList<String>();
 	for(Field filed : declaredFields){
	    try {
		final    String name =  filed.getName();
		final String columnName =retireveColumnName(name);
		
		Object value = 	PropertyUtils.getProperty(rldf088t,name);
		String valueStr=StringUtils.EMPTY;
		if( value instanceof Integer || value instanceof String){
		    valueStr= value.toString();
		}
		 
		columenList.add(columnName);
		parameterList.add("'"+valueStr+"'");
	    } catch (Exception e) {
		e.printStackTrace();
	    } 
	}
 	 
 	String result = String.format("insert into Rldf088t ( %s ) VALUES ( %s ) ;", StringUtils.join(columenList,","),StringUtils.join(parameterList,","));
 	return result ;
    }
    protected List<String> getSql(final List<Rldf088tType> enities){
	final List<String>  sqls = new ArrayList<String>();
	for(Rldf088tType entity : enities){
	    final String sql = convertInserSql(entity);
	    sqls.add(sql);
	}
	return sqls ; 
    }
    public static void main02(String[] args) {
	String line ="10007010安溪里　　　　　001鄰安溪路３２８巷３９弄７號　　　　　　　　　　　　　NO4 MARACHALE WAY,WOODBRIDGE ISLAND MILNERTON,CAPE TOWN,SOUTH AFRICA";
	System.out.println(line.length());
	String siteId = StringUtils.substring(line, 0, 8);
	String cityCountycode = StringUtils.substring(line, 0, 5);
	String townCode = StringUtils.substring(line, 5, 8);
	String village = StringUtils.substring(line, 8, 16);
	String neighbor = StringUtils.substring(line, 16, 19);
	String neighborChar = StringUtils.substring(line, 19, 20);
	String streetDoorplate = StringUtils.substring(line, 20, 45);
	String foreignAddress = StringUtils.substring(line, 45, line.length());
	System.out.println(siteId);
	System.out.println(cityCountycode);
	System.out.println(townCode);
	System.out.println(village);
	System.out.println(neighbor);
	System.out.println(neighborChar);
	System.out.println(streetDoorplate);
	System.out.println(foreignAddress);
    }
    class Address {
	private String siteId;
	private String cityCountycode;
	private String townCode;
	private String village;
	private String neighbor;
	private String neighborChar;
	private String streetDoorplate; 
	private String foreignAddress;
	public String getSiteId() {
	    return siteId;
	}
	public void setSiteId(String siteId) {
	    this.siteId = siteId;
	}
	public String getCityCountycode() {
	    return cityCountycode;
	}
	public void setCityCountycode(String cityCountycode) {
	    this.cityCountycode = cityCountycode;
	}
	public String getTownCode() {
	    return townCode;
	}
	public void setTownCode(String townCode) {
	    this.townCode = townCode;
	}
	public String getVillage() {
	    return village;
	}
	public void setVillage(String village) {
	    this.village = village;
	}
	public String getNeighbor() {
	    return neighbor;
	}
	public void setNeighbor(String neighbor) {
	    this.neighbor = neighbor;
	}
	public String getNeighborChar() {
	    return neighborChar;
	}
	public void setNeighborChar(String neighborChar) {
	    this.neighborChar = neighborChar;
	}
	public String getStreetDoorplate() {
	    return streetDoorplate;
	}
	public void setStreetDoorplate(String streetDoorplate) {
	    this.streetDoorplate = streetDoorplate;
	}
	public String getForeignAddress() {
	    return foreignAddress;
	}
	public void setForeignAddress(String foreignAddress) {
	    this.foreignAddress = foreignAddress;
	} 
	
    }   
    private Address convertFromLine(String line){
	String siteId = StringUtils.substring(line, 0, 8).replace("　", "");
	String cityCountycode = StringUtils.substring(line, 0, 5).replace("　", "");
	String townCode = StringUtils.substring(line, 5, 8).replace("　", "");
	String village = StringUtils.substring(line, 8, 16).replace("　", "");
	String neighbor = StringUtils.substring(line, 16, 19).replace("　", "");
	String neighborChar = StringUtils.substring(line, 19, 20).replace("　", "");
	String streetDoorplate = StringUtils.substring(line, 20, 45).replace("　", "");
	String foreignAddress = StringUtils.substring(line, 45, line.length()).replace("　", "");
	
	final Address result = new Address();
	result.setSiteId(siteId);
	result.setCityCountycode(cityCountycode);
	result.setTownCode(townCode);
	result.setVillage(village);
	result.setNeighbor(neighbor);
	result.setNeighborChar(neighborChar);	
	result.setStreetDoorplate(streetDoorplate);
	result.setForeignAddress(foreignAddress);
	return result;
    }
}
