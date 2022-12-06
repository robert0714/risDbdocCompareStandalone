package com.iisi.sd.main.gui.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import com.iisi.doc.process.WordExtract;
import com.iisigroup.toolkits.service.ExcelManipulationServiceImpl;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class V1PanelFunc18 extends JPanel {
    protected static Logger log = LoggerFactory.getLogger(V1PanelFunc18.class);
    private static final long serialVersionUID = -1852906988920722628L;
    private Pattern rldfPattern = Pattern.compile("6-4-.*.doc");

    public V1PanelFunc18() {
        setLayout(new FormLayout(new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"),
                FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC,
                FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC,
                FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC,
                FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow") },
                new RowSpec[] { FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
                        RowSpec.decode("default:grow"), FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("default:grow"),
                        FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC }));

        JLabel tableSchemalabel = new JLabel("1.6-4 table schema :");
        add(tableSchemalabel, "2, 2");

        JButton tableSchemaSrcButton = new JButton("目錄選擇");
        DirectionChooserActionListener docListener = new DirectionChooserActionListener(this) {
            @Override
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
        DirectionChooserActionListener exportDirChooserListener = new DirectionChooserActionListener(this) {
            void implementedMethod(File direction) {
            }
        };
        exportDirecButton.addActionListener(exportDirChooserListener);
        add(exportDirecButton, "4, 4");

        JButton execButton = new JButton("執行作業");
        ExecActionListener execActionListener = new ExecActionListener();
        execActionListener.setDestiDir(exportDirChooserListener);
        execActionListener.setSrcDir(docListener);
        execButton.addActionListener(execActionListener);

        JLabel outputShowLabel = exportDirChooserListener.getLabel();
        add(outputShowLabel, "6, 4");

        add(execButton, "2, 24, left, bottom");
    }

    private class ExecActionListener implements ActionListener {
        private DirectionChooserActionListener srcDir;
        private DirectionChooserActionListener destiDir;

        public void setSrcDir(DirectionChooserActionListener srcDir) {
            this.srcDir = srcDir;
        }

        public void setDestiDir(DirectionChooserActionListener destiDir) {
            this.destiDir = destiDir;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (this.srcDir.getFile() != null && this.destiDir.getFile() != null) {
                final String message = String.format("匯出檔案目錄為： %s", this.destiDir.getFile().getAbsolutePath()) ;
                ExcelManipulationServiceImpl service2 = new ExcelManipulationServiceImpl();
                
                WordExtract aWordExtract = new WordExtract();
                final  File[] srcFiles = this.srcDir.getFile().listFiles();
                final List<String[]> titleInfoList = new ArrayList<String[]>(); 
                final  Iterator<File> iterator = new ArrayList(Arrays.asList(srcFiles)).iterator();
                while(iterator.hasNext()){
                    final File srcFileUnit = iterator.next();
                    if (rldfPattern.matcher(srcFileUnit.getName()).matches()) {
                        final String tableName = StringUtils
                                .remove(StringUtils.remove(srcFileUnit.getName(), "6-4-"), ".doc").toUpperCase().trim();
                        final String title = StringUtils.trim(StringUtils.remove(aWordExtract.convertTOTitle(srcFileUnit),
                                tableName));
                        
                        titleInfoList.add(new String[] { tableName, title == null ? "檔案損毀無法轉出資料" : title });
                    }
                    iterator.remove();
                }
                
                final String srcFolederName = this.srcDir.getFile().getName();

                service2.outputCSVFile(titleInfoList,  this.destiDir.getFile().getAbsolutePath()+File.separator+srcFolederName+".csv");
                JOptionPane.showMessageDialog(V1PanelFunc18.this, message, "匯出結果", JOptionPane.INFORMATION_MESSAGE);
                aWordExtract =null;
                service2=null;
            } else {
                JOptionPane
                        .showMessageDialog(V1PanelFunc18.this, "請必須指定目錄.........", "操作錯誤", JOptionPane.ERROR_MESSAGE);
            }

        }

    }
}