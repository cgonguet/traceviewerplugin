package com.nortel.oam.traceviewer.ideaplugin.gui;

import junit.framework.TestCase;

import java.io.File;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: cgonguet
 * Date: 11 juin 2007
 * Time: 16:46:24
 * To change this template use File | Settings | File Templates.
 */
public class TraceFileChooserTest extends TestCase {

  public void testWithDefault() throws Exception {
    File selectedFile = new TraceFileChooser(new File("tmp/cp_utran.traces")).run();
    System.out.println("selectedFile = " + selectedFile);
  }

  public void testWithDir() throws Exception {
    File selectedFile = new TraceFileChooser(new File("tmp")).run();
    System.out.println("selectedFile = " + selectedFile);
  }

}
