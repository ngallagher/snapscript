package org.snapscript.web.resource.tree;

import java.io.PrintStream;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.snapscript.web.resource.Resource;

public class TreeResource implements Resource {

   @Override
   public void handle(Request request, Response response) throws Throwable {
      String tree =
            "<div id='tree'>"+
                  "    <ul id='treeData' style='display: none;'>"+
                  "      <li id='id1' title='Look, a tool tip!'>item1 with key and tooltip"+
                  "      <li id='id2'>item2"+
                  "      <li id='id3' class='folder'>Folder <em>with some</em> children"+
                  "        <ul>"+
                  "          <li id='id3.1'>Sub-item 3.1"+
                  "            <ul>"+
                  "              <li id='id3.1.1'>Sub-item 3.1.1"+
                  "              <li id='id3.1.2'>Sub-item 3.1.2"+
                  "            </ul>"+
                  "          <li id='id3.2'>Sub-item 3.2"+
                  "            <ul>"+
                  "              <li id='id3.2.1'>Sub-item 3.2.1"+
                  "              <li id='id3.2.2'>Sub-item 3.2.2"+
                  "            </ul>"+
                  "        </ul>"+
                  "      <li id='id4' class='expanded'>Document with some children (expanded on init)"+
                  "        <ul>"+
                  "          <li id='id4.1'  class='active focused'>Sub-item 4.1 (active and focus on init)"+
                  "            <ul>"+
                  "              <li id='id4.1.1'>Sub-item 4.1.1"+
                  "              <li id='id4.1.2'>Sub-item 4.1.2"+
                  "            </ul>"+
                  "          <li id='id4.2'>Sub-item 4.2"+
                  "            <ul>"+
                  "              <li id='id4.2.1'>Sub-item 4.2.1"+
                  "              <li id='id4.2.2'>Sub-item 4.2.2"+
                  "            </ul>"+
                  "        </ul>"+
                  "    </ul>"+
                  "  </div>";


      PrintStream out = response.getPrintStream();
      response.setContentType("text/html");
      out.println(tree);
      out.close();
   }

}
