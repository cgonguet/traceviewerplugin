package com.idea.plugin.traceviewer.gui;

import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.wm.ToolWindow;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: cgonguet
 * Date: 21 juin 2007
 * Time: 16:19:13
 * To change this template use File | Settings | File Templates.
 */
public class TraceFileChooser extends JFileChooser {
  private File file;
  private Component parent;

  public TraceFileChooser(Component parent, File file) {
    super(file);
    this.parent = parent;
    this.file = file;
  }

  public File run() {
    setDialogTitle("Choose your trace file");
    if (file.exists() && file.isFile()) {
      setSelectedFile(file);
    }
    setFileSelectionMode(JFileChooser.FILES_ONLY);
    addChoosableFileFilter(new FileFilter() {
      public boolean accept(File file) {
        return isTraceFile(file)
               || file.isDirectory();
      }

      public String getDescription() {
        return "Traces";
      }
    });

    int choice = showDialog(parent, "OK");
    if (choice == JFileChooser.APPROVE_OPTION) {
      return getSelectedFile();
    }
    return null;
  }

  private boolean isTraceFile(File file) {
    return file.getName().matches(".*\\.(tra|log).*");
  }

  static Icon TRACE_FILE_ICON = IconLoader.getIcon("icons/lamp_16.png");

  public Icon getIcon(File f) {
    if (isTraceFile(f)) {
      return TRACE_FILE_ICON;
    }
    return super.getIcon(f);
  }
}
