package com.idea.plugin.traceviewer.core;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.JDOMExternalizable;
import com.intellij.openapi.util.WriteExternalException;
import com.idea.plugin.traceviewer.TraceViewerComponent;
import org.jdom.Element;

import java.awt.*;
import java.util.StringTokenizer;


/**
 * Created by IntelliJ IDEA.
 * User: cgonguet
 * Date: 21 juin 2007
 * Time: 10:55:17
 * To change this template use File | Settings | File Templates.
 */
public class TraceStyle implements JDOMExternalizable {
  public String regexp;
  public TextAttributes style;
  public boolean filter;
  public boolean resume;

  public TraceStyle() {
    regexp = "";
    style = new TextAttributes();
    style.setForegroundColor(Color.BLACK);
    style.setBackgroundColor(Color.WHITE);
    resume = false;
    filter = false;
  }

  public TraceStyle(String regexp, Color fgColor, Color bgColor, int font, boolean resume, boolean filter) {
    this.regexp = regexp;
    style = new TextAttributes();
    style.setForegroundColor(fgColor);
    style.setBackgroundColor(bgColor);
    style.setFontType(font);
    this.resume = resume;
    this.filter = filter;
  }

  public TraceStyle(Object[] data) {
    this.regexp = (String) data[0];
    this.style = ((TextAttributes) data[1]).clone();
    this.resume = (Boolean) data[2];
    this.filter = (Boolean) data[3];
  }

  public TraceStyle clone() {
    TraceStyle c = new TraceStyle();
    c.regexp = regexp;
    c.style = style.clone();
    c.resume = resume;
    c.filter = filter;
    return c;
  }

  public Object[] getData() {
    return new Object[]{regexp, style, resume, filter};
  }


  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    TraceStyle that = (TraceStyle) o;

    if (filter != that.filter) {
      return false;
    }
    if (resume != that.resume) {
      return false;
    }
    if (regexp != null ? !regexp.equals(that.regexp) : that.regexp != null) {
      return false;
    }
    if (style != null ? !style.equals(that.style) : that.style != null) {
      return false;
    }

    return true;
  }

  public int hashCode() {
    int result;
    result = (regexp != null ? regexp.hashCode() : 0);
    result = 31 * result + (style != null ? style.hashCode() : 0);
    result = 31 * result + (filter ? 1 : 0);
    result = 31 * result + (resume ? 1 : 0);
    return result;
  }

  public String toString() {
    return "TraceStyle(regexp=" + regexp + ", style=(" + style.getForegroundColor() + ", " +
           style.getBackgroundColor() + ", " + style.getFontType() + "))";
  }


  private static final String TRACESTYLE_REGEXP_ATTRIBUTE = "regexp";
  private static final String TRACESTYLE_STYLE_FG_ATTRIBUTE = "fg";
  private static final String TRACESTYLE_STYLE_BG_ATTRIBUTE = "bg";
  private static final String TRACESTYLE_STYLE_FONT_ATTRIBUTE = "font";
  private static final String TRACESTYLE_RESUME_ATTRIBUTE = "resume";
  private static final String TRACESTYLE_FILTER_ATTRIBUTE = "filterable";

  public void readExternal(Element element) throws InvalidDataException {
    try {
      TextAttributes textAttributes = new TextAttributes();
      textAttributes
              .setFontType(Integer.valueOf(element.getAttributeValue(TRACESTYLE_STYLE_FONT_ATTRIBUTE)));
      textAttributes.setForegroundColor(decodeColor(element.getAttributeValue(TRACESTYLE_STYLE_FG_ATTRIBUTE)));
      textAttributes.setBackgroundColor(decodeColor(element.getAttributeValue(TRACESTYLE_STYLE_BG_ATTRIBUTE)));

      this.regexp = element.getAttributeValue(TRACESTYLE_REGEXP_ATTRIBUTE);
      this.style = textAttributes;
      this.resume = Boolean.valueOf(element.getAttributeValue(TRACESTYLE_RESUME_ATTRIBUTE));
      this.filter = Boolean.valueOf(element.getAttributeValue(TRACESTYLE_FILTER_ATTRIBUTE));

    } catch (NumberFormatException ex) {
      Logger.getInstance(TraceViewerComponent.class.getName()).error("Read saved config failure", ex);
    } catch (ArrayIndexOutOfBoundsException ex) {
      Logger.getInstance(TraceViewerComponent.class.getName()).error("Read saved config failure", ex);
    }

  }

  public static Color decodeColor(String colorStr) {
    try {
      colorStr = colorStr.substring(colorStr.indexOf("[") + 1, colorStr.indexOf("]"));
      colorStr = colorStr.replaceAll("r=", "");
      colorStr = colorStr.replaceAll("g=", "");
      colorStr = colorStr.replaceAll("b=", "");
      StringTokenizer st = new StringTokenizer(colorStr, ",");
      int fr = Integer.parseInt(st.nextToken());
      int fg = Integer.parseInt(st.nextToken());
      int fb = Integer.parseInt(st.nextToken());
      Color c = new Color(fr, fg, fb);
      return c;
    } catch (Exception e) {
      Logger.getInstance(TraceViewerComponent.class.getName()).error("Cannot decode color: " + colorStr, e);
    }
    return null;
  }

  public void writeExternal(Element element) throws WriteExternalException {
    setAttributeForEntry(element, TRACESTYLE_REGEXP_ATTRIBUTE, this.regexp);
    setAttributeForEntry(element, TRACESTYLE_STYLE_FG_ATTRIBUTE, this.style.getForegroundColor());
    setAttributeForEntry(element, TRACESTYLE_STYLE_BG_ATTRIBUTE, this.style.getBackgroundColor());
    setAttributeForEntry(element, TRACESTYLE_STYLE_FONT_ATTRIBUTE, this.style.getFontType());
    setAttributeForEntry(element, TRACESTYLE_RESUME_ATTRIBUTE, this.resume);
    setAttributeForEntry(element, TRACESTYLE_FILTER_ATTRIBUTE, this.filter);
  }

  private void setAttributeForEntry(Element entryElement, String name, Object value) {
    entryElement.setAttribute(name, value == null ? "" : value.toString());
  }


}
