package com.iisi.sd.main.gui.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreeSelectionModel;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.robert.study.service.TableScriptGenaratorService;
import org.robert.study.utils.POIUtils;
import org.robert.study.utils.Utils;

import com.iisi.doc.process.DocWriter;
import com.iisi.doc.process.WordExtract;
import com.iisi.rl.table.DataTable;
import com.iisi.sd.main.gui.DirectionChooserInterface;
import com.iisi.sd.main.gui.swing.V1PanelFunc02.FileChooserActionListener;
import com.iisigroup.config.model.LoadServerConfigXML;
import com.iisigroup.config.model.RLRegApplicationConfigModelBean;
import com.iisigroup.config.model.RLRegApplicationConfigModelBeanConverter;
import com.iisigroup.config.model.jaxb.ItemList;
import com.iisigroup.config.model.jaxb.ItemType;
import com.iisigroup.toolkits.service.ExcelManipulationServiceImpl;
import com.iisigroup.toolkits.service.TLDFServiceImpl;
import com.iisigroup.toolkits.service.TableScriptGenaratorServiceImpl;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class V1PanelFunc07 extends JPanel {
	protected static Logger log = LoggerFactory.getLogger(V1PanelFunc07.class);

	/**
     * 
     */
	private static final long serialVersionUID = -1852906988920722628L;

	/**
	 * Create the panel.
	 */
	public V1PanelFunc07() {

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

		final DirectionChooserActionListener exportDirChooserListener = new DirectionChooserActionListener(this) {
            void implementedMethod(File direction) {
            }
        };
		
		JLabel exportSQLLabel = new JLabel("2.sql 輸出目錄選擇");
		add(exportSQLLabel, "2, 4");

		JButton button = new JButton("目錄選擇");
		
		button.addActionListener(exportDirChooserListener);
		add(button, "4, 4");

		JButton execButton = new JButton("執行作業");
		ExecActionListener execActionListener =  new ExecActionListener();
		execActionListener.setDestiDir(exportDirChooserListener);
		execActionListener.setSrcDir(docListener);
		execButton.addActionListener(execActionListener);
		
		JLabel exportShowLabel = exportDirChooserListener.getLabel();
		add(exportShowLabel, "6, 4");
		add(execButton, "2, 24, left, bottom");
	}

	private void handleExceptionErrors(Exception e1) {
		StringBuffer sbf = new StringBuffer();
		sbf.append(e1.getClass()).append(" : ").append(e1.getMessage()).append("\n\r");
		e1.printStackTrace();
		Throwable cause = e1.getCause();
		handleStackTraceElement(cause, sbf);
		JOptionPane.showMessageDialog(V1PanelFunc07.this, sbf.toString(), "操作錯誤", JOptionPane.ERROR_MESSAGE);
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
			int returnVal = fc.showOpenDialog(V1PanelFunc07.this);
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
	private  Pattern rldfPattern = Pattern.compile(".*[R|X]LDF.*.doc");
//	private  Pattern rldfPattern = Pattern.compile("6-4-XLDFSPECIALNOTICE.doc");
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
				final TLDFServiceImpl service =new TLDFServiceImpl();
				final ExcelManipulationServiceImpl service2 = new ExcelManipulationServiceImpl();
				final WordExtract aWordExtract = new WordExtract();
				final  DocWriter aDocWriter = new DocWriter();
				File[] srcFile = srcDir.getFile().listFiles();
				List<DataTable> srcList = new ArrayList<DataTable>();
				for(File srcFileUnit: srcFile){
					if(rldfPattern.matcher(srcFileUnit.getName()).matches()){						
						DataTable dataTable = aWordExtract.convertTOUnit(srcFileUnit);
						srcList.add(dataTable);
					}
				}
				
//				List<DataTable>  dableList = service.covertToTLDFFormRldfByCommonmethod(srcList);				
				Map<String, String> scriptFinalDataMap = service2.generateFinalScript(srcList);
				 try {
	                    for (DataTable aDataTable : srcList) {
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

	                JOptionPane.showMessageDialog(V1PanelFunc07.this, message, "匯出結果", JOptionPane.INFORMATION_MESSAGE);
			}else{
				JOptionPane.showMessageDialog(V1PanelFunc07.this, "請必須指定目錄.........", "操作錯誤", JOptionPane.ERROR_MESSAGE);
			}
			
		}
		
	}	
}