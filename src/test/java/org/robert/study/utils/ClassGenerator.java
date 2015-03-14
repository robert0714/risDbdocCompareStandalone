 package org.robert.study.utils;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

import javax.swing.JTextArea;

public class ClassGenerator {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		JTextArea myArea = new JTextArea();
		Method[] methods = myArea.getClass().getDeclaredMethods();

		for(int i=0; i<methods.length; i++){
		     System.out.println("Method Name:: " + methods[i].getName() );
		     System.out.println("Return Type:: " + methods[i].getReturnType());
		     Type[] params = methods[i].getGenericParameterTypes();
		     for(int j=0; j<params.length; j++){
		          System.out.println("Parameter: " + params[j].toString());
		     }
		} 

	}

}
