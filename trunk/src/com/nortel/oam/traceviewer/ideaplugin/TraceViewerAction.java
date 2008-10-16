package com.nortel.oam.traceviewer.ideaplugin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataConstants;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.nortel.oam.traceviewer.ideaplugin.gui.TraceFileChooser;
import com.nortel.oam.traceviewer.ideaplugin.gui.TraceViewerToolWindow;

import java.io.File;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: cgonguet
 * Date: 11 juin 2007
 * Time: 16:00:56
 * To change this template use File | Settings | File Templates.
 */
public class TraceViewerAction extends AnAction {
  public void actionPerformed(AnActionEvent e) {
    Project project = (Project) e.getDataContext().getData(DataConstants.PROJECT);

    File defaultFile = null;
    try {
      defaultFile = TraceViewerComponent.getInstance().init(project);
    } catch (Exception ex) {
      Messages.showErrorDialog(ex.getMessage(), "Trace Viewer Error");
      return;
    }
    open(project, defaultFile);
  }

  public static void open(Project project, File defaultFile) {
    try {
      File selectedFile = new TraceFileChooser(defaultFile).run();
      if(selectedFile == null){
        return;
      }

      TraceViewerToolWindow.getInstance(project).open(selectedFile);

    } catch (Throwable ex) {
      ex.printStackTrace();
      Messages.showErrorDialog(ex.getMessage(), "Trace Viewer Error");
    }
  }

}
