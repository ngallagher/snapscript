package org.snapscript.interpret;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.StreamTokenizer;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * A fairly conventional 3D matrix object that can transform sets of 3D points
 * and perform a variety of manipulations on the transform
 */
class Matrix3D {
   float xx;
   float xy;
   float xz;
   float xo;
   float yx;
   float yy;
   float yz;
   float yo;
   float zx;
   float zy;
   float zz;
   float zo;
   static final double pi = 3.14159265;

   /** Create a new unit matrix */
   Matrix3D() {
      xx = 1.0f;
      yy = 1.0f;
      zz = 1.0f;
   }

   /** Scale by f in all dimensions */
   void scale(float f) {
      xx *= f;
      xy *= f;
      xz *= f;
      xo *= f;
      yx *= f;
      yy *= f;
      yz *= f;
      yo *= f;
      zx *= f;
      zy *= f;
      zz *= f;
      zo *= f;
   }

   /** Scale along each axis independently */
   void scale(float xf, float yf, float zf) {
      xx *= xf;
      xy *= xf;
      xz *= xf;
      xo *= xf;
      yx *= yf;
      yy *= yf;
      yz *= yf;
      yo *= yf;
      zx *= zf;
      zy *= zf;
      zz *= zf;
      zo *= zf;
   }

   /** Translate the origin */
   void translate(float x, float y, float z) {
      xo += x;
      yo += y;
      zo += z;
   }

   /** rotate theta degrees about the y axis */
   void yrot(double theta) {
      theta *= (pi / 180);
      double ct = Math.cos(theta);
      double st = Math.sin(theta);

      float Nxx = (float) (xx * ct + zx * st);
      float Nxy = (float) (xy * ct + zy * st);
      float Nxz = (float) (xz * ct + zz * st);
      float Nxo = (float) (xo * ct + zo * st);

      float Nzx = (float) (zx * ct - xx * st);
      float Nzy = (float) (zy * ct - xy * st);
      float Nzz = (float) (zz * ct - xz * st);
      float Nzo = (float) (zo * ct - xo * st);

      xo = Nxo;
      xx = Nxx;
      xy = Nxy;
      xz = Nxz;
      zo = Nzo;
      zx = Nzx;
      zy = Nzy;
      zz = Nzz;
   }

   /** rotate theta degrees about the x axis */
   void xrot(double theta) {
      theta *= (pi / 180);
      double ct = Math.cos(theta);
      double st = Math.sin(theta);

      float Nyx = (float) (yx * ct + zx * st);
      float Nyy = (float) (yy * ct + zy * st);
      float Nyz = (float) (yz * ct + zz * st);
      float Nyo = (float) (yo * ct + zo * st);

      float Nzx = (float) (zx * ct - yx * st);
      float Nzy = (float) (zy * ct - yy * st);
      float Nzz = (float) (zz * ct - yz * st);
      float Nzo = (float) (zo * ct - yo * st);

      yo = Nyo;
      yx = Nyx;
      yy = Nyy;
      yz = Nyz;
      zo = Nzo;
      zx = Nzx;
      zy = Nzy;
      zz = Nzz;
   }

   /** rotate theta degrees about the z axis */
   void zrot(double theta) {
      theta *= (pi / 180);
      double ct = Math.cos(theta);
      double st = Math.sin(theta);

      float Nyx = (float) (yx * ct + xx * st);
      float Nyy = (float) (yy * ct + xy * st);
      float Nyz = (float) (yz * ct + xz * st);
      float Nyo = (float) (yo * ct + xo * st);

      float Nxx = (float) (xx * ct - yx * st);
      float Nxy = (float) (xy * ct - yy * st);
      float Nxz = (float) (xz * ct - yz * st);
      float Nxo = (float) (xo * ct - yo * st);

      yo = Nyo;
      yx = Nyx;
      yy = Nyy;
      yz = Nyz;
      xo = Nxo;
      xx = Nxx;
      xy = Nxy;
      xz = Nxz;
   }

   /** Multiply this matrix by a second: M = M*R */
   void mult(Matrix3D rhs) {
      float lxx = xx * rhs.xx + yx * rhs.xy + zx * rhs.xz;
      float lxy = xy * rhs.xx + yy * rhs.xy + zy * rhs.xz;
      float lxz = xz * rhs.xx + yz * rhs.xy + zz * rhs.xz;
      float lxo = xo * rhs.xx + yo * rhs.xy + zo * rhs.xz + rhs.xo;

      float lyx = xx * rhs.yx + yx * rhs.yy + zx * rhs.yz;
      float lyy = xy * rhs.yx + yy * rhs.yy + zy * rhs.yz;
      float lyz = xz * rhs.yx + yz * rhs.yy + zz * rhs.yz;
      float lyo = xo * rhs.yx + yo * rhs.yy + zo * rhs.yz + rhs.yo;

      float lzx = xx * rhs.zx + yx * rhs.zy + zx * rhs.zz;
      float lzy = xy * rhs.zx + yy * rhs.zy + zy * rhs.zz;
      float lzz = xz * rhs.zx + yz * rhs.zy + zz * rhs.zz;
      float lzo = xo * rhs.zx + yo * rhs.zy + zo * rhs.zz + rhs.zo;

      xx = lxx;
      xy = lxy;
      xz = lxz;
      xo = lxo;

      yx = lyx;
      yy = lyy;
      yz = lyz;
      yo = lyo;

      zx = lzx;
      zy = lzy;
      zz = lzz;
      zo = lzo;
   }

   /** Reinitialize to the unit matrix */
   void unit() {
      xo = 0;
      xx = 1;
      xy = 0;
      xz = 0;
      yo = 0;
      yx = 0;
      yy = 1;
      yz = 0;
      zo = 0;
      zx = 0;
      zy = 0;
      zz = 1;
   }

   /**
    * Transform nvert points from v into tv. v contains the input coordinates in
    * floating point. Three successive entries in the array constitute a point.
    * tv ends up holding the transformed points as integers; three successive
    * entries per point
    */
   void transform(float v[], int tv[], int nvert) {
      float lxx = xx;
      float lxy = xy;
      float lxz = xz;
      float lxo = xo;
      float lyx = yx;
      float lyy = yy;
      float lyz = yz;
      float lyo = yo;
      float lzx = zx;
      float lzy = zy;
      float lzz = zz;
      float lzo = zo;
      int i = nvert * 3;
      i -= 3; 
      while(i >= 0) {
         float x = v[i];
         float y = v[i + 1];
         float z = v[i + 2];
         tv[i] = Math.round(x * lxx + y * lxy + z * lxz + lxo);
         tv[i + 1] = Math.round(x * lyx + y * lyy + z * lyz + lyo);
         tv[i + 2] = Math.round(x * lzx + y * lzy + z * lzz + lzo);
         i -= 3;
      }
   }

   public String toString() {
      return ("[" + xo + "," + xx + "," + xy + "," + xz + ";" + yo + "," + yx + "," + yy + "," + yz + ";" + zo + "," + zx + "," + zy + "," + zz + "]");
   }
}

/** The representation of a 3D model */
class Model3D {
   float vert[];
   int tvert[];
   int nvert, maxvert;
   int con[];
   int ncon, maxcon;
   boolean transformed;
   Matrix3D mat;

   float xmin;
   float xmax;
   float ymin;
   float ymax;
   float zmin;
   float zmax;

   Model3D() {
      mat = new Matrix3D();
      mat.xrot(20);
      mat.yrot(30);
   }

   /** Create a 3D model by parsing an input stream */
   Model3D(InputStream is) throws IOException {
      this();
      parse(is);
   }

   void parse(InputStream is) throws IOException {
      StreamTokenizer st = new StreamTokenizer(is);
      st.eolIsSignificant(true);
      st.commentChar('#');
      try {
         while (true) {
            int token = st.nextToken();

            if (token == StreamTokenizer.TT_EOL) {
               continue;
            } else if (token == StreamTokenizer.TT_WORD) {
               if ("v".equals(st.sval)) {
                  double x = 0.0;
                  double y = 0.0;
                  double z = 0.0;
                  if (st.nextToken() == StreamTokenizer.TT_NUMBER) {
                     x = st.nval;
                     if (st.nextToken() == StreamTokenizer.TT_NUMBER) {
                        y = st.nval;
                        if (st.nextToken() == StreamTokenizer.TT_NUMBER)
                           z = st.nval;
                     }
                  }
                  addVert((float) x, (float) y, (float) z);
                  while (st.ttype != StreamTokenizer.TT_EOL && st.ttype != StreamTokenizer.TT_EOF)
                     st.nextToken();
               } else if ("f".equals(st.sval) || "fo".equals(st.sval) || "l".equals(st.sval)) {
                  int start = -1;
                  int prev = -1;
                  int n = -1;
                  while (true)
                     if (st.nextToken() == StreamTokenizer.TT_NUMBER) {
                        n = (int) st.nval;
                        if (prev >= 0)
                           add(prev - 1, n - 1);
                        if (start < 0)
                           start = n;
                        prev = n;
                     } else if (st.ttype == '/')
                        st.nextToken();
                     else
                        break;
                  if (start >= 0)
                     add(start - 1, prev - 1);
                  if (st.ttype != StreamTokenizer.TT_EOL)
                     return;
               } else {
                  while (st.nextToken() != StreamTokenizer.TT_EOL && st.ttype != StreamTokenizer.TT_EOF){}
                     
               }
            }else {
               break;
            }
         }
      } finally {
         is.close();
         if (st.ttype != StreamTokenizer.TT_EOF) {
            throw new IOException(st.toString());
         }
      }
   }

   /** Add a vertex to this model */
   int addVert(float x, float y, float z) {
      int i = nvert;
      if (i >= maxvert)
         if (vert == null) {
            maxvert = 100;
            vert = new float[maxvert * 3];
         } else {
            maxvert *= 2;
            float nv[] = new float[maxvert * 3];
            System.arraycopy(vert, 0, nv, 0, vert.length);
            vert = nv;
         }
      i *= 3;
      vert[i] = x;
      vert[i + 1] = y;
      vert[i + 2] = z;
      return nvert++;
   }

   /** Add a line from vertex p1 to vertex p2 */
   void add(int p1, int p2) {
      int i = ncon;
      if (p1 >= nvert || p2 >= nvert)
         return;
      if (i >= maxcon)
         if (con == null) {
            maxcon = 100;
            con = new int[maxcon];
         } else {
            maxcon *= 2;
            int nv[] = new int[maxcon];
            System.arraycopy(con, 0, nv, 0, con.length);
            con = nv;
         }
      if (p1 > p2) {
         int t = p1;
         p1 = p2;
         p2 = t;
      }
      con[i] = (p1 << 16) | p2;
      ncon = i + 1;
   }

   /** Transform all the points in this model */
   void transform() {
      if (transformed || nvert <= 0)
         return;
      if (tvert == null || tvert.length < nvert * 3)
         tvert = new int[nvert * 3];
      mat.transform(vert, tvert, nvert);
      transformed = true;
   }

   /*
    * Quick Sort implementation
    */
   private void quickSort(int a[], int left, int right) {
      int leftIndex = left;
      int rightIndex = right;
      int partionElement;
      if (right > left) {

         /*
          * Arbitrarily establishing partition element as the midpoint of the
          * array.
          */
         partionElement = a[(left + right) / 2];

         // loop through the array until indices cross
         while (leftIndex <= rightIndex) {
            /*
             * find the first element that is greater than or equal to the
             * partionElement starting from the leftIndex.
             */
            while ((leftIndex < right) && (a[leftIndex] < partionElement))
               ++leftIndex;

            /*
             * find an element that is smaller than or equal to the
             * partionElement starting from the rightIndex.
             */
            while ((rightIndex > left) && (a[rightIndex] > partionElement))
               --rightIndex;

            // if the indexes have not crossed, swap
            if (leftIndex <= rightIndex) {
               swap(a, leftIndex, rightIndex);
               ++leftIndex;
               --rightIndex;
            }
         }

         /*
          * If the right index has not reached the left side of array must now
          * sort the left partition.
          */
         if (left < rightIndex)
            quickSort(a, left, rightIndex);

         /*
          * If the left index has not reached the right side of array must now
          * sort the right partition.
          */
         if (leftIndex < right)
            quickSort(a, leftIndex, right);

      }
   }

   private void swap(int a[], int i, int j) {
      int T;
      T = a[i];
      a[i] = a[j];
      a[j] = T;
   }

   /** eliminate duplicate lines */
   void compress() {
      int limit = ncon;
      int c[] = con;
      quickSort(con, 0, ncon - 1);
      int d = 0;
      int pp1 = -1;
      for (int i = 0; i < limit; i++) {
         int p1 = c[i];
         if (pp1 != p1) {
            c[d] = p1;
            d++;
         }
         pp1 = p1;
      }
      ncon = d;
   }

   static Color gr[];

   /**
    * Paint this model to a graphics context. It uses the matrix associated with
    * this model to map from model space to screen space. The next version of
    * the browser should have double buffering, which will make this *much*
    * nicer
    */
   void paint(Graphics g) {
      if (vert == null || nvert <= 0)
         return;
      transform();
      if (gr == null) {
         gr = new Color[16];
         for (int i = 0; i < 16; i++) {
            int grey = (int) (170 * (1 - Math.pow(i / 15.0, 2.3)));
            gr[i] = new Color(grey, grey, grey);
         }
      }
      int lg = 0;
      int lim = ncon;
      int c[] = con;
      int v[] = tvert;
      if (lim <= 0 || nvert <= 0)
         return;
      for (int i = 0; i < lim; i++) {
         int T = c[i];
         int p1 = ((T >> 16) & 0xFFFF) * 3;
         int p2 = (T & 0xFFFF) * 3;
         int grey = v[p1 + 2] + v[p2 + 2];
         if (grey < 0)
            grey = 0;
         if (grey > 15)
            grey = 15;
         if (grey != lg) {
            lg = grey;
            g.setColor(gr[grey]);
         }
         g.drawLine(v[p1], v[p1 + 1], v[p2], v[p2 + 1]);
      }
   }

   /** Find the bounding box of this model */
   void findBB() {
      if (nvert <= 0)
         return;
      float v[] = vert;
      float xmin = v[0];
      float xmax = xmin;
      float ymin = v[1];
      float ymax = ymin;
      float zmin = v[2];
      float zmax = zmin;
      for (int i = nvert * 3; (i -= 3) > 0;) {
         float x = v[i];
         if (x < xmin)
            xmin = x;
         if (x > xmax)
            xmax = x;
         float y = v[i + 1];
         if (y < ymin)
            ymin = y;
         if (y > ymax)
            ymax = y;
         float z = v[i + 2];
         if (z < zmin)
            zmin = z;
         if (z > zmax)
            zmax = z;
      }
      this.xmax = xmax;
      this.xmin = xmin;
      this.ymax = ymax;
      this.ymin = ymin;
      this.zmax = zmax;
      this.zmin = zmin;
   }
}

/** An applet to put a 3D model into a page */
public class ThreeD implements JPanelInterface, MouseMotionListener {
   Model3D md;
   boolean painted = true;
   float xfac;
   int prevx, prevy;
   float xtheta, ytheta;
   float scalefudge = 1;
   Matrix3D amat = new Matrix3D(), tmat = new Matrix3D();
   String message = null;
   JPanel panel;

   public void init(JPanel panel) {
      try {
         scalefudge = Float.valueOf("0.8").floatValue();
      } catch (Exception e) {
      }
      ;
      amat.yrot(20);
      amat.xrot(20);
      this.panel = panel;
      panel.setVisible(true);
      panel.setSize(new Dimension(600, 600));
      InputStream is = null;
      try {
         Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
         is = ThreeD.class.getClassLoader().getResourceAsStream("wireframe/cube.obj");
         Model3D m = new Model3D(is);
         md = m;
         m.findBB();
         m.compress();
         float xw = m.xmax - m.xmin;
         float yw = m.ymax - m.ymin;
         float zw = m.zmax - m.zmin;
         if (yw > xw)
            xw = yw;
         if (zw > xw)
            xw = zw;
         float f1 = panel.getSize().width / xw;
         float f2 = panel.getSize().height / xw;
         xfac = 0.7f * (f1 < f2 ? f1 : f2) * scalefudge;
      } catch (Exception e) {
         md = null;
         message = e.toString();
      }
      try {
         if (is != null)
            is.close();
      } catch (Exception e) {
         e.printStackTrace();
      }
      panel.repaint();
   }

   @Override
   public void mouseMoved(MouseEvent e) {
      prevx = e.getX();
      prevy = e.getY();
   }

   public void mouseDragged(MouseEvent e) {
      int x = e.getX();
      int y = e.getY();
      tmat.unit();
      float xtheta = (prevy - y) * 360.0f / panel.getSize().width;
      float ytheta = (x - prevx) * 360.0f / panel.getSize().height;
      tmat.xrot(xtheta);
      tmat.yrot(ytheta);
      amat.mult(tmat);
      if (painted) {
         painted = false;
         panel.repaint();
      }
      prevx = x;
      prevy = y;
   }

   @Override
   public void update(JPanel panel, Graphics g) {
      paint(panel, g);
   }

   @Override
   public void paint(JPanel panel, Graphics g) {
      g.clearRect(0, 0, panel.getSize().width, panel.getSize().height);
      if (md != null) {
         md.mat.unit();
         md.mat.translate(-(md.xmin + md.xmax) / 2, -(md.ymin + md.ymax) / 2, -(md.zmin + md.zmax) / 2);
         md.mat.mult(amat);
         // md.mat.scale(xfac, -xfac, 8 * xfac / size().width);
         md.mat.scale(xfac, -xfac, 16 * xfac / panel.getSize().width);
         md.mat.translate(panel.getSize().width / 2, panel.getSize().height / 2, 8);
         md.transformed = false;
         md.paint(g);
         painted = true;
      } else if (message != null) {
         g.drawString("Error in model:", 3, 20);
         g.drawString(message, 10, 40);
      }
   }

   public static void main(String[] list) throws Exception {
      ThreeD d = new ThreeD();
      JPanel panel = new JPanelAdapter(d);
      JFrame f = new JFrame();
      d.init(panel);
      f.add(panel);
      f.getContentPane().addMouseMotionListener(d);
      f.setSize(new Dimension(600, 600));
      f.setVisible(true);

   }
   // private synchronized void waitPainted() {
   // while (!painted)
   // wait();
   // painted = false;
   // }

}