package com.iisigroup.config.model;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;

import com.iisigroup.config.model.jaxb.Item;
import com.iisigroup.config.model.jaxb.ItemList;

public class RLRegApplicationConfigModelBeanConverter {
    private final String RL_REG_DATA_BATCH = "RL_REG_DATA_BATCH";

    public List<RLRegApplicationConfigModelBean> restore(final ItemList originalItemList)
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, Class<?>> propertyNameTypeMap = new HashMap<String, Class<?>>();
        final java.beans.PropertyDescriptor[] desc = PropertyUtils
                .getPropertyDescriptors(RLRegApplicationConfigModelBean.class);
        for (java.beans.PropertyDescriptor theDesc : desc) {
            Class<?> type = theDesc.getPropertyType();
            String descPropertyName = theDesc.getName();
            propertyNameTypeMap.put(descPropertyName, type);
        }
        List<RLRegApplicationConfigModelBean> result = new ArrayList<RLRegApplicationConfigModelBean>();
        if (RL_REG_DATA_BATCH.equalsIgnoreCase(originalItemList.getKey())) {
            for (ItemList aItemList : originalItemList.getItemList()) {
                RLRegApplicationConfigModelBean modelBean = new RLRegApplicationConfigModelBean();
                List<Item> items = aItemList.getItem();
                for (Item aItem : items) {
                    String key = aItem.getKey();
                    String value = aItem.getValue();
                    Class<?> type = propertyNameTypeMap.get(key);

                    if (type == null) {
                        Map<String, String> valueMap = (Map<String, String>) PropertyUtils.getProperty(modelBean,
                                "tableTypeRefLocationMap");

                        valueMap.put(key, value);
                        PropertyUtils.setProperty(modelBean, "tableTypeRefLocationMap", valueMap);
                    } else if (type.equals(String.class)) {
                        PropertyUtils.setProperty(modelBean, key, value);
                    } else if (type.getName().equals("boolean")) {
                        // System.out.println("boolean attributes");
                        Boolean valueBoolean = Boolean.valueOf(value.toLowerCase());
                        PropertyUtils.setProperty(modelBean, key, valueBoolean);
                    }
                }
                result.add(modelBean);
            }
        }
        return result;
    }

    public ItemList generateItemList(final List<RLRegApplicationConfigModelBean> originalItemList)
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        ItemList result = new ItemList();
        result.setKey(RL_REG_DATA_BATCH);
        List<ItemList> aList = convertItemList(originalItemList, RLRegApplicationConfigModelBean.class,
                RL_REG_DATA_BATCH);
        result.getItemList().addAll(aList);
        return result;
    }

    private <T> List<ItemList> convertItemList(final List<T> srcList, Class<T> aClass, String keyName)
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        String keyName2 = "";
        List<ItemList> outerItemLists = new ArrayList<ItemList>();
        final java.beans.PropertyDescriptor[] desc = PropertyUtils.getPropertyDescriptors(aClass);
        for (int i = 0; i < srcList.size(); ++i) {
            Object beanUnit = srcList.get(i);
            ItemList outerItemList = new ItemList();
            List<Item> outerItems = new ArrayList<Item>();
            List<ItemList> innerItemList = new ArrayList<ItemList>();
            for (java.beans.PropertyDescriptor theDesc : desc) {
                Class type = theDesc.getPropertyType();
                String descPropertyName = theDesc.getName();
                if (type.equals(String.class)) {
                    String value = (String) PropertyUtils.getProperty(beanUnit, descPropertyName);
                    outerItems.add(genItemValue(descPropertyName, value));
                    if ("symbolCode".equals(descPropertyName)) {
                        keyName2 = value;
                    } else {
                        keyName2 = keyName;
                    }
                } else if (type.equals(List.class)) {
                    List valueList = (List) PropertyUtils.getProperty(beanUnit, descPropertyName);
                    if (valueList != null && valueList.size() > 0) {
                        for (Object oo : valueList) {
                            List<ItemList> ainnerItemList = convertItemList(valueList, oo.getClass(),
                                    convertFirstCharToHighCase(descPropertyName));
                            innerItemList.addAll(ainnerItemList);
                            break;
                        }
                    }

                } else if (type.equals(Map.class)) {
                    Map<String, String> characteristics = (Map<String, String>) PropertyUtils.getProperty(beanUnit,
                            descPropertyName);
                    for (String key : characteristics.keySet()) {
                        String value = characteristics.get(key);
                        outerItems.add(genItemValue(key, value));

                    }

                } else if (type.getName().equals("boolean")) {

                    Boolean value = (Boolean) PropertyUtils.getProperty(beanUnit, descPropertyName);
                    outerItems.add(genItemValue(descPropertyName, value.toString()));
                }
            }
            outerItemList.getItem().addAll(outerItems);
            outerItemList.setKey(keyName2);
            if (innerItemList != null) {
                outerItemList.getItemList().addAll(innerItemList);
            }
            outerItemLists.add(outerItemList);

            srcList.remove(i);
            --i;
        }
        return outerItemLists;
    }

    private Item genItemValue(String key, String value) {
        Item item = new Item();
        item.setKey(key);
        if (value == null) {
            value = "";
        }
        item.setValue(value);
        return item;
    }

    private String convertFirstCharToHighCase(final String srcString) {
        String result = null;
        if (srcString != null && srcString.trim().length() > 0) {
            char[] charArray = srcString.trim().toCharArray();
            String firstChar = new String(new char[] { charArray[0] }).toUpperCase();
            char[] changCharArray = firstChar.toCharArray();
            charArray[0] = changCharArray[0];
            result = new String(charArray);
        }
        return result;
    }
}
