package com.iisi.sd.main.gui.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.filechooser.FileFilter;

import org.apache.log4j.Logger;

import com.iisi.sd.main.gui.DirectionChooserInterface;

public abstract class DirectionChooserActionListener implements ActionListener, DirectionChooserInterface {
    private final Logger log = Logger.getLogger(DirectionChooserActionListener.class);
    private File direction;
    private JLabel label = new JLabel();
    private JProgressBar progressBar = new JProgressBar(0, 100);
    private JComponent component;

    public DirectionChooserActionListener(JComponent component) {
        this.component = component;
    };

    abstract void implementedMethod(final File direction);

    public JLabel getLabel() {
    	if (this.direction != null) {
    	    this.label.setText(this.direction.getAbsolutePath());
		}else{
		    this.label.setText("");
		}
		return this.label;
    }

    @Override
    public JProgressBar getProgressBar() {
        return this.progressBar;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser fc = new JFileChooser();
        if (this.direction != null && this.direction.isDirectory()) {
            fc.setCurrentDirectory(this.direction);
        }
        FileFilter filter = new FileFilter() {
            @Override
            public boolean accept(File f) {
                if (f.isDirectory()) {
                    return true;
                }
                return false;
            }

            @Override
            public String getDescription() {
                return "僅限資料夾";
            }

        };

        fc.setFileFilter(filter);
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnVal = fc.showOpenDialog(this.component);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            this.direction = fc.getSelectedFile();
            this.label.setText(this.direction.getAbsolutePath());
            this.log.debug(this.direction.getAbsolutePath());
            // This is where a real application would open the file.
            this.log.debug("Opening: " + this.direction.getName());

            implementedMethod(this.direction);

        } else {
            this.direction = null;
            // label.setText("");
        }
    }
    protected void setDirection(final File direction){
		this.direction =direction;
	}
    @Override
    public File getFile() {
        return this.direction;
    }

    @Override
    public void clear() {
        this.direction = null;
        this.label.setText("");
        this. progressBar.setValue(0);
    }
}
