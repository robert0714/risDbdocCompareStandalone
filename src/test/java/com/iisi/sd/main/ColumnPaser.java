package com.iisi.sd.main;

import java.io.File;
import java.util.List;
import java.util.StringTokenizer;

import org.robert.study.utils.Utils;

public class ColumnPaser {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		List<String> lineList = Utils.inputReadFile(new File("/home/weblogic/Desktop/S0Z5.csv"));
		for(String tmp :lineList){
			String result  = convertStringFromTableColumnName2JavaConventionNamingRulePropertyName(tmp);
			System.out.println(result);
		}
//		System.out.println(convertStringFromTableColumnName2JavaConventionNamingRulePropertyName("ROW_LINE_DREAM_WEAR"));
		
	}
	private static String convertStringFromTableColumnName2JavaConventionNamingRulePropertyName(final String originalName) {
        String result = null;
        StringTokenizer str = new StringTokenizer(originalName, "_");
        StringBuffer sbf = new StringBuffer();
        while (str.hasMoreElements()) {
            String tmp = ((String) str.nextElement()).toLowerCase();
            sbf.append(tmp.substring(0, 1).toUpperCase()).append(tmp.toLowerCase().substring(1));
        }
        String tmpString = sbf.toString();
        try {
            result = tmpString.substring(0, 1).toLowerCase() + tmpString.substring(1);
        } catch (Exception e) {
            System.out.println("發生錯誤.........." + originalName + "( " + tmpString + "  )");
            // e.printStackTrace();
//            log.debug(e.getStackTrace());
        }
        return result;
    }
}
