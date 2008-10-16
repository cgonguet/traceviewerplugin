package com.nortel.oam.traceviewer.ideaplugin.gui;

import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.ui.DialogWrapper;
import com.nortel.oam.traceviewer.ideaplugin.TraceViewerComponent;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class ConfigurationDialog extends DialogWrapper {

  public ConfigurationDialog() {
    super(true);
  }

  @Nullable
  protected JComponent createCenterPanel() {
    setTitle("TraceViewer configuration");
    JComponent component = TraceViewerComponent.getInstance().createComponent();
    TraceViewerComponent.getInstance().reset();
    return component;
  }

  protected void doOKAction() {
    try {
      TraceViewerComponent.getInstance().apply();
    } catch (ConfigurationException e) {
      e.printStackTrace();
    }
    super.doOKAction();
  }

  protected void dispose() {
    TraceViewerComponent.getInstance().disposeUIResources();
    super.dispose();
  }

  public void execute(){
    init();
    show();
  }
}
