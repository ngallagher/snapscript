package org.snapscript.core;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.Executor;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import org.snapscript.assemble.InstructionResolver;
import org.snapscript.assemble.ScriptCompiler;
import org.snapscript.assemble.ScriptContext;
import org.snapscript.common.ThreadPool;
import org.snapscript.core.execute.InterpretationResolver;

public class ScriptPad extends JFrame implements ActionListener, DocumentListener {

   private static final Map<String, String> STYLE_WORDS = new LinkedHashMap<String, String>();

   static {
      STYLE_WORDS.put("public", "keyword");
      STYLE_WORDS.put("private", "keyword");
      STYLE_WORDS.put("class", "keyword");
      STYLE_WORDS.put("trait", "keyword");
      STYLE_WORDS.put("function", "keyword");
      STYLE_WORDS.put("abstract", "keyword");
      STYLE_WORDS.put("return", "keyword");
      STYLE_WORDS.put("new", "keyword");
      STYLE_WORDS.put("var", "keyword");
      STYLE_WORDS.put("this", "keyword");
      STYLE_WORDS.put("super", "keyword");
      STYLE_WORDS.put("if", "keyword");
      STYLE_WORDS.put("else", "keyword");
      STYLE_WORDS.put("break", "keyword");
      STYLE_WORDS.put("continue", "keyword");
      STYLE_WORDS.put("while", "keyword");
      STYLE_WORDS.put("for", "keyword");
      STYLE_WORDS.put("static", "keyword");
      STYLE_WORDS.put("const", "keyword");
      STYLE_WORDS.put("import", "keyword");
      STYLE_WORDS.put("throw", "keyword");
      STYLE_WORDS.put("extends", "keyword");
      STYLE_WORDS.put("try", "keyword");
      STYLE_WORDS.put("catch", "keyword");
      STYLE_WORDS.put("finally", "keyword");
      STYLE_WORDS.put("in", "keyword");
      STYLE_WORDS.put("true", "keyword");
      STYLE_WORDS.put("false", "keyword");
      STYLE_WORDS.put("null", "keyword");
      STYLE_WORDS.put("with", "keyword");
      STYLE_WORDS.put("override", "keyword");
      STYLE_WORDS.put("module", "keyword");
      STYLE_WORDS.put("enum", "keyword");
   }

   private Executor executor;
   private CodeHighlighter highlighter;
   private JTextArea output;
   private JTextArea console;
   private JTextPane ta;
   private JMenuBar menuBar;
   private JMenu fileM, editM, programM;
   private JScrollPane scpane;
   private JScrollPane cpane;
   private JScrollPane opane;
   private JMenuItem exitI, cutI, copyI, pasteI, selectI, saveI, openI, newI, runI;
   private String pad;
   private JToolBar toolBar;
   private DefaultStyledDocument doc;
   private Font font;
   private String source;
   private JSplitPane vsplit;
   private JSplitPane hsplit;
   private JSplitPane mainSplit;
   private JTree fileTree;
   private FileSystemModel fileSystemModel;
   private File rootFile;
   private JFileChooser fc;
   private File file;

   public ScriptPad() {
      super("ScriptPad");
      setSize(600, 600);
      setLocationRelativeTo(null);
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      Container pane = getContentPane();
      pane.setLayout(new BorderLayout());

      JPanel codePanel = new JPanel();
      JPanel consolePanel = new JPanel();
      JPanel outputPanel = new JPanel();
      ScriptFileFilter filter = new ScriptFileFilter(".*(\\.js|\\.snap)");
      codePanel.setLayout(new BorderLayout());
      consolePanel.setLayout(new BorderLayout());
      outputPanel.setLayout(new BorderLayout());

      executor = new ThreadPool(1);
      highlighter = new CodeHighlighter(STYLE_WORDS);
      hsplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, consolePanel, outputPanel);
      hsplit.setOneTouchExpandable(true);
      hsplit.setDividerLocation(300);

      vsplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, codePanel, hsplit);
      vsplit.setOneTouchExpandable(true);
      vsplit.setDividerLocation(400);

      rootFile = new File(".");
      fileSystemModel = new FileSystemModel(rootFile, filter);
      fileTree = new JTree(fileSystemModel);
      fileTree.setEditable(true);
      fileTree.addTreeSelectionListener(new TreeSelectionListener() {
         public void valueChanged(TreeSelectionEvent event) {
            File file = (File) fileTree.getLastSelectedPathComponent();
            openFile(file);
         }
      });
      for (int i = 0; i < fileTree.getRowCount(); i++) {
         fileTree.expandRow(i);
      }
      mainSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, new JScrollPane(fileTree), vsplit);

      pane.add(mainSplit, BorderLayout.CENTER);

      // Provide minimum sizes for the two components in the split pane
      codePanel.setMinimumSize(new Dimension(300, 400));
      consolePanel.setMinimumSize(new Dimension(150, 100));
      outputPanel.setMinimumSize(new Dimension(150, 100));

      pad = " ";
      fc = new JFileChooser();
      fc.setFileFilter(filter);
      fc.setCurrentDirectory(rootFile);
      output = new JTextArea();
      console = new JTextArea();
      doc = new CodeDocument();
      font = new Font("Lucida Console", Font.PLAIN, 14);
      ta = new CodePane(doc);
      menuBar = new JMenuBar(); // menubar
      fileM = new JMenu("File"); // file menu
      editM = new JMenu("Edit"); // edit menu
      programM = new JMenu("Program"); // edit menu

      CodeNumberPanel lineNums = new CodeNumberPanel(ta);

      lineNums.setFont(font);
      scpane = new JScrollPane(ta); // scrollpane and add textarea to scrollpane
      scpane.setRowHeaderView(lineNums);
      cpane = new JScrollPane(console); // scrollpane and add textarea to
                                        // scrollpane
      opane = new JScrollPane(output); // scrollpane and add textarea to
                                       // scrollpane
      exitI = new JMenuItem("Exit");
      cutI = new JMenuItem("Cut");
      copyI = new JMenuItem("Copy");
      pasteI = new JMenuItem("Paste");
      selectI = new JMenuItem("Select All"); // menuitems
      newI = new JMenuItem("New");
      saveI = new JMenuItem("Save"); // menuitems
      openI = new JMenuItem("Open"); // menuitems
      runI = new JMenuItem("Run"); // menuitems
      toolBar = new JToolBar();

      ta.setFont(font);
      ta.setForeground(Color.BLACK);
      ta.setBackground(Color.WHITE);
      ta.setCaretColor(Color.BLACK);

      console.setFont(font);
      console.setEditable(false);
      console.setForeground(Color.BLACK);
      console.setBackground(Color.WHITE);
      console.setCaretColor(Color.BLACK);

      output.setFont(font);
      output.setEditable(false);
      output.setForeground(Color.BLACK);
      output.setBackground(Color.LIGHT_GRAY);
      output.setCaretColor(Color.BLACK);

      // ta.setLineWrap(true);
      // ta.setWrapStyleWord(true);

      setJMenuBar(menuBar);
      menuBar.add(fileM);
      menuBar.add(editM);
      menuBar.add(programM);

      fileM.add(newI);
      fileM.add(openI);
      fileM.add(saveI);
      fileM.add(exitI);

      editM.add(cutI);
      editM.add(copyI);
      editM.add(pasteI);
      editM.add(selectI);

      programM.add(runI);

      saveI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
      saveI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
      openI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.CTRL_MASK));
      cutI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
      copyI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
      pasteI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
      selectI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));

      consolePanel.add(cpane, BorderLayout.CENTER);
      codePanel.add(scpane, BorderLayout.CENTER);
      codePanel.add(toolBar, BorderLayout.SOUTH);
      outputPanel.add(opane, BorderLayout.CENTER);

      doc.addDocumentListener(this);
      newI.addActionListener(this);
      saveI.addActionListener(this);
      openI.addActionListener(this);
      exitI.addActionListener(this);
      cutI.addActionListener(this);
      copyI.addActionListener(this);
      pasteI.addActionListener(this);
      selectI.addActionListener(this);
      runI.addActionListener(this);

      Style keyword = doc.addStyle(CodeHighlight.KEYWORD, null);
      Style number = doc.addStyle(CodeHighlight.NUMBER, null);
      Style string = doc.addStyle(CodeHighlight.STRING, null);
      Style normal = doc.addStyle(CodeHighlight.NORMAL, null);
      Style comment = doc.addStyle(CodeHighlight.COMMENT, null);

      StyleConstants.setForeground(keyword, Color.decode("#b03060"));
      StyleConstants.setBold(keyword, true);
      StyleConstants.setForeground(comment, Color.decode("#006400"));
      StyleConstants.setItalic(comment, true);
      StyleConstants.setForeground(number, Color.PINK);
      StyleConstants.setForeground(string, Color.BLUE);
      StyleConstants.setForeground(normal, Color.BLACK);

      setVisible(true);
   }

   private void selectFile(File file) { // does not work
      try {
         File location = file.getAbsoluteFile();
         String path = location.getCanonicalPath();
         List<Integer> navigate = fileSystemModel.getLocation(path);
         JTree tree = fileTree;
         int[] rows = fileTree.getSelectionRows();

         fileTree.setSelectionRow(0);

         for (Integer row : navigate) {
            tree.expandRow(row);
            tree.setSelectionRow(row);
         }
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   private void openFile(File file) {
      try {
         if (file != null && file.isFile()) {
            String source = loadFile(file);
            File location = file.getAbsoluteFile();
            String name = location.getName();
            String path = location.getCanonicalPath();

            setTitle("ScriptPad " + name + " - " + path);
            ta.setText(source);
            this.file = file;

            SwingUtilities.invokeLater(new Runnable() {
               @Override
               public void run() {
                  scpane.getVerticalScrollBar().setValue(0);
               }
            });
         }
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   private void saveFile(File file, String text) {
      try {
         FileOutputStream out = new FileOutputStream(file);

         try {
            out.write(text.getBytes("UTF-8"));
         } finally {
            out.close();
         }
         openFile(file);
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   private String loadFile(File file) throws Exception {
      FileInputStream in = new FileInputStream(file);

      try {
         ByteArrayOutputStream out = new ByteArrayOutputStream();
         byte[] buffer = new byte[1024];
         int count = 0;

         while ((count = in.read(buffer)) != -1) {
            out.write(buffer, 0, count);
         }
         return out.toString("UTF-8");
      } finally {
         in.close();
      }
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      JMenuItem choice = (JMenuItem) e.getSource();
      if (choice == saveI) {
         if (this.file != null) {
            fc.setSelectedFile(this.file);
         }
         int returnVal = fc.showSaveDialog(ScriptPad.this);

         if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            // This is where a real application would open the file.
            if (!file.exists()) {
               file.getParentFile().mkdirs();
            }
            saveFile(file, ta.getText());
         }
      } else if (choice == openI) {
         int returnVal = fc.showOpenDialog(ScriptPad.this);

         if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            // This is where a real application would open the file.
            if (file.exists()) {
               openFile(file);
            }
         }

      } else if (choice == newI) {
         setTitle("ScriptPad");
         ta.setText("");
      } else if (choice == exitI) {
         System.exit(0);
      } else if (choice == cutI) {
         pad = ta.getSelectedText();
         // ta.replaceRange("", ta.getSelectionStart(), ta.getSelectionEnd());
      } else if (choice == copyI) {
         pad = ta.getSelectedText();
      } else if (choice == pasteI) {
         // ta.insert(pad, ta.getCaretPosition());
      } else if (choice == selectI) {
         ta.selectAll();
      } else if (e.getSource() == runI) {
         new Thread(new Runnable() {
            public void run() {
               executeScript();
            }
         }).start();
      }
   }

   public void executeScript() {
      ThreadGroup group = Thread.currentThread().getThreadGroup();
      ScriptRunner runner = new ScriptRunner(source);
      Thread thread = new Thread(group, runner, "ScriptRunner", 512 * 1024 * 1024);

      thread.run();
   }

   @Override
   public void removeUpdate(DocumentEvent e) {
      highlight((DefaultStyledDocument) ta.getDocument());
   }

   @Override
   public void insertUpdate(DocumentEvent e) {
      highlight((DefaultStyledDocument) ta.getDocument());
   }

   @Override
   public void changedUpdate(DocumentEvent e) {
      highlight((DefaultStyledDocument) ta.getDocument());
   }

   // Creates highlights around all occurrences of pattern in textComp
   public void highlight(final DefaultStyledDocument doc) {
      try {
         final int length = doc.getLength();
         final String text = doc.getText(0, length);

         if (source == null || !source.equals(text)) {
            source = text; // set the current text
            SwingUtilities.invokeLater(new Runnable() {
               public void run() {
                  MutableAttributeSet attrs = ta.getInputAttributes();
                  doc.removeDocumentListener(ScriptPad.this);

                  try {
                     long startTime = System.currentTimeMillis();
                     List<CodeHighlight> highlights = highlighter.createHighlights(text);
                     long highlightTime = System.currentTimeMillis();
                     
                     CodeDocument change = new CodeDocument();
                     
                     int pos = ta.getCaretPosition();
                     change.insertString(0, text, attrs);
                     ta.setDocument(change);
                     ta.setCaretPosition(pos);
                     //change.insertString(0, text, attrs);

                     for (CodeHighlight highlight : highlights) {
                        String name = highlight.getStyle();
                        Style style = doc.getStyle(name);
                        int offset = highlight.getOffset();
                        int size = highlight.getLength();

                        doc.setCharacterAttributes(offset, size, style, true);
                        // System.out.print(">>"+highlight.getCode()+"<<");
                     }
                     ta.setDocument(doc);
                     ta.setCaretPosition(pos);
                     //ta.setDocument(doc);
                     long finishTime = System.currentTimeMillis();
                     //System.err.println("Highlight took " + (highlightTime - startTime) + " ms and render took " + (finishTime - highlightTime) + "  ms");
                  } catch (Exception e) {
                     e.printStackTrace();
                  } finally {
                     doc.addDocumentListener(ScriptPad.this);
                  }
               }
            });
         }
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   private class CodePane extends JTextPane {

      public CodePane(StyledDocument doc) {
         super(doc);
      }

      @Override
      public void setText(String text) {
         try {
            AttributeSet attr = getInputAttributes();
            Document doc = getDocument();
            doc.remove(0, doc.getLength());
            if (text == null || text.equals("")) {
               return;
            }
            text = text.replace("\t", "   ");
            text = text.replace("\r\n", "\n");
            doc.insertString(0, text, attr);
         } catch (BadLocationException ble) {
            UIManager.getLookAndFeel().provideErrorFeedback(this);
         }
      }

      // wrap text https://tips4java.wordpress.com/2009/01/25/no-wrap-text-pane/
      public boolean getScrollableTracksViewportWidth() {
         return getUI().getPreferredSize(this).width <= getParent().getSize().width;
      }
   }
   
   private class CodeDocument extends DefaultStyledDocument {
      
      @Override
      public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
         if (str != null && str.length() > 0 && str.indexOf('\t') != -1) {
            str = str.replaceAll("\t", "   ");
         }
         super.insertString(offs, str, a);
      }
   };

   private class ConsoleWriter {

      private final Executor executor;
      private final JTextArea console;

      public ConsoleWriter(Executor executor, JTextArea console) {
         this.executor = executor;
         this.console = console;
      }

      public void log(final Object text) {
         executor.execute(new Runnable() {
            public void run() {
               String original = console.getText();
               if (original != null && original.length() > 0) {
                  console.setText(original + "\r\n" + text);
               } else {
                  console.setText("" + text);
               }
            }
         });
      }
   }

   private class FileSystemModel implements TreeModel {

      private FilenameFilter filter;
      private Vector listeners;
      private File root;

      public FileSystemModel(File root, FilenameFilter filter) {
         this.listeners = new Vector();
         this.filter = filter;
         this.root = root;
      }

      public List<Integer> getLocation(String location) {
         List<Integer> list = new ArrayList<Integer>();
         File file = new File(location);

         try {
            String full = file.getCanonicalPath();
            String prefix = root.getCanonicalPath();
            int length = prefix.length();
            String path = "." + full.substring(length);
            String[] segments = path.split("\\\\");
            String prev = segments[0];

            for (int i = 0; i < segments.length; i++) {
               File current = new File(root, prev + "/" + segments[i]);

               if (current.isDirectory()) {
                  String[] children = current.list(filter);
                  boolean found = false;

                  for (int j = 0; j < children.length; j++) {
                     String child = children[j];

                     if (child.equals(segments[i + 1])) {
                        list.add(j);
                        found = true;
                        break;
                     }
                  }
                  if (!found) {
                     break;
                  }
               }
               prev = prev + "/" + segments[i];
            }
         } catch (Exception e) {
            e.printStackTrace();
         }
         return list;
      }

      @Override
      public Object getRoot() {
         return root;
      }

      @Override
      public Object getChild(Object parent, int index) {
         File directory = (File) parent;
         String[] children = directory.list(filter);
         return new TreeFile(directory, children[index]);
      }

      @Override
      public int getChildCount(Object parent) {
         File file = (File) parent;
         if (file.isDirectory()) {
            String[] fileList = file.list(filter);
            if (fileList != null)
               return file.list(filter).length;
         }
         return 0;
      }

      @Override
      public boolean isLeaf(Object node) {
         File file = (File) node;
         return file.isFile();
      }

      @Override
      public int getIndexOfChild(Object parent, Object child) {
         File directory = (File) parent;
         File file = (File) child;
         String[] children = directory.list(filter);
         for (int i = 0; i < children.length; i++) {
            if (file.getName().equals(children[i])) {
               return i;
            }
         }
         return -1;

      }

      @Override
      public void valueForPathChanged(TreePath path, Object value) {
         File oldFile = (File) path.getLastPathComponent();
         String fileParentPath = oldFile.getParent();
         String newFileName = (String) value;
         File targetFile = new File(fileParentPath, newFileName);
         oldFile.renameTo(targetFile);
         File parent = new File(fileParentPath);
         int[] changedChildrenIndices = new int[] { getIndexOfChild(parent, targetFile) };
         Object[] changedChildren = new Object[] { targetFile };
         fireTreeNodesChanged(path.getParentPath(), changedChildrenIndices, changedChildren);

      }

      private void fireTreeNodesChanged(TreePath parentPath, int[] indices, Object[] children) {
         TreeModelEvent event = new TreeModelEvent(this, parentPath, indices, children);
         Iterator iterator = listeners.iterator();
         TreeModelListener listener = null;
         while (iterator.hasNext()) {
            listener = (TreeModelListener) iterator.next();
            listener.treeNodesChanged(event);
         }
      }

      @Override
      public void addTreeModelListener(TreeModelListener listener) {
         listeners.add(listener);
      }

      @Override
      public void removeTreeModelListener(TreeModelListener listener) {
         listeners.remove(listener);
      }

      private class TreeFile extends File {
         public TreeFile(File parent, String child) {
            super(parent, child);
         }

         public String toString() {
            return getName();
         }
      }
   }

   private class ScriptFileFilter extends FileFilter implements FilenameFilter {

      private final String fileFilter;

      public ScriptFileFilter(String fileFilter) {
         this.fileFilter = fileFilter;
      }

      @Override
      public boolean accept(File dir, String name) {
         File file = new File(dir, name);

         if (file.isDirectory()) {
            return true;
         }
         if (fileFilter != null) {
            return name != null && name.matches(fileFilter);
         }
         return true;
      }

      @Override
      public boolean accept(File path) {
         String name = path.getName();
         File parent = path.getParentFile();

         return accept(parent, name);
      }

      public String getDescription() {
         return "Script File";
      }
   }

   private class ScriptRunner implements Runnable {

      private final String source;

      public ScriptRunner(String source) {
         this.source = source;
      }

      @Override
      public void run() {
         ConsoleWriter consoleWriter = new ConsoleWriter(executor, console);
         ConsoleWriter outputWriter = new ConsoleWriter(executor, output);
         console.setText("");
         output.setText("");
         try {

            InstructionResolver set = new InterpretationResolver();
            Context context =new ScriptContext(set);
            ScriptCompiler compiler = new ScriptCompiler(context);
            Map<String, Object> map = new HashMap<String, Object>();
            Model model = new MapModel(map);
            map.put("console", consoleWriter);
            long start = System.currentTimeMillis();
            Executable executable = compiler.compile(source);
            long finish = System.currentTimeMillis();
            long duration = finish - start;
            outputWriter.log("Time taken to compile was " + duration + " ms, size was " + source.length());
            start = System.currentTimeMillis();
            executable.execute(model);
            finish = System.currentTimeMillis();
            duration = finish - start;
            outputWriter.log("Time taken to execute was " + duration + " ms");
         } catch (Exception e) {
            StringWriter w = new StringWriter();
            PrintWriter p = new PrintWriter(w);
            e.printStackTrace(p);
            p.flush();
            outputWriter.log(w.toString());
            e.printStackTrace(System.err);
         }
      }
   }

   public static void main(String[] args) throws Exception {
      String lookAndFeel = "Metal";

      if (args.length > 0) {
         lookAndFeel = args[0];
      }
      if (lookAndFeel.equals("Metal")) {
         UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
      } else if (lookAndFeel.equals("System")) {
         UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      } else if (lookAndFeel.equals("Motif")) {
         UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
      } else if (lookAndFeel.equals("GTK")) {
         UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
      } else {
         UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      }
      new ScriptPad();
   }
}