package org.robert.study.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import tw.gov.moi.ae.checker.annotation.FieldName;
import tw.gov.moi.rl.component.dto.HouseholdOperation;

public class ClassGenerate {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		process(TestClass.class);
		
	}
	public static void process(Class clazz){
		Method[] methodArray = clazz.getMethods();
		Field[] aFields = clazz.getDeclaredFields();
		Class[] interfaces = clazz.getInterfaces();
		System.out.println("package "+clazz.getPackage().getName()+";");
		System.out.println("import tw.gov.moi.ae.checker.annotation.FieldName;");
		//import tw.gov.moi.ae.checker.annotation.FieldName;
		System.out.println("@XmlAccessorType(XmlAccessType.FIELD)");
		System.out.print("@XmlType(name = \""+clazz.getSimpleName()+"\",propOrder = {");
		
		int j =0;
		for(Field aField:aFields){
			if(aFields.length==(j+1)){
				System.out.print("\""+aField.getName()+"\"");
			}else {
				System.out.print("\""+aField.getName()+"\",");
			}
			
		}
		System.out.println("})");
		//@XmlRootElement(name = "Rl08210DTO")
		System.out.println("@XmlRootElement(name = \""+clazz.getSimpleName()+"\" )");
		System.out.print("public class "+clazz.getSimpleName()+" ");
		if(interfaces.length>0){
			System.out.print("implements ");
		}
		int i=0;
		for(Class interfaceClass : interfaces){
			if(interfaces.length==(i+1)){
				System.out.println(interfaceClass.getCanonicalName()+" {");
			}else {
				System.out.println(interfaceClass.getCanonicalName()+",");
			}
			
		}
		if(interfaces.length==0){
			System.out.println("{");
		}
		System.out.println("");
		for(Field aField:aFields){			
			//@XmlElement(name = "PrintHousemap")
			if(aField.getAnnotation(FieldName.class)!=null&&aField.getAnnotation(FieldName.class).value()!=null){
				System.out.println("@FieldName(\""+aField.getAnnotation(FieldName.class).value()+"\")");
			}else{
				System.out.println("@FieldName(\"請輸入中文名稱\")");
			}
			
			
			System.out.println("@XmlElement(name = \""+aField.getName()+"\"  required=\"true\" )");
			System.out.print("private ");
			System.out.print(aField.getType().getCanonicalName());
			System.out.print(" ");
			System.out.print(aField.getName());
			System.out.println(" ;\n\r");
		}
		System.out.println("}");
		
	}
}
