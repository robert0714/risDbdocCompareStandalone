package org.robert.study.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.commons.beanutils.PropertyUtils;

import com.iisi.rl.table.DataColumnInfo;

public class DBTableAnalysisUtils {
    public static final Set<Integer> getNumerCharacterUnicodeSymbolCode() {
        Set<Integer> result = new HashSet<Integer>();
        for (int i = 97; i < 123; i++) {
            result.add(Integer.valueOf(i));
        }
        for (int i = 65; i < 90; i++) {
            result.add(Integer.valueOf(i));
        }
        return result;
    }

    public static String validateTableName(final String srcTablerName) {
        String result = null;
        int firstChar = (int) srcTablerName.toCharArray()[0];

        if (getNumerCharacterUnicodeSymbolCode().contains(Integer.valueOf(firstChar))) {
            result = srcTablerName;
        } else {
            result = srcTablerName.substring(1);
        }
        return result;
    }

    public static String convertStringFromLowerCase2JavaConventionNamingRuleClassName(final String originalName) {
        String result = null;
        result = "tw.gov.moi.domain." + originalName.substring(0, 1) + originalName.trim().toLowerCase().substring(1)
                + "Type";
        return result;
    }

    public static String convertStringFromTableColumnName2JavaConventionNamingRulePropertyName(final String originalName) {
        String result = null;
        StringTokenizer str = new StringTokenizer(originalName, "_");
        StringBuffer sbf = new StringBuffer();
        while (str.hasMoreElements()) {
            String tmp = ((String) str.nextElement()).toLowerCase();
            sbf.append(tmp.substring(0, 1).toUpperCase()).append(tmp.toLowerCase().substring(1));
        }
        String tmpString = sbf.toString();
        try {
            result = tmpString.substring(0, 1).toLowerCase() + tmpString.substring(1);
        } catch (Exception e) {
            System.out.println("發生錯誤.........." + originalName + "( " + tmpString + "  )");

        }
        return result;
    }

    public static List<String> getPropertiesFromAttributes(final List<DataColumnInfo> aAttributes) {
        List<String> result = new ArrayList<String>();
        for (DataColumnInfo aAttribute : aAttributes) {
            // System.out.println("columnNamer: "+aAttribute.getColumnName());
            // System.out.println("java attribute: "+convertStringFromTableColumnName2JavaConventionNamingRulePropertyName(aAttribute.getColumnName()));
            result.add(convertStringFromTableColumnName2JavaConventionNamingRulePropertyName(aAttribute.getColumnName()));
        }
        return result;
    }

    public static Map<String, String> getPropertiesFromAttributesMap(final List<DataColumnInfo> aAttributes) {
        Map<String, String> result = new HashMap<String, String>();
        for (DataColumnInfo aAttribute : aAttributes) {
            result.put(
                    convertStringFromTableColumnName2JavaConventionNamingRulePropertyName(aAttribute.getColumnName()),
                    aAttribute.getColumnName());
        }
        return result;
    }

    private <T> Set<String> getProperties(final Class<T> aClass) throws IllegalAccessException,
            InvocationTargetException, NoSuchMethodException {
        Set<String> result = new HashSet<String>();
        final java.beans.PropertyDescriptor[] desc = PropertyUtils.getPropertyDescriptors(aClass);
        for (java.beans.PropertyDescriptor theDesc : desc) {
            String descPropertyName = theDesc.getName();
            result.add(descPropertyName);
        }
        return result;
    }

    private <T1, T2> Set<String> comparePropertiesAndShowFormerLackProperties(Class<T1> k1, Class<T2> k2)
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        System.out.println("k1: " + k1.getSimpleName());
        System.out.println("k2: " + k2.getSimpleName());
        Set<String> result = new HashSet<String>();
        Set<String> former = getProperties(k1);
        Set<String> later = getProperties(k2);// later collection is standard...
        for (String sample : former) {
            if (!later.contains(sample)) {
                result.add(sample);
            }
        }
        return result;
    }

    public static <T> Set<String> getPropertiesSet(final Class<T> aClass) {
        final Set<String> result = new HashSet<String>();
        final java.beans.PropertyDescriptor[] desc = PropertyUtils.getPropertyDescriptors(aClass);
        for (java.beans.PropertyDescriptor theDesc : desc) {
            // Class<?> type = theDesc.getPropertyType();
            String descPropertyName = theDesc.getName();
            // System.out.println("property name: " + descPropertyName);
            // System.out.println("property type name: " + type.getName());
            // System.out.println("===========================");
            result.add(descPropertyName);
        }
        return result;
    }

    private <T> void displayProperties(final Class<T> aClass) throws IllegalAccessException, InvocationTargetException,
            NoSuchMethodException {
        final java.beans.PropertyDescriptor[] desc = PropertyUtils.getPropertyDescriptors(aClass);
        for (java.beans.PropertyDescriptor theDesc : desc) {
            Class<?> type = theDesc.getPropertyType();
            String descPropertyName = theDesc.getName();
            System.out.println("property name: " + descPropertyName);
            System.out.println("property type name: " + type.getName());
            System.out.println("===========================");
        }
    }

    private <T> void displayPropertiesValues(T javaBean) throws IllegalAccessException, InvocationTargetException,
            NoSuchMethodException {
        final java.beans.PropertyDescriptor[] desc = PropertyUtils.getPropertyDescriptors(javaBean.getClass());
        for (java.beans.PropertyDescriptor theDesc : desc) {
            Class<?> type = theDesc.getPropertyType();
            String descPropertyName = theDesc.getName();

            System.out.println("property name: " + descPropertyName);
            System.out.println("property type name: " + type.getName());

            if (type.equals(String.class)) {
                String value = (String) PropertyUtils.getProperty(javaBean, descPropertyName);
                System.out.println("property value: " + value);
            } else {
                System.out.println("property value: " + PropertyUtils.getProperty(javaBean, descPropertyName));
            }
            System.out.println("===========================");
        }
    }

    public static <T> T deserialization(InputStream inputStream) {
        T result = null;
        ObjectInputStream ois = null;

        try {
            ois = new ObjectInputStream(inputStream);
            // int i = ois.readInt();
            // String today = (String) ois.readObject();
            result = (T) ois.readObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return result;
    }

    private <T1, T2> void printReportAfterComparing(Class<T1> k1, Class<T2> k2) {
        Set<String> analyzingResult01 = null;
        Set<String> analyzingResult02 = null;
        try {
            analyzingResult01 = comparePropertiesAndShowFormerLackProperties(k1, k2);
            analyzingResult02 = comparePropertiesAndShowFormerLackProperties(k2, k1);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        System.out.println(k1.getName() + " size: " + analyzingResult01.size());

        if (analyzingResult01 != null) {
            System.out.println(k1.getName() + " has additional properties: \n");
            for (String property : analyzingResult01) {
                // System.out.println("properties: " + property);
                System.out.println(property);
            }
        }
        System.out.println(k2.getName() + " size: " + analyzingResult02.size());
        if (analyzingResult02 != null) {
            System.out.println(k2.getName() + " has additional properties: \n");
            for (String property : analyzingResult02) {
                // System.out.println("properties: " + property);
                System.out.println(property);
            }
        }
    }
}
