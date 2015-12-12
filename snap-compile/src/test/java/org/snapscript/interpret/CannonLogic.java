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
 * Description: Swing application, plot points from data file, convert machine learning class
 *  example to Java implementation.
 * Based on code from: physics for game developers, David Bourg
 * Home Page: http://code.google.com/u/berlin.brown/
 * 
 * Contact: Berlin Brown <berlin dot brown at gmail.com>
 */

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Description: Swing application, plot points for physics simulation.
 * 
 * @author berlin brown
 * 
 */
public class CannonLogic {

    private final PhysicsCannon graphics;

    private List<Double[]> data = new ArrayList<Double[]>();
    private List<Integer[]> renderData = new ArrayList<Integer[]>();

    private double scaleX = 1;
    private double scaleY = 1;

    private double moreOffsetY = 6.0;

    // vm 50, 54
    // alpha 25, 40, 55
    
    private double Vm    = 60;  // m/s
    private double Alpha = 50;  // degrees
    private double Gamma = 0;   // along x-axis
    private double L  = 12;     // m
    private double Yb = 10;     // on x-z plane

    private double time = 0;    // seconds
    private double tInc = 0.05; // seconds
    private double g = 9.8;     // m /(s*s)

    private double lastTimeHitGround = 0;
    private double lastXhitGround = 0;
    
    private double maxHeightCannon = 150.0;

    private Tuple s = new Tuple();

    private List<Integer[]> points = new ArrayList<Integer[]>();

    /**
     * Constructor.
     * 
     * @param graphics
     */
    public CannonLogic(final PhysicsCannon graphics) {
        this.graphics = graphics;
    }

    private class Tuple {
        private double i = 0;
        private double j = 0;
        private double k = 0;

        public String toString() {
            return "{i=" + i + " j=" + j + " k=" + k + "}";
        }
    }

    /**
     * Set scale to convert data point values to values for rendering to screen.
     */
    public void setScale() {
        final double x = graphics.getRenderGridWidth() * 0.8;
        final double y = maxHeightCannon;
        this.scaleX = (graphics.getRenderGridWidth() - 10.0) / x;
        this.scaleY = (graphics.getRenderGridHeight() - 10.0) / y;
        System.out.println("INFO : ScaleX : " + this.scaleX);
    }

    public void loadForRender() {
        this.setScale();
        this.renderData = new ArrayList<Integer[]>();
        for (final Double[] arr : this.data) {
            final Integer a = (int) (arr[0] * this.scaleX) + this.graphics.getOffX();
            final int forReverseY = (int) ((arr[1] + moreOffsetY) * this.scaleY);
            final Integer b = (this.graphics.getRenderGridHeight() - forReverseY) + this.graphics.getOffY();
            final Integer[] d = { a, b };
            renderData.add(d);
        } // End of the for //
    }

    public void render(final Graphics g) {
        g.setColor(Color.red);
        final Integer a = (int) (s.i * this.scaleX) + this.graphics.getOffX();
        final int forReverseY = (int) ((s.j + moreOffsetY) * this.scaleY);
        final Integer b = (this.graphics.getRenderGridHeight() - forReverseY) + this.graphics.getOffY();
        final Integer[] d = { a, b };
        g.fillRect(d[0], d[1], 6, 6);
        points.add(d);
        if (points.size() > 2) {
            Integer[] lastPt = points.get(0);
            for (final Integer[] pt : points) {
                g.fillRect(pt[0], pt[1], 4, 4);
                g.drawLine(pt[0], pt[1], lastPt[0], lastPt[1]);
                lastPt = pt;
            }
        }
    }

    public int doSimulation() {
        double cosX;
        double cosY;
        double cosZ;
        double xe, ze;
        double b, Lx, Ly, Lz;

        // Check for collision with ground (x-z plane)
        
        if (s.j != 0 && s.j <= 6) {
            this.lastTimeHitGround = this.time;
            this.lastXhitGround = s.i;
            
            // Now, restart //
            final Random rand = new Random(System.currentTimeMillis());            
            s = new Tuple();            
            this.Vm += (3.5 - (9.0 * rand.nextDouble()));
            this.Alpha += (3.5 - (9.0 * rand.nextDouble()));            
            this.time = 0;
            return 2;
        }
        
        // Step to the next time in the simulation
        this.time += tInc;

        final double degreeToRadianConv = 3.14 / 180.0;
        // First calculate the direction cosines for the cannon orientation.
        // Projection b of cannon length L,
        // b = L times cos (90 degrees - alpha)
        b = L * Math.cos((90.0 - Alpha) * degreeToRadianConv); // projection of barrel
                                                               // onto x-z plane      
        // Cannon length, xyz.  Lx = b * cos(gamma)
        Lx = b * Math.cos(Gamma * degreeToRadianConv); // x-component of barrel length
        Ly = L * Math.cos(Alpha * degreeToRadianConv); // y-component of barrel length
        Lz = b * Math.sin(Gamma * degreeToRadianConv); // z-component of barrel length

        cosX = Lx / L;
        cosY = Ly / L;
        cosZ = Lz / L;

        // These are the x and z coordinates of the very end of the cannon
        // barrel
        // we'll use these as the initial x and z displacements
        xe = L * Math.cos((90 - Alpha) * degreeToRadianConv) * Math.cos(Gamma * degreeToRadianConv);
        ze = L * Math.cos((90 - Alpha) * degreeToRadianConv) * Math.sin(Gamma * degreeToRadianConv);

        // Now we can calculate the position vector at this time
        // x = vxt = (vm times cos(theta x) * t
        s.i = Vm * cosX * time + xe;
        s.j = (Yb + (L * Math.cos(Alpha * degreeToRadianConv))) + (Vm * cosY * time) - (0.5 * g * (time * time));
        s.k = Vm * cosZ * time + ze;
        // Cutoff the simulation if it's taking too long
        // This is so the program does not get stuck in the while loop
        if (time > 3600) {
            return 3;
        }
        System.out.println("Position Projectile : " + s + " time=" + time);
        return 0;
    } // End of the method, do simulation //

    public String infoDuringSimulation() {
        final StringBuffer buf = new StringBuffer(100);
        buf.append("Time=");
        buf.append(String.format("%.2f", this.time));
        buf.append("s");
        
        buf.append(" X=");
        buf.append(String.format("%.2f", s.i));
        buf.append("m");
        
        buf.append(" Y=");
        buf.append(String.format("%.2f", s.j));
        buf.append("m");
        
        buf.append(" lastTimeHit=");
        buf.append(String.format("%.2f", this.lastTimeHitGround));
        buf.append("s");
        
        buf.append(" lastXhit=");
        buf.append(String.format("%.2f", this.lastXhitGround));
        buf.append("m");
        return buf.toString();
    }
    
} // End of the class //