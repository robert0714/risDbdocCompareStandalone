package com.iisi.sd.main.gui.swing;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTree;

public class TestPanel extends JPanel {

    /**
     * Create the panel.
     */
    public TestPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JPanel panel = new JPanel();
        add(panel);

        JSplitPane splitPane = new JSplitPane();
        panel.add(splitPane);

        JTree tree = new JTree();
        splitPane.setLeftComponent(tree);

    }

}
