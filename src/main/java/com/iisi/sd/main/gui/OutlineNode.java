package com.iisi.sd.main.gui;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

/**
 * Simple TreeNode that builds children on the fly. The key idea is that
 * getChildCount is always called before any actual children are requested. So
 * getChildCount builds the children if they don't already exist.
 * <P>
 * In this case, it just builds an "outline" tree. Ie if the root is current
 * node is "x", the children are "x.0", "x.1", "x.2", and "x.3".
 * <P>
 * 1999 Marty Hall, http://www.apl.jhu.edu/~hall/java/
 */

public class OutlineNode extends DefaultMutableTreeNode {
    private boolean areChildrenDefined = false;
    private int outlineNum;
    private int numChildren;

    public OutlineNode(int outlineNum, int numChildren) {
        this.outlineNum = outlineNum;
        this.numChildren = numChildren;
    }

    public boolean isLeaf() {
        return (false);
    }

    public int getChildCount() {
        if (!areChildrenDefined)
            defineChildNodes();
        return (super.getChildCount());
    }

    private void defineChildNodes() {
        // You must set the flag before defining children if you
        // use "add" for the new children. Otherwise you get an infinite
        // recursive loop, since add results in a call to getChildCount.
        // However, you could use "insert" in such a case.
        areChildrenDefined = true;
        for (int i = 0; i < numChildren; i++)
            add(new OutlineNode(i + 1, numChildren));
    }

    public String toString() {
        TreeNode parent = getParent();
        if (parent == null)
            return (String.valueOf(outlineNum));
        else
            return (parent.toString() + "." + outlineNum);
    }
}
