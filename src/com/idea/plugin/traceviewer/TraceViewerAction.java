package com.idea.plugin.traceviewer;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataConstants;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.idea.plugin.traceviewer.gui.TraceFileChooser;
import com.idea.plugin.traceviewer.gui.TraceViewerToolWindow;

import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: cgonguet
 * Date: 11 juin 2007
 * Time: 16:00:56
 * To change this template use File | Settings | File Templates.
 */
public class TraceViewerAction extends AnAction {

  public void actionPerformed(AnActionEvent e) {
    try {
      Project project = (Project) e.getDataContext().getData(DataConstants.PROJECT);
      File defaultFile = null;
      defaultFile = TraceViewerComponent.getInstance().init(project);
      new TraceViewerToolWindow(project, defaultFile).open();
    } catch (Exception ex) {
      ex.printStackTrace();
      Messages.showErrorDialog(ex.getMessage(), "Trace Viewer Error");
      return;
    }
  }

}
