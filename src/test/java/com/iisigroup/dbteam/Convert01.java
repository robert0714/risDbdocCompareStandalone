package com.iisigroup.dbteam;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;

import com.iisi.doc.process.WordExtract;
import com.iisi.rl.table.DataTable;

public class Convert01 {
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		List<String> tableList = getRCDFXXXA();
		
		List<String> tableRLDFSList = getRLDFSXXX();
		
		for(String name: tableList){
			System.out.println(name);
		}
		System.out.println("-------------------------------------");
		for(String name: tableRLDFSList){
			System.out.println(name);
		}
	}
	public static List<String> getRCDFXXXA(){
		Pattern rldfPattern = Pattern.compile("6-4-RCDF.*A.doc");
		String folderName="/home/weblogic/Desktop/tmpInput/RC";
		File[] srcFile = new File(folderName).listFiles();
		List<String> tableList = new ArrayList<String>();
		for(File srcFileUnit: srcFile){
			if(rldfPattern.matcher(srcFileUnit.getName()).matches()){
				tableList.add(srcFileUnit.getName().replace(".doc", "").replace("6-4-", ""));
			}
		}
		
		Collections.sort(tableList);
		
		return tableList;
	}

	public static List<String> getRLDFSXXX() {
		 Pattern rldfPattern = Pattern.compile("6-4-RLDFR0.*.doc");
		String folderName="/home/weblogic/Desktop/tmpInput/RL";
		final WordExtract aWordExtract = new WordExtract();
		File[] srcFile = new File(folderName).listFiles();
		List<DataTable> srcList = new ArrayList<DataTable>();
		for(File srcFileUnit: srcFile){
			if(rldfPattern.matcher(srcFileUnit.getName()).matches()){
				DataTable dataTable = aWordExtract.convertTOUnit(srcFileUnit);
				srcList.add(dataTable);
			}
		}
		List<String> tableList = new ArrayList<String>();
		for(DataTable table:srcList){
			if(table!=null){
				tableList.add(table.getTableName().replace("RLDFS", "RLDFR"));				
//				System.out.println(table.getTableName()+","+table.getRemark());
			}			
		}
		Collections.sort(tableList);
//		for(String name: tableList){
//			System.out.println(name);
//		}
		return tableList;
	}

}
