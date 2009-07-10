package com.idea.plugin.traceviewer.core;

import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.idea.plugin.traceviewer.TraceViewerComponent;

import javax.swing.text.BadLocationException;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TracesAnalyzerEngine {

  static final String LINE_SEPARATOR = "%BR%";

  private File file;
  private Project project;
  private TraceViewer viewer;
  private boolean resume;
  private boolean filter;
  private Map<String, Key> keys;
  List<TraceStyle> styles;
  String traceFormat;
  boolean printByBlock;
  int loaded;

  public TracesAnalyzerEngine(Project project, final File file, TraceViewer ui, boolean resume, boolean filter
  )
          throws IOException {
    this.file = file;
    this.project = project;
    this.viewer = ui;
    this.resume = resume;
    this.filter = filter;
  }

  public void run() throws IOException, BadLocationException {
    viewer.reset();

    styles = TraceViewerComponent.getInstance().getTraceStyles();
    traceFormat = TraceViewerComponent.getInstance().getTraceFormat();
    printByBlock = TraceViewerComponent.getInstance().getPrintByBlock();

    registerStyles();

    ProgressManager.getInstance()
            .runProcessWithProgressSynchronously(getTracesAnalyzer(), "Load trace file", true, project);

  }

  public int getLoaded() {
    if (loaded > 100) {
      loaded = 100;
    }
    return loaded;
  }

  TracesAnalyzer getTracesAnalyzer() {
    return new TracesAnalyzer(viewer.getWorker(),
                              new ProgressIndicatorManager() {
                                ProgressIndicator indicator;

                                public void init() {
                                  indicator = ProgressManager.getInstance().getProgressIndicator();
                                  indicator.setIndeterminate(false);
                                  indicator.setText("Load file");
                                }

                                public void setProgress(double fraction) {
                                  indicator.setFraction(fraction);
                                  loaded = (int) (fraction * 100);
                                }

                                public boolean isCanceled() {
                                  return indicator.isCanceled();
                                }
                              });
  }

  void registerStyles() {
    keys = new HashMap<String, Key>();
    int i = 0;
    for (TraceStyle traceStyle : styles) {
      Logger.getInstance(TracesAnalyzerEngine.class.getName()).info("Register " + traceStyle);
      Key key = new Key(String.valueOf(i++));
      keys.put(traceStyle.toString(), key);
      ConsoleViewContentType.registerNewConsoleViewType(key,
                                                        new ConsoleViewContentType(traceStyle.toString(),
                                                                                   traceStyle.style) {
                                                        });
    }
  }


  public class TracesAnalyzer implements Runnable {
    double maxLine;
    private double curLine;
    private TraceWorker worker;
    private ProgressIndicatorManager progress;

    public TracesAnalyzer(TraceWorker worker, ProgressIndicatorManager progress) {
      this.worker = worker;
      this.progress = progress;
    }

    public void run() {
      progress.init();
      if (file != null) {
        try {
          countLines();
          if (maxLine == 0) {
            loaded = 100;
            return;
          }
          loadText();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }

    void countLines() throws IOException {
      FileReader fileReader = new FileReader(file);
      LineNumberReader lineReader = new LineNumberReader(fileReader);
      lineReader.skip(file.length());
      maxLine = lineReader.getLineNumber();
      fileReader.close();
      lineReader.close();
    }


    private void loadText() throws Exception {

      FileReader reader = new FileReader(file);
      BufferedReader in = new BufferedReader(reader);
      registerStyles();

      while (printLine(in)) {
      }

      reader.close();
      in.close();
    }

    boolean printLine(BufferedReader in) {
      try {
        String readline = readLine(in);
        if (readline == null) {
          return false;
        }

        String buf = readline;
        if (printByBlock) {
          if (isTraceLine(readline)) {
            buf += "\n" + bufferizeBlock(in);
          } else {
            buf += "\n";
          }
        }

        for (TraceStyle traceStyle : styles) {
          String regexp = computeTraceRegexp(traceFormat, traceStyle.regexp);

          if (buf.replaceAll("\n", LINE_SEPARATOR).matches(regexp)) {
            if (resume && !traceStyle.resume) {
              worker.ignore(buf);
              return true;
            }
            if (filter && traceStyle.filter) {
              worker.ignore(buf);
              return true;
            }
            if (!printByBlock) {
              buf += "\n";
            }
            worker.print(buf, ConsoleViewContentType.getConsoleViewType(keys.get(traceStyle.toString())));
            return true;
          }
        }

        if (resume) {
          worker.ignore(buf);
          return true;
        }

        if (!printByBlock) {
          buf += "\n";
        }
        worker.print(buf, ConsoleViewContentType.NORMAL_OUTPUT);
        return true;
      }
      catch (IOException e) {
        Logger.getInstance(TracesAnalyzerEngine.class.getName()).error(e);
      }
      return false;
    }

    private String readLine(BufferedReader in) throws IOException {
      progress.setProgress(curLine++ / maxLine);
      if (progress.isCanceled()) {
        return null;
      }
      return in.readLine();
    }

    private String bufferizeBlock(BufferedReader in) throws IOException {
      String buf = "";
      String line;
      boolean inBlock = true;
      do {
        in.mark(100000);
        line = readLine(in);
        if (line == null
            || isTraceLine(line)) {
          inBlock = false;
        } else {
          buf += line + "\n";
        }
      } while (inBlock);

      try {
        in.reset();
      } catch (IOException e) {
        Logger.getInstance(TracesAnalyzerEngine.class.getName()).warn("Cannot reset the BufferedReader. Probably due to very long line.",e);
       }
      curLine--;
      return buf;
    }

    private boolean isTraceLine(String line) {
      return line.matches(traceFormat + ".*");
    }


    private String computeTraceRegexp(String traceFormat, String regexp) {
      return traceFormat + regexp + "$";
    }
  }

  public interface ProgressIndicatorManager {
    void init();

    void setProgress(double fraction);

    boolean isCanceled();
  }
}

