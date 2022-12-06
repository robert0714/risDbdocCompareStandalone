package com.iisi.sd.main.gui.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.regex.Pattern;

import javax.sql.DataSource;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.robert.study.service.PseudoDataService;
import org.robert.study.tradition.dao.GenericDao;

import com.iisi.sd.main.gui.DirectionChooserInterface;
import com.iisigroup.toolkits.service.PseudoDataServiceImpl;
import com.iisigroup.tradition.dao.jdbc.GenericDaoImpl;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class V1PanelFunc04 extends JPanel {
    private static final long serialVersionUID = -8352934034535997008L;
    protected static Logger log = LoggerFactory.getLogger(V1PanelFunc04.class);
    private JTextField tableName;
    private boolean customJDBCconfig;

    /**
     * Create the panel.
     */
    public V1PanelFunc04() {
        setLayout(new FormLayout(new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC,
                FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC,
                FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC,
                FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"), }, new RowSpec[] {
                FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
                FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
                FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, }));

        JLabel tableNamelabel = new JLabel("1.請輸入表格名稱:");
        add(tableNamelabel, "2, 2, left, default");

        tableName = new JTextField();
        add(tableName, "4, 2, fill, default");
        tableName.setColumns(10);

        JLabel jdbcConfigLabel = new JLabel("2.請指定JDBC設定檔:");
        add(jdbcConfigLabel, "2, 4");

        JRadioButton defaultJDBCConfig = new JRadioButton("使用預設值");
        defaultJDBCConfig.setSelected(true);

        add(defaultJDBCConfig, "4, 4");
        ButtonGroup jdbcConfigGroup = new ButtonGroup();
        jdbcConfigGroup.add(defaultJDBCConfig);

        JRadioButton customJDBCConfig = new JRadioButton("使用自定植");

        add(customJDBCConfig, "4, 6");
        jdbcConfigGroup.add(customJDBCConfig);
        final JLabel configFileLabel = new JLabel("test");
        configFileLabel.setVisible(false);
        add(configFileLabel, "6, 8");

        final JButton configFileButton = new JButton("設定");
        defaultJDBCConfig.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                configFileButton.setVisible(false);
                configFileLabel.setVisible(false);
                customJDBCconfig = false;
            }
        });
        customJDBCConfig.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                configFileButton.setVisible(true);
                configFileLabel.setVisible(true);

                customJDBCconfig = true;
            }
        });

        ExportTemplateFileActionListener aExportTemplateFileActionListener = new ExportTemplateFileActionListener(this);
        FileChooserActionListener selectedFileChooserActionListener = new FileChooserActionListener();
        ExecActionListener execActionListener = new ExecActionListener();
        execActionListener.setSelectedFile(selectedFileChooserActionListener);

        JLabel outputTemplateShowLabel = aExportTemplateFileActionListener.getLabel();
        // JLabel outputTemplateShowLabel = new JLabel("----------------");
        add(outputTemplateShowLabel, "6, 10");

        JLabel exportFileShowLabel = selectedFileChooserActionListener.getLabel();
        // JLabel exportFileShowLabel = new JLabel("rrrrrrrrrrrrrr");
        add(exportFileShowLabel, "6, 12");

        JButton importFileButton = new JButton("選擇");
        importFileButton.addActionListener(selectedFileChooserActionListener);

        configFileButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                V1PanelFunc04Dialog01 dialog = new V1PanelFunc04Dialog01(V1PanelFunc04.this);
                dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                dialog.setVisible(true);
                SwingUtilities.getWindowAncestor(V1PanelFunc04.this).setEnabled(false);
            }
        });
        configFileButton.setVisible(false);

        add(configFileButton, "4, 8, left, top");

        JLabel outputTemplateLabel = new JLabel("3.請選擇當前表格輸出template位置");
        add(outputTemplateLabel, "2, 10");

        final JButton outputTemplateButton = new JButton("選擇");
        outputTemplateButton.addActionListener(aExportTemplateFileActionListener);

        add(outputTemplateButton, "4, 10");

        JLabel exportFileLabel = new JLabel("4.請指定匯入檔案");
        add(exportFileLabel, "2, 12");
        add(importFileButton, "4, 12");

        JLabel execLabel = new JLabel("5.執行匯入");
        add(execLabel, "2, 14");

        JButton execButton = new JButton("匯入");
        execButton.addActionListener(execActionListener);
        add(execButton, "4, 14");

    }

    private void handleExceptionErrors(Exception e1) {
        StringBuffer sbf = new StringBuffer();
        sbf.append(e1.getClass()).append(" : ").append(e1.getMessage()).append("\n\r");
        e1.printStackTrace();
        StackTraceElement[] stackTraces = e1.getStackTrace();
        for (StackTraceElement stackTrace : stackTraces) {
            log.info(e1.getMessage() ,stackTrace);
        }
        Throwable cause = e1.getCause();
        handleStackTraceElement(cause, sbf);
        JOptionPane.showMessageDialog(V1PanelFunc04.this, sbf.toString(), "操作錯誤", JOptionPane.ERROR_MESSAGE);
    }

    private Throwable handleStackTraceElement(final Throwable cause, final StringBuffer sbf) {
        if (cause != null) {
            sbf.append(cause.getClass()).append(" : ").append(cause.getMessage()).append("\n\r");
            StackTraceElement[] stackTraces = cause.getStackTrace();
            for (StackTraceElement stackTrace : stackTraces) {
                log.info( cause.getMessage(), stackTrace);
            }
            return handleStackTraceElement(cause.getCause(), sbf);
        } else {
            return null;
        }
    }

    class ExecActionListener implements ActionListener {
        private DirectionChooserInterface selectedFile;

        public void setSelectedFile(DirectionChooserInterface selectedFile) {
            this.selectedFile = selectedFile;
        }

        public void actionPerformed(ActionEvent e) {
            String symol = tableName.getText();
            if (selectedFile.getFile() == null) {
                String message = "無匯入來源檔案：";
                JOptionPane.showMessageDialog(V1PanelFunc04.this, message, "操作錯誤", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (StringUtils.isNotEmpty(symol)) {
                PseudoDataService pseudoDataServiceImpl = new PseudoDataServiceImpl();
                org.robert.study.utils.DataSourceConfig dataSourceConfig = org.robert.study.utils.DataSourceConfig
                        .getInstance();
                DataSource ds = null;
                if (customJDBCconfig) {
                    ds = dataSourceConfig.getCustomeDataSource();
                } else {
                    ds = dataSourceConfig.getDefaultDataSource();
                }
                try {
                    GenericDao genericDao = new GenericDaoImpl();
                    genericDao.setDataSource(ds);
                    pseudoDataServiceImpl.setDao(genericDao);
                    pseudoDataServiceImpl.readXLS(symol, selectedFile.getFile());
                } catch (Exception e1) {
                    handleExceptionErrors(e1);
                    return;
                } finally {
                    try {
                        pseudoDataServiceImpl.shutdownDataSource(ds);
                    } catch (SQLException e1) {
                        handleExceptionErrors(e1);
                        return;
                    }
                }

                String message = "匯入檔案為：" + selectedFile.getFile().getAbsolutePath();
                JOptionPane.showMessageDialog(V1PanelFunc04.this, message, "匯入結果", JOptionPane.INFORMATION_MESSAGE);
            } else {
                String message = "表格名稱請務必輸入";
                JOptionPane.showMessageDialog(V1PanelFunc04.this, message, "操作錯誤", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    class ExportTemplateFileActionListener extends DirectionChooserActionListener {
        public ExportTemplateFileActionListener(JComponent component) {
            super(component);
        }

        void implementedMethod(File direction) {
            String symol = tableName.getText();
            if (StringUtils.isNotEmpty(symol)) {
                File export = new File(direction.getAbsolutePath() + "//" + symol + "template.xls");
                String message = "匯出檔案為：" + export.getAbsolutePath();

                FileOutputStream fileOut = null;
                PseudoDataService pseudoDataServiceImpl = new PseudoDataServiceImpl();
                org.robert.study.utils.DataSourceConfig dataSourceConfig = org.robert.study.utils.DataSourceConfig
                        .getInstance();
                DataSource ds = null;
                if (customJDBCconfig) {
                    ds = dataSourceConfig.getCustomeDataSource();
                } else {
                    ds = dataSourceConfig.getDefaultDataSource();
                }
                try {
                    GenericDao genericDao = new GenericDaoImpl();
                    genericDao.setDataSource(ds);
                    pseudoDataServiceImpl.setDao(genericDao);

                    Workbook wb = pseudoDataServiceImpl.generateTemplateXLS(symol);

                    fileOut = new FileOutputStream(export);
                    wb.write(fileOut);
                } catch (Exception e1) {
                    handleExceptionErrors(e1);
                    return;
                } finally {
                    if (fileOut != null) {
                        try {
                            fileOut.close();
                        } catch (IOException e) {
                            handleExceptionErrors(e);
                        }
                    }
                    try {
                        pseudoDataServiceImpl.shutdownDataSource(ds);
                    } catch (SQLException e) {
                        handleExceptionErrors(e);
                        return;
                    }
                }
                JOptionPane.showMessageDialog(V1PanelFunc04.this, message, "匯出結果", JOptionPane.INFORMATION_MESSAGE);
            } else {
                String message = "表格名稱請務必輸入";
                JOptionPane.showMessageDialog(V1PanelFunc04.this, message, "操作錯誤", JOptionPane.ERROR_MESSAGE);
            }

        }
    }

    class FileChooserActionListener implements ActionListener, DirectionChooserInterface {
        private File file;
        private JLabel label = new JLabel();
        private JProgressBar progressBar = new JProgressBar(0, 100);

        public JLabel getLabel() {
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
            int returnVal = fc.showOpenDialog(V1PanelFunc04.this);
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
}
