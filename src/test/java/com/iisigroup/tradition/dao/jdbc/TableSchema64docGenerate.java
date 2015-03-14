package com.iisigroup.tradition.dao.jdbc;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.robert.study.service.TableScriptGenaratorService;
import org.robert.study.utils.POIUtils;
import org.robert.study.utils.Utils;

import com.iisi.doc.process.DocWriter;
import com.iisi.rl.table.DataTable;
import com.iisi.rl.table.jdbc.schema.JDBCTablseSchema;
import com.iisigroup.toolkits.service.TableScriptGenaratorServiceImpl;

public class TableSchema64docGenerate {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		processRC();
		
	}
	public static void processRC(){
		final TableScriptGenaratorService service =new TableScriptGenaratorServiceImpl();
		final String rcSerialFile = "/home/weblogic/Desktop/RL_jdbclist";
		final File filter =new File("/home/weblogic/Desktop/list/RL_lacking_doc.txt");
		final 	List<String> lineList = Utils.inputReadFile(filter);
		final List<JDBCTablseSchema> jdbclist = Utils.deserialization(new File(rcSerialFile));
		if(CollectionUtils.isNotEmpty(jdbclist)){
			List<JDBCTablseSchema> sampleList = new ArrayList<JDBCTablseSchema>();
			
			for(int i=0 ;i<lineList.size() ;++i){
				String line  = lineList.get(i);
				for(int j=0 ; j<jdbclist.size() ;++j){
					JDBCTablseSchema aJDBCTablseSchema = 	jdbclist.get(j);
					if(StringUtils.equalsIgnoreCase(StringUtils.trim(line), aJDBCTablseSchema.getTableName())){
						sampleList.add(aJDBCTablseSchema);
						
						jdbclist.remove(j);
						lineList.remove(i);
						--i;
						--j;
					}
				}
			}
			
			
			List<DataTable> tableList = service.convertDataTableListfromJDBCTablseSchemaList(sampleList);
			System.out.println(tableList.size());
			try {
//				for (DataTable aDataTable : tableList) {
//					service.validation(aDataTable);
//				}
				;
				DocWriter aDocWriter = new DocWriter();
				for (DataTable aDataTable : tableList) {
					XWPFDocument doc = aDocWriter.createNewWord(aDataTable);
					POIUtils.writePOIXMLDocumentPartOut(new File("/home/weblogic/Desktop/createDoc/RL/" + aDataTable.getFileName()), doc);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
