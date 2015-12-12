package org.snapscript.interpret;

/**
 * Copyright (c) 2006-2011 Berlin Brown.  All Rights Reserved
 *
 * http://www.opensource.org/licenses/bsd-license.php
 * All rights reserved.
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * * Neither the name of the Botnode.com (Berlin Brown) nor
 * the names of its contributors may be used to endorse or promote
 * products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Date: 8/15/2011
 *  
 * Description: Swing application, plot points for physics simulation.
 * 
 * Based on code from: physics for game developers, David Bourg
 *
 * Home Page: http://code.google.com/u/berlin.brown/
 * 
 * Contact: Berlin Brown <berlin dot brown at gmail.com>
 */

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.Date;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * Convert machine learning class, gradient descent example to Java
 * implementation. This class contains the main entry point, Swing setup,
 * graphics logic.
 * 
 * @author berlin brown
 * 
 */
public class PhysicsCannon {

    private final int w = 790;
    private final int h = 820;

    private JFrame frame;
    public int counter = 0;
    public UpdatableCanvas canvas;

    public int maxGridRows = 32;
    public int maxGridCols = 32;
    public int sizeCell = 20;

    public final int offX = 30;
    public final int offY = 30;

    public final int perCellOffset = 2;

    public int renderGridWidth = 0;
    public int renderGridHeight = 0;

    public int timeInMsForRenderUpdate = 80;

    public CannonLogic data = new CannonLogic(this);


    /**
     * Launch the 2D frame window.
     */
    public void invokeLater() {
        SwingUtilities.invokeLater(new InitializerTask(this));
    }

    public int getMaxGridRows() {
        return maxGridRows;
    }

    public int getMaxGridCols() {
        return maxGridCols;
    }

    public int getOffX() {
        return offX;
    }

    public int getOffY() {
        return offY;
    }

    public int getRenderGridWidth() {
        return renderGridWidth;
    }

    public int getRenderGridHeight() {
        return renderGridHeight;
    }

    /**
     * Create application. Do not extend the JFrame class.
     */
    public void initializeApplication() {

        final int szrows = (maxGridRows * (sizeCell + perCellOffset));
        final int szcols = (maxGridCols * (sizeCell + perCellOffset));

        this.frame = new JFrame("Simple Physics Cannon Test - " + new Date());
        frame.setLocation(200, 200);
        frame.setPreferredSize(new Dimension(w, h));
        frame.setSize(new Dimension(w, h));
        frame.pack();
        frame.setResizable(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        /*
         * Add default panel.
         */
        final JPanel panel = new JPanel();
        canvas = new UpdatableCanvas(this);
        panel.setVisible(true);
        panel.setPreferredSize(new Dimension(w, h));
        panel.setFocusable(true);
        panel.setBackground(Color.black);

        /*
         * Add default canvas.
         */
        canvas.panel.setPreferredSize(new Dimension(w, h));
        canvas.panel.setSize(new Dimension(w, h));
        canvas.panel.setBackground(Color.black);
        panel.add(canvas.panel);

        // Panel setup, toggle visibility on frame, set visible
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);

        PhysicsCannon.this.renderGridWidth = szcols;
        PhysicsCannon.this.renderGridHeight = szrows;

        data.setScale();
        data.loadForRender();
        this.startServerWait();
    }

    protected void startServerWait() {
        try {
            final TimerCounterTask waitOnApprTimerTask = new TimerCounterTask(this);
            final ScheduledThreadPoolExecutor waitOnApprTimer = new ScheduledThreadPoolExecutor(2);
            
            waitOnApprTimer.scheduleAtFixedRate(waitOnApprTimerTask, 0, 2 * 1000, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Main entry point for application.
     * 
     * @param args
     */
    public static void main(final String[] args) {
        System.out.println("Running");
        final PhysicsCannon o = new PhysicsCannon();
        o.invokeLater();
        System.out.println("Done");
    }

} 

class InitializerTask implements Runnable {
   private final PhysicsCannon cannon;
   public InitializerTask(PhysicsCannon cannon) {
      this.cannon = cannon;
   }
   public void run() {
      cannon.initializeApplication();
  }
}

class TimerCounterTask implements Runnable {
   private final PhysicsCannon cannon;
   public TimerCounterTask(PhysicsCannon cannon) {
      this.cannon = cannon;
   }
   public void run() {
      System.out.println("Still waiting ... " + cannon.counter);
      cannon.counter++;
  }
}
/**
 * Canvas.
 */
class UpdatableCanvas implements JPanelInterface {

    private Image offScreenImage = null;
    private Graphics offScreenGraphics = null;
    private Image offScreenImageDrawed = null;
    private Graphics offScreenGraphicsDrawed = null;

    private final ScheduledThreadPoolExecutor timer = new ScheduledThreadPoolExecutor(2);

    private int canvasCounter = 0;
    private PhysicsCannon cannon;
    public JPanelAdapter panel;
    
    public UpdatableCanvas(PhysicsCannon cannon) {
       this.panel = new JPanelAdapter(this);
       this.cannon = cannon;
        timer.scheduleAtFixedRate(new UpdatableCanvasTask(this), 0, cannon.timeInMsForRenderUpdate, TimeUnit.MILLISECONDS);
    }


    /**
     * Render the cell line grid.
     */
    public void renderCellLineGrid(final Graphics g) {

        final Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(1));

        final int szrows = (cannon.maxGridRows * (cannon.sizeCell + cannon.perCellOffset));
        final int szcols = (cannon.maxGridCols * (cannon.sizeCell + cannon.perCellOffset));
        g.setColor(Color.GREEN);
        for (int i = 0; i < cannon.maxGridRows + 1; i++) {
            g.drawLine(cannon.offX + (i * (cannon.sizeCell + cannon.perCellOffset)), cannon.offY + 0, cannon.offX + (i * (cannon.sizeCell + cannon.perCellOffset)), cannon.offY
                    + szrows);
        } // End of the For //
        for (int j = 0; j < cannon.maxGridCols + 1; j++) {
            g.drawLine(cannon.offX + 0, cannon.offY + (j * (cannon.sizeCell + cannon.perCellOffset)), cannon.offX + szcols, cannon.offY
                    + (j * (cannon.sizeCell + cannon.perCellOffset)));
        }
        final int xEndLow = cannon.offX + szcols;
        final int yEndLow = cannon.offY + szrows;

        g.setColor(Color.MAGENTA);
        g.drawString("X", cannon.offX, cannon.offY);
        g.drawString("Z" + xEndLow, xEndLow, cannon.offY);
        g.drawString("A" + yEndLow, cannon.offX, yEndLow);
    }

    /**
     * Use double buffering.
     * 
     * @see java.awt.Component#update(java.awt.Graphics)
     */
    @Override
    public void update(final JPanel panel, final Graphics g) {
        final Dimension d = cannon.canvas.panel.getSize();
        if (offScreenImage == null) {
            offScreenImage = cannon.canvas.panel.createImage(d.width, d.height);
            offScreenGraphics = offScreenImage.getGraphics();
        }
        cannon.canvas.panel.paint(offScreenGraphics);
        g.drawImage(offScreenImage, 0, 0, null);
    }

    /**
     * Draw this generation.
     * 
     * @see java.awt.Component#paint(java.awt.Graphics)
     */
    @Override
    public void paint(final JPanel panel, final Graphics g) {
        // Draw grid on background image, which is faster
        if (offScreenImageDrawed == null) {
            final Dimension d = panel.getSize();
            offScreenImageDrawed = panel.createImage(d.width, d.height);
            offScreenGraphicsDrawed = offScreenImageDrawed.getGraphics();
            offScreenGraphicsDrawed.setColor(Color.black);                
            offScreenGraphicsDrawed.fillRect(0, 0, d.width, d.height);
            renderCellLineGrid(offScreenGraphicsDrawed);
        }
        g.drawImage(offScreenImageDrawed, 0, 0, null);
        cannon.data.render(g);
        g.setColor(Color.GREEN);
        g.drawString("UpdatableCanvas:" + canvasCounter, 6, 14);            
        
        g.drawString(cannon.data.infoDuringSimulation(), 200, 14);
        
        canvasCounter++;
        cannon.data.doSimulation();
        cannon.data.render(g);
    }

}


/**
 * Timer task, refresh canvas.
 */
class UpdatableCanvasTask implements Runnable  {
    
    private final UpdatableCanvas canvas;
    
    public UpdatableCanvasTask(UpdatableCanvas canvas) {
       this.canvas = canvas;
    }

    @Override
    public void run() {
        if (!EventQueue.isDispatchThread()) {
            EventQueue.invokeLater(this);
        } else {
           canvas.panel.repaint();
        }
    }
} // End of the Class //