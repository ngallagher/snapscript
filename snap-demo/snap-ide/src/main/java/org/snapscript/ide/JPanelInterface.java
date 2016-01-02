package org.snapscript.ide;

import java.awt.Graphics;

import javax.swing.JPanel;

public interface JPanelInterface {
   void update(JPanel panel, Graphics graphics);
   void paint(JPanel panel, Graphics graphics);
}
