package com.iisi.sd.main.gui.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.robert.study.utils.POIUtils;
import org.robert.study.utils.Utils;

import com.iisi.doc.process.DocWriter;
import com.iisi.doc.process.WordExtract;
import com.iisi.rl.table.DataTable;
import com.iisigroup.toolkits.service.ExcelManipulationServiceImpl;
import com.iisigroup.toolkits.service.RRRCTableServiceImpl;
import com.iisigroup.toolkits.service.SearchDataFrom64TableServiceImpl;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import javax.swing.JTextField;
public class V1PanelFunc17 extends JPanel {
	protected static Logger log = Logger.getLogger(V1PanelFunc17.class);

	/**
     * 
     */
	private static final long serialVersionUID = -1852906988920722628L;

	/**
	 * Create the panel.
	 */
	public V1PanelFunc17() {

		setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));

		JLabel tableSchemalabel = new JLabel("1.6-4 table(6-4-RLDS___.doc) :");
		add(tableSchemalabel, "2, 2");

		JButton tableSchemaSrcButton = new JButton("目錄選擇");
		final DirectionChooserActionListener docListener = new DirectionChooserActionListener(this) {
            void implementedMethod(File direction) {
            }
        };
        tableSchemaSrcButton.addActionListener(docListener);
        
        
		add(tableSchemaSrcButton, "4, 2");
		
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
		ExecActionListener execActionListener =  new ExecActionListener();
		execActionListener.setDestiDir(exportDirChooserListener);
		execActionListener.setSrcDir(docListener);
		
		JLabel outputShowLabel = exportDirChooserListener.getLabel();
		add(outputShowLabel, "6, 4");
		
		JLabel serachForAttributeLabel = new JLabel("3.搜尋的中文名稱");
		add(serachForAttributeLabel, "2, 8");
		
		textField = new JTextField();
		add(textField, "4, 8");
		textField.setColumns(10);
		

		JButton execButton = new JButton("執行作業");
		execButton.addActionListener( execActionListener);
		
		add(execButton, "2, 22, left, bottom");
	}
	 
	private  Pattern rldfPattern = Pattern.compile("6-4-RLDFS.*.doc");
	private JTextField textField;
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
			SimpleDateFormat sdf =new SimpleDateFormat("yyyy_MM_dd_hh_mm");
			if(srcDir.getFile()!=null && destiDir.getFile()!=null){
				final SearchDataFrom64TableServiceImpl serchService = new SearchDataFrom64TableServiceImpl();
				final WordExtract aWordExtract = new WordExtract();
				File[] srcFile = srcDir.getFile().listFiles();
				List<DataTable> srcList = new ArrayList<DataTable>();
				for(File srcFileUnit: srcFile){
					if(rldfPattern.matcher(srcFileUnit.getName()).matches()){
						DataTable dataTable = aWordExtract.convertTOUnit(srcFileUnit);
						srcList.add(dataTable);
					}
				}
				final String searchResult = serchService.getResultForSearch(textField.getText(), null, srcList);
				File outFile = new File(destiDir.getFile().getAbsolutePath() + "//" + "searchResult"+sdf.format(new Date())+".txt");
				String message = "匯出檔案為：" + outFile.getAbsolutePath();
				
				try {
					Utils.outputFile(outFile, searchResult);
				} catch (IOException e1) {
					handleExceptionErrors(e1);
					return;
				}

				JOptionPane.showMessageDialog(V1PanelFunc17.this, message, "匯出結果", JOptionPane.INFORMATION_MESSAGE);
			}else{
				JOptionPane.showMessageDialog(V1PanelFunc17.this, "請必須指定目錄.........", "操作錯誤", JOptionPane.ERROR_MESSAGE);
			}
			
		}
		
	}
	private void handleExceptionErrors(Exception e1) {
		StringBuffer sbf = new StringBuffer();
		sbf.append(e1.getClass()).append(" : ").append(e1.getMessage()).append("\n\r");
		e1.printStackTrace();
		Throwable cause = e1.getCause();
		handleStackTraceElement(cause, sbf);
		JOptionPane.showMessageDialog(V1PanelFunc17.this, sbf.toString(), "操作錯誤", JOptionPane.ERROR_MESSAGE);
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