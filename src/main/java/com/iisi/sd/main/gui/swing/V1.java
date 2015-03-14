package com.iisi.sd.main.gui.swing;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.looks.windows.WindowsLookAndFeel;

public class V1 {

    private JFrame frmRobertsDiscrepancyAnalysis;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    V1 window = new V1();
                    window.frmRobertsDiscrepancyAnalysis.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    private V1() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        try {
            OSValidator osValidator = new OSValidator();
            if (osValidator.isWindows()) {
                UIManager.setLookAndFeel(new WindowsLookAndFeel());
            } else if (osValidator.isMac()) {
                System.out.println("This is Mac");
            } else if (osValidator.isUnix()) {
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
            } else if (osValidator.isSolaris()) {
                System.out.println("This is Solaris");
            } else {
                System.out.println("Your OS is not support!!");
            }
        } catch (UnsupportedLookAndFeelException e1) {
            e1.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        frmRobertsDiscrepancyAnalysis = new JFrame();
        frmRobertsDiscrepancyAnalysis.setTitle("Robert's  Tool  (ext:3190)");
        frmRobertsDiscrepancyAnalysis.setBounds(100, 100, 680, 434);
        frmRobertsDiscrepancyAnalysis.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frmRobertsDiscrepancyAnalysis.getContentPane().setLayout(
                new FormLayout(
                        new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"), },
                        new RowSpec[] { FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("default:grow"), }));

        JMenuBar menuBar = new JMenuBar();
        frmRobertsDiscrepancyAnalysis.setJMenuBar(menuBar);

        JMenu funcMenu = new JMenu("Function");
        menuBar.add(funcMenu);

        config(frmRobertsDiscrepancyAnalysis, funcMenu, getFunctionMap());

        JMenu tableProcessMenu = new JMenu("6-4 table基本處理");
        menuBar.add(tableProcessMenu);
        config(frmRobertsDiscrepancyAnalysis, tableProcessMenu, getTableProcessMap());

        // final V1PanelFunc1 v1PanelFunc1 = new V1PanelFunc1();
        // frmRobertsDiscrepancyAnalysis.getContentPane().add(v1PanelFunc1,
        // "2, 2, fill, fill");
        // v1PanelFunc1.setVisible(false);
        //
        // funcMenu.add(func01MenuItem);
        //
        // JMenuItem func02MenuItem = new JMenuItem("function02");
        // func02MenuItem.addActionListener(new ActionListener() {
        // public void actionPerformed(ActionEvent e) {
        // v1PanelFunc1.setVisible(false);
        // v1PanelFunc2.setVisible(true);
        // v1PanelFunc3.setVisible(false);
        // v1PanelFunc4.setVisible(false);
        // v1PanelFunc5.setVisible(false);
        // }
        // });

    }

    private <T extends JPanel> Map<String, ? extends JPanel> getFunctionMap() {
        Map<String, T> result = new HashMap<String, T>();
        result.put("01.Discrepancy Analysis(6-4 table ,script) ", (T) new V1PanelFunc01());
        result.put("02.記事欄位化表格產生器", (T) new V1PanelFunc02());
        result.put("03.戶役政 RL 6-4 table ,script 產生器", (T) new V1PanelFunc03());
        result.put("04.假資料匯入程式（針對常變動的table schema）", (T) new V1PanelFunc04());
        result.put("06.戶籍登記申請書table schema 批次產出作業", (T) new V1PanelFunc06());
        result.put("08.戶籍登記申請書-補登作業專用table產生功能", (T) new V1PanelFunc08());
        result.put("16.Discrepancy Analysis(JDBC table ,script...GUI還沒有完工) ", (T) new V1PanelFunc16());
        return result;
    }

    private <T extends JPanel> Map<String, ? extends JPanel> getTableProcessMap() {
        Map<String, T> result = new HashMap<String, T>();
        ;
        result.put("05.6-4 RL table 除戶資料產生器", (T) new V1PanelFunc05());
        result.put("07.table schema 轉換SQL script 作業", (T) new V1PanelFunc07());
        result.put("09.書政,戶籍辦理他所申請書(W系列產生器)", (T) new V1PanelFunc09());
        result.put("10.書政,戶籍臨時申請書(XLDF產生器-不提供列印欄位)", (T) new V1PanelFunc10());
        result.put("11.RR,RC申請書產生器", (T) new V1PanelFunc11());
        result.put("12.職權更正申請書產生器", (T) new V1PanelFunc12());
        result.put("13.職權更正戶籍資料產生器", (T) new V1PanelFunc13());
        result.put("14.6-4 中文欄位資訊轉換(for DBS)", (T) new V1PanelFunc14());
        result.put("15.RR,RC申請書產生器for職權更正", (T) new V1PanelFunc15());
        result.put("17.6-4 文件欄位搜尋", (T) new V1PanelFunc17());
        result.put("18.6-4 中文欄位資訊轉換摘要(for 2014.09.03)", (T) new V1PanelFunc18());
        return result;
    }

    private <T extends JPanel> void config(final JFrame body, final JMenu funcMenu, final Map<String, T> aMap) {
        List<String> labelList = new ArrayList<String>(aMap.keySet());
        Collections.sort(labelList);
        for (final String label : labelList) {
            final T selectUnit = aMap.get(label);
            body.getContentPane().add(selectUnit, "2, 2, fill, fill");
            selectUnit.setVisible(false);

            JMenuItem menuItem = new JMenuItem(label);
            menuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    for (T unit : aMap.values()) {
                        unit.setVisible(false);
                    }
                    frmRobertsDiscrepancyAnalysis.setTitle(label + "---Robert's  Tool  (ext:3190) ");
                    selectUnit.setVisible(true);
                }
            });
            funcMenu.add(menuItem);
        }
    }
}

class OSValidator {
    public boolean isWindows() {
        String os = System.getProperty("os.name").toLowerCase();
        return (os.indexOf("win") >= 0);
    }

    public boolean isMac() {
        String os = System.getProperty("os.name").toLowerCase();
        // Mac
        return (os.indexOf("mac") >= 0);
    }

    public boolean isUnix() {
        String os = System.getProperty("os.name").toLowerCase();
        // linux or unix
        return (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0);
    }

    public boolean isSolaris() {
        String os = System.getProperty("os.name").toLowerCase();
        // Solaris
        return (os.indexOf("sunos") >= 0);
    }

}
