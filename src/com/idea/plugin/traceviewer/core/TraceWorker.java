package com.idea.plugin.traceviewer.core;

import com.intellij.execution.ui.ConsoleViewContentType;

public interface TraceWorker {
  void print(String str, ConsoleViewContentType style);

  void ignore(String str);
}
