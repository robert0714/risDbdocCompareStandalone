package com.iisigroup.toolkits.service;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.robert.study.discrepancy.model.DiscrepancyColumnInterface;
import org.robert.study.service.CompareDBTableAnd64TableService;


public class CompareDBTableAnd64TableServiceV2Impl extends AbstractComareTableServiceImpl implements CompareDBTableAnd64TableService {
    protected static Logger log = Logger.getLogger(CompareDBTableAnd64TableServiceV2Impl.class);
    
	<T extends DiscrepancyColumnInterface> boolean columnAttributeComparisonLogic(T scriptColumn, T dataColumnInfo) {
		String scriptColumnformat =  scriptColumn.getFormat();
		String dataColumnInfoformat = dataColumnInfo.getFormat();
		
		String scriptColumnformatName = getNameOfFormat(scriptColumnformat);
		String dataColumnInfoformatName = getNameOfFormat(dataColumnInfoformat);
		int scriptColumnformatScale = getScaleOfFormat(scriptColumnformat);
		int dataColumnInfoformatScale = getScaleOfFormat(dataColumnInfoformat);
		
		boolean  result = (!StringUtils.equalsIgnoreCase(scriptColumnformatName ,dataColumnInfoformatName) )||(scriptColumnformatScale != dataColumnInfoformatScale);
		return result;
	}

	
	<T extends DiscrepancyColumnInterface> Map<String, String[]>  columnAttributeComparisonLogic(final T scriptColumn,  T dataColumnInfo,String[] columnNames)  {
		
		final  Map<String, String[]> sameColumnAttributeDiscrepancy = new HashMap<String, String[]>();
		for(String attributeName : columnNames){
			try {
				if(StringUtils.equals("format", attributeName)){
					boolean  tmpResult = columnAttributeComparisonLogic(scriptColumn, dataColumnInfo);
					if(tmpResult){
						sameColumnAttributeDiscrepancy.put(scriptColumn.getColumnName()+" ("+attributeName+")", new String[] { scriptColumn.getFormat(), dataColumnInfo.getFormat() });
					}
				}else{
					Object scriptColumnAttribute = PropertyUtils.getProperty(scriptColumn, attributeName);
					Object dataColumnInfoAttribute = PropertyUtils.getProperty(dataColumnInfo, attributeName);
					boolean  tmpResult = ! ObjectUtils.equals(scriptColumnAttribute, dataColumnInfoAttribute);
					if(tmpResult){
						sameColumnAttributeDiscrepancy.put(scriptColumn.getColumnName()+" ("+attributeName+")", new String[] { scriptColumnAttribute.toString(), dataColumnInfoAttribute.toString() });
					}
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return sameColumnAttributeDiscrepancy;
	}
	
	 private  int getScaleOfFormat(final String format) {
		 	int result =0;
	        int start = StringUtils.indexOf(format, '(');
	        int end = StringUtils.indexOf(format, ')');
	        String initdata = StringUtils.substring(format  ,start + 1, end);
	        result =NumberUtils.toInt(initdata);	        
	        return result;
	}
	 private  String getNameOfFormat(final String format) {
		 	
	        int end = StringUtils.indexOf(format, '(');
	       
	        String result = StringUtils.substring(format  ,0, end).trim();
	              
	        return result;
	}

}
