package com.iisigroup.toolkits.service;

import java.io.File;

public class TableScriptGenaratorServiceImplTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TableScriptGenaratorServiceImpl test =new TableScriptGenaratorServiceImpl();
		test.readTemplateDataTable(new File("/home/weblogic/Documents/SD製作文件/2012_0628_現無戶籍者結離婚登記W100/產出/S0Z5/各項XLDFS系列獨有欄位.xls"));
	}

}
