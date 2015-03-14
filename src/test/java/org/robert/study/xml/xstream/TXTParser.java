package org.robert.study.xml.xstream;

import java.io.File;
import java.io.FileInputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import org.apache.commons.lang.StringUtils;
import org.codehaus.stax2.XMLInputFactory2;
import org.codehaus.stax2.XMLStreamReader2;
import org.robert.study.service.stax2.CountingWriter;
import org.robert.study.utils.Utils;

public class TXTParser {
    public static void main(String[] args) throws Exception {
        TXTParser xfp = new TXTParser();
        String filename = "/media/SSD/IISI/workspaces/eclipse_7_RIS_tt/DBdocCompare/test/org/robert/study/xml/xstream/operationContext.xml";
        xfp.parseFile(filename);
    }

    public void parseFile(String filename) {
    	List<String> aList = Utils.inputReadFile(new File(filename));
    	extractTxt(aList);
    }
    private void extractTxt(final List<String> aList){
    	List<String> result = new ArrayList<String>();
    	for(String txt : aList){
    		if(txt.contains("operationCode") || txt.contains("operationName")){
    			int start = txt.indexOf("value=\"");
    			int end = txt.lastIndexOf("\"");
    			String sample = txt.substring(start+7, end); 
    			result.add(sample);
    		}
    	}
    	System.out.println(aList);
    	int size = (aList.size() /2);
    	for(int i =0;i<aList.size();i+=2){
    		
    	}
    }
    public void parseFileV2(String filename) {}
}
