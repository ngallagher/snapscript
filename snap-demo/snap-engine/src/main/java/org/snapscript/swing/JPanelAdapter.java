package org.snapscript.swing;

import java.awt.Graphics;

import javax.swing.JPanel;

public class JPanelAdapter extends JPanel {

   private final JPanelInterface panel;
   
   public JPanelAdapter(JPanelInterface panel) {
      this.panel = panel;
   }
   
   @Override
   public void update(final Graphics g) {
      panel.update(this, g);
   }
   
   @Override
   public void paint(final Graphics g) {
      panel.paint(this, g);
   }
}
