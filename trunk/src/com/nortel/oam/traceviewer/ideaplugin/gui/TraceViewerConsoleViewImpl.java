package com.nortel.oam.traceviewer.ideaplugin.gui;

import com.intellij.execution.filters.TextConsoleBuilder;
import com.intellij.execution.filters.TextConsoleBuilderFactory;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.openapi.project.Project;
import com.nortel.oam.traceviewer.ideaplugin.core.TraceViewer;
import com.nortel.oam.traceviewer.ideaplugin.core.TraceWorker;

import java.awt.*;

public class TraceViewerConsoleViewImpl implements TraceViewer {
  private ConsoleViewTraceWorker worker;
  private ConsoleView consoleView;

  public TraceViewerConsoleViewImpl init(Project project) {
    TextConsoleBuilder consoleBuilder = TextConsoleBuilderFactory.getInstance().createBuilder(project);
    consoleView = consoleBuilder.getConsole();

    worker = new ConsoleViewTraceWorker();
    return this;
  }

  public void reset() {
    consoleView.clear();
  }

  public Component getComponent() {
    return consoleView.getComponent();
  }

  public TraceWorker getWorker() {
    return worker;
  }

  class ConsoleViewTraceWorker implements TraceWorker {

    public void print(String str, ConsoleViewContentType style) {
      consoleView.print(str, style);
    }

    public void ignore(String str) {
    }
  }

}
