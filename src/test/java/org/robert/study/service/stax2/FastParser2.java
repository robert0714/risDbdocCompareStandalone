package org.robert.study.service.stax2;

import java.io.FileOutputStream;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;

import org.codehaus.stax2.XMLOutputFactory2;
import org.codehaus.stax2.XMLStreamWriter2;

public class FastParser2 {
    public static void main(String[] args) throws Exception {
        FastParser2 xfp = new FastParser2();
        String filename = "z:/bigfile.xml";
        xfp.parseFile(filename);
    }

    /***
     * {@code} <mydoc> <myelement> <bigelement> abcdefghijklmnopqrstuvwxyzABC
     * ... </bigelement> <withnested> <nested1>abcdefg... </nested1>
     * <nested2>abcdefg... </nested2> ... <nested49>abcdefg... </nested49>
     * </withnested> </myelement> ... </mydoc>
     * 
     * ***/
    public void parseFile(String filename) {
        XMLOutputFactory2 xmlof = null;
        try {
            xmlof = (XMLOutputFactory2) XMLOutputFactory2.newInstance();
            xmlof.setProperty(XMLOutputFactory.IS_REPAIRING_NAMESPACES, Boolean.FALSE);
            xmlof.configureForSpeed();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println("Starting to parse " + filename);
        System.out.println("");
        long starttime = System.currentTimeMillis();
        try {
            XMLStreamWriter2 xmlo = (XMLStreamWriter2) xmlof.createXMLStreamWriter(new FileOutputStream(filename),
                    "utf-8");
            xmlo.writeStartDocument();
            xmlo.writeStartElement("myDoc");
            xmlo.writeStartElement("myelement");
            xmlo.writeStartElement("bigelement");
            xmlo.writeCharacters("abcdefghijklmnopqrstuvwxyzABC");
            xmlo.writeEndElement();
            xmlo.writeStartElement("withnested");
            xmlo.writeStartElement("nested1");
            xmlo.writeCharacters("abcdefghijklmnopqrstuvwxyzABC");
            xmlo.writeEndElement();
            xmlo.writeStartElement("nested2");
            xmlo.writeCharacters("abcdefghijklmnopqrstuvwxyzABC");
            xmlo.writeEndElement();
            xmlo.writeStartElement("nested3");
            xmlo.writeCharacters("abcdefghijklmnopqrstuvwxyzABC");
            xmlo.writeEndElement();
            xmlo.writeEndElement();
            xmlo.writeEndElement();
            xmlo.writeEndElement();
            xmlo.writeEndDocument();
            xmlo.flush();
            xmlo.close();
            xmlo.closeCompletely();
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
