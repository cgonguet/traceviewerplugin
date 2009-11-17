package com.idea.plugin.traceviewer.gui;

import junit.framework.TestCase;

import javax.swing.*;
import java.io.File;

public class TraceFileChooserTest extends TestCase {

  public void testWithDefault() throws Exception {
    File selectedFile = new TraceFileChooser(new JFrame(),new File("tmp/cp_utran.traces")).run();
    System.out.println("selectedFile = " + selectedFile);
  }

  public void testWithDir() throws Exception {
    File selectedFile = new TraceFileChooser(new JFrame(),new File("tmp")).run();
    System.out.println("selectedFile = " + selectedFile);
  }

}
