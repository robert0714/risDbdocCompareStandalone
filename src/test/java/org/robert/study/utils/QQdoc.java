package org.robert.study.utils;

import java.io.FileNotFoundException;
import java.io.FileReader;

import com.thoughtworks.qdox.JavaClassContext;
import com.thoughtworks.qdox.JavaDocBuilder;
import com.thoughtworks.qdox.model.JavaSource;

public class QQdoc {

	/**
	 * @param args
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException {
		
		String fileFullPath = "/home/weblogic/ramdisk/work/workspaces/eclipse_7_RIS_tt/DBdocCompare/test/org/robert/study/utils/StringTester.java";
	    JavaDocBuilder builder = new JavaDocBuilder();
	    FileReader fr =  new FileReader( fileFullPath  );
	    builder.addSource(fr);

	    JavaSource src = builder.getSources()[0];
	    String[] imports = src.getImports();
	    JavaClassContext JavaClassContext = src.getJavaClassContext();
	    for ( String imp : imports )
	    {
	        System.out.println(imp);
	    }

	}
	

}
