package com.iisi.sd.main.gui.swing;

import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import net.sf.jxls.exception.ParsePropertyException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.robert.study.discrepancy.model.DiscrepancyTableInterface;
import org.robert.study.service.CompareDBTableAnd64TableDisplayService;
import org.robert.study.service.CompareDBTableAnd64TableService;

import com.iisi.doc.process.WordExtract;
import com.iisi.rl.table.DataTable;
import com.iisi.rl.table.discrepancy.DiscrepancyReport;
import com.iisi.rl.table.jdbc.schema.JDBCTablseSchema;
import com.iisi.rl.table.script.ScriptTableInfo;
import com.iisi.sd.main.gui.DataSourceInterface;
import com.iisi.sd.main.gui.DirectionChooserInterface;
import com.iisigroup.toolkits.service.CompareDBTableAnd64TableDisplayServiceImpl;
import com.iisigroup.toolkits.service.CompareDBTableAnd64TableServiceV2Impl;
import com.iisigroup.toolkits.service.JdbcProcessServiceImpl;
import com.iisigroup.toolkits.service.ScanInspectServiceImpl;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

/***
 * @author robert.lee
 * *****/
public class V1PanelFunc16 extends JPanel {

    private static final long serialVersionUID = -5689885189672393451L;
    private final JTextArea taskOutput;
    protected static Logger log = LoggerFactory.getLogger(V1PanelFunc16.class);

    public V1PanelFunc16() {
        setLayout(new FormLayout(new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC,
                FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"), FormFactory.RELATED_GAP_COLSPEC,
                ColumnSpec.decode("default:grow"), FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC,
                FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC,
                FormFactory.DEFAULT_COLSPEC, }, new RowSpec[] { FormFactory.RELATED_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
                FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
                FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("default:grow"), }));

        JLabel directionLabel = new JLabel("1.JDBC 資料庫設定位址");
        add(directionLabel, "4, 2, fill, default");

        JButton docDirectionButton = new JButton("設定 ");
        final JDBCChooserActionListener jdbcListener = new JDBCChooserActionListener(V1PanelFunc16.this) {
            void implementedMethod(BasicDataSource dataSource) {
        	V1PanelFunc16Dialog01 dialog = new V1PanelFunc16Dialog01(V1PanelFunc16.this);
                dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                dialog.setVisible(true);
                SwingUtilities.getWindowAncestor(V1PanelFunc16.this).setEnabled(false);
            }
        };
        
        
        docDirectionButton.addActionListener(jdbcListener);

        add(docDirectionButton, "6, 2");

        final JLabel driectionShow = jdbcListener.getLabel();
        add(driectionShow, "8, 2, fill, default");

        JButton docDirectionClearButton = new JButton("clear");
        docDirectionClearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                jdbcListener.clear();
            }
        });

        JProgressBar docProgressBar = jdbcListener.getProgressBar();
        add(docProgressBar, "4, 4, fill, default");
        add(docDirectionClearButton, "6, 4");

        final JButton docImportButton = new JButton("import");
        final DocImportActionListener jdbcDataImportActionListener = new DocImportActionListener();
        jdbcDataImportActionListener.setDirectionChooserInterface(jdbcListener);
        // docImportActionListener.setObserveredButton(docImportButton);
        docImportButton.addActionListener(jdbcDataImportActionListener);
        add(docImportButton, "8, 4, left, default");

        JLabel scriptLabel = new JLabel("2.table script目錄位置");
        add(scriptLabel, "4, 6, fill, default");

        JButton scriptDirectionButton = new JButton("open");
        final DirectionChooserActionListener scriptListener = new DirectionChooserActionListener(V1PanelFunc16.this) {
            void implementedMethod(File direction) {
            }
        };
        ;
        scriptDirectionButton.addActionListener(scriptListener);
        add(scriptDirectionButton, "6, 6");

        final JLabel scriptShow = scriptListener.getLabel();
        add(scriptShow, "8, 6, fill, default");

        JButton scriptDirectionClearButton = new JButton("clear");
        scriptDirectionClearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                scriptListener.clear();
            }
        });

        JProgressBar scriptProgressBar = scriptListener.getProgressBar();
        add(scriptProgressBar, "4, 8, fill, default");
        add(scriptDirectionClearButton, "6, 8");

        final JButton scriptImportButton = new JButton("import");
        final ScriptImportActionListener scriptImportActionListener = new ScriptImportActionListener();
        scriptImportActionListener.setObserveredButton(scriptImportButton);
        scriptImportActionListener.setDirectionChooserInterface(scriptListener);
        scriptImportButton.addActionListener(scriptImportActionListener);
        add(scriptImportButton, "8, 8, left, default");

        JLabel outputFileDirText = new JLabel("3.差異性比對報表輸出目錄");
        add(outputFileDirText, "4, 10, fill, default");

        JButton outputFileButton = new JButton("open");
        final DirectionChooserActionListener outputFileListener = new DirectionChooserActionListener(V1PanelFunc16.this) {
            void implementedMethod(File direction) {
            }
        };
        outputFileButton.addActionListener(outputFileListener);
        add(outputFileButton, "6, 10");

        final JLabel outputFileShow = outputFileListener.getLabel();
        add(outputFileShow, "8, 10, fill, default");

        JButton outputFileClearButton = new JButton("clear");
        outputFileClearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                outputFileListener.clear();
            }
        });
        add(outputFileClearButton, "6, 12");

        JButton outPutFileButton = new JButton("Generate Discrepancy Report");
        GenerateDiscrepancyReportActionListener<JDBCTablseSchema, ScriptTableInfo> generateDiscrepancyReportActionListener = new GenerateDiscrepancyReportActionListener<JDBCTablseSchema, ScriptTableInfo>();
        generateDiscrepancyReportActionListener.setDocDataSource(jdbcDataImportActionListener);
        generateDiscrepancyReportActionListener.setScriptDataSource(scriptImportActionListener);
        generateDiscrepancyReportActionListener.setDirectionChooserInterface(outputFileListener);
        generateDiscrepancyReportActionListener.setObserveredButton(outPutFileButton);
        generateDiscrepancyReportActionListener.getObserveredButtonList().add(outPutFileButton);
        generateDiscrepancyReportActionListener.getObserveredButtonList().add(scriptDirectionButton);
        generateDiscrepancyReportActionListener.getObserveredButtonList().add(docDirectionButton);
        generateDiscrepancyReportActionListener.getObserveredButtonList().add(scriptImportButton);
        generateDiscrepancyReportActionListener.getObserveredButtonList().add(docImportButton);
        outPutFileButton.addActionListener(generateDiscrepancyReportActionListener);
        add(outPutFileButton, "8, 12, left, default");

        JButton clearImportDataButton = new JButton("清除所有的匯入資料");
        clearImportDataButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                scriptImportActionListener.getDataList().clear();
                jdbcDataImportActionListener.getDataList().clear();
                outputFileListener.clear();
                scriptListener.clear();
                jdbcListener.clear();
                taskOutput.append("clear all imported Data.....\n\r");
            }
        });
        add(clearImportDataButton, "8, 14, left, default");

        taskOutput = new JTextArea();
        taskOutput.setMargin(new Insets(5, 5, 5, 5));
        add(new JScrollPane(taskOutput), "2, 16, 9, 1, fill, fill");

        jdbcDataImportActionListener.getObserveredButtonList().add(docImportButton);
        jdbcDataImportActionListener.getObserveredButtonList().add(docDirectionButton);
        jdbcDataImportActionListener.getObserveredButtonList().add(outPutFileButton);

        scriptImportActionListener.getObserveredButtonList().add(scriptImportButton);
        scriptImportActionListener.getObserveredButtonList().add(outPutFileButton);
        scriptImportActionListener.getObserveredButtonList().add(scriptDirectionButton);
    }

    abstract class Task extends SwingWorker<Void, Void> {
        // private AbstractButton button;
        private List<JButton> observeredButtonList;
        private JTextArea taskOutput;

        abstract public void implementsMethod();

        // public void setButton(AbstractButton button) {
        // this.button = button;
        // }

        public void setTaskOutput(JTextArea taskOutput) {
            this.taskOutput = taskOutput;
        }

        public void setObserveredButtonList(List<JButton> observeredButtonList) {
            this.observeredButtonList = observeredButtonList;
        }

        /*
         * Main task. Executed in background thread.
         */
        @Override
        public Void doInBackground() {
            // Random random = new Random();
            // int progress = 0;
            // // Initialize progress property.
            // setProgress(0);
            // while (progress < 100) {
            // // Sleep for up to one second.
            // try {
            // Thread.sleep(random.nextInt(1000));
            // } catch (InterruptedException ignore) {
            // }
            // // Make random progress.
            // progress += random.nextInt(10);
            // setProgress(Math.min(progress, 100));
            // }
            implementsMethod();
            return null;
        }

        /*
         * Executed in event dispatching thread
         */
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

    class DocImportActionListener implements ActionListener, DataSourceInterface<JDBCTablseSchema>, PropertyChangeListener {
        private JDBCChooserActionListener directionChooserInterface;
        private List<JDBCTablseSchema> dataList = new ArrayList<JDBCTablseSchema>();
        private Task task;
        private List<JButton> observeredButtonList = new ArrayList<JButton>();
        private JProgressBar progressBar;

       
        public void setDirectionChooserInterface(JDBCChooserActionListener directionChooserInterface) {
            this.directionChooserInterface = directionChooserInterface;
        }

        public List<JDBCTablseSchema> getDataList() {
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
            if (directionChooserInterface.getDataSource() == null) {
                JOptionPane
                        .showMessageDialog(V1PanelFunc16.this, "請指定DataSource", "error operation", JOptionPane.ERROR_MESSAGE);

            } else {
                dataList.clear();
               DataSource dataSource = directionChooserInterface.getDataSource();
                final   JdbcProcessServiceImpl aJdbcProcessServiceImpl =new JdbcProcessServiceImpl();
        
                try {
                    for (JButton aJButton : observeredButtonList) {
                        aJButton.setEnabled(false);
                    }
                   final  List<JDBCTablseSchema> alist = aJdbcProcessServiceImpl.retirieveTableInfoFromDB(dataSource);
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

                            for (JDBCTablseSchema aFile : alist) {
                              
                                try {
//                                    DataTable aDataTable = aWordExtract.convertTOUnit(fileName);
//                                    dataList.add(aDataTable);
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
                                JOptionPane.showMessageDialog(V1PanelFunc16.this, message, "統計結果",
                                        JOptionPane.INFORMATION_MESSAGE);
                            }
                        }
                    };
                    // task.setButton(observeredButton);
                    task.setTaskOutput(taskOutput);
                    task.addPropertyChangeListener(this);
                    task.setObserveredButtonList(observeredButtonList);
                    task.execute();
               
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    class ScriptImportActionListener implements ActionListener, DataSourceInterface<ScriptTableInfo>,
            PropertyChangeListener {
        private DirectionChooserInterface directionChooserInterface;
        private List<ScriptTableInfo> dataList = new ArrayList<ScriptTableInfo>();
        private Task task;
        private JButton observeredButton;
        private List<JButton> observeredButtonList = new ArrayList<JButton>();
        private JProgressBar progressBar;

        public void setObserveredButton(JButton observeredButton) {
            this.observeredButton = observeredButton;
        }

        public void setDirectionChooserInterface(DirectionChooserInterface directionChooserInterface) {
            this.directionChooserInterface = directionChooserInterface;
        }

        public List<ScriptTableInfo> getDataList() {
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
            dataList.clear();
            progressBar = directionChooserInterface.getProgressBar();
            progressBar.setValue(0);
            progressBar.setStringPainted(true);

            if (directionChooserInterface.getFile() == null) {
                JOptionPane
                        .showMessageDialog(V1PanelFunc16.this, "請指定資料夾", "error operation", JOptionPane.ERROR_MESSAGE);
                progressBar.setValue(0);
            } else {
                for (JButton aJButton : observeredButtonList) {
                    aJButton.setEnabled(false);
                }
                observeredButton.setEnabled(false);
                setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.WAIT_CURSOR));
                final ScanInspectServiceImpl scanInspect = new ScanInspectServiceImpl();
                File folder = directionChooserInterface.getFile();
                try {
                    final List<File> alist = scanInspect.getQualifiedFile(folder);
                    // dataList = scanInspect.covertFromFolder(folder);
                    // log.debug("aList size: " + dataList.size());

                    // Instances of javax.swing.SwingWorker are not reusuable,
                    // so
                    // we create new instances as needed.
                    task = new Task() {
                        public void implementsMethod() {
                            int progress = 0;
                            double tmp = 0d;
                            int errors = 0;
                            // Initialize progress property.
                            setProgress(0);
                            double totalNum = (double) alist.size();

                            for (File aFile : alist) {
                                try {
                                    ScriptTableInfo aDataTable = scanInspect.convertTOUnit(aFile);
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
                            }
                            if (dataList != null) {
                                log.debug("aList size: " + dataList.size());
                                String message = "計有:  " + (dataList.size() - errors) + " 檔案，成功匯入比較格式。\n\r" + "\t "
                                        + errors + " 檔案，失敗匯入比較格式。\n\r";
                                JOptionPane.showMessageDialog(V1PanelFunc16.this, message, "統計結果",
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

    class GenerateDiscrepancyReportActionListener<T extends DiscrepancyTableInterface, Z extends DiscrepancyTableInterface>
            implements ActionListener, PropertyChangeListener {
        private DirectionChooserInterface directionChooserInterface;
        private DataSourceInterface<T> docDataSource;
        private DataSourceInterface<Z> scriptDataSource;
        private Task task;
        private List<JButton> observeredButtonList = new ArrayList<JButton>();
        private JButton observeredButton;

        public List<JButton> getObserveredButtonList() {
            return observeredButtonList;
        }

        public void setObserveredButton(JButton observeredButton) {
            this.observeredButton = observeredButton;
        }

        public void setDocDataSource(DataSourceInterface<T> docDataSource) {
            this.docDataSource = docDataSource;
        }

        public void setScriptDataSource(DataSourceInterface<Z> scriptDataSource) {
            this.scriptDataSource = scriptDataSource;
        }

        public void setDirectionChooserInterface(DirectionChooserInterface directionChooserInterface) {
            this.directionChooserInterface = directionChooserInterface;
        }

        public void actionPerformed(ActionEvent e) {
            log.info("starting analysis........... ");
            taskOutput.append("starting analysis........... \n\r");

            if (directionChooserInterface.getFile() != null && CollectionUtils.isNotEmpty(docDataSource.getDataList())
                    && CollectionUtils.isNotEmpty(scriptDataSource.getDataList())) {
                observeredButton.setEnabled(false);
                for (JButton aJButton : observeredButtonList) {
                    aJButton.setEnabled(false);
                }
                task = new Task() {
                    public void implementsMethod() {
                        String message = "";
                        long start = System.nanoTime();
                        CompareDBTableAnd64TableService exec = new CompareDBTableAnd64TableServiceV2Impl();
                        CompareDBTableAnd64TableDisplayService displayer = new CompareDBTableAnd64TableDisplayServiceImpl();

                        Map<String, Z> scriptDataSourceMap = exec.getCommonDataMap(scriptDataSource.getDataList());
                        Map<String, T> docDataSourceMap = exec.getCommonDataMap(docDataSource.getDataList());
                        DiscrepancyReport report = exec.comparationStandardForScript64Table(scriptDataSourceMap,
                                docDataSourceMap);

                        log.debug("final report size: " + report.getDiscrepencyTables().size());
                        displayer.display(report);
                        String outfileName = directionChooserInterface.getFile().getAbsolutePath()
                                + "//disprenpancyReport" + new SimpleDateFormat("yyyyMMddHHmm").format(new Date())
                                + ".xls";
                        boolean isException = false;
                        String errorMessage = "";
                        message = "檔案產出為: " + outfileName;
                        long end = System.nanoTime();
                        try {
                            displayer.exportReport3(report, outfileName);

                        } catch (ParsePropertyException e1) {
                            e1.printStackTrace();
                            errorMessage = e1.getMessage();
                            isException = true;
                        } catch (InvalidFormatException e1) {
                            e1.printStackTrace();
                            errorMessage = e1.getMessage();
                            isException = true;
                        } catch (IOException e1) {
                            e1.printStackTrace();
                            errorMessage = e1.getMessage();
                            isException = true;
                        } catch (Exception e1) {
                            e1.printStackTrace();
                            errorMessage = e1.getMessage();
                            isException = true;
                        } finally {
                            end = System.nanoTime();

                            taskOutput.append("Time (seconds) taken is " + ((end - start) / 1.0e9) + "\n\r");
                            if (isException) {
                                JOptionPane.showMessageDialog(V1PanelFunc16.this, message + "失敗：  " + errorMessage,
                                        "error Exception", JOptionPane.ERROR_MESSAGE);
                            } else {
                                JOptionPane.showMessageDialog(V1PanelFunc16.this, message, "比對結果",
                                        JOptionPane.INFORMATION_MESSAGE);
                            }
                            taskOutput.append(message + "\n\r");
                            observeredButton.setEnabled(true);
                            for (JButton aJButton : observeredButtonList) {
                                aJButton.setEnabled(true);
                            }
                        }
                    }

                };
                // task.setButton(observeredButton);
                task.setTaskOutput(taskOutput);
                task.addPropertyChangeListener(this);
                task.setObserveredButtonList(observeredButtonList);
                task.execute();
            } else if (directionChooserInterface.getFile() == null) {
                JOptionPane
                        .showMessageDialog(V1PanelFunc16.this, "請指定資料夾", "error operation", JOptionPane.ERROR_MESSAGE);
                taskOutput.append("請指定資料夾" + "\n\r");
            }

        }

        public void propertyChange(PropertyChangeEvent evt) {
            // if ("progress" == evt.getPropertyName()) {
            // int progress = (Integer) evt.getNewValue();
            // progressBar.setValue(progress);
            // taskOutput.append(String.format("Completed %d%% of task.\n",
            // task.getProgress()));
            // }

        }
    }
}
