package com.idea.plugin.traceviewer;

import com.idea.plugin.traceviewer.core.TraceStyle;
import com.idea.plugin.traceviewer.gui.TraceViewerToolWindow;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.*;
import org.jdom.Element;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: cgonguet
 * Date: 11 juin 2007
 * Time: 15:40:43
 * To change this template use File | Settings | File Templates.
 */
public class TraceViewerComponent implements ProjectComponent, Configurable, JDOMExternalizable {

  public String reposDir;
  public String defaultFile = "";

  public String traceFormat = "^[0-9]+-[0-9]+-[0-9]+ [0-9]+:[0-9]+:[0-9]+,[0-9]+ ";
  public List<TraceStyle> traceStyles = new ArrayList<TraceStyle>();
  public boolean printByBlock = true;

  {
    traceStyles.add(new TraceStyle("ERROR.+", Color.RED, Color.WHITE, Font.BOLD, true, false));
    traceStyles.add(new TraceStyle("WARN.+TEST.+", Color.YELLOW, Color.BLUE, Font.BOLD, true, false));
    traceStyles.add(new TraceStyle("WARN.+", Color.ORANGE, Color.WHITE, Font.BOLD, true, false));
    traceStyles.add(new TraceStyle("DEBUG.+", Color.GRAY, Color.WHITE, Font.ITALIC, false, true));
  }

  String absoluteReposDir;

  private TraceViewerConfigurationForm form;

  private static final String TRACESTYLES_ELEMENT = "tracestyles";
  private static final String TRACESTYLE_ELEMENT_PREFIX = "tracestyle_";

  private static TraceViewerComponent theInstance = null;
  protected TraceViewerToolWindow toolWindow;

  private TraceViewerComponent() {
  }

  public static TraceViewerComponent getInstance() {
    if (theInstance == null) {
      Logger.getInstance(TraceViewerComponent.class.getName()).info("getInstance(): create new instance");
      theInstance = new TraceViewerComponent();
    }
    return theInstance;
  }

  public void initComponent() {
    Logger.getInstance(TraceViewerComponent.class.getName()).info("initComponent()");
    theInstance = this;
  }

  public void disposeComponent() {
    Logger.getInstance(TraceViewerComponent.class.getName()).info("disposeComponent()");
    theInstance = null;
  }

  public synchronized void init(Project project) throws Exception {
    if (toolWindow == null) {
      toolWindow = new TraceViewerToolWindow(project, getFileOrDirToOpen(project));
    }
    toolWindow.open();
  }

  private File getFileOrDirToOpen(Project project) throws Exception {
    File absoluteDefaultFile = new File(reposDir + File.separator + defaultFile);
    if (absoluteDefaultFile.exists() && absoluteDefaultFile.isFile()) {
      return absoluteDefaultFile;
    }

    if (reposDir == null) {
      String projectPath = getProjectPath(project);
      reposDir = projectPath + "/tmp";
      if (!new File(reposDir).exists()) {
        reposDir = projectPath;
      }
    }

    File dir = new File(reposDir);
    if (dir.exists()) {
      return dir;
    }

    Logger.getInstance(TraceViewerComponent.class.getName())
            .warn("getFileOrDirToOpen(): cannot open default dir " + dir.getAbsolutePath());
    throw new Exception("The default directory is not valid.\nGo to 'Settings/TraceViewer'.");
  }

  public String getComponentName() {
    return "TraceViewerPlugin";
  }

  public String getReposDir() {
    return reposDir;
  }

  public void setReposDir(final String reposDir) {
    this.reposDir = reposDir;
  }

  public String getDefaultFile() {
    return defaultFile;
  }

  public void setDefaultFile(final String file) {
    this.defaultFile = file;
  }


  public boolean getPrintByBlock() {
    return printByBlock;
  }

  public void setPrintByBlock(boolean printByBlock) {
    this.printByBlock = printByBlock;
  }

  public String getTraceFormat() {
    return traceFormat;
  }

  public void setTraceFormat(final String traceFormat) {
    this.traceFormat = traceFormat;
  }

  public List<TraceStyle> getTraceStyles() {
    return traceStyles;
  }

  public void setTraceStyles(List<TraceStyle> traceStyles) {
    this.traceStyles = new ArrayList<TraceStyle>();
    for (TraceStyle traceStyle : traceStyles) {
      this.traceStyles.add(traceStyle.clone());
    }
  }

  @Nls
  public String getDisplayName() {
    return "TraceViewer";
  }

  public Icon getIcon() {
    return IconLoader.getIcon("gui/icons/lamp_32.png");
  }

  @Nullable
  @NonNls
  public String getHelpTopic() {
    return null;
  }

  public JComponent createComponent() {
    if (form == null) {
      Logger.getInstance(TraceViewerComponent.class.getName())
              .info("createComponent(): new TraceViewerConfigurationForm");
      form = new TraceViewerConfigurationForm();
    }
    return form.getRootComponent();
  }

  public boolean isModified() {
    return form != null && form.isModified(this);
  }

  public void apply() throws ConfigurationException {
    Logger.getInstance(TraceViewerComponent.class.getName()).info("apply new configuration");

    if (form != null) {
      form.getData(this);
    }
  }

  public void reset() {
    Logger.getInstance(TraceViewerComponent.class.getName()).info("reset configuration");

    if (form != null) {
      form.setData(this);
    }
  }

  public void disposeUIResources() {
    form = null;
  }

  public void readExternal(Element element) throws InvalidDataException {
    Logger.getInstance(TraceViewerComponent.class.getName()).info("read external configuration");
    DefaultJDOMExternalizer.readExternal(this, element);

    final Element parentElement = element.getChild(TRACESTYLES_ELEMENT);
    if (parentElement == null) {
      return;
    }
    traceStyles.clear();
    for (Object child : parentElement.getChildren()) {
      try {
        Element entryElement = (Element) child;
        TraceStyle traceStyle = new TraceStyle();
        traceStyle.readExternal(entryElement);
        traceStyles.add(traceStyle);
      } catch (NumberFormatException ex) {
        Logger.getInstance(TraceViewerComponent.class.getName()).error("Read saved config failure", ex);
      } catch (ArrayIndexOutOfBoundsException ex) {
        Logger.getInstance(TraceViewerComponent.class.getName()).error("Read saved config failure", ex);
      }
    }
  }

  public void writeExternal(Element element) throws WriteExternalException {

    Logger.getInstance(TraceViewerComponent.class.getName()).info("write external configuration");

    DefaultJDOMExternalizer.writeExternal(this, element);

    Element parentElement = new Element(TRACESTYLES_ELEMENT);
    int i = 0;
    for (TraceStyle traceStyle : traceStyles) {
      Element entryElement = new Element(TRACESTYLE_ELEMENT_PREFIX + i++);
      traceStyle.writeExternal(entryElement);
      parentElement.addContent(entryElement);
    }
    element.addContent(parentElement);
  }

  public void projectOpened() {
    Logger.getInstance(TraceViewerComponent.class.getName()).info("projectOpened()");
  }

  public void projectClosed() {
    Logger.getInstance(TraceViewerComponent.class.getName()).info("projectClosed()");
  }


  public static String getProjectPath(Project project) {
    String projectFilePath = project.getProjectFilePath();
    String projectPath = projectFilePath.replaceFirst(project.getProjectFile().getName(), "");
    return projectPath;
  }

}
