package com.iisi.sd.main.gui;

import java.io.File;

import javax.swing.JProgressBar;

public interface DirectionChooserInterface {
    public File getFile();

    public JProgressBar getProgressBar();

    public void clear();
}
