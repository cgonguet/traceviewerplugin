package com.idea.plugin.traceviewer.gui;

import com.idea.plugin.traceviewer.core.TraceViewer;
import com.idea.plugin.traceviewer.core.TracesAnalyzerEngine;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowAnchor;
import com.intellij.openapi.wm.ToolWindowManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

public class TraceViewerToolWindow extends JPanel {
  private Project project;
  private static final String WINDOW_ID = "TraceViewer";
  private JTabbedPane tabbedPane;
  private ArrayList<TabData> tabs;
  private File defaultFile;
  private ToolWindow toolWindow;


  TraceViewer getTraceViewer() {
    return new TraceViewerConsoleViewImpl().init(project);
  }

  public TraceViewerToolWindow(Project project, File defaultFile) {
    this.defaultFile = defaultFile;
    this.project = project;
    init();
  }

  private void init() {
    ToolWindowManager manager = ToolWindowManager.getInstance(project);
    toolWindow = manager.getToolWindow(WINDOW_ID);
    if (toolWindow != null) {
      manager.unregisterToolWindow(WINDOW_ID);
    }
    toolWindow = manager.registerToolWindow(WINDOW_ID, this, ToolWindowAnchor.BOTTOM);
    toolWindow.setTitle(WINDOW_ID);
    toolWindow.setIcon(IconLoader.getIcon("icons/lamp_16.png"));

    tabs = new ArrayList<TabData>();
    createUI();
  }

  public void createUI() {
    setLayout(new BorderLayout());

    JToolBar tb = new JToolBar(JToolBar.VERTICAL);
    tb.setFloatable(false);

    JButton openButton = new JButton(IconLoader.getIcon("icons/open.png"));
    openButton.setToolTipText("Open");
    openButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent actionEvent) {
        open();
      }
    });
    tb.add(openButton);

    JButton closeButton = new JButton(IconLoader.getIcon("icons/close.png"));
    closeButton.setToolTipText("Close");
    closeButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent actionEvent) {
        int selected = tabbedPane.getSelectedIndex();
        if (selected == -1) {
          return;
        }
        close(selected);
      }
    });

    tb.add(closeButton);
    JButton refreshButton = new JButton(IconLoader.getIcon("icons/refresh.png"));
    refreshButton.setToolTipText("Refresh");
    refreshButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent actionEvent) {
        int selected = tabbedPane.getSelectedIndex();
        if (selected == -1) {
          return;
        }
        loadFileInTab(selected);
      }
    });
    tb.add(refreshButton);

    JButton resumeButton = new JButton(IconLoader.getIcon("icons/resume.png"));
    resumeButton.setToolTipText("Resume");
    resumeButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent actionEvent) {
        int selected = tabbedPane.getSelectedIndex();
        if (selected == -1) {
          return;
        }
        resume(selected);
      }
    });
    tb.add(resumeButton);

    JButton filterButton = new JButton(IconLoader.getIcon("icons/blue_arrow.png"));
    filterButton.setToolTipText("Filter");
    filterButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent actionEvent) {
        int selected = tabbedPane.getSelectedIndex();
        if (selected == -1) {
          return;
        }
        filter(selected);
      }
    });
    tb.add(filterButton);

    add(tb, BorderLayout.WEST);

    tabbedPane = new JTabbedPane();
    add(tabbedPane, BorderLayout.CENTER);
  }

  private void addNewTab(String name, File file, TabData.Status status) {
    TraceViewer viewer = getTraceViewer();
    tabbedPane.addTab(name, viewer.getComponent());
    int index = tabbedPane.getTabCount() - 1;
    tabbedPane.setSelectedIndex(index);
    TabData data = new TabData(file, viewer, status);
    tabs.add(data);
    loadFileInTab(index);
  }

  private void loadFileInTab(int index) {

    TabData data = tabs.get(index);
    if (data == null) {
      return;
    }

    try {
      TracesAnalyzerEngine engine =
              new TracesAnalyzerEngine(project, data.file, data.viewer, data.isResume(), data.isFilter());
      engine.run();
      tabbedPane.setToolTipTextAt(index, "Loaded: " + engine.getLoaded() + "%");
      if (engine.getLoaded() != 100) {
        tabbedPane.setIconAt(index, IconLoader.getIcon("icons/warning.gif"));
      } else {
        tabbedPane.setIconAt(index, null);
      }
    } catch (Exception e) {
      Messages.showErrorDialog(e.getMessage(), "Error");
    }
    tabbedPane.setSelectedIndex(index);
  }

  private boolean showTabIfAlreadyOpened(File file, TabData.Status status) {
    int pos = 0;
    for (TabData data : tabs) {
      if (data.file.equals(file) && data.status.equals(status)) {
        loadFileInTab(pos);
        return true;
      }
      pos++;
    }
    return false;
  }

  private void close(int selected) {
    tabbedPane.remove(selected);
    tabs.remove(selected);
  }

  public void open() {
    File file = new TraceFileChooser(toolWindow.getComponent(), defaultFile).run();
    if (file == null) {
      return;
    }

    if (!file.exists()) {
      Messages.showErrorDialog("Cannot open file: " + file, "Error");
      return;
    }

    toolWindow.show(new Runnable() {
      public void run() {
      }
    });

    if (showTabIfAlreadyOpened(file, TabData.Status.MAIN)) {
      return;
    }

    defaultFile = file;

    addNewTab(file.getName(), file, TabData.Status.MAIN);
  }

  private void filter(int selected) {
    TabData data = tabs.get(selected);
    if (data == null) {
      return;
    }

    if (showTabIfAlreadyOpened(data.file, TabData.Status.FILTER)) {
      return;
    }

    try {
      File file = data.file;
      addNewTab("(F) " + file.getName(), file, TabData.Status.FILTER);
    } catch (Exception e) {
      Messages.showErrorDialog(e.getMessage(), "Error");
    }
  }

  private void resume(int selected) {
    TabData data = tabs.get(selected);
    if (data == null) {
      return;
    }

    if (showTabIfAlreadyOpened(data.file, TabData.Status.RESUME)) {
      return;
    }

    try {
      File file = data.file;
      addNewTab("(R) " + file.getName(), file, TabData.Status.RESUME);
    } catch (Exception e) {
      Messages.showErrorDialog(e.getMessage(), "Error");
    }
  }

  static class TabData {
    enum Status {
      MAIN,
      RESUME,
      FILTER
    }

    File file;
    private TraceViewer viewer;
    Status status;

    public TabData(File file, TraceViewer viewer, Status status) {
      this.file = file;
      this.viewer = viewer;
      this.status = status;
    }

    public boolean isResume() {
      return status.equals(Status.RESUME);
    }

    public boolean isFilter() {
      return status.equals(Status.FILTER);
    }
  }
}
