package org.robert.study.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;

public class PrintCombination {

    public static void main(String[] args) {
//	String[] elements = { "a", "b", "c", "d", "e", "f", "g" };
	//005,084,0B2,001
	List<String> testResult = new ArrayList<String>();
	testResult.add("0B2");
	testResult.add("005");
	testResult.add("084");
	Collections.sort(testResult);
	String[] elements = testResult.toArray(new String[]{});
	
	
	int maxNo = 3;//所取出的最大數量值
	 List<String[]>   results = getReportNameCombination(testResult, maxNo);
	 for(String[] noArray :results){
	     System.out.println(ArrayUtils.toString(noArray));
	     
	 }
    }
    public static List<String[]>  getReportNameCombination(final List<String> testResult , final int maxNo ){
	Collections.sort(testResult);
	String[] elements = testResult.toArray(new String[]{});
	Map<Integer, List<String>> tmpResult =  getCombination(elements, maxNo);
	final List<String[]> result = new ArrayList<String[]>();
	for(Integer no:tmpResult.keySet()){
	    List<String> showList = tmpResult.get(no);
//	    System.out.println("從"+elements.length+"取"+no);
	    for(String targetReportName : showList){
//		System.out.print(targetReportName);
//		System.out.print(",");
		String[]  excludeArrays = getExcludeMenu(elements, targetReportName);
		List<String> tmpList = new ArrayList<String>();
		tmpList.add(targetReportName);
		for(String otherReportName :excludeArrays){
//		    System.out.print(otherReportName+",");
		    tmpList.add(otherReportName);
		}
//		System.out.println(" ");
//		ArrayUtils.add(excludeArrays, targetReportName);
//		ArrayUtils.reverse(excludeArrays);
		
		result.add(tmpList.toArray(new String[]{}));
	    }
	}
	return result;
    }
    private  static String[] getExcludeMenu(final String[] elements,final String combinationResult  ){
	List<String> tmpList = new LinkedList<String>(); 
	for(String element :elements){
	    if(!combinationResult.contains(element)){
		tmpList.add(element);
	    }
	} 
	return tmpList.toArray(new String[]{});
    }
    
    private static Map<Integer, List<String>> getCombination(String[] elements, int number) {
	final Map<Integer, List<String>> result = new HashMap<Integer, List<String>>();	
	for (int j = 1; j <= number; ++j) {
	    int[] indices;
	    CombinationGenerator x = new CombinationGenerator(elements.length, j);
	    final List<String> record = new ArrayList<String>();
	    StringBuffer combination;
	    while (x.hasMore()) {
		combination = new StringBuffer();
		indices = x.getNext();
		for (int i = 0; i < indices.length; i++) {
		    combination.append(elements[indices[i]]);
		}
		// System.out.println(combination.toString());
		record.add(combination.toString());
	    }
	    result.put(j, record);
	}
	return result;
    }
}
