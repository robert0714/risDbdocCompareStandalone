package com.iisi.sd.main.gui.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.robert.study.service.TableScriptGenaratorService;
import org.robert.study.utils.POIUtils;
import org.robert.study.utils.Utils;

import com.iisi.doc.process.DocWriter;
import com.iisi.doc.process.WordExtract;
import com.iisi.rl.table.DataTable;
import com.iisi.sd.main.gui.DirectionChooserInterface;
import com.iisigroup.toolkits.service.ExcelManipulationServiceImpl;
import com.iisigroup.toolkits.service.TableScriptGenaratorServiceImpl;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class V1PanelFunc02 extends JPanel {
	/**
     * 
     */
	private static final long serialVersionUID = -8972764315428124143L;
	private JTextField textField;
	protected static Logger log = LoggerFactory.getLogger(V1PanelFunc02.class);
	private JTextField chtTable;

	/**
	 * Create the panel.
	 */
	public V1PanelFunc02() {
		setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("left:max(136dlu;default):grow"),},
			new RowSpec[] {
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
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));

		JLabel applySymbolCodeText = new JLabel("1.申請書類別代碼：");
		add(applySymbolCodeText, "2, 2, left, default");

		textField = new JTextField();
		add(textField, "4, 2, left, default");
		textField.setColumns(10);

		JLabel blank = new JLabel("...blank");
		add(blank, "6, 2");
		FileChooserActionListener fileListener = new FileChooserActionListener(false);

//		JLabel exportPathNameLabel = new JLabel("");
		JLabel exportPathNameLabel = fileListener.getLabel();
		add(exportPathNameLabel, "6, 6");
		ExportImplementedFileActionListener exportImplementedFileActionListener = new ExportImplementedFileActionListener(V1PanelFunc02.this);
		exportImplementedFileActionListener.setFileChooserInterface(fileListener);

		JLabel chtTableNameLabel = new JLabel("2.中文名稱：");
		add(chtTableNameLabel, "2, 4, left, default");

		ExportTemplateFileActionListener exportTemplateFileActionListener = new ExportTemplateFileActionListener(V1PanelFunc02.this);

		chtTable = exportImplementedFileActionListener.getChtName();

		add(chtTable, "4, 4, fill, default");
		chtTable.setColumns(10);

		JLabel fileTemplateFormat = new JLabel("3.檔案匯入格式輸出");
		add(fileTemplateFormat, "2, 6");

		JButton templateExportButton = new JButton("export");
		templateExportButton.addActionListener(exportTemplateFileActionListener);
		add(templateExportButton, "4, 6");

		JLabel dataImportPathLabel = new JLabel("4.資料匯入檔案路徑：");
		add(dataImportPathLabel, "2, 8");

		JButton selectButton = new JButton("選擇");
		selectButton.addActionListener(fileListener);
		add(selectButton, "4, 8");

		JLabel dataImportPathShowLabel = fileListener.getLabel();
//		JLabel dataImportPathShowLabel = new JLabel("...blank");
		add(dataImportPathShowLabel, "6, 8");
		
		JLabel rldf002mLabel = new JLabel("5.RLDF002M:");
		add(rldf002mLabel, "2, 10");
		
		FileChooserActionListener rldf002mFileListener = new FileChooserActionListener(true);
		JButton rldf002mButton =  new JButton("選擇");
		rldf002mButton.addActionListener(rldf002mFileListener);
		add(rldf002mButton, "4, 10");
		
//		JLabel rldf002mShowLabel = new JLabel("New label");
		JLabel rldf002mShowLabel = rldf002mFileListener.getLabel();
		add(rldf002mShowLabel, "6, 10");
		
		JLabel rldf005mLabel = new JLabel("6.RLDF005M:");
		add(rldf005mLabel, "2, 12");
		
		FileChooserActionListener rldf005mFileListener = new FileChooserActionListener(true);
		JButton rldf005mButton =  new JButton("選擇");
		rldf005mButton.addActionListener(rldf005mFileListener);
		add(rldf005mButton, "4, 12");
		
//		JLabel rldf005mShowLabel= new JLabel("New label");
		JLabel rldf005mShowLabel= rldf005mFileListener.getLabel();
		add(rldf005mShowLabel, "6, 12");
		
		JLabel rldf002hLabel = new JLabel("7.RLDF002H:");
		add(rldf002hLabel, "2, 14");
		
		FileChooserActionListener rldf002hFileListener = new FileChooserActionListener(true);
		JButton rldf002hButton =  new JButton("選擇");
		rldf002hButton.addActionListener(rldf002hFileListener);
		add(rldf002hButton, "4, 14");
		
//		JLabel rldf002hShowLabel = new JLabel("New label");
		JLabel rldf002hShowLabel = rldf002hFileListener.getLabel();
		add(rldf002hShowLabel, "6, 14");
				
		JLabel rldf005hLabel = new JLabel("8.RLDF005H:");
		add(rldf005hLabel, "2, 16");
		
		FileChooserActionListener rldf005hFileListener = new FileChooserActionListener(true);
		JButton rldf005hButton =  new JButton("選擇");
		rldf005hButton.addActionListener(rldf005hFileListener);
		add(rldf005hButton, "4, 16");
		
//		JLabel rldf005hshowLabel = new JLabel("New label");
		JLabel rldf005hshowLabel = rldf005hFileListener.getLabel();
		add(rldf005hshowLabel, "6, 16");

		JLabel tableDataLabel = new JLabel("產出記事欄位化表格");
		add(tableDataLabel, "2, 18");

		JButton executeButton = new JButton("執行");
		exportImplementedFileActionListener.setFileChooserInterface(fileListener);
		executeButton.addActionListener(exportImplementedFileActionListener);
		add(executeButton, "4, 18");
//		String customMessage = "由於有非常多priority高於眼前程式的工作，像是很多PDS沒有完成、大簿書政流程的 討論...etc，\n\r所以\"自動欄位化表格產生器\"要等到其他高priority的工作量降低才進行製作";
//		JOptionPane.showMessageDialog(V1PanelFunc2.this, customMessage, "是這樣的......", JOptionPane.WARNING_MESSAGE);
		
		exportImplementedFileActionListener.setRldf002mFileListener(rldf002mFileListener);
		exportImplementedFileActionListener.setRldf005mFileListener(rldf005mFileListener);
		exportImplementedFileActionListener.setRldf002hFileListener(rldf002hFileListener);
		exportImplementedFileActionListener.setRldf005hFileListener(rldf005hFileListener);
	}

	class ExportImplementedFileActionListener extends DirectionChooserActionListener {
		private FileChooserActionListener fileChooserInterface;
		private FileChooserActionListener rldf002mFileListener;
		private FileChooserActionListener rldf005mFileListener;
		private FileChooserActionListener rldf002hFileListener;
		private FileChooserActionListener rldf005hFileListener;
		private JTextField chtName = new JTextField();

		public ExportImplementedFileActionListener(JComponent component) {
			super(component);
		}

		public void setFileChooserInterface(FileChooserActionListener dir) {
			this.fileChooserInterface = dir;
		}

		public void setRldf002mFileListener(FileChooserActionListener rldf002mFileListener) {
			this.rldf002mFileListener = rldf002mFileListener;
		}

		public void setRldf005mFileListener(FileChooserActionListener rldf005mFileListener) {
			this.rldf005mFileListener = rldf005mFileListener;
		}

		public void setRldf002hFileListener(FileChooserActionListener rldf002hFileListener) {
			this.rldf002hFileListener = rldf002hFileListener;
		}

		public void setRldf005hFileListener(FileChooserActionListener rldf005hFileListener) {
			this.rldf005hFileListener = rldf005hFileListener;
		}

		public JTextField getChtName() {
			return chtName;
		}

		void implementedMethod(File direction) {			
			
			String symol = textField.getText();
			if (this.fileChooserInterface == null || this.fileChooserInterface.getFile() == null) {
				String message = "資料匯入檔案必輸入";
				JOptionPane.showMessageDialog(V1PanelFunc02.this, message, "操作錯誤", JOptionPane.ERROR_MESSAGE);
				return;
			}
			if (this.rldf002mFileListener == null || this.rldf002mFileListener.getFile() == null) {
				String message = "6-4 RLDF002M.doc 檔案必輸入";
				JOptionPane.showMessageDialog(V1PanelFunc02.this, message, "操作錯誤", JOptionPane.ERROR_MESSAGE);
				return;
			}
			if (this.rldf005mFileListener == null || this.rldf005mFileListener.getFile() == null) {
				String message = "6-4 RLDF005M.doc 檔案必輸入";
				JOptionPane.showMessageDialog(V1PanelFunc02.this, message, "操作錯誤", JOptionPane.ERROR_MESSAGE);
				return;
			}
			if (this.rldf002hFileListener == null || this.rldf002hFileListener.getFile() == null) {
				String message = "6-4 RLDF002H.doc 檔案必輸入";
				JOptionPane.showMessageDialog(V1PanelFunc02.this, message, "操作錯誤", JOptionPane.ERROR_MESSAGE);
				return;
			}
			if (this.rldf005hFileListener == null || this.rldf005hFileListener.getFile() == null) {
				String message = "6-4 RLDF005H.doc 檔案必輸入";
				JOptionPane.showMessageDialog(V1PanelFunc02.this, message, "操作錯誤", JOptionPane.ERROR_MESSAGE);
				return;
			}
			if (StringUtils.isNotEmpty(symol)) {
				String message = "匯出檔案目錄為：" + direction.getAbsolutePath();

				ExcelManipulationServiceImpl service = new ExcelManipulationServiceImpl();
				final File templateFile = this.fileChooserInterface.getFile();
				final WordExtract aWordExtract = new WordExtract();
				try {
					DataTable rldf002mDataTable = aWordExtract.convertTOUnit(this.rldf002mFileListener.getFile().getAbsolutePath());
					DataTable rldf005mDataTable = aWordExtract.convertTOUnit(this.rldf005mFileListener.getFile().getAbsolutePath());
					DataTable rldf002hDataTable = aWordExtract.convertTOUnit(this.rldf002hFileListener.getFile().getAbsolutePath());
					DataTable rldf005hDataTable = aWordExtract.convertTOUnit(this.rldf005hFileListener.getFile().getAbsolutePath());
					
					DataTable templateDataTable = service.readTemplateDataTable(templateFile);
					
					List<DataTable> docList = service.generate64TableDocFromTemplate(templateDataTable, symol, chtName.getText(),rldf002mDataTable,rldf005mDataTable,rldf002hDataTable,rldf005hDataTable);
					
					for (DataTable aDataTable : docList) {
                        service.validation(aDataTable);
                        
                    }
					
					
					Map<String, String> scriptInitDataMap = service.generateInitialScript(docList);
					Map<String, String> scriptFinalDataMap = service.generateFinalScript(docList);

					DocWriter aDocWriter = new DocWriter();

					// final File dir = direction;

					for (DataTable aDataTable : docList) {
						XWPFDocument doc = aDocWriter.createNewWord(aDataTable);
						POIUtils.writePOIXMLDocumentPartOut(new File(direction.getAbsolutePath() + "//" + aDataTable.getFileName()), doc);
					}
					for (String fileName : scriptInitDataMap.keySet()) {
						String content = scriptInitDataMap.get(fileName);
						File aFile = new File(direction.getAbsolutePath() + "//" + fileName);
						Utils.outputFile(aFile, content);
					}
					for (String fileName : scriptFinalDataMap.keySet()) {
						String content = scriptFinalDataMap.get(fileName);
						File aFile = new File(direction.getAbsolutePath() + "//" + fileName);
						if (content != null) {
							Utils.outputFile(aFile, content);
						} else {
							System.out.println("file name: " + fileName);
						}

					}
				} catch (Exception e) {
					handleExceptionErrors(e);
					return;
				}

				JOptionPane.showMessageDialog(V1PanelFunc02.this, message, "匯出結果", JOptionPane.INFORMATION_MESSAGE);

			} else {
				String message = "申請書類別代碼請務必輸入";
				JOptionPane.showMessageDialog(V1PanelFunc02.this, message, "操作錯誤", JOptionPane.ERROR_MESSAGE);
			}

		}
	}

	class ExportTemplateFileActionListener extends DirectionChooserActionListener {
		public ExportTemplateFileActionListener(JComponent component) {
			super(component);
		}

		void implementedMethod(File direction) {
			String symol = textField.getText();
			if (StringUtils.isNotEmpty(symol)) {
				File export = new File(direction.getAbsolutePath() + "//" + symol + "template.xls");
				String message = "匯出檔案為：" + export.getAbsolutePath();
				ExcelManipulationServiceImpl main = new ExcelManipulationServiceImpl();
				Workbook aWorkbook = main.generateTemplate();
				FileOutputStream fileOut = null;
				try {
					fileOut = new FileOutputStream(export);
					aWorkbook.write(fileOut);
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (fileOut != null) {
						try {
							fileOut.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
				JOptionPane.showMessageDialog(V1PanelFunc02.this, message, "匯出結果", JOptionPane.INFORMATION_MESSAGE);

			} else {
				String message = "申請書類別代碼請務必輸入";
				JOptionPane.showMessageDialog(V1PanelFunc02.this, message, "操作錯誤", JOptionPane.ERROR_MESSAGE);
			}

		}
	}

	class FileChooserActionListener implements ActionListener, DirectionChooserInterface {
		private File file;
		private JLabel label = new JLabel();
		private JProgressBar progressBar = new JProgressBar(0, 100);
		private final  boolean fileTpyeIsDoc ;
		public JLabel getLabel() {
			return label;
		}

		public JProgressBar getProgressBar() {
			return progressBar;
		}
		private FileChooserActionListener(boolean fileTpyeIsDoc){
			this. fileTpyeIsDoc =fileTpyeIsDoc;
		}
		public void actionPerformed(ActionEvent e) {
			JFileChooser fc = new JFileChooser();
			if (file != null && file.isFile()) {
				fc.setSelectedFile(file);
			}
			FileFilter filter = new FileFilter() {
				public boolean accept(File f) {
					String patternString = ".*.((doc)|(xls))";
					if(fileTpyeIsDoc){
						patternString = ".*.doc";
					}else{
						patternString = ".*.xls";
					}
                    if ((f.isFile() && Pattern.compile(patternString).matcher(f.getName()).matches())
                            || f.isDirectory()) {
                        return true;
                    } else {
                        return false;
                    }
				}

				public String getDescription() {
					return "僅限template File";
				}

			};

			fc.setFileFilter(filter);
			fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
			int returnVal = fc.showOpenDialog(V1PanelFunc02.this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				file = fc.getSelectedFile();
				label.setText(file.getAbsolutePath());
				log.debug(file.getAbsolutePath());
				// This is where a real application would open the file.
				log.debug("Opening: " + file.getName());

			} else {
				file = null;
				// label.setText("");
			}
		}

		public File getFile() {
			return file;
		}

		public void clear() {
			file = null;
			label.setText("");
			progressBar.setValue(0);
		}
	}

	private void handleExceptionErrors(Exception e1) {
		StringBuffer sbf = new StringBuffer();
		sbf.append(e1.getClass()).append(" : ").append(e1.getMessage()).append("\n\r");
		e1.printStackTrace();
		Throwable cause = e1.getCause();
		handleStackTraceElement(cause, sbf);
		JOptionPane.showMessageDialog(V1PanelFunc02.this, sbf.toString(), "操作錯誤", JOptionPane.ERROR_MESSAGE);
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
