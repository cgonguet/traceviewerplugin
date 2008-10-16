package com.idea.plugin.traceviewer.core;

import junit.framework.TestCase;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import com.intellij.execution.ui.ConsoleViewContentType;

public class TracesAnalyzerEngineTest extends TestCase {

  public void testPrintLine() throws Exception {
    MyEngine engine = new MyEngine(false, false);
    engine.traceFormat = "NEW TRACE - ";
    engine.styles = new ArrayList<TraceStyle>();
    engine.styles.add(new TraceStyle("ERROR.+", Color.red, Color.WHITE, Font.BOLD, true, false));
    engine.styles.add(new TraceStyle("WARNING.+", Color.orange, Color.WHITE, Font.BOLD, true, false));
    engine.styles.add(new TraceStyle("DEBUG.+", Color.black, Color.WHITE, Font.PLAIN, false, true));
    engine.styles.add(new TraceStyle("INFO.*%BR%.*TADA.*", Color.black, Color.WHITE, Font.PLAIN, false, false));
    engine.registerStyles();

    engine.printByBlock = true;

    String text = "NEW TRACE - ERROR - (1) this is an error\n" +
                  "(1.1) in block ERROR\n" +
                  "(1.2) exception here ...\n" +
                  "NEW TRACE - INFO - (2) this is an info\n" +
                  "(2.1) blablabla\n" +
                  "(2.2) TADA\n" +
                  "(2.3) bidibidibidi\n" +
                  "NEW TRACE - WARNING - (3) this is a warning\n" +
                  "(3.1) warning continue at this line\n\n" +
                  "NEW TRACE - DEBUG - (4) this is a debug\n" +
                  "(4.1) in block DEBUG\n" +
                  "NEW TRACE - INFO - (5) this is an info\n" +
                  "NOT A TRACE - ERROR - (6) this is not an error\n";

    BufferedReader reader = new BufferedReader(new StringReader(text));
    TracesAnalyzerEngine.TracesAnalyzer textLoader = engine.getTracesAnalyzer();
    textLoader.countLines();
    while (textLoader.printLine(reader)) {
    }

  }

  class MyEngine extends TracesAnalyzerEngine {

    public MyEngine(boolean resume, boolean filter)
            throws IOException {
      super(null, null, null, resume, filter);
    }


    TracesAnalyzer getTracesAnalyzer() {
      return new TracesAnalyzer(new TraceWorker(){

        public void print(String str, ConsoleViewContentType style) {
          System.out.println("PRINT WITH STYLE: " + style);
          System.out.println("--> " + str);
        }

        public void ignore(String str) {
          System.out.println("IGNORE: " + str);
        }
      }, new ProgressIndicatorManager(){

        public void init() {
        }

        public void setProgress(double fraction) {
          System.out.println("progress = " + fraction);
        }

        public boolean isCanceled() {
          return false;
        }
      }){

        void countLines() throws IOException {
           maxLine = 14;
        }
      };
    }

  }


  public void testRegexpByBlock() throws Exception {
    String block = "NEW TRACE - INFO - begin here\n" +
                  "continue and print this: \n" +
                  "TADA TADA\n" +
                  "...\n";

    String blockline = block.replaceAll("\n", "%BR%");
    System.out.println("blockline = " + blockline);
    if(blockline.matches("^NEW TRACE - INFO - .+%BR%TADA TADA.*")){
      System.out.println("match");
    }else{
      System.out.println("no");
    }
  }

}
