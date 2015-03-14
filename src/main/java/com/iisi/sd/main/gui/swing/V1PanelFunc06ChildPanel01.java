package com.iisi.sd.main.gui.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
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

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.robert.study.service.TableScriptGenaratorService;
import org.robert.study.utils.POIUtils;
import org.robert.study.utils.Utils;

import com.iisi.doc.process.DocWriter;
import com.iisi.rl.table.DataTable;
import com.iisi.sd.main.gui.DirectionChooserInterface;
import com.iisigroup.config.model.RLRegApplicationConfigModelBean;
import com.iisigroup.toolkits.service.TableScriptGenaratorServiceImpl;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class V1PanelFunc06ChildPanel01 extends JPanel {
	/**
     * 
     */
	private static final long serialVersionUID = 4897122268357584890L;
	protected static Logger log = Logger.getLogger(V1PanelFunc06ChildPanel01.class);
	private JTextField chtTable;
	private JTextField textField;
	final ExportImplementedFileActionListener exportImplementedFileActionListener = new ExportImplementedFileActionListener(
			V1PanelFunc06ChildPanel01.this);
	private Map<Integer, RLRegApplicationConfigModelBean> beanHashMap = new HashMap<Integer, RLRegApplicationConfigModelBean>(50);
	private Integer beanHashCode;
	private List<JLabel> fileLabelList = new LinkedList<JLabel>();

	public Map<Integer, RLRegApplicationConfigModelBean> getBeanHashMap() {
		return beanHashMap;
	}

	public void setBeanHashCode(Integer beanHashCode) {
		this.beanHashCode = beanHashCode;
	}

	public Integer getBeanHashCode() {
		return beanHashCode;
	}

	public void maintainModelBeans(final Integer beanHashCode) {
		final RLRegApplicationConfigModelBean bean = beanHashMap.get(beanHashCode);
		if (bean != null) {
			bean.setSymbolCode(textField.getText());
			bean.setChineseName(chtTable.getText());
			Map<String, String> tableTypeRefLocationMap = bean.getTableTypeRefLocationMap();
			if (exportImplementedFileActionListener.eachRegRLDFSFileChooserInterface.file != null) {
				tableTypeRefLocationMap.put(V1PanelFunc06Constant.eachRegRLDFSFileLocation,
						exportImplementedFileActionListener.eachRegRLDFSFileChooserInterface.file.getAbsolutePath());
			}
			if (exportImplementedFileActionListener.eachRegXLDFSFileChooserInterface.file != null) {
				tableTypeRefLocationMap.put(V1PanelFunc06Constant.eachRegXLDFSFileLocation,
						exportImplementedFileActionListener.eachRegXLDFSFileChooserInterface.file.getAbsolutePath());
			}
			if (exportImplementedFileActionListener.externalRegFileChooserInterface.file != null) {
				tableTypeRefLocationMap.put(V1PanelFunc06Constant.externalRegFileLocation,
						exportImplementedFileActionListener.externalRegFileChooserInterface.file.getAbsolutePath());
			}
			if (exportImplementedFileActionListener.regCommonFileChooserInterface.file != null) {
				tableTypeRefLocationMap.put(V1PanelFunc06Constant.regCommonFileLocation,
						exportImplementedFileActionListener.regCommonFileChooserInterface.file.getAbsolutePath());
			}
			if (exportImplementedFileActionListener.xldfsCommonFileChooserInterface.file != null) {
				tableTypeRefLocationMap.put(V1PanelFunc06Constant.xldfsCommonFileLocation,
						exportImplementedFileActionListener.xldfsCommonFileChooserInterface.file.getAbsolutePath());
			}
			if(exportImplementedFileActionListener.getFile()!=null){
				tableTypeRefLocationMap.put(V1PanelFunc06Constant.outputFileLocation,
						exportImplementedFileActionListener.getFile().getAbsolutePath());
			}
		}
	}

	public void loadData(final RLRegApplicationConfigModelBean bean) {
		Integer beanHashCode = Integer.valueOf(bean.hashCode());
		this.beanHashCode = beanHashCode;
		beanHashMap.put(beanHashCode, bean);
		String beanSymbolCode = bean.getSymbolCode();
		String beanChineseName = bean.getChineseName();
		textField.setText(beanSymbolCode);
		chtTable.setText(beanChineseName);
		exportImplementedFileActionListener.chtName.setText(beanChineseName);
		Map<String, String> tableTypeRefLocationMap = bean.getTableTypeRefLocationMap();
		String eachRegRLDFSFileLocation = tableTypeRefLocationMap.get(V1PanelFunc06Constant.eachRegRLDFSFileLocation);
		String eachRegXLDFSFileLocation = tableTypeRefLocationMap.get(V1PanelFunc06Constant.eachRegXLDFSFileLocation);
		String externalRegFileLocation = tableTypeRefLocationMap.get(V1PanelFunc06Constant.externalRegFileLocation);
		String regCommonFileLocation = tableTypeRefLocationMap.get(V1PanelFunc06Constant.regCommonFileLocation);
		String xldfsCommonFileLocation = tableTypeRefLocationMap.get(V1PanelFunc06Constant.xldfsCommonFileLocation);
		String outputFileLocation = tableTypeRefLocationMap.get(V1PanelFunc06Constant.outputFileLocation);
		if (eachRegRLDFSFileLocation != null) {
			exportImplementedFileActionListener.eachRegRLDFSFileChooserInterface.file = new File(eachRegRLDFSFileLocation);
		} else {
			exportImplementedFileActionListener.eachRegRLDFSFileChooserInterface.file = null;
		}

		if (eachRegXLDFSFileLocation != null){
			exportImplementedFileActionListener.eachRegXLDFSFileChooserInterface.file = new File(eachRegXLDFSFileLocation);
		}else{
			exportImplementedFileActionListener.eachRegXLDFSFileChooserInterface.file = null;
		}
			
		if (externalRegFileLocation != null){
			exportImplementedFileActionListener.externalRegFileChooserInterface.file = new File(externalRegFileLocation);
		}else{
			exportImplementedFileActionListener.externalRegFileChooserInterface.file = null;
		}
			
		if (regCommonFileLocation != null){
			exportImplementedFileActionListener.regCommonFileChooserInterface.file = new File(regCommonFileLocation);
		}else{
			exportImplementedFileActionListener.regCommonFileChooserInterface.file = null;
		}
			
		if (xldfsCommonFileLocation != null){
			exportImplementedFileActionListener.xldfsCommonFileChooserInterface.file = new File(xldfsCommonFileLocation);
		}else{
			exportImplementedFileActionListener.xldfsCommonFileChooserInterface.file = null;
		}
		if (outputFileLocation != null){
			exportImplementedFileActionListener.setDirection(new File(outputFileLocation))  ;
		}else{
			exportImplementedFileActionListener.setDirection(null)  ;
		}
		exportImplementedFileActionListener.eachRegRLDFSFileChooserInterface.getLabel();
		exportImplementedFileActionListener.eachRegXLDFSFileChooserInterface.getLabel();
		exportImplementedFileActionListener.externalRegFileChooserInterface.getLabel();
		exportImplementedFileActionListener.regCommonFileChooserInterface.getLabel();
		exportImplementedFileActionListener.xldfsCommonFileChooserInterface.getLabel();
		exportImplementedFileActionListener.getLabel();
	}

	public void clear() {
		// bean = new RLRegApplicationConfigModelBean();
		textField.setText("");
		chtTable.setText("");
		exportImplementedFileActionListener.chtName.setText("");
		exportImplementedFileActionListener.eachRegRLDFSFileChooserInterface.clear();
		exportImplementedFileActionListener.eachRegXLDFSFileChooserInterface.clear();
		exportImplementedFileActionListener.externalRegFileChooserInterface.clear();
		exportImplementedFileActionListener.regCommonFileChooserInterface.clear();
		exportImplementedFileActionListener.xldfsCommonFileChooserInterface.clear();
	}

	/**
	 * Create the panel.
	 */
	public V1PanelFunc06ChildPanel01() {
		setLayout(new FormLayout(new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"), FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC, }, new RowSpec[] { FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, }));

		JLabel regCommonLabel = new JLabel("1.申請書共通欄項:");
		add(regCommonLabel, "2, 2");

		final FileChooserActionListener regCommonFileListener = new FileChooserActionListener();
		JButton regCommonButton = new JButton("選擇");
		regCommonButton.addActionListener(regCommonFileListener);
		add(regCommonButton, "4, 2");

		JLabel regCommonShowLabel = regCommonFileListener.getLabel();
		add(regCommonShowLabel, "6, 2");
		fileLabelList.add(regCommonShowLabel);

		JButton regCommonClearButton = new JButton("清除");
		regCommonClearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				regCommonFileListener.clear();
			}
		});
		add(regCommonClearButton, "8, 2");

		JLabel externalRegLabel = new JLabel("2.外來申請書獨有共通欄項:");
		add(externalRegLabel, "2, 4");

		final FileChooserActionListener externalRegFileListener = new FileChooserActionListener();
		JButton externalRegButton = new JButton("選擇");
		externalRegButton.addActionListener(externalRegFileListener);
		add(externalRegButton, "4, 4");

		JLabel externalRegShowLabel = externalRegFileListener.getLabel();
		add(externalRegShowLabel, "6, 4");
		fileLabelList.add(externalRegShowLabel);

		JButton externalRegClearButton = new JButton("清除");
		externalRegClearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				externalRegFileListener.clear();
			}
		});
		add(externalRegClearButton, "8, 4");

		JLabel xldfsCommonLabel = new JLabel("3.XLDF共通欄項:");
		add(xldfsCommonLabel, "2, 6");

		final FileChooserActionListener xldfsCommonFileListener = new FileChooserActionListener();
		JButton xldfsCommonButton = new JButton("選擇");
		xldfsCommonButton.addActionListener(xldfsCommonFileListener);
		add(xldfsCommonButton, "4, 6");

		JLabel xldfsCommonShowLabel = xldfsCommonFileListener.getLabel();
		add(xldfsCommonShowLabel, "6, 6");
		fileLabelList.add(xldfsCommonShowLabel);

		JButton xldfsCommonClearButton = new JButton("清除");
		xldfsCommonClearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				xldfsCommonFileListener.clear();
			}
		});
		add(xldfsCommonClearButton, "8, 6");

		JLabel eachRegRLDFSLabel = new JLabel("4.各項登記申請書獨有欄位:");
		add(eachRegRLDFSLabel, "2, 8");

		final FileChooserActionListener eachRegRLDFSFileListener = new FileChooserActionListener();
		JButton eachRegRLDFSButton = new JButton("選擇");
		eachRegRLDFSButton.addActionListener(eachRegRLDFSFileListener);
		add(eachRegRLDFSButton, "4, 8");

		JLabel eachRegRLDFSShowLabel = eachRegRLDFSFileListener.getLabel();
		add(eachRegRLDFSShowLabel, "6, 8");
		fileLabelList.add(eachRegRLDFSShowLabel);

		JButton eachRegRLDFSClearButton = new JButton("清除");
		eachRegRLDFSClearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				eachRegRLDFSFileListener.clear();
			}
		});
		add(eachRegRLDFSClearButton, "8, 8");

		JLabel eachRegXLDFSLabel = new JLabel("5.各項XLDFS系列獨有欄位:");
		add(eachRegXLDFSLabel, "2, 10");

		final FileChooserActionListener eachRegXLDFSFileListener = new FileChooserActionListener();
		JButton eachRegXLDFSButton = new JButton("選擇");
		eachRegXLDFSButton.addActionListener(eachRegXLDFSFileListener);
		add(eachRegXLDFSButton, "4, 10");

		JLabel eachRegXLDFSShowLabel = eachRegXLDFSFileListener.getLabel();
		add(eachRegXLDFSShowLabel, "6, 10");
		fileLabelList.add(eachRegXLDFSShowLabel);

		JButton eachRegXLDFSClearButton = new JButton("清除");
		eachRegXLDFSClearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				eachRegXLDFSFileListener.clear();
			}
		});
		add(eachRegXLDFSClearButton, "8, 10");

		JLabel applySymbolCodeText = new JLabel("6.申請書類別代碼:");
		add(applySymbolCodeText, "2, 12");

		textField = new JTextField();

		add(textField, "4, 12, 5, 1, fill, default");
		textField.setColumns(10);

		JLabel chtTableNameLabel = new JLabel("7.中文名稱：");
		add(chtTableNameLabel, "2, 14");

		exportImplementedFileActionListener.setEachRegRLDFSFileChooserInterface(eachRegRLDFSFileListener);
		exportImplementedFileActionListener.setEachRegXLDFSFileChooserInterface(eachRegXLDFSFileListener);
		exportImplementedFileActionListener.setRegCommonFileChooserInterface(regCommonFileListener);
		exportImplementedFileActionListener.setEachRegRLDFSFileChooserInterface(eachRegRLDFSFileListener);
		exportImplementedFileActionListener.setExternalRegFileChooserInterface(externalRegFileListener);
		exportImplementedFileActionListener.setXldfsCommonFileChooserInterface(xldfsCommonFileListener);
//		chtTable=new JTextField();
		chtTable = exportImplementedFileActionListener.getChtName();
		add(chtTable, "4, 14, 5, 1, fill, default");
		chtTable.setColumns(10);

		
		JLabel outputDirLabel = new JLabel("產出目錄路徑:");
		add(outputDirLabel, "2, 16");

		JButton outputDirButton = new JButton("選擇");
		outputDirButton.setEnabled(true);
		outputDirButton.addActionListener(exportImplementedFileActionListener);
		add(outputDirButton, "4, 16");
		
//		JLabel exportShowLabel = new JLabel("...........");
		JLabel exportShowLabel = exportImplementedFileActionListener.getLabel();
		add(exportShowLabel, "6, 16");
	}

	private void handleExceptionErrors(Exception e1) {
		StringBuffer sbf = new StringBuffer();
		sbf.append(e1.getClass()).append(" : ").append(e1.getMessage()).append("\n\r");
		e1.printStackTrace();
		Throwable cause = e1.getCause();
		handleStackTraceElement(cause, sbf);
		JOptionPane.showMessageDialog(V1PanelFunc06ChildPanel01.this, sbf.toString(), "操作錯誤", JOptionPane.ERROR_MESSAGE);
	}

	private Throwable handleStackTraceElement(final Throwable cause, final StringBuffer sbf) {
		if (cause != null) {
			sbf.append(cause.getClass()).append(" : ").append(cause.getMessage()).append("\n\r");
			return handleStackTraceElement(cause.getCause(), sbf);
		} else {
			return null;
		}
	}

	class FileChooserActionListener implements ActionListener, DirectionChooserInterface {
		private File file;
		private JLabel label = new JLabel();
		private JProgressBar progressBar = new JProgressBar(0, 100);

		public JLabel getLabel() {
			if (file != null) {
				label.setText(file.getAbsolutePath());
			}else{
				label.setText("");
			}
			return label;
		}

		public JProgressBar getProgressBar() {
			return progressBar;
		}

		public void actionPerformed(ActionEvent e) {
			JFileChooser fc = new JFileChooser();
			if (file != null && file.isFile()) {
				fc.setSelectedFile(file);
			}
			FileFilter filter = new FileFilter() {
				public boolean accept(File f) {
					String patternString = ".*.xls";
					if ((f.isFile() && Pattern.compile(patternString).matcher(f.getName()).matches()) || f.isDirectory()) {
						return true;
					} else {
						return false;
					}
				}

				public String getDescription() {
					return "僅限xls File";
				}
			};
			fc.setFileFilter(filter);
			fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
			int returnVal = fc.showOpenDialog(V1PanelFunc06ChildPanel01.this);
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
	class ExportImplementedFileActionListener extends DirectionChooserActionListener {
		private FileChooserActionListener eachRegRLDFSFileChooserInterface;
		private FileChooserActionListener eachRegXLDFSFileChooserInterface;
		private FileChooserActionListener regCommonFileChooserInterface;
		private FileChooserActionListener externalRegFileChooserInterface;
		private FileChooserActionListener xldfsCommonFileChooserInterface;
		public void setDirection(final File direction){
			super.setDirection(direction);
		}
		/****
		 * @param eachRegRLDFSFileChooserInterface
		 *            各項登記申請書獨有欄位FileChooser
		 * ***/
		public void setEachRegRLDFSFileChooserInterface(FileChooserActionListener eachRegRLDFSFileChooserInterface) {
			this.eachRegRLDFSFileChooserInterface = eachRegRLDFSFileChooserInterface;
		}

		/****
		 * @param eachRegXLDFSFileChooserInterface
		 *            各項XLDFS系列獨有欄位FileChooser
		 * ***/
		public void setEachRegXLDFSFileChooserInterface(FileChooserActionListener eachRegXLDFSFileChooserInterface) {
			this.eachRegXLDFSFileChooserInterface = eachRegXLDFSFileChooserInterface;
		}

		/****
		 * @param regCommonFileChooserInterface
		 *            申請書共通欄項FileChooser
		 * ***/
		public void setRegCommonFileChooserInterface(FileChooserActionListener regCommonFileChooserInterface) {
			this.regCommonFileChooserInterface = regCommonFileChooserInterface;
		}

		/****
		 * @param externalRegFileChooserInterface
		 *            外來申請書共通欄項FileChooser
		 * ***/
		public void setExternalRegFileChooserInterface(FileChooserActionListener externalRegFileChooserInterface) {
			this.externalRegFileChooserInterface = externalRegFileChooserInterface;
		}

		/****
		 * @param xldfsCommonFileChooserInterface
		 *            XLDF共通欄項FileChooser
		 * ***/
		public void setXldfsCommonFileChooserInterface(FileChooserActionListener xldfsCommonFileChooserInterface) {
			this.xldfsCommonFileChooserInterface = xldfsCommonFileChooserInterface;
		}

		private JTextField chtName = new JTextField();

		public ExportImplementedFileActionListener(JComponent component) {
			super(component);
		}

		public JTextField getChtName() {
			return chtName;
		}
		void implementedMethod(File direction) {}
		void exeGenerate(File direction) {
			String symol = textField.getText();
			if (this.eachRegRLDFSFileChooserInterface == null || this.eachRegRLDFSFileChooserInterface.getFile() == null) {
				String message = "各項登記申請書獨有欄位資料檔案必匯入..";
				JOptionPane.showMessageDialog(V1PanelFunc06ChildPanel01.this, message, "操作錯誤", JOptionPane.ERROR_MESSAGE);
				return;
			}
			if (this.eachRegXLDFSFileChooserInterface == null || this.eachRegXLDFSFileChooserInterface.getFile() == null) {
				String message = "各項XLDFS系列獨有欄位資料檔案必匯入..";
				JOptionPane.showMessageDialog(V1PanelFunc06ChildPanel01.this, message, "操作錯誤", JOptionPane.ERROR_MESSAGE);
				return;
			}
			if (this.regCommonFileChooserInterface == null || this.regCommonFileChooserInterface.getFile() == null) {
				String message = "申請書共通欄項資料檔案必匯入..";
				JOptionPane.showMessageDialog(V1PanelFunc06ChildPanel01.this, message, "操作錯誤", JOptionPane.ERROR_MESSAGE);
				return;
			}
			if (this.externalRegFileChooserInterface == null || this.externalRegFileChooserInterface.getFile() == null) {
				String message = "外來申請書共通欄項資料檔案必匯入..";
				JOptionPane.showMessageDialog(V1PanelFunc06ChildPanel01.this, message, "操作錯誤", JOptionPane.ERROR_MESSAGE);
				return;
			}
			if (this.xldfsCommonFileChooserInterface == null || this.xldfsCommonFileChooserInterface.getFile() == null) {
				String message = "XLDF共通欄項資料檔案必匯入..";
				JOptionPane.showMessageDialog(V1PanelFunc06ChildPanel01.this, message, "操作錯誤", JOptionPane.ERROR_MESSAGE);
				return;
			}

			if (StringUtils.isNotEmpty(symol)) {
				String message = "匯出檔案目錄為：" + direction.getAbsolutePath();
				List<DataTable> docList = null;
				TableScriptGenaratorService service = new TableScriptGenaratorServiceImpl();
				try {

					// 各項登記申請書獨有欄位
					final DataTable eachRegRLDFSDataTable = service.readTemplateDataTable(this.eachRegRLDFSFileChooserInterface.getFile());

					// 各項XLDFS系列獨有欄位
					DataTable eachRegXLDFSDataTable = service.readTemplateDataTable(this.eachRegXLDFSFileChooserInterface.getFile());

					// 申請書共通欄項
					DataTable regCommonDataTable = service.readTemplateDataTable(this.regCommonFileChooserInterface.getFile());

					// 外來申請書共通欄項
					DataTable externalRegDataTable = service.readTemplateDataTable(this.externalRegFileChooserInterface.getFile());

					// XLDF共通欄項
					DataTable xldfsCommonDataTable = service.readTemplateDataTable(this.xldfsCommonFileChooserInterface.getFile());

					// 產生6-4 table doc
					String chtName = this.chtName.getText();
					docList = service.generate64TableDocFromTemplate(symol, chtName, eachRegRLDFSDataTable, eachRegXLDFSDataTable,
							regCommonDataTable, externalRegDataTable, xldfsCommonDataTable);

					for (DataTable aDataTable : docList) {
						service.validation(aDataTable);
					}
				} catch (Exception e) {
					handleExceptionErrors(e);
					return;
				}

				Map<String, String> scriptInitDataMap = service.generateInitialScript(docList);
				Map<String, String> scriptFinalDataMap = service.generateFinalScript(docList);

				DocWriter aDocWriter = new DocWriter();

				try {
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
				} catch (IOException e) {
					handleExceptionErrors(e);
					return;
				}
				JOptionPane.showMessageDialog(V1PanelFunc06ChildPanel01.this, message, "匯出結果", JOptionPane.INFORMATION_MESSAGE);
			} else {
				String message = "申請書類別代碼請務必輸入";
				JOptionPane.showMessageDialog(V1PanelFunc06ChildPanel01.this, message, "操作錯誤", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}
