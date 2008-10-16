package com.idea.plugin.traceviewer.gui;

import junit.framework.TestCase;

import com.intellij.openapi.editor.markup.TextAttributes;

public class TextAttributesChooserTest extends TestCase {

  public void test() throws Exception {
    TextAttributesChooser textAttributesChooser = new TextAttributesChooser(null);
    textAttributesChooser.showDialog();
    TextAttributes textAttributes = textAttributesChooser.getTextAttributes();
    System.out.println("FG" + textAttributes.getForegroundColor());
    System.out.println("BG" + textAttributes.getBackgroundColor());
    System.out.println("Font" + textAttributes.getFontType());

   textAttributesChooser = new TextAttributesChooser(textAttributes);
    textAttributesChooser.showDialog();
    textAttributes = textAttributesChooser.getTextAttributes();
    System.out.println("FG" + textAttributes.getForegroundColor());
    System.out.println("BG" + textAttributes.getBackgroundColor());
    System.out.println("Font" + textAttributes.getFontType());

  }
}
