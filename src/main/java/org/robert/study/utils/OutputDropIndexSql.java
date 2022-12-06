package org.robert.study.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

public class OutputDropIndexSql {

	public static void main(String[] args) throws IOException {
		File dir =new File("/home/weblogic/Downloads/RL");
		File[] list = dir.listFiles();
		List<String> result =new ArrayList<String>();
		for(File file :list){
		   final List<String> lines = FileUtils.readLines(file);
		   for(String line: lines){
		       if(StringUtils.contains(line.toUpperCase(), "CREATE INDEX")){
			String[]  tmp  = StringUtils.split(line, " ");
			   result.add("drop index "+tmp[2]+" ;");
		       }
		   }
		};
		for(String tmp: result){
		    System.out.println(tmp);
		}
		Utils.outputFile("/home/weblogic/Downloads/deletUnuseXLDFIndex.sql", result);

	}

}
