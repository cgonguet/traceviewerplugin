package com.nortel.oam.traceviewer.ideaplugin.gui;

import junit.framework.TestCase;

import com.intellij.openapi.editor.markup.TextAttributes;
import com.nortel.oam.traceviewer.ideaplugin.gui.TextAttributesChooser;

/**
 * Created by IntelliJ IDEA.
 * User: cgonguet
 * Date: 11 juin 2007
 * Time: 16:46:24
 * To change this template use File | Settings | File Templates.
 */
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
