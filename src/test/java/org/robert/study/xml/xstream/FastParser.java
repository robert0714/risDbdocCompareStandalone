package org.robert.study.xml.xstream;

import java.io.FileInputStream;
import java.io.StringWriter;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import org.apache.commons.lang.StringUtils;
import org.codehaus.stax2.XMLInputFactory2;
import org.codehaus.stax2.XMLStreamReader2;
import org.robert.study.service.stax2.CountingWriter;

public class FastParser {
    public static void main(String[] args) throws Exception {
        FastParser xfp = new FastParser();
        // String filename = "z:/bigfile.xml";
        // xfp.parseFile(filename);
        String filename = "/media/SSD/IISI/workspaces/eclipse_7_RIS_tt/DBdocCompare/test/org/robert/study/xml/xstream/operationContext.xml";
        xfp.parseFile(filename);
    }

    public void parseFile(String filename) {
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
        System.out.println("Starting to parse " + filename);
        System.out.println("");
        long starttime = System.currentTimeMillis();
        int elementCount = 0;
        int filteredCharCount = 0;

        try {
            XMLStreamReader2 xmlr = (XMLStreamReader2) xmlif.createXMLStreamReader(filename, new FileInputStream(
                    filename));
            int eventType = xmlr.getEventType();
            String curElement = "";
            CountingWriter cw = new CountingWriter(new StringWriter(), '@');
            while (xmlr.hasNext()) {
                eventType = xmlr.next();

                switch (eventType) {
                case XMLEvent.START_ELEMENT:
                    QName ocurElement = xmlr.getName();
                    curElement = ocurElement.getLocalPart();

                    filteredCharCount = 0;
                    int attNameCount = xmlr.getAttributeCount();
                    for (int i = 0; i < attNameCount; ++i) {
                        QName attName = xmlr.getAttributeName(i);
                        String value = xmlr.getAttributeValue(i);
                        if(attName.getLocalPart().equalsIgnoreCase("property")){
                        	 System.out.println("attName: " + attName.getLocalPart());
                             System.out.println("value: " + value);
                        }
                        System.out.println("attName: " + attName.getLocalPart());
                        System.out.println("value: " + value);
                       
                    }
//                    if (curElement.equals("itemType")) {
//                        System.out.println("tag: itemType");
//                    } else if (curElement.equals("dataType")) {
//                        System.out.println("tag: dataType");
//                    } else if (curElement.equals("itemList")) {
//                        System.out.println("tag: itemList");
//                    } else if (curElement.equals("item")) {
//                        System.out.println("tag: item");
//                    } else if (curElement.equals("key")) {
//                        System.out.println("tag: key");
//                    } else if (curElement.equals("value")) {
//                        System.out.println("tag: value");
//                    }
                    break;
                case XMLEvent.CHARACTERS:
                    if (curElement.equals("property") && StringUtils.isNotBlank(xmlr.getText())) {
                        System.out.println("itemType.getText(): " + xmlr.getText());
                    } else if (curElement.equals("dataType") && StringUtils.isNotBlank(xmlr.getText())) {
                        System.out.println("dataType.getText(): " + xmlr.getText());
                    } else if (curElement.equals("itemList") && StringUtils.isNotBlank(xmlr.getText())) {
                        System.out.println("itemList.getText(): " + xmlr.getText());
                    } else if (curElement.equals("item") && StringUtils.isNotBlank(xmlr.getText())) {
                        System.out.println("item.getText(): " + xmlr.getText());
                    } else if (curElement.equals("key") && StringUtils.isNotBlank(xmlr.getText())) {
                        System.out.println("key.getText(): " + xmlr.getText());
                    } else if (curElement.equals("value") && StringUtils.isNotBlank(xmlr.getText())) {
                        System.out.println("value.getText(): " + xmlr.getText());
                    }
                    break;
                case XMLEvent.END_ELEMENT:
                    if (curElement.equals("entry")) {
                        elementCount++;
                        System.out.println("Element #" + elementCount + " : " + filteredCharCount);
                    }
                    if (curElement.equals("end itemType")) {
                        System.out.println("end tag: itemType");
                    } else if (curElement.equals("dataType")) {
                        System.out.println("end tag: dataType");
                    } else if (curElement.equals("itemList")) {
                        System.out.println("end tag: itemList");
                    } else if (curElement.equals("item")) {
                        System.out.println("end tag: item");
                    } else if (curElement.equals("key")) {
                        System.out.println("end tag: key");
                    } else if (curElement.equals("value")) {
                        System.out.println("end tag: value");
                    }
                    break;

                case XMLEvent.ATTRIBUTE:

                    // curElement = xmlr.getName().toString();
                    curElement = xmlr.getName().getLocalPart();
                    break;
                case XMLEvent.START_DOCUMENT:
                    // curElement = xmlr.getName().toString();
                    curElement = xmlr.getName().getLocalPart();
                    break;
                case XMLEvent.END_DOCUMENT:
                    System.out.println("Total of " + elementCount + " occurrences");
                }

                if (eventType == XMLEvent.START_ELEMENT) {
                    // curElement = xmlr.getName().toString();
                    curElement = xmlr.getName().getLocalPart();
                } else {
                    if (eventType == XMLEvent.CHARACTERS) {
                    }
                }
            }
            xmlr.close();
            xmlr.closeCompletely();
        } catch (XMLStreamException ex) {
            System.out.println(ex.getMessage());
            if (ex.getNestedException() != null) {
                ex.getNestedException().printStackTrace();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println(" completed in " + (System.currentTimeMillis() - starttime) + " ms");
    }

    public void parseFileV2(String filename) {
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
        System.out.println("Starting to parse " + filename);
        System.out.println("");
        long starttime = System.currentTimeMillis();
        int elementCount = 0;
        int filteredCharCount = 0;

        try {
            XMLStreamReader2 xmlr = (XMLStreamReader2) xmlif.createXMLStreamReader(filename, new FileInputStream(
                    filename));
            int eventType = xmlr.getEventType();
            String curElement = "";
            CountingWriter cw = new CountingWriter(new StringWriter(), '@');
            while (xmlr.hasNext()) {
                eventType = xmlr.next();

                switch (eventType) {
                case XMLEvent.START_ELEMENT:
                    curElement = xmlr.getName().toString();
                    filteredCharCount = 0;
                    int attNameCount = xmlr.getAttributeCount();
                    for (int i = 0; i < attNameCount; ++i) {

                        QName attName = xmlr.getAttributeName(i);
                        String value = xmlr.getAttributeValue(i);
                        System.out.println("value: " + value);
                        System.out.println("attName: " + attName.getLocalPart());
                    }

                    break;
                case XMLEvent.CHARACTERS:

                    if (curElement.equals("bigelement")) {
                        cw.resetCount();
                        filteredCharCount += cw.getCount();
                        System.out.println("xmlr.getText(): " + xmlr.getText());
                        ;
                    }
                    break;
                case XMLEvent.END_ELEMENT:
                    if (curElement.equals("bigelement")) {
                        elementCount++;
                        System.out.println("Element #" + elementCount + " : " + filteredCharCount);
                    }
                    break;

                case XMLEvent.ATTRIBUTE:

                    curElement = xmlr.getName().toString();
                    ;
                    break;
                case XMLEvent.START_DOCUMENT:
                    curElement = xmlr.getName().toString();
                    ;
                    break;
                case XMLEvent.END_DOCUMENT:
                    System.out.println("Total of " + elementCount + " occurrences");
                }

                if (eventType == XMLEvent.START_ELEMENT) {
                    curElement = xmlr.getName().toString();
                } else {
                    if (eventType == XMLEvent.CHARACTERS) {

                    }
                }
            }
            xmlr.close();
            xmlr.closeCompletely();
        } catch (XMLStreamException ex) {
            System.out.println(ex.getMessage());
            if (ex.getNestedException() != null) {
                ex.getNestedException().printStackTrace();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println(" completed in " + (System.currentTimeMillis() - starttime) + " ms");
    }
}
