package com.iisi.sd.main.gui.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.robert.study.service.TableScriptGenaratorService;
import org.robert.study.utils.POIUtils;
import org.robert.study.utils.Utils;

import com.iisi.doc.process.DocWriter;
import com.iisi.doc.process.WordExtract;
import com.iisi.rl.table.DataTable;
import com.iisigroup.toolkits.service.ExcelManipulationServiceImpl;
import com.iisigroup.toolkits.service.TableScriptGenaratorServiceImpl;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class V1PanelFunc09 extends JPanel {
	protected static Logger log = LoggerFactory.getLogger(V1PanelFunc09.class);

	/**
     * 
     */
	private static final long serialVersionUID = -1852906988920722628L;

	/**
	 * Create the panel.
	 */
	public V1PanelFunc09() {

		setLayout(new FormLayout(new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"), },
				new RowSpec[] { FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("default:grow"),
						FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
						RowSpec.decode("default:grow"), FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, }));

		JLabel tableSchemalabel = new JLabel("1.6-4 table schema :");
		add(tableSchemalabel, "2, 2");

		JButton tableSchemaSrcButton = new JButton("目錄選擇");
		final DirectionChooserActionListener docListener = new DirectionChooserActionListener(this) {
            void implementedMethod(File direction) {
            }
        };
        tableSchemaSrcButton.addActionListener(docListener);
        
        
		add(tableSchemaSrcButton, "4, 2, left, top");
		
		JLabel tableSchemaShowlabel = docListener.getLabel();
		add(tableSchemaShowlabel, "6, 2");
		
		JLabel outputFolderLabel = new JLabel("2.輸出目錄");
		add(outputFolderLabel, "2, 4");
		
		JButton exportDirecButton = new JButton("目錄選擇");
		final DirectionChooserActionListener exportDirChooserListener = new DirectionChooserActionListener(this) {
            void implementedMethod(File direction) {
            }
        };
        exportDirecButton.addActionListener(exportDirChooserListener);
		add(exportDirecButton, "4, 4");
		

		JButton execButton = new JButton("執行作業");
		ExecActionListener execActionListener =  new ExecActionListener();
		execActionListener.setDestiDir(exportDirChooserListener);
		execActionListener.setSrcDir(docListener);
		execButton.addActionListener( execActionListener);
		
		JLabel outputShowLabel = exportDirChooserListener.getLabel();
		add(outputShowLabel, "6, 4");
		
		add(execButton, "2, 24, left, bottom");
	}
	 
	private  Pattern rldfPattern = Pattern.compile("6-4-RLDFS\\d{1}[A|B|C|D|Z|\\d][A|B|C|D|\\d]\\.doc");
	private class ExecActionListener implements ActionListener{		
		private DirectionChooserActionListener srcDir;
		private DirectionChooserActionListener destiDir;
		
		public void setSrcDir(DirectionChooserActionListener srcDir) {
			this.srcDir = srcDir;
		}

		public void setDestiDir(DirectionChooserActionListener destiDir) {
			this.destiDir = destiDir;
		}

		public void actionPerformed(ActionEvent e) {
			if(srcDir.getFile()!=null && destiDir.getFile()!=null){
				String message = "匯出檔案目錄為：" + destiDir.getFile().getAbsolutePath();
				TableScriptGenaratorService service =new TableScriptGenaratorServiceImpl();
				final ExcelManipulationServiceImpl service2 = new ExcelManipulationServiceImpl();
				final WordExtract aWordExtract = new WordExtract();
				final  DocWriter aDocWriter = new DocWriter();
				File[] srcFile = srcDir.getFile().listFiles();
				List<DataTable> dableList = new ArrayList<DataTable>();
				for(File srcFileUnit: srcFile){
					if(rldfPattern.matcher(srcFileUnit.getName()).matches()){						
						DataTable dataTable = aWordExtract.convertTOUnit(srcFileUnit);
						dableList.add(service.RLDFWXXXFromRLDFSXXX(dataTable));
						;
					}
				}
				
			
				
				Map<String, String> scriptFinalDataMap = service2.generateFinalScript(dableList);
				 try {
	                    for (DataTable aDataTable : dableList) {
	                        XWPFDocument doc = aDocWriter.createNewWord(aDataTable);
	                        POIUtils.writePOIXMLDocumentPartOut(
	                                new File(destiDir.getFile().getAbsolutePath() + "//" + aDataTable.getFileName()), doc);
	                    }
	                    
	                    for (String fileName : scriptFinalDataMap.keySet()) {
	                        String content = scriptFinalDataMap.get(fileName);
	                        File aFile = new File(destiDir.getFile().getAbsolutePath() + "//" + fileName);
	                        if (content != null) {
	                            Utils.outputFile(aFile, content);
	                        } else {
	                        	log.info("file name: " + fileName);
	                        }

	                    }
	                } catch (IOException e1) {
	                    handleExceptionErrors(e1);
	                    return;
	                }

	                JOptionPane.showMessageDialog(V1PanelFunc09.this, message, "匯出結果", JOptionPane.INFORMATION_MESSAGE);
			}else{
				JOptionPane.showMessageDialog(V1PanelFunc09.this, "請必須指定目錄.........", "操作錯誤", JOptionPane.ERROR_MESSAGE);
			}
			
		}
		
	}
	private void handleExceptionErrors(Exception e1) {
		StringBuffer sbf = new StringBuffer();
		sbf.append(e1.getClass()).append(" : ").append(e1.getMessage()).append("\n\r");
		e1.printStackTrace();
		Throwable cause = e1.getCause();
		handleStackTraceElement(cause, sbf);
		JOptionPane.showMessageDialog(V1PanelFunc09.this, sbf.toString(), "操作錯誤", JOptionPane.ERROR_MESSAGE);
	}

	private Throwable handleStackTraceElement(final Throwable cause, final StringBuffer sbf) {
		if (cause != null) {
			sbf.append(cause.getClass()).append(" : ").append(cause.getMessage()).append("\n\r");
			return handleStackTraceElement(cause.getCause(), sbf);
		} else {
			return null;
		}
	}
	
}