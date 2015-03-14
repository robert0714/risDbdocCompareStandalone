package com.iisi.sd.main.gui.swing;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.robert.study.service.TableScriptGenaratorService;
import org.robert.study.utils.POIUtils;
import org.robert.study.utils.Utils;

import com.iisi.doc.process.DocWriter;
import com.iisi.doc.process.WordExtract;
import com.iisi.rl.table.DataTable;
import com.iisi.sd.main.gui.DataSourceInterface;
import com.iisi.sd.main.gui.DirectionChooserInterface;
import com.iisigroup.toolkits.service.TableScriptGenaratorServiceImpl;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class V1PanelFunc05 extends JPanel {
    protected static Logger log = Logger.getLogger(V1PanelFunc05.class);
    /**
     * 
     */
    private static final long serialVersionUID = 6898692208490581080L;
    private JTextArea taskOutput;

    /**
     * Create the panel.
     */
    public V1PanelFunc05() {
        setLayout(new FormLayout(new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"),
                FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"), FormFactory.RELATED_GAP_COLSPEC,
                FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"), },
                new RowSpec[] { FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("default:grow"), }));

        JLabel docFolderLabel = new JLabel("1.原始6-4Table文件：");
        add(docFolderLabel, "2, 2");

        JButton docFolderPathButton = new JButton("選擇路徑");
        final DirectionChooserActionListener docListener = new DirectionChooserActionListener(this) {
            void implementedMethod(File direction) {
            }
        };
        docFolderPathButton.addActionListener(docListener);
        add(docFolderPathButton, "4, 2, left, default");

        final DocImportActionListener docImportActionListener = new DocImportActionListener();
        docImportActionListener.setDirectionChooserInterface(docListener);
        JLabel docFolderPathShowLabel = docListener.getLabel();
        // JLabel docFolderPathShowLabel = new JLabel(".........");
        add(docFolderPathShowLabel, "6, 2");

        JButton clearDocButton = new JButton("clear");
        clearDocButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                docListener.clear();
            }
        });
        add(clearDocButton, "8, 2, left, default");

        JProgressBar progressdocProgressBar = docListener.getProgressBar();
        add(progressdocProgressBar, "2, 4");

        JButton docImportButton = new JButton("import");
        docImportButton.addActionListener(docImportActionListener);
        add(docImportButton, "4, 4, left, default");
        ExportExecActionListener aExportExecActionListener = new ExportExecActionListener();

        JButton exeExportButton = new JButton("執行匯出");
        exeExportButton.addActionListener(aExportExecActionListener);

        JButton exportPathSelectiveButton = new JButton("選擇路徑");
        DirectionChooserActionListener docExportListener = new DirectionChooserActionListener(V1PanelFunc05.this) {
            void implementedMethod(File direction) {
            }
        };
        exportPathSelectiveButton.addActionListener(docExportListener);

        JLabel docFolderPathExportLabel = new JLabel("2.除戶文件產出路徑");
        add(docFolderPathExportLabel, "2, 6");
        add(exportPathSelectiveButton, "4, 6, left, default");

        JLabel exportPathShowLabel = docExportListener.getLabel();
        // JLabel exportPathShowLabel = new JLabel(".........");
        add(exportPathShowLabel, "6, 6");
        aExportExecActionListener.setDataSource(docImportActionListener);
        aExportExecActionListener.setExportFolder(docExportListener);
        add(exeExportButton, "4, 8, left, top");
        taskOutput = new JTextArea();
        add(new JScrollPane(taskOutput), "2, 10, 7, 1, fill, fill");

        docImportActionListener.getObserveredButtonList().add(exportPathSelectiveButton);
        docImportActionListener.getObserveredButtonList().add(exeExportButton);
        docImportActionListener.getObserveredButtonList().add(clearDocButton);
        docImportActionListener.getObserveredButtonList().add(docFolderPathButton);
        docImportActionListener.getObserveredButtonList().add(docImportButton);

        aExportExecActionListener.getObserveredButtonList().add(exportPathSelectiveButton);
        aExportExecActionListener.getObserveredButtonList().add(exeExportButton);
        aExportExecActionListener.getObserveredButtonList().add(clearDocButton);
        aExportExecActionListener.getObserveredButtonList().add(docFolderPathButton);
        aExportExecActionListener.getObserveredButtonList().add(docImportButton);
    }

    abstract class Task extends SwingWorker<Void, Void> {
        private List<JButton> observeredButtonList;
        private JTextArea taskOutput;

        abstract public void implementsMethod();

        public void setTaskOutput(JTextArea taskOutput) {
            this.taskOutput = taskOutput;
        }

        public void setObserveredButtonList(List<JButton> observeredButtonList) {
            this.observeredButtonList = observeredButtonList;
        }

        public Void doInBackground() {
            implementsMethod();
            return null;
        }

        public void done() {
            Toolkit.getDefaultToolkit().beep();
            // button.setEnabled(true);
            setCursor(null); // turn off the wait cursor
            taskOutput.append("Done!\n");
            if (CollectionUtils.isNotEmpty(observeredButtonList)) {
                for (JButton aJButton : observeredButtonList) {
                    aJButton.setEnabled(true);
                }
            }
        }
    }

    class DocImportActionListener implements ActionListener, DataSourceInterface<DataTable>, PropertyChangeListener {
        private DirectionChooserInterface directionChooserInterface;
        private List<DataTable> dataList = new ArrayList<DataTable>();
        private Task task;
        private List<JButton> observeredButtonList = new ArrayList<JButton>();
        private JProgressBar progressBar;

        public void setDirectionChooserInterface(DirectionChooserInterface directionChooserInterface) {
            this.directionChooserInterface = directionChooserInterface;
        }

        public List<DataTable> getDataList() {
            return dataList;
        }

        public List<JButton> getObserveredButtonList() {
            return observeredButtonList;
        }

        public void propertyChange(PropertyChangeEvent evt) {
            if ("progress" == evt.getPropertyName()) {
                int progress = (Integer) evt.getNewValue();
                progressBar.setValue(progress);
                taskOutput.append(String.format("Completed %d%% of task.\n", task.getProgress()));
            }

        }

        public void actionPerformed(ActionEvent e) {
            progressBar = directionChooserInterface.getProgressBar();
            progressBar.setValue(0);
            progressBar.setStringPainted(true);
            if (directionChooserInterface.getFile() == null) {
                JOptionPane
                        .showMessageDialog(V1PanelFunc05.this, "請指定資料夾", "error operation", JOptionPane.ERROR_MESSAGE);

            } else {
                dataList.clear();
                File folder = directionChooserInterface.getFile();
                final WordExtract aWordExtract = new WordExtract();
                try {
                    for (JButton aJButton : observeredButtonList) {
                        aJButton.setEnabled(false);
                    }
                    final List<File> alist = aWordExtract.getQualifiedFile(folder);
                    setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.WAIT_CURSOR));

                    // Instances of javax.swing.SwingWorker are not reusuable,
                    // so
                    // we create new instances as needed.
                    task = new Task() {
                        public void implementsMethod() {
                            int errors = 0;
                            int progress = 0;
                            double tmp = 0d;
                            // Initialize progress property.
                            setProgress(0);
                            double totalNum = (double) alist.size();

                            for (File aFile : alist) {
                                String fileName = aFile.getAbsolutePath();
                                try {
                                    DataTable aDataTable = aWordExtract.convertTOUnit(fileName);
                                    dataList.add(aDataTable);
                                } catch (RuntimeException e) {
                                    e.printStackTrace();
                                    log.debug("=====================" + e.getMessage());
                                    errors++;
                                    taskOutput.append(e.getMessage() + "\n\r");
                                } finally {
                                    tmp += ((100 / totalNum));
                                    progress = (int) tmp;
                                    setProgress(Math.min(progress, 100));
                                }

                                tmp += ((100 / totalNum));
                                progress = (int) tmp;
                                setProgress(Math.min(progress, 100));
                            }
                            if (dataList != null) {
                                log.debug("aList size: " + dataList.size());
                                String message = "計有:  " + (dataList.size() - errors) + " 檔案，成功匯入比較格式。\n\r" + "\t "
                                        + errors + " 檔案，失敗匯入比較格式。\n\r";
                                JOptionPane.showMessageDialog(V1PanelFunc05.this, message, "統計結果",
                                        JOptionPane.INFORMATION_MESSAGE);
                            }
                        }
                    };
                    // task.setButton(observeredButton);
                    task.setTaskOutput(taskOutput);
                    task.addPropertyChangeListener(this);
                    task.setObserveredButtonList(observeredButtonList);
                    task.execute();
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    final String patternString = "6-4-[R|X]LDF[M|0]\\d{2}M\\.doc";

    // System.out.println(Pattern.compile(patternString).matcher("6-4-XLDF000M.doc").matches());
    class ExportExecActionListener implements ActionListener, DataSourceInterface<DataTable> {
        private DataSourceInterface<DataTable> dataSource;
        private DirectionChooserInterface exportFolder;
        private List<JButton> observeredButtonList = new ArrayList<JButton>();

        public void setDataSource(DataSourceInterface<DataTable> dataSource) {
            this.dataSource = dataSource;
        }

        public void setExportFolder(DirectionChooserInterface exportFolder) {
            this.exportFolder = exportFolder;
        }

        public List<DataTable> getDataList() {
            return dataSource.getDataList();
        }

        public List<JButton> getObserveredButtonList() {
            return observeredButtonList;
        }

        public void actionPerformed(ActionEvent e) {
            if (CollectionUtils.isNotEmpty(observeredButtonList)) {
                for (JButton aJButton : observeredButtonList) {
                    aJButton.setEnabled(false);
                }
            }
            File direction = exportFolder.getFile();
            System.out.println("exportFolder: " + direction);

            if (dataSource != null && direction != null) {
                TableScriptGenaratorService service = new TableScriptGenaratorServiceImpl();
                List<DataTable> aList = getDataList();
                System.out.println("aList: " + aList.size());
                try {
                    List<DataTable> docList = service.generateHistoryRegData(aList);
                    Map<String, String> scriptInitDataMap = service.generateInitialScript(docList);
                    Map<String, String> scriptFinalDataMap = service.generateFinalScript(docList);
                    DocWriter aDocWriter = new DocWriter();
                    for (DataTable aDataTable : docList) {
                        XWPFDocument doc = aDocWriter.createNewWord(aDataTable);
                        POIUtils.writePOIXMLDocumentPartOut(
                                new File(direction.getAbsolutePath() + "//" + aDataTable.getFileName()), doc);
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
                } catch (Exception e1) {
                    handleExceptionErrors(e1);
                    return;
                } finally {
                    if (CollectionUtils.isNotEmpty(observeredButtonList)) {
                        for (JButton aJButton : observeredButtonList) {
                            aJButton.setEnabled(true);
                        }
                    }
                }
            }
            JOptionPane.showMessageDialog(V1PanelFunc05.this, "匯出路徑為: " + direction.getAbsolutePath(), "匯出結果",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void handleExceptionErrors(Exception e1) {
        StringBuffer sbf = new StringBuffer();
        sbf.append(e1.getClass()).append(" : ").append(e1.getMessage()).append("\n\r");
        e1.printStackTrace();
        Throwable cause = e1.getCause();
        handleStackTraceElement(cause, sbf);
        JOptionPane.showMessageDialog(V1PanelFunc05.this, sbf.toString(), "操作錯誤", JOptionPane.ERROR_MESSAGE);
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
