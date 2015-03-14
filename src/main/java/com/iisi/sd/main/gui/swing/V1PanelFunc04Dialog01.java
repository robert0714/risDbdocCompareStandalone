package com.iisi.sd.main.gui.swing;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import org.robert.study.utils.DataSourceConfig;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class V1PanelFunc04Dialog01 extends JDialog {
    private final JPanel parent;
    private final JPanel contentPanel = new JPanel();
    private JTextField userNameTextField;
    private JTextField userPasswordTextField;
    private final JTextArea jdbcURLTextArea = new JTextArea();

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        // try {
        // V1PanelFunc4Dialog1 dialog = new V1PanelFunc4Dialog1();
        // dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        // dialog.setVisible(true);
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
    }

    /**
     * Create the dialog.
     */
    public V1PanelFunc04Dialog01(final JPanel parent) {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                SwingUtilities.getWindowAncestor(parent).setEnabled(true);
            }
        });
        this.parent = parent;
        setBounds(100, 100, 450, 300);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(new FormLayout(new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC,
                FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"), },
                new RowSpec[] { FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
                        RowSpec.decode("default:grow"), FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC, }));
        {
            JLabel userNameLabel = new JLabel("1.資料庫使用者名稱:");
            contentPanel.add(userNameLabel, "2, 2, right, default");
        }
        {
            userNameTextField = new JTextField();
            contentPanel.add(userNameTextField, "4, 2, fill, default");
            userNameTextField.setColumns(10);
        }
        {
            JLabel userPasswordLabel = new JLabel("2.資料庫使用者密碼:");
            contentPanel.add(userPasswordLabel, "2, 4, right, default");
        }
        {
            userPasswordTextField = new JTextField();
            contentPanel.add(userPasswordTextField, "4, 4, fill, default");
            userPasswordTextField.setColumns(10);
        }
        {
            JLabel lblNewLabel = new JLabel("3.資料庫連線URL:");
            contentPanel.add(lblNewLabel, "2, 6, left, top");
        }
        {
            JButton restoreDefaultButton = new JButton("還原預設值");
            restoreDefaultButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    org.robert.study.utils.DataSourceConfig dataSourceConfig = org.robert.study.utils.DataSourceConfig
                            .getInstance();
                    userNameTextField.setText(dataSourceConfig.getDefaulUsername());
                    userPasswordTextField.setText(dataSourceConfig.getDefaulPassword());
                    jdbcURLTextArea.setText(dataSourceConfig.getDefaulUrl());
                }
            });
            {
                jdbcURLTextArea.setLineWrap(true);

                contentPanel.add(new JScrollPane(jdbcURLTextArea), "4, 6, 1, 11, fill, fill");
            }
            contentPanel.add(restoreDefaultButton, "4, 18, left, bottom");
        }
        {
            JPanel buttonPane = new JPanel();
            buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
            getContentPane().add(buttonPane, BorderLayout.SOUTH);
            {
                JButton okButton = new JButton("OK");
                okButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        org.robert.study.utils.DataSourceConfig dataSourceConfig = org.robert.study.utils.DataSourceConfig
                                .getInstance();
                        Map<String, String> data = dataSourceConfig.getGeneralConfig();
                        data.put(DataSourceConfig.JDBC_USERNAME, userNameTextField.getText());
                        data.put(DataSourceConfig.JDBC_PASSWORD, userPasswordTextField.getText());
                        data.put(DataSourceConfig.JDBC_URL, jdbcURLTextArea.getText());
                        dataSourceConfig.saveGeneralConfig(data);
                        SwingUtilities.getWindowAncestor(parent).setEnabled(true);
                        V1PanelFunc04Dialog01.this.dispose();
                    }
                });
                okButton.setActionCommand("OK");
                buttonPane.add(okButton);
                getRootPane().setDefaultButton(okButton);
            }
            {
                JButton cancelButton = new JButton("Cancel");
                cancelButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        SwingUtilities.getWindowAncestor(parent).setEnabled(true);
                        V1PanelFunc04Dialog01.this.dispose();

                    }
                });
                cancelButton.setActionCommand("Cancel");
                buttonPane.add(cancelButton);
            }
        }
        loadConfig();
    }

    private void loadConfig() {
        org.robert.study.utils.DataSourceConfig dataSourceConfig = org.robert.study.utils.DataSourceConfig
                .getInstance();
        Map<String, String> data = dataSourceConfig.getGeneralConfig();
        String username = data.get(DataSourceConfig.JDBC_USERNAME);
        String password = data.get(DataSourceConfig.JDBC_PASSWORD);
        String url = data.get(DataSourceConfig.JDBC_URL);

        userNameTextField.setText(username);
        userPasswordTextField.setText(password);
        jdbcURLTextArea.setText(url);

    }
}
