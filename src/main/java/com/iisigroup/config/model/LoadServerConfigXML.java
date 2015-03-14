package com.iisigroup.config.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;

import org.codehaus.stax2.XMLInputFactory2;
import org.codehaus.stax2.XMLOutputFactory2;
import org.codehaus.stax2.XMLStreamReader2;
import org.codehaus.stax2.XMLStreamWriter2;
import org.robert.study.utils.DataSourceConfig;

import com.iisigroup.config.model.jaxb.ItemList;
import com.iisigroup.config.model.jaxb.ItemType;
import com.iisigroup.config.model.jaxb.ObjectFactory;

public class LoadServerConfigXML {
    private volatile static LoadServerConfigXML uniqueInstance;
    private static final String DIALOG_SETTING_FILE = System.getProperty("user.home") + "/" + "rober_toolkitsV2.xml";

    public static synchronized LoadServerConfigXML getInstance() {
        if (uniqueInstance == null) {
            synchronized (DataSourceConfig.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new LoadServerConfigXML();
                }
            }
        }
        return uniqueInstance;
    }

    private LoadServerConfigXML() {
    }

    public static void main(String[] args) throws Exception {
        // unmarshaller("z://ItemsSample.xml");
        // marshaller("z://test.xml");
        // unmarshaller("z://test.xml");
        // loadOrCreateConfig();
        ItemType configFile = new ItemType();
        configFile.setDataType("test");
        ItemList rlRegDataBatchitem = new LoadServerConfigXML().testMethod001();
        rlRegDataBatchitem.setKey("RL_REG_DATA_BATCH");
        configFile.getItemList().add(rlRegDataBatchitem);

        LoadServerConfigXML aLoadServerConfigXML = new LoadServerConfigXML();
        aLoadServerConfigXML.saveConfig(configFile);
    }

    public ItemType loadOrCreateConfig() {
        ItemType result = null;
        try {
            result = unmarshaller(DIALOG_SETTING_FILE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public void saveConfig(ItemType configFile) {
        try {
            marshaller(DIALOG_SETTING_FILE, configFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void marshaller(final String fileName, ItemType configFile) throws FileNotFoundException,
            XMLStreamException, JAXBException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        XMLStreamWriter2 xmlo = null;
        try {
            XMLOutputFactory2 xmlof = (XMLOutputFactory2) XMLOutputFactory2.newInstance();
            xmlof.setProperty(XMLOutputFactory.IS_REPAIRING_NAMESPACES, Boolean.FALSE);
            xmlof.configureForSpeed();
            xmlo = (XMLStreamWriter2) xmlof.createXMLStreamWriter(new FileOutputStream(fileName), "utf-8");
            JAXBContext context = JAXBContext.newInstance("com.iisigroup.config.model.jaxb");

            final Marshaller marshaller = context.createMarshaller();

            ObjectFactory factory = new ObjectFactory();
            JAXBElement<ItemType> aJAXBElement = factory.createConfig(configFile);

            marshaller.marshal(aJAXBElement, xmlo);

        } finally {
            if (xmlo != null) {
                try {
                    xmlo.flush();
                    xmlo.closeCompletely();
                } catch (XMLStreamException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public ItemType unmarshaller(final String fileName) throws FileNotFoundException, XMLStreamException, JAXBException {
        ItemType result = null;
        XMLStreamReader2 xmlr = null;
        File srcFile = new File(fileName);
        if (!srcFile.exists()) {
            result = new ItemType();
        } else {
            try {
                XMLInputFactory2 xmlif = (XMLInputFactory2) XMLInputFactory2.newInstance();
                xmlif.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, Boolean.FALSE);
                xmlif.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.FALSE);
                xmlif.setProperty(XMLInputFactory.IS_COALESCING, Boolean.FALSE);
                xmlif.configureForSpeed();

                xmlr = (XMLStreamReader2) xmlif.createXMLStreamReader(fileName, new FileInputStream(srcFile));
                JAXBContext context = JAXBContext.newInstance("com.iisigroup.config.model.jaxb");
                final Unmarshaller unmarshaller = context.createUnmarshaller();

                JAXBElement<ItemType> itemType = (JAXBElement<ItemType>) unmarshaller.unmarshal(xmlr);
                result = itemType.getValue();

                // System.out.println(result);
                // System.out.println(result.getDataType());
                // for (ItemList aItemList : result.getItemList()) {
                // System.out.println(aItemList.getKey());
                // }
                // List<ItemList> everyItemList = result.getItemList();
                // System.out.println(everyItemList.size());

            } finally {
                if (xmlr != null) {
                    try {
                        xmlr.closeCompletely();
                    } catch (XMLStreamException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return result;
    }

    public RLRegApplicationConfigModelBean getPseudoData001() {
        RLRegApplicationConfigModelBean result = new RLRegApplicationConfigModelBean();
        result.setChineseName("無戶籍者結離婚登記");
        result.setSymbolCode("0W100");
        result.setTableTypeR(false);
        result.setTableTypeS(true);
        result.setTableTypeR(true);
        result.setTableTypeW(false);
        Map<String, String> aTableTypeRefLocationMap = new HashMap<String, String>();
        aTableTypeRefLocationMap.put("0Z4", "GGGGHHHH");
        aTableTypeRefLocationMap.put("0Z5", "XXXXYYYY");
        result.setTableTypeRefLocationMap(aTableTypeRefLocationMap);
        return result;
    }

    public RLRegApplicationConfigModelBean getPseudoData002() {
        RLRegApplicationConfigModelBean result = new RLRegApplicationConfigModelBean();
        result.setChineseName("離婚登記");
        result.setSymbolCode("1240");
        result.setTableTypeR(true);
        result.setTableTypeS(true);
        result.setTableTypeR(true);
        result.setTableTypeW(true);
        Map<String, String> aTableTypeRefLocationMap = new HashMap<String, String>();
        aTableTypeRefLocationMap.put("004", "GGGGHHHH");
        aTableTypeRefLocationMap.put("commonRLDFLocation", "GGGzzGHHHH");
        aTableTypeRefLocationMap.put("commonXLDFLocation", "GGGGHHHzzH");
        aTableTypeRefLocationMap.put("uniqueApplicationData", "GGGGzzHHHH");
        aTableTypeRefLocationMap.put("commonRzzzLDFLocation", "GGzzGGHHHH");
        result.setTableTypeRefLocationMap(aTableTypeRefLocationMap);
        return result;
    }

    public ItemList testMethod001() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        RLRegApplicationConfigModelBean sample01 = getPseudoData001();
        RLRegApplicationConfigModelBean sample02 = getPseudoData002();
        List<RLRegApplicationConfigModelBean> srcList = new ArrayList<RLRegApplicationConfigModelBean>();
        srcList.add(sample01);
        srcList.add(sample02);
        return new RLRegApplicationConfigModelBeanConverter().generateItemList(srcList);
        // return convertItemList(srcList,
        // RLRegApplicationConfigModelBean.class, "RL_REG_DATA_BATCH");
    }

}
