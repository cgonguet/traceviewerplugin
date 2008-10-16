package com.nortel.oam.traceviewer.ideaplugin.core;

import com.intellij.execution.ui.ConsoleViewContentType;

public interface TraceWorker {
  void print(String str, ConsoleViewContentType style);

  void ignore(String str);
}
