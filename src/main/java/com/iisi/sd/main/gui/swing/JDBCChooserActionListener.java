package com.iisi.sd.main.gui.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.sql.DataSource;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.slf4j.Logger;

import com.iisi.sd.main.gui.DirectionChooserInterface;

public abstract class JDBCChooserActionListener implements ActionListener {
    private final Logger log = LoggerFactory.getLogger(JDBCChooserActionListener.class);
    private BasicDataSource dataSource  ;
    private JLabel label = new JLabel();
    private JProgressBar progressBar = new JProgressBar(0, 100);
    private JComponent component;

    public JDBCChooserActionListener(JComponent component) {
        this.component = component;
    };

    abstract void implementedMethod(final BasicDataSource dataSource);

    public JLabel getLabel() {
    	if (dataSource != null) {
			label.setText(dataSource.getUrl());
		}else{
			label.setText("");
		}
		return label;
    }

    public JProgressBar getProgressBar() {
        return progressBar;
    }

    public void actionPerformed(ActionEvent e) {
	implementedMethod(dataSource);
    }
    protected void setDataSource(final BasicDataSource direction){
		this.dataSource =direction;
	}
    public BasicDataSource getDataSource() {
        return dataSource;
    }

    public void clear() {
	dataSource = null;
        label.setText("");
        progressBar.setValue(0);
    }
}
