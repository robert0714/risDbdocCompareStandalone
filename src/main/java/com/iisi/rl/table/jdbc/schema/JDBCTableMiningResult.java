package com.iisi.rl.table.jdbc.schema;

import java.io.Serializable;
import java.util.List;

import com.iisi.rl.table.script.ScriptTableInfo;

public class JDBCTableMiningResult implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2573240254400986506L;
	private List<ScriptTableInfo> initInputData;
	private List<JDBCTablseSchema> result;
	private List<String> exceptionList;
	public List<JDBCTablseSchema> getResult() {
		return result;
	}
	public void setResult(List<JDBCTablseSchema> result) {
		this.result = result;
	}
	public List<String> getExceptionList() {
		return exceptionList;
	}
	public void setExceptionList(List<String> exceptionList) {
		this.exceptionList = exceptionList;
	}
	public List<ScriptTableInfo> getInitInputData() {
		return initInputData;
	}
	public void setInitInputData(List<ScriptTableInfo> initInputData) {
		this.initInputData = initInputData;
	}
	
}
