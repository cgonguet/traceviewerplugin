package com.idea.plugin.traceviewer.core;

import com.intellij.openapi.project.Project;

import java.awt.*;

public interface TraceViewer {

  TraceViewer init(Project project);

  void reset();

  Component getComponent();

  TraceWorker getWorker();

}
