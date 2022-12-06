package com.iisigroup.toolkits.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.math.NumberUtils;

public class testClass {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SimpleDateFormat sdf =new SimpleDateFormat("HH");
		int hour = NumberUtils.toInt(sdf.format(new Date()))+2;
		System.out.println(hour);
	}

}
