package org.robert.study.xml.xstream;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;

import org.codehaus.stax2.XMLInputFactory2;
import org.codehaus.stax2.XMLStreamReader2;

import tw.gov.moi.rl.component.dto.HouseholdOperation;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;

public class XstreamReaderV1 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		XStream xstream = new XStream(new StaxDriver());
//		String xml = "";
		// String filename = "";
		File srFile = new File("/media/SSD/IISI/workspaces/eclipse_7_RIS_tt/DBdocCompare/test/org/robert/study/xml/xstream/operationContext.xml");

		XMLInputFactory2 xmlif = null;
		try {
			xmlif = (XMLInputFactory2) XMLInputFactory2.newInstance();
			xmlif.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, Boolean.FALSE);
			xmlif.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.FALSE);
			xmlif.setProperty(XMLInputFactory.IS_COALESCING, Boolean.FALSE);
			xmlif.configureForSpeed();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		System.out.println("Starting to parse " + srFile.getName());

		FileReader fr = null;
		try {
			XMLStreamReader2 xmlr = (XMLStreamReader2) xmlif.createXMLStreamReader(srFile.getName(), new FileInputStream(srFile));
			Map<String,HouseholdOperation> object = new HashMap<String, HouseholdOperation>();

			fr = new FileReader(srFile);
			Object result = 
					xstream.fromXML(fr, object);
			System.out.println(result);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		} finally {
			if (fr != null) {
				try {
					fr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
