package org.robert.study.utils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;


public class StringTester {

    /**
     * @param args
     * @throws NoSuchMethodException 
     * @throws InvocationTargetException 
     * @throws InstantiationException 
     * @throws IllegalAccessException 
     * @throws IOException 
     */
    public static void main(String[] args) throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException, IOException {
//    	File realFile = new File("/home/weblogic/Desktop");
//    	System.out.println("11111ww111".matches("\\d*"));
	
//	BigDecimal bigDecimal =new BigDecimal("5");
//	BigDecimal multiplicand =new BigDecimal("1");
//	BigDecimal result = bigDecimal.multiply(multiplicand);
//	System.out.println(result.toPlainString());
//	List<String> aList = new ArrayList<String>();
//	Object aa = CollectionUtils.get(aList, 0);
//	System.out.println(aa);
//	String areaCode ="64000052";
//	String code = areaCode.substring(0, 5);
//	System.out.println(code);
//	String serno ="0000001";
//	if(StringUtils.isNotBlank(serno) && serno.length() ==7 ){
//	    String tmp = serno.substring(1, 7);
//	    System.out.println(tmp);
//	}
//	SimpleDateFormat sdf =new SimpleDateFormat("yyyy");
//	System.out.println(sdf.format(new Date()).substring(2,4));
//	
//	System.out.println(".........");
//	File a =new File("/home/weblogic/Desktop/transport_log");
//	long checksumCRC32 = FileUtils.checksumCRC32(a);
//	System.out.println("checksumCRC32: "+checksumCRC32);
//	String[] symbolCode =new String[]{"02100","016","111"};
//	final String beanName = "code" + StringUtils.join(symbolCode,null,0,2);
//	System.out.println("beanName: "+beanName);
//	String areaCode="65000030";
//	System.out.println("countyCode: "+areaCode.substring(0,5));
//	String personId ="0123456789";
//	String personIda ="888";
//	System.out.println(personId.substring(1, 2));
//	System.out.println(personId.matches("\\d{10}"));
//	System.out.println(personIda.matches("8{3}"));
	String aa = "Rldf(.){3,4}hType";
	String expr = "Rldf((.){3,4}||[^A])hType";
	String expr2 = "Rldf([^\"A\"])hType";
	String expr3 = "^[A-Z]+$" ;
	final String sample001 = "Rldfs0dhType";
	final String sample002 = "Rldf004hType";
	System.out.println(sample001.matches(expr));
	System.out.println(sample002.matches(expr));
	System.out.println(sample001.matches(expr2));
	System.out.println(sample002.matches(expr2));
	System.out.println("RldfBhType".matches(expr2));
	System.out.println("RT".matches(expr3));
    }
    private static void test001(){
    	String patternString = ".*.((doc)|(xls))";
    	System.out.println("000"+Pattern.compile(patternString).matcher("aa.xl").matches());
    	
        StringTester aStringTester = new StringTester();
        String sample = "1";
        boolean colleaction = aStringTester.isEnglishCharacters(sample);
        System.out.println(colleaction);
        System.out.println((int)'1');
        System.out.println((int)'0');
        System.out.println((int)'9');
        
        System.out.println(StringUtils.isNotBlank(" ff"));
        System.out.println(StringUtils.isNotBlank(null));
        System.out.println(StringUtils.isBlank(null));
        System.out.println(StringUtils.isBlank(" ff"));
        System.out.println(StringUtils.isBlank(" "));
        String tmp ="88888888";
        if(Pattern.compile("[9|8]{8}").matcher(tmp).matches()){
        	System.out.println("test:..........");
        }else {
        	System.out.println("false:..........");
        }
        Pattern rldfPattern = Pattern.compile("6-4-RLDFS.*.doc");
        String actionStr="6-4-RLDFSAAAA.doc";
        ;
        if(rldfPattern.matcher(actionStr).matches()){
        	System.out.println("Test0: "+actionStr);
        }
        if (actionStr.matches("[^RCI]*(R|C|I|D)$")) {
            System.out.println("Test1");
        }
        if (!actionStr.matches("([^RCI]*(R|C|I)$)|RCD|RDC")) {
            System.out.println("Test2");
        }
    }
    private boolean isEnglishCharacters(String sample) {
        char[] charArray = sample.toCharArray();
        boolean result = charArray.length > 0 ? true : false;
        Set<Integer> colleaction = getNumerCharacterUnicodeSymbolCode();
        for (char tmpcahr : charArray) {
            int intTmp = (int) tmpcahr;
            if (!colleaction.contains(Integer.valueOf(intTmp))) {
                result = false;
            }
        }

        return result;
    }

    private static final Set<Integer> getNumerCharacterUnicodeSymbolCode() {
        Set<Integer> result = new HashSet<Integer>();
        for (int i = 97; i < 123; i++) {
            result.add(Integer.valueOf(i));
        }
        for (int i = 65; i < 90; i++) {
            result.add(Integer.valueOf(i));
        }
        for (int i = 48; i < 60; i++) {
            result.add(Integer.valueOf(i));
        }
        result.add(95);// '_'
        return result;
    }
}
